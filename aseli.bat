@echo off

if "%1" == "run" (
    go run main.go
) else if "%1" == "generate" (
    go get github.com/99designs/gqlgen && go run github.com/99designs/gqlgen generate
) else if "%1" == "database" (
    surreal start file://./SurrealDB
) else (
    echo "Command not found."
)