#!/bin/sh
cd ..
. ./setEnv.sh

RUN_CMD="$JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH examples..account.VerifyCredentials"
echo $RUN_CMD ${1+"$@"}
exec $RUN_CMD ${1+"$@"}
