package main

import (
	"aseli-api/graph/generated"
	"aseli-api/graph/middleware"
	"aseli-api/graph/resolver"
	"log"
	"net/http"
	"os"

	"github.com/99designs/gqlgen/graphql/handler"
	"github.com/99designs/gqlgen/graphql/playground"
	"github.com/go-chi/chi/v5"
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
	port := os.Getenv("SERVER_PORT")
	router := chi.NewRouter()

	router.Use(middleware.UserMiddleware)

	srv := handler.NewDefaultServer(generated.NewExecutableSchema(generated.Config{Resolvers: &resolver.Resolver{}}))
	// srv := handler.NewDefaultServer(generated.NewExecutableSchema(generated.Config{Resolvers: &resolver.Resolver{}}))

	router.Handle("/", playground.Handler("GraphQL playground", "/query"))
	router.Handle("/query", srv)

	log.Printf("connect to http://localhost:%s/ for GraphQL playground", port)
	log.Fatal(http.ListenAndServe("127.0.0.1:"+port, router))
}
