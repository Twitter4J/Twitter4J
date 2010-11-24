<<<<<<< HEAD
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.GetDirectMessages %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.GetDirectMessages %*

=======
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo on
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.GetDirectMessages %*

>>>>>>> riceisland
ENDLOCAL