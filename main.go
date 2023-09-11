package main

import (
	"aseli-api/graph/generated"
	"aseli-api/graph/resolver"
	"log"
	"net/http"
	"os"

	"github.com/99designs/gqlgen/graphql/handler"
	"github.com/99designs/gqlgen/graphql/playground"
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

	srv := handler.NewDefaultServer(generated.NewExecutableSchema(generated.Config{Resolvers: &resolver.Resolver{}}))

	http.Handle("/", playground.Handler("GraphQL playground", "/query"))
	http.Handle("/query", srv)

	log.Printf("connect to http://localhost:%s/ for GraphQL playground", port)
	log.Fatal(http.ListenAndServe("127.0.0.1:"+port, nil))
}
