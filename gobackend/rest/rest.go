package rest

// For an example see https://www.codementor.io/codehakase/building-a-restful-api-with-golang-a6yivzqdo

import (
	"fmt"
	"log"
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/devng/ldbn/dao"
	"github.com/devng/ldbn/types"
	"github.com/gorilla/mux"
	"reflect"
)

const RestPathPrefix = "/rest/api/v1"

func InitRestRouter(router *mux.Router) {
	restRouter := router.PathPrefix(RestPathPrefix).Subrouter()
	restRouter.HandleFunc("/users", getUsers).Methods("GET")
	restRouter.HandleFunc("/users/{id}", getUserById).Methods("GET")
	restRouter.HandleFunc("/assignments", getAssignments).Methods("GET")
	restRouter.HandleFunc("/assignments", postAssignment).Methods("POST")
	restRouter.HandleFunc("/assignments/{id}", getAssignmentById).Methods("GET")
	restRouter.HandleFunc("/assignments/{id}", putAssignment).Methods("PUT")
	restRouter.HandleFunc("/assignments/{id}", deleteAssignment).Methods("DELETE")
	restRouter.HandleFunc("/assignments/{id}/comments", getAssignmentComments).Methods("GET")
}

func getUsers(w http.ResponseWriter, r *http.Request) {
	activeOnly := r.FormValue("activeOnly")
	var users []*types.User
	var err error
	if activeOnly == "true" {
		users, err = dao.SelectActiveUsers()
	} else {
		users, err = dao.SelectAllUsers()
	}
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, users)
}

func getUserById(w http.ResponseWriter, r *http.Request) {
	id := extractIdParamOrSend400ErrorResponse(w, r)
	if id == -1 {
		return
	}
	user, err := dao.SelectUserById(id)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, user)
}

func getAssignments(w http.ResponseWriter, r *http.Request) {
	includeXml := r.FormValue("includeXml") == "true"
	assignments, err := dao.SelectAllssignments(includeXml)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, assignments)
}

func postAssignment(w http.ResponseWriter, r *http.Request) {
	var a types.Assignment
	err := json.NewDecoder(r.Body).Decode(&a)
	if err != nil {
		log.Println(err)
		errorJsonResponse(w, "Invalid JSON format", http.StatusBadRequest)
		return
	}

	if a.Name == "" || a.XML == "" || a.Author == nil || a.Author.ID <= 0 {
		errorJsonResponse(w, "Missing JSON attributes", http.StatusBadRequest)
		return
	}

	asmt, err := dao.InsertAssignment(&a)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}

	okJsonResponseOr404(w, asmt)
}

func getAssignmentById(w http.ResponseWriter, r *http.Request) {
	id := extractIdParamOrSend400ErrorResponse(w, r)
	if id == -1 {
		return
	}

	assignment, err := dao.SelectAssignmentById(id)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, assignment)
}

func putAssignment(w http.ResponseWriter, r *http.Request) {
	id := extractIdParamOrSend400ErrorResponse(w, r)
	if id == -1 {
		return
	}

	var a types.Assignment
	if err := json.NewDecoder(r.Body).Decode(&a); err != nil {
		log.Println(err)
		errorJsonResponse(w, "Invalid JSON format", http.StatusBadRequest)
		return
	}

	a.ID = id
	if a.Name == "" || a.XML == "" || a.Author == nil || a.Author.ID <= 0 {
		errorJsonResponse(w, "Missing JSON attributes", http.StatusBadRequest)
		return
	}

	asmt, err := dao.InsertAssignment(&a)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, asmt)
}

func deleteAssignment(w http.ResponseWriter, r *http.Request) {
	id := extractIdParamOrSend400ErrorResponse(w, r)
	if id == -1 {
		return
	}

	err := dao.DeleteAssignment(id)
	if err != nil {
		handleDaoErrorResponse(w, err)
	}
}

func getAssignmentComments(w http.ResponseWriter, r *http.Request) {
	id := extractIdParamOrSend400ErrorResponse(w, r)
	if id == -1 {
		return
	}
	comments, err := dao.SelectAssignmentComments(id)
	if err != nil {
		handleDaoErrorResponse(w, err)
		return
	}
	okJsonResponseOr404(w, comments)
}

// === Helper functions === //

func okJsonResponseOr404(w http.ResponseWriter, dto interface{}) {
	// WTF we need to use reflection to check for nil of an unknown type
	// see: https://groups.google.com/forum/#!topic/golang-nuts/wnH302gBa4I/discussion
	if reflect.ValueOf(dto).IsNil() {
		errorJsonResponse(w, "Not found", http.StatusNotFound)
		return
	}
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(dto)
}

func errorJsonResponse(w http.ResponseWriter, errorMsg string, httpStatus int) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(httpStatus)
	fmt.Fprintf(w, `{"errorCode": %d, "errorText": %q}`, httpStatus, errorMsg)
}

func extractIdParamOrSend400ErrorResponse(w http.ResponseWriter, r *http.Request) int {
	params := mux.Vars(r)
	id, err := strconv.Atoi(params["id"])
	if err != nil {
		log.Println(err)
		errorJsonResponse(w, "Invalid ID", http.StatusBadRequest)
		return -1
	}
	if id <= 0 {
		errorJsonResponse(w, "Invalid ID", http.StatusBadRequest)
		return -1
	}

	return id
}

func handleDaoErrorResponse(w http.ResponseWriter, err error) {
	log.Println(err)
	if err == dao.ErrNoUpdate {
		errorJsonResponse(w, "Not found", http.StatusNotFound)
	} else if err == dao.ErrInvalidInput {
		errorJsonResponse(w, "Invalid input", http.StatusBadRequest)
	} else {
		errorJsonResponse(w, "SQL error", http.StatusInternalServerError)
	}
}
