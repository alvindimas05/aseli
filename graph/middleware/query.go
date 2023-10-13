package middleware

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strings"
)

var allowed = []string{"IntrospectionQuery", "registerUser", "loginUser", "posts"}

func QueryMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		data, _ := io.ReadAll(r.Body)
		r.Body = io.NopCloser(bytes.NewReader(data))

		body := RequestBody{}
		json.Unmarshal(data, &body)

		// Check if has file
		if (!strings.Contains(r.Header.Get("Content-Type"), "multipart/form-data") &&
			// Check if allowed query or mutation
			contains(body.Query, allowed)) ||
			// Check if getting images
			strings.Contains(r.URL.Path, "/images") ||
			// Check if method is OPTIONS
			r.Method == "OPTIONS" {

			rForce := r.WithContext(context.WithValue(r.Context(), "force_allow", true))
			next.ServeHTTP(w, rForce)
			return
		}
		next.ServeHTTP(w, r)
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