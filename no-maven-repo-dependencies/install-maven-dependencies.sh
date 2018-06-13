#!/bin/bash

clear

#constants
DYNAMODB_DIRECTORY="dynamodb"
GOOGLE_DIRECTORY="google"
IBM_DIRECTORY="ibm"

echo "Installing No Maven Repo dependencies: "$1
echo "Copying jars to repository: "$1

echo "Installing DynamoDB DAX dependency"
mvn install:install-file -Dfile=$DYNAMODB_DIRECTORY/DaxJavaClient-latest.jar \
-DgroupId=com.amazonaws -DartifactId=DaxJavaClient -Dversion=latest -Dpackaging=jar

echo "Installing DynamoDB GEO dependency"
mvn install:install-file -Dfile=$DYNAMODB_DIRECTORY/dynamodb-geo-1.0.0.jar \
-DgroupId=com.amazonaws.geo -DartifactId=dynamodb-geo -Dversion=1.0.0 -Dpackaging=jar

echo "Installing Google Geometry dependency"
mvn install:install-file -Dfile=$GOOGLE_DIRECTORY/geometry-1.0.jar \
-DgroupId=com.google.common -DartifactId=geometry -Dversion=1.0.0 -Dpackaging=jar

echo "Installing IMB MQ All Client dependency"
mvn install:install-file -Dfile=$IBM_DIRECTORY/allclient-1.0.jar \
-DgroupId=com.ibm.mq -DartifactId=allclient -Dversion=1.0 -Dpackaging=jar





