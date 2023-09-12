package helper

import (
	"reflect"

	"github.com/surrealdb/surrealdb.go"
)

type User struct{}

func (_ User) CheckUsername(db *surrealdb.DB, username string) bool {
	user, err := db.Query("SELECT id FROM user WHERE username=$username", map[string]interface{}{"username": username})
	if err != nil {
		return false
	}

	nuser := reflect.ValueOf(NormalizeQueryResult(user))
	return nuser.Len() > 0
}
