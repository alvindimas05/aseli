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

	surrealdb "github.com/surrealdb/surrealdb.go"
)

// RegisterUser is the resolver for the registerUser field.
func (r *mutationResolver) RegisterUser(ctx context.Context, input *model.NewUser) (*model.User, error) {
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}
	data, err := db.Create("user", input)
	if err != nil {
		panic(err)
	}

	result := model.User{}
	surrealdb.Unmarshal(reflect.ValueOf(data).Index(0).Interface(), &result)

	defer db.Close()
	return &result, nil
}

// Users is the resolver for the users field.
func (r *queryResolver) Users(ctx context.Context) ([]*model.User, error) {
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}

	fields := helper.NormalizeFields(ctx)
	data, err := db.Query(fmt.Sprintf("SELECT %s FROM user", fields), nil)
	if err != nil {
		panic(err)
	}

	ndata := helper.NormalizeQueryResult(data)

	users := []*model.User{}
	surrealdb.Unmarshal(ndata, &users)

	defer db.Close()
	return users, nil
}

// GetUser is the resolver for the getUser field.
func (r *queryResolver) GetUser(ctx context.Context, id *string) (*model.User, error) {
	db, err := database.Connect()
	if err != nil {
		panic(err)
	}

	fields := helper.NormalizeFields(ctx)
	data, err := db.Query(fmt.Sprintf("SELECT %s FROM $id", fields), map[string]interface{}{"id":id})
	if err != nil {
		panic(err)
	}

	ndata := helper.NormalizeQueryResult(data)

	users := []*model.User{}
	surrealdb.Unmarshal(ndata, &users)

	defer db.Close()
	return users[0], nil
}
