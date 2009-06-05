echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA_HOME%\bin\java %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.AsyncExample %*
"%JAVA_HOME%\bin\java" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.AsyncExample %*

ENDLOCAL