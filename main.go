package main

import (
	"aseli-api/graph/generated"
	"aseli-api/graph/helper"
	"aseli-api/graph/middleware"
	"aseli-api/graph/resolver"
	"fmt"
	"log"
	"net/http"
	"os"
	"path/filepath"

	"github.com/99designs/gqlgen/graphql/handler"
	// "github.com/99designs/gqlgen/graphql/playground"
	"github.com/go-chi/chi/v5"
	"github.com/go-chi/cors"
	"github.com/joho/godotenv"
)

type User struct {
	Username string `json:"username,omitempty"`
	Password string `json:"password,omitempty"`
}

func main() {
	godotenv.Load()
	// db, err := ConnectDatabase()
	// if err != nil {
	// 	panic(err)
	// }
	// GetUsers(*db)
	SetServer()
}

func SetServer() {
	host, port := os.Getenv("SERVER_HOST"), os.Getenv("SERVER_PORT")
	router := chi.NewRouter()

	router.Use(middleware.UserMiddleware)
	router.Use(cors.Handler(cors.Options{
		// AllowedOrigins:   []string{"https://foo.com"}, // Use this to allow specific origin hosts
		AllowedOrigins: []string{"https://*", "http://*"},
		// AllowOriginFunc:  func(r *http.Request, origin string) bool { return true },
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Accept", "Content-Type", "Auth-Key"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: false,
		MaxAge:           300, // Maximum value not ignored by any of major browsers
	}))

	srv := handler.NewDefaultServer(generated.NewExecutableSchema(generated.Config{Resolvers: &resolver.Resolver{}}))
	// srv := handler.NewDefaultServer(generated.NewExecutableSchema(generated.Config{Resolvers: &resolver.Resolver{}}))

	// router.Handle("/", playground.Handler("GraphQL playground", "/query"))
	router.Handle("/query", srv)

	fs := CustomFileServer(http.Dir(helper.MkdirImages()))
	router.Handle("/images/*", http.StripPrefix("/images/", fs))
	// router.Handle("/images/*", http.StripPrefix("/images/", http.FileServer(http.Dir(helper.MkdirImages()))))

	// log.Printf("connect to http://localhost:%s/ for GraphQL playground", port)
	addr := fmt.Sprintf("%s:%s", host, port)
	log.Printf("Server is running on http://%s", addr)
	log.Fatal(http.ListenAndServe(addr, router))
}

func CustomFileServer(root http.Dir) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		filePath := filepath.Clean(r.URL.Path)
		file, err := root.Open(filePath)

		if err != nil {
			http.NotFound(w, r)
			return
		}
		defer file.Close()

		fileInfo, err := file.Stat()
		if err != nil {
			http.NotFound(w, r)
			return
		}

		if fileInfo.IsDir() {
			http.NotFound(w, r)
			return
		}
		http.FileServer(root).ServeHTTP(w, r)
	})
}
