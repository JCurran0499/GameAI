#!/bin/bash

if [ -z "$1" ]
then
  echo "========================================"
  echo "1: Build app (maven)"
  echo "2: Build app and run command line"
  echo "3: Run command line (without a build)"
  echo "4: Run full test suite"
  echo "5: Run specific JUnit test"
  echo "========================================"
  echo " "

  read -p "Select: " -r CHOICE

else
  CHOICE="$1"
fi

case $CHOICE in
  1)
    mvn clean package -DskipTests
    ;;
  2)
    mvn clean package -DskipTests
    java -jar target/GameAI-1.0-jar-with-dependencies.jar
    ;;
  3)
    java -jar target/GameAI-1.0-jar-with-dependencies.jar
    ;;
  4)
    mvn test
    ;;
  5)
    read -p "Test class name: " -r TEST_CLASS
    mvn test -Dtest="$TEST_CLASS"
    ;;
  *)
    echo "Invalid selection!"
    ;;
esac
