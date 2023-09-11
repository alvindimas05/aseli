package database

import "github.com/surrealdb/surrealdb.go"

func Connect() (*surrealdb.DB, error) {
	db, err := surrealdb.New("ws://localhost:8000/rpc")
	if err != nil {
		return nil, err
	}

	// Auth
	// if _, err = db.Signin(map[string]string{
	// 	"user": "root",
	// 	"pass": "root",
	// }); err != nil {
	// 	return nil, err
	// }

	// Select namespace and database
	if _, err = db.Use("test", "test"); err != nil {
		return nil, err
	}
	return db, nil
}
