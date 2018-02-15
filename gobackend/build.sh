#!/bin/bash

set -e

golint   -set_exit_status $(go list ./... | grep -v /vendor/)
go vet   -v $(go list ./... | grep -v /vendor/)
go build -v -o ./build/ldbn_backend main.go
go test  -v $(go list ./... | grep -v /vendor/)