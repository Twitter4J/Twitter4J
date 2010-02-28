#!/bin/sh
if  [ -z $JAVA_HOME ] ; then
 export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/1.5.0/Home"
fi

for jar in ../*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

for jar in lib/*.jar;do
 export CLASSPATH=$CLASSPATH:$jar
done

export MEM_ARGS="-Xms30m -Xmx30m"