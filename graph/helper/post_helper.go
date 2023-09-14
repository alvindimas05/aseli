package helper

import (
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
	return imgPath
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
