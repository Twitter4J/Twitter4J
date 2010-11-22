#/bin/sh
cd ..
. ./setEnv.sh

RUN_CMD="$JAVA_HOME/bin/java $MEM_ARGS -cp $CLASSPATH twitter4j.examples.listmembers.DeleteUserListMember $@"
echo $RUN_CMD
exec $RUN_CMD
