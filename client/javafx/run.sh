#!/bin/bash

# export JAVA_HOME=`/usr/libexec/java_home -v 1.8.0_25`

if [ -f target/classes/com/yisoft/javafx/tetris/TetrisApp.class ]; then
	java -cp target/classes com.yisoft.javafx.tetris.TetrisApp &
else
	if [ ! -f target/tetris-*.jar ]; then
		mvn package
	fi
	java -cp target/tetris-*.jar com.yisoft.javafx.tetris.TetrisApp &
fi

# alternatively
# mvn exec:java -Dexec.mainClass="com.yisoft.javafx.tetris.TetrisApp"

