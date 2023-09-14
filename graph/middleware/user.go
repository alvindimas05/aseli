package middleware

import (
	"aseli-api/graph/database"
	"aseli-api/graph/helper"
	"bytes"
	"encoding/json"
	"io"
	"net/http"
	"reflect"

	"golang.org/x/exp/slices"
)

var allowed = []string{"RegisterUser", "LoginUser"}

type RequestBody struct {
	// Query         string `json:"query"`
	OperationName *string `json:"operationName"`
}

func UserMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		data, _ := io.ReadAll(r.Body)
		r.Body = io.NopCloser(bytes.NewReader(data))

		body := RequestBody{}
		json.Unmarshal(data, &body)

		if body.OperationName == nil || slices.Contains(allowed, *body.OperationName) {
			next.ServeHTTP(w, r)
			return
		}

		key := r.Header.Get("Auth-Key")
		if key == "" {
			http.Error(w, "Invalid Auth-Key", http.StatusForbidden)
			return
		}

		db, err := database.Connect()
		if err != nil {
			http.Error(w, "Database connection error", http.StatusInternalServerError)
			return
		}

		query, err := db.Query("SELECT id FROM auth_user WHERE auth_key=$auth_key", map[string]interface{}{"auth_key": key})
		if err != nil {
			http.Error(w, "Authentication check error.", http.StatusInternalServerError)
			return
		}
		defer db.Close()

		user := helper.NormalizeQueryResult(query)
		if reflect.ValueOf(user).Len() > 0 {
			next.ServeHTTP(w, r)
		} else {
			http.Error(w, "Invalid Auth-Key", http.StatusForbidden)
		}
	})
}
