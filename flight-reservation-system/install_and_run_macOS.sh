#!/bin/bash



# Function to execute a command with sudo
run_with_sudo() {
    sudo $1
}

# Install MySQL if not already installed
if ! brew list mysql &> /dev/null; then
    echo "Installing MySQL..."
    brew install mysql
    brew services start mysql
else
    echo "MySQL is already installed."
fi

# Install Java SDK 17 if not already installed
if ! brew list openjdk@17 &> /dev/null; then
    echo "Installing Java SDK 17..."
    brew install openjdk@17
else
    echo "Java SDK 17 is already installed."
fi

# Install Maven if not already installed
if ! brew list maven &> /dev/null; then
    echo "Installing Maven..."
    brew install maven
else
    echo "Maven is already installed."
fi

# Check if mysql_credentials.txt is empty
if [ ! -s mysql_credentials.txt ]; then
    read -p "Enter MySQL Username: " mysql_user
    read -s -p "Enter MySQL Password: " mysql_password
    echo
    echo "MySQL username and password saved in mysql_credentials.txt"
    echo "MySQL User: $mysql_user" > mysql_credentials.txt
    echo "MySQL Password: $mysql_password" >> mysql_credentials.txt
else
    echo "MySQL credentials already exist in mysql_credentials.txt."
fi

# Execute Maven commands
echo "Running 'mvn clean install'..."
mvn clean install

echo "Running 'mvn compile exec:java'..."
mvn compile exec:java
