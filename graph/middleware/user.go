package middleware

import (
	"bytes"
	"encoding/json"
	"io"
	"net/http"

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

		http.Error(w, "Invalid cookie", http.StatusForbidden)
	})
}
