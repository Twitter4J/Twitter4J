#/bin/sh
. ./setEnv.sh

echo $JAVA_HOME/bin/java $MEM_ARGS -cp ./classes:$CLASSPATH twitter4j.examples.GetDirectMessages $@
$JAVA_HOME/bin/java $MEM_ARGS -cp ./classes:$CLASSPATH twitter4j.examples.GetDirectMessages $@