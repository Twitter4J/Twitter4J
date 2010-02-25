#/bin/sh
. ./setEnv.sh

echo $JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.OAuthUpdate "$@"
$JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.OAuthUpdate "$@"