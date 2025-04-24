#!/bin/bash
mvn clean -DskipTests install -e -X
cd docs-web
mvn jetty:run