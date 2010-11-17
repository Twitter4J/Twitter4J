echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.stream.PrintFiltetrStream %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.stream.PrintFilterStream %*

ENDLOCAL