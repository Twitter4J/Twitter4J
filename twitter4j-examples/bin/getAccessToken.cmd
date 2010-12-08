echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo on
"%JAVA_HOME%\bin\java" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.oauth.GetAccessToken %*

ENDLOCAL