package main

import (
	"log"
	"net/http"
	"time"

	"os"

	"github.com/devng/ldbn/dao"
	"github.com/devng/ldbn/rest"
	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
)

func main() {
	dbFilePath := os.Getenv("LDBN_DB_FILE_PATH")
	if dbFilePath == "" {
		dbFilePath = "/Users/ngg/dev/git/github/ldbn/db/example-db/ldbn.db"
	}
	dao.InitDB(dbFilePath)
	router := mux.NewRouter()
	rest.InitRestRouter(router)

	srv := &http.Server{
		Addr:         ":8000",
		Handler:      router,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	log.Fatal(srv.ListenAndServe())
}
