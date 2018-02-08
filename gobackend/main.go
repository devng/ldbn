package main

import (
	"net/http"
	"time"
	"log"

	"github.com/devng/ldbn/rest"
	"github.com/devng/ldbn/dao"
	"github.com/gorilla/mux"
	"os"
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
		Addr: ":8000",
		Handler: router,
		ReadTimeout:  10 * time.Second,
		WriteTimeout: 10 * time.Second,
	}

	log.Fatal(srv.ListenAndServe())
}
