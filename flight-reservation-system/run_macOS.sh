#!/bin/bash

# Execute Maven commands to run the project
echo "Running 'mvn clean install'..."
mvn clean install

echo "Running 'mvn compile exec:java'..."
mvn compile exec:java
