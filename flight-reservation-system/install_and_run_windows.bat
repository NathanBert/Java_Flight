@echo off
REM Check if MySQL is installed
where mysql >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Installing MySQL...
    winget install -e --id Oracle.MySQL --accept-package-agreements --accept-source-agreements
) else (
    echo MySQL is already installed.
)

REM Check if Java SDK 17 is installed
where java >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Installing Java SDK 17...
    winget install -e --id Oracle.JDK.17 --accept-package-agreements --accept-source-agreements
) else (
    echo Java SDK 17 is already installed.
)

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo Installing Maven...
    winget install -e --id Apache.Maven --accept-package-agreements --accept-source-agreements
) else (
    echo Maven is already installed.
)

REM Check if mysql_credentials.txt is empty
if not exist mysql_credentials.txt (
    set /p mysql_user=Enter MySQL Username:
    set /p mysql_password=Enter MySQL Password:
    (
        echo MySQL User: %mysql_user%
        echo MySQL Password: %mysql_password%
    ) > mysql_credentials.txt
) else (
    echo MySQL credentials already exist in mysql_credentials.txt.
)

REM Execute Maven commands
echo Running 'mvn clean install'...
mvn clean install

echo Running 'mvn compile exec:java'...
mvn compile exec:java
