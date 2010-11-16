echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetHomeTimeline %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.timeline.GetHomeTimeline %*

ENDLOCAL