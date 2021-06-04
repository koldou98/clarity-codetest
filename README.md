[![Build Status](https://travis-ci.com/koldou98/clarity-codetest.svg?branch=master)](https://travis-ci.com/koldou98/clarity-codetest) 
[![Java CI with Gradle](https://github.com/koldou98/clarity-codetest/actions/workflows/gradle.yml/badge.svg)](https://github.com/koldou98/clarity-codetest/actions/workflows/gradle.yml)
# Clarity-codetest
This is the solution I have come up with for the codetest
## How to build the project
### Prerequisites
- Java 11 or higher
### Project structure
This project contains three gradle subprojects:
```sh
gradle projets

# Root project 'clarity-codetest' Project with all the gradle dependencies and  configuration
# +--- Project ':infiniteloggenerator' Project to generate infinite logs with the appropiate format (for testing purpose)
# +--- Project ':listofhostnames' Project for Parse the data with a time_init and time_end
# \--- Project ':unlimitedinputparser' Project for un Unlimited Iput Parser
```

### How to build
All the projects can be built using **gradle wrapper** or **make**. The build will run the tests and generate a jar file for each project.

To build the project with make, use `make` command. To clean the project, use `make clean`.

To build the project with gradle, use `./gradlew build` or for a clean build `./gradlew clean build`. To clean the project, use `./gradlew clean`

To build only a certain subproject, use `./gradlew :[projectName]:build`. This will only generate the project jar and run the tests.

## How to use
Each subproject can be run using the generated jar file `java -jar [jarFileName.jar]`, or using `./gradlew :[projectName]:run`
### Parse the data with a time_init, time_end
This tool will parse a given log and output the obtained data into a file. The file will be stored in the output folder and will contain a list of hostnames connected to the given host during the given period.
#### Arguments
- filepath: Filepath of the file to be processed (String)
- initDateTime: Timestamp in milliseconds or a String with the following format "yyyy-MM-dd HH:mm:ss". This timestamp will determine the start datetime of the period
- endDateTime: Timestamp in milliseconds or a String with the following format "yyyy-MM-dd HH:mm:ss". This timestamp will determine the end datetime of the period
- targetHostname: Destination hostname of the connection

Example:

**gradlew**
```sh
./gradlew :listofhostnames:run --args "'inputLogs/input-file-10000 (12).txt' '2019-08-12 22:00:30' 1565647444398  Rishima"
```
**jar**
```sh
java -jar listofhostnames/build/libs/listofhostnames-1.0.jar 'inputLogs/input-file-10000 (12).txt' '2019-08-12 22:00:30' 1565647444398  Rishima
```
### Unlimited Input Parser
The tool should both parse previously written log files and terminate or collect input from a new log file while it's being written and run indefinitely. The script will print each hour the following data: 
- a list of hostnames connected to a given (configurable) host during the last hour
- a list of hostnames received connections from a given (configurable) host during the last hour
- the hostname that generated most connections in the last hour

The hostnames can be modified during the runtime, modifying the *properties.ini* file

#### Arguments
- hostToConnect: Destination hostname of the connection to list the hostnames connected to this hostname (String)
- hostConnectedFrom: Origin hostname of the connection to list the hostnames who received connections from this hostname (String)
- filePath: Log filepath. This file can be logging while the application is running, so the application will read the logs in real time, or can be written file. This argument is optional, and if it is no data on it, the application will get the data form the standard input.

Example:

**gradlew**
```sh
./gradlew :unlimitedinputparser:run hostname1 hostname2 'inputLogs/input-file-10000 (12).txt'
```
**jar**
```sh
tail -f 'inputLogs/input-file-10000 (12).txt'|java -jar unlimitedinputparser-1.0.jar hostname1 hostname1
```
### Infinite log generator
This application will generate a log with the following format: <unix_timestamp> <hostname> <hostname>

The unix timestamp will have an error of maximum 5 minutes. The log will be stored in a folder called inputLogs

Example:
  
**gradlew**
```sh
./gradlew :infiniteloggenerator:run
```
