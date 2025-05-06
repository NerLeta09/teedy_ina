#!/bin/bash
mvn clean 
mvn compile install -DskipTests
cd docs-web
mvn jetty:run