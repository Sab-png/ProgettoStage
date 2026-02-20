#!/bin/bash
clear
mvn clean package -DskipTests
docker-compose build
docker-compose up


