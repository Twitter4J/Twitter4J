#/bin/sh
. ./setEnv.sh

echo $JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.StreamingExample $@
$JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.StreamingExample $@