package resolver

// This file will be automatically regenerated based on the schema, any resolver implementations
// will be copied through when generating and any unknown code will be moved to the end.
// Code generated by github.com/99designs/gqlgen version v0.17.37

import (
	"aseli-api/graph/database"
	"aseli-api/graph/helper"
	"aseli-api/graph/model"
	"context"
	"fmt"
	"reflect"

	"github.com/99designs/gqlgen/graphql"
	surrealdb "github.com/surrealdb/surrealdb.go"
	"golang.org/x/exp/slices"
)

// CreatePost is the resolver for the createPost field.
func (r *mutationResolver) CreatePost(ctx context.Context, title string, description string, image graphql.Upload) (*model.ResponseCreatePost, error) {
	if !slices.Contains([]string{"image/png", "image/jpeg"}, image.ContentType) {
		return nil, fmt.Errorf("file is not an image")
	}
	imgName := helper.Post.SaveImage(helper.Post{}, image)

	db, err := database.Connect()
	if err != nil {
		panic(err)
	}

	query, err := db.Create("post", &model.Post{
		User:        ctx.Value("user").(string),
		Title:       title,
		Description: description,
		Image:       imgName,
	})
	if err != nil {
		panic(err)
	}

	post := model.Post{}
	surrealdb.Unmarshal(reflect.ValueOf(query).Index(0).Interface(), &post)

	return &model.ResponseCreatePost{
		PostID: *post.ID,
	}, nil
}
