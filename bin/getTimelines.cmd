echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA_HOME%\bin\java %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.GetTimelines %*
"%JAVA_HOME%\bin\java" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.GetTimelines %*

ENDLOCAL