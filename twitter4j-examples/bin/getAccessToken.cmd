echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo on
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.oauth.GetAccessToken %*

ENDLOCAL