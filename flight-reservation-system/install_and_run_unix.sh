#!/bin/bash

# Update packages
sudo apt-get update

# Install MySQL if not already installed
if ! dpkg -l | grep -q mysql-server; then
    echo "Installing MySQL..."
    sudo apt-get install -y mysql-server
    sudo systemctl start mysql
    sudo systemctl enable mysql
else
    echo "MySQL is already installed."
fi

# Install Java SDK 17 if not already installed
if ! dpkg -l | grep -q openjdk-17-jdk; then
    echo "Installing Java SDK 17..."
    sudo apt-get install -y openjdk-17-jdk
else
    echo "Java SDK 17 is already installed."
fi

# Install Maven if not already installed
if ! dpkg -l | grep -q maven; then
    echo "Installing Maven..."
    sudo apt-get install -y maven
else
    echo "Maven is already installed."
fi

# Check if mysql_credentials.txt is empty
if [ ! -s sql/mysql_credentials.txt ]; then
    # Ask the user to enter a MySQL username and password
    read -p "Enter MySQL Username: " mysql_user
    read -s -p "Enter MySQL Password: " mysql_password
    echo
    echo "MySQL username and password saved in mysql_credentials.txt"
    echo "MySQL User: $mysql_user" > sql/mysql_credentials.txt
    echo "MySQL Password: $mysql_password" >> sql/mysql_credentials.txt
else
    echo "MySQL credentials already exist in mysql_credentials.txt."
fi

# Execute Maven commands
echo "Running 'mvn clean install'..."
sudo mvn clean install

echo "Running 'mvn compile exec:java'..."
sudo mvn compile exec:java

