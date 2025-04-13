#!bin/bash

#build
mvn clean -DskipTests install 

#run
cd docs-web
mvn jetty:run

#tests
mvn test

#generate docs(with surfire jacoco)
mvn site

# Run tests and generate reports
mvn verify

#with Jacoco
mvn clean verify site

