package helper

import (
	"aseli-api/graph/database"
	"context"
	"fmt"
	"io"
	"os"
	"path"
	"path/filepath"
	"strings"

	"github.com/99designs/gqlgen/graphql"
	"github.com/google/uuid"
)

type Post struct{}

func (_p Post) SaveImage(image graphql.Upload) string {
	filename := strings.ReplaceAll(uuid.New().String(), "-", "")
	imgPath := path.Join(MkdirImages(), filename)

	dest, err := os.Create(imgPath)
	if err != nil {
		panic(err)
	}
	defer dest.Close()

	if _, err := io.Copy(dest, image.File); err != nil {
		panic(err)
	}
	return filename
}

func (_p Post) ValidatePost(post_id string) bool {
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}
	
	post, err := db.Select(post_id)
	if err != nil {
		panic(err)
	}

	defer db.Close()
	return post != nil
}

func (_p Post) SendRilOrFek(send_type string, ctx context.Context, postID string) (*bool, error) {
	if !Post.ValidatePost(_p, postID) {
		return nil, fmt.Errorf("post not found")
	}

	user := ctx.Value("user").(string)
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}

	data := map[string]string{
		"user": user, "post": postID,
	}

	existQuery, err := db.Query(fmt.Sprintf("SELECT array::find_index(%s, $user) != NULL AS exist FROM $post", send_type), data)
	if err != nil {
		panic(err)
	}

	exist := NormalizeQueryResult(existQuery).([]interface{})[0].(map[string]interface{})["exist"].(bool)
	if exist {
		_, err := db.Query(fmt.Sprintf("UPDATE $post SET %s = array::remove(%s, array::find_index(%s, '%s'))",
		send_type, send_type, send_type, data["user"]), data)
		if err != nil {
			panic(err)
		}
	} else {
		_, err := db.Query(fmt.Sprintf("UPDATE $post SET %s += $user", send_type), data)
		if err != nil {
			panic(err)
		}
	}

	exist = !exist
	defer db.Close()
	return &exist, nil
}

func MkdirImages() string {
	ex, err := os.Executable()
	if err != nil {
		panic(err)
	}

	imgPath := path.Join(filepath.Dir(ex), "/images")
	exist, err := Exists(imgPath)
	if err != nil {
		panic(err)
	}

	if !exist {
		os.Mkdir(imgPath, os.ModeAppend)
	}
	return imgPath
}

func Exists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return false, err
}
