#!/bin/bash

# Script to run unit tests
echo "Running unit tests..."
./mvnw -Dtest=*Spec* test
