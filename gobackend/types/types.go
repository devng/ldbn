package types

import "time"

// User represents a JSON user in LDBN
type User struct {
	ID     int    `json:"id,omitempty"`
	Name   string `json:"name,omitempty"`
	Email  string `json:"email,omitempty"`
	Active bool   `json:"active,omitempty"`
	Admin  bool   `json:"admin,omitempty"`
	SU     bool   `json:"su,omitempty"`
}

// Comment represents a JSON comment in LDBN
type Comment struct {
	ID           int       `json:"id,omitempty"`
	AssignmentID int       `json:"assignmentId,omitempty"`
	ModifiedOn   time.Time `json:"modifiedOn,omitempty"`
	CommentVal   string    `json:"commentVal,commentVal"`
	Author       *User     `json:"author,omitempty"`
}

// Assignment represents a JSON assignment in LDBN
type Assignment struct {
	ID         int       `json:"id,omitempty"`
	Name       string    `json:"name,omitempty"`
	ModifiedOn time.Time `json:"modifiedOn,omitempty"`
	XML        string    `json:"xml,omitempty"`
	Author     *User     `json:"author,omitempty"`
}
