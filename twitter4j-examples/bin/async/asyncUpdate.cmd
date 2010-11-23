<<<<<<< HEAD
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.async.AsyncUpdate %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.async.AsyncUpdate %*

=======
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo on
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.async.AsyncUpdate %*

>>>>>>> riceisland
ENDLOCAL