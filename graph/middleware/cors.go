package middleware

import "net/http"

func CorsMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        w.Header().Set("Access-Control-Allow-Origin", "http://localhost:5173, http://127.0.0.1:5173")
        w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Content-Length, Accept-Encoding, Auth-Key, accept, origin, Cache-Control, X-Requested-With")
        w.Header().Set("Access-Control-Allow-Methods", "POST, OPTIONS, GET, PUT, DELETE")
        if r.Method == "OPTIONS" {
            w.WriteHeader(http.StatusNoContent)
            return
        }
        next.ServeHTTP(w, r)
    })
}
