echo off
SETLOCAL enabledelayedexpansion
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.misc.FeedMonitor %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.misc.FeedMonitor %*

ENDLOCAL