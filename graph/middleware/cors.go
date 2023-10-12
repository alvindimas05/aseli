package middleware

import (
	"context"
	"net/http"
)

func CorsMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        w.Header().Set("Access-Control-Allow-Origin", "*")
        w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Content-Length, Accept-Encoding, Auth-Key, accept, origin, Cache-Control, X-Requested-With")
        w.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS, GET, PUT, DELETE")
        if r.Method == "OPTIONS" {
            w.WriteHeader(http.StatusNoContent)
            return
        }

        rForce := r.WithContext(context.WithValue(r.Context(), "force_allow", true))
        next.ServeHTTP(w, rForce)
    })
}
