echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetUserTimeline %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetUserTimeline %*

ENDLOCAL