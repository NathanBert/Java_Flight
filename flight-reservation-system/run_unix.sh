#!/bin/bash

# Execute Maven commands to run the project
echo "Running 'mvn clean install'..."
sudo mvn clean install

echo "Running 'mvn compile exec:java'..."
sudo mvn compile exec:java


