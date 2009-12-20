echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA_HOME%\bin\java %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.StreamingExample %*
"%JAVA_HOME%\bin\java" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.StreamingExample %*

ENDLOCAL