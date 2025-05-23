# Java Grep Application
# Introduction
The Grep application is a Java-based tool developed to recursively search for matching string patterns within a specified file directory. It functions similarly to the `grep` command on Linux, identifying matching patterns and storing the results in a file. The application uses core Java Object-Oriented Programming principles such as inheritance, and polymorphism. Streams and Lambda expressions were used to implement functional programming constructs, improving code readability and processing efficiency. Maven was utilized in this project for dependency management, and the project has been containerized using Docker. A custom Docker image, containing the project requirements, has been built and published to Docker Hub. Development was done using IntelliJ IDEA, and Git was used for version control throughout the project.    

# Quick Start
The Grep application can be run through 2 methods:
1. Clone this Git repository, and use Maven to build and run the JAR files. 
2. Pull the pre-built Docker image and create a container. 

Both methods require three parameters: 
- `<pattern>`: String pattern to match 
- `<data_loc>`: File directory to recursively search files in
- `<output_loc>`: Output path to store matching results 

1. Using Maven generated JAR files 
```shell
cd core_java/grep
mvn clean compile package
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp <pattern> <data_loc> <output_loc>
```

2. Using Docker containers
```shell
docker pull 77ruthvik/grep
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
77ruthvik/grep <pattern> <data_loc> <output_loc>
```

# Implementation
The grep application is designed to recursively traverse a user-specified directory, search for lines matching the pattern, and output the matching lines to a file. It is implemented using `JavaGrepImp.java` and `JavaGrepLambdaStreamImp.java` files. They recursively identify all the files in the user-provided directory. Then, they read the content within all the files using BufferedReader/Streams. Following this, they check if any content in each file matches the user-provided pattern. If any matching content is found, it is added to a list. Finally, the list is written to the output path provided by the user using BufferedWriter.

## Pseudocode
The pseudocode of the Grep implementation can be seen below.
```text
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
The first implementation of the Grep application could not process large input files, and led to `OutOfMemoryError`. This issue is caused due to the limited heap size when the application is run in the JVM. This issue has been resolved with the current implementation, as Streams and Lambda expressions are used to optimize memory usage.

# Test
The Grep application has been tested manually by modifying the `Run/Debug Configurations` in IntelliJ IDEA to execute with various pre-defined arguments. Both `JavaGrepImp.java` and `JavaGrepLambdaStreamImp.java` implementations were tested to ensure correct functionality. SLF4J and Log4j were integrated for effective logging and debugging, enabling effective error reporting in the terminal. 

# Deployment
- This application has been packaged into a JAR file using the Maven command: `mvn clean compile package`
- A Dockerfile is created to containerize the application
- The Dockerfile uses Alpine Linux as the base image, copies the JAR file from the local directory into the container, and executes the JAR file
- Docker image is built locally using the command: `docker build -t ${docker_user}/grep .`
- Docker image is pushed to Docker Hub using the command: `docker push ${docker_user}/grep`

# Improvements
In the future, I would like to make the following changes to further improve the functionality of this application.
- Adding support for filtering by file extensions to restrict the search to certain file types.
- Enhancing application performance by updating the `JavaGrep` interface to use Streams instead of lists for data handling.
- Performing comprehensive unit testing for `JavaGrepImp.java` and `JavaGrepLambdaStreamImp.java` using JUnit to ensure correctness and improve maintainability.
