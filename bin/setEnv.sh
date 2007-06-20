#!/bin/sh
if $JAVA_HOME; then
 export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home"
fi

for jar in ../lib/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

export CLASSPATH=$CLASSPATH:../classes
export MEM_ARGS="-Xms10m -Xmx10m"