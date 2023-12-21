#!/bin/bash

# Script to run integration tests
echo "Running integration tests..."
./mvnw -Dtest=*IntegrationTest test
