package helper

import (
	"aseli-api/graph/database"
	"aseli-api/graph/model"
	"context"
	"fmt"
	"os"
	"path"
	"path/filepath"
	"github.com/surrealdb/surrealdb.go"
)

type Post struct{}

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
	if !_p.ValidatePost(postID) {
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

func (_p Post) StoreComment(ctx context.Context, input model.RequestSendComment) (*model.Comment, error) {
	if !_p.ValidatePost(input.PostID) {
		return nil, fmt.Errorf("post not found")
	}
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}

	query, err := db.Create("comment", model.Comment{
		Post: input.PostID,
		User: ctx.Value("user").(string),
		Time: GetCurrentDateTime(),
		Comment: input.Comment,
	})
	if err != nil {
		panic(err)
	}

	cmt := query.([]interface{})[0]
	comment := model.Comment{}

	surrealdb.Unmarshal(cmt, &comment)

	fmt.Printf("%v", comment)
	defer db.Close()
	return &comment, nil
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
