package types

import "time"

type User struct {
	ID     int    `json:"id,omitempty"`
	Name   string `json:"name,omitempty"`
	Email  string `json:"email,omitempty"`
	Active bool   `json:"active,omitempty"`
	Admin  bool   `json:"admin,omitempty"`
	SU     bool   `json:"su,omitempty"`
}

type Comment struct {
	ID           int       `json:"id,omitempty"`
	AssignmentID int       `json:"assignmentId,omitempty"`
	ModifiedOn   time.Time `json:"modifiedOn,omitempty"`
	CommentVal   string    `json:"commentVal,commentVal"`
	Author       *User     `json:"author,omitempty"`
}

type Assignment struct {
	ID         int       `json:"id,omitempty"`
	Name       string    `json:"name,omitempty"`
	ModifiedOn time.Time `json:"modifiedOn,omitempty"`
	XML        string    `json:"xml,omitempty"`
	Author     *User     `json:"author,omitempty"`
}
