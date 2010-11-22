#/bin/sh
cd ..
. ./setEnv.sh
RUN_CMD="$JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.stream.PrintFilterStream $@"
echo $RUN_CMD
exec $RUN_CMD