#!/bin/bash
#
source ~/.bashrc

java_use 1.8.0

P=8177
curl -X POST http://localhost:${P}/shutdown || true

export JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -Xms512m -Xmx2048m"
mvn clean verify -Dserver.port=${P}

