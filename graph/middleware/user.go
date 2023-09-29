package middleware

import (
	"aseli-api/graph/database"
	"aseli-api/graph/helper"
	"aseli-api/graph/model"
	"bytes"
	"context"
	"encoding/json"
	"io"
	"net/http"
	"reflect"
	"strings"

	"github.com/surrealdb/surrealdb.go"
)

var allowed = []string{"IntrospectionQuery", "registerUser", "loginUser", "posts"}

type RequestBody struct {
	// Query         string `json:"query"`
	// OperationName string `json:"operationName"`
	Query string `json:"query"`
}

func UserMiddleware(next http.Handler) http.Handler {	
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		data, _ := io.ReadAll(r.Body)
		r.Body = io.NopCloser(bytes.NewReader(data))

		body := RequestBody{}
		json.Unmarshal(data, &body)

		// Check if has file
		if !strings.Contains(r.Header.Get("Content-Type"), "multipart/form-data") &&
			// Check if allowed query or mutation
			contains(body.Query, allowed) &&
			// Check if getting images
			strings.Contains(r.URL.Path, "/images") {

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
		} else {
			http.Error(w, "Invalid Auth-Key", http.StatusForbidden)
		}
	})
}

func contains(s string, c []string) bool {
	for _, co := range c {
		if strings.Contains(s, co) {
			return true
		}
	}
	return false
}
