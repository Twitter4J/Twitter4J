echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo on
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.friendship.ExistsFriendship %*

ENDLOCAL
