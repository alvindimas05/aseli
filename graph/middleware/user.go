package middleware

import (
	"aseli-api/graph/database"
	"aseli-api/graph/helper"
	"aseli-api/graph/model"
	"context"
	"net/http"
	"reflect"

	"github.com/surrealdb/surrealdb.go"
)

type RequestBody struct {
	// Query         string `json:"query"`
	// OperationName string `json:"operationName"`
	Query string `json:"query"`
}

func UserMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {

		key := r.Header.Get("Auth-Key")
		if key == "" && r.Context().Value("force_allow") == nil {
			http.Error(w, "Invalid Auth-Key", http.StatusForbidden)
			return
		}

		db, err := database.Connect()
		if err != nil {
			http.Error(w, "Database connection error", http.StatusInternalServerError)
			return
		}

		query, err := db.Query("SELECT user FROM auth_user WHERE auth_key=$auth_key", map[string]interface{}{"auth_key": key})
		if err != nil {
			http.Error(w, "Authentication check error.", http.StatusInternalServerError)
			return
		}
		defer db.Close()

		auth := helper.NormalizeQueryResult(query)
		if reflect.ValueOf(auth).Len() > 0 {
			authData := model.AuthUser{}
			surrealdb.Unmarshal(auth.([]interface{})[0], &authData)

			rAuth := r.WithContext(context.WithValue(r.Context(), "user", authData.User))
			next.ServeHTTP(w, rAuth)
		} else if r.Context().Value("force_allow") == nil {
			http.Error(w, "Invalid Auth-Key", http.StatusForbidden)
		} else {
			next.ServeHTTP(w, r)
		}
	})
}
