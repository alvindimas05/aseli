package helper

import (
	"aseli-api/graph/model"
	"reflect"

	"github.com/google/uuid"
	"github.com/surrealdb/surrealdb.go"
)

type User struct{}

func (_u User) CheckUsername(db *surrealdb.DB, username string) bool {
	user, err := db.Query("SELECT id FROM user WHERE username=$username", map[string]interface{}{"username": username})
	if err != nil {
		return false
	}

	nuser := reflect.ValueOf(NormalizeQueryResult(user))
	return nuser.Len() > 0
}

func (_u User) RegisterAuthKey(db *surrealdb.DB, user_id string) string {
	key := uuid.New().String()

	db.Create("auth_user", model.AuthUser{
		User: user_id,
		AuthKey: key,
	})

	return key
}