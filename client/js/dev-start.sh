#!/bin/bash

source ~/.bashrc
java_use 1.8.0

TETRIS_PORT=${TETRIS_PORT:-8177}

kill $( ps aux | grep 'spring-boot' | grep -v grep | awk '{print $2}')
export MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Xms512m -Xmx2048m"
mvn spring-boot:run -Dserver.port=$TETRIS_PORT
