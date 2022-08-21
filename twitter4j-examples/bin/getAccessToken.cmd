echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo on
"%JAVA_HOME%\bin\java" %MEM_ARGS% -classpath "%CLASSPATH%" examples..oauth.GetAccessToken %*

ENDLOCAL