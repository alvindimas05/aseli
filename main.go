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
	"github.com/joho/godotenv"
	chiMiddleware "github.com/go-chi/chi/middleware"
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

	router.Use(chiMiddleware.Timeout(120))
	router.Use(middleware.QueryMiddleware)
	router.Use(middleware.CorsMiddleware)
	router.Use(middleware.UserMiddleware)

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
