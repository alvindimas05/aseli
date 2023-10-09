@echo off

if "%1" == "run"  (
    go build -o aseli-api && ./aseli-api
) else if "%1" == "start" (
    go build -o aseli-api && ./aseli-api
) else if "%1" == "generate" (
    go get github.com/99designs/gqlgen && go run github.com/99designs/gqlgen generate
) else if "%1" == "database" (
    surreal start file://./SurrealDB
) else if "%1" == "db" (
    surreal start file://./SurrealDB
) else (
    echo "Command not found."
)