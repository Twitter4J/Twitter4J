echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetRetweetsOfMe %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetRetweetsOfMe %*

ENDLOCAL