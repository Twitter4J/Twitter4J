<<<<<<< HEAD
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo %JAVA% %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.SendDirectMessage %*
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.SendDirectMessage %*

=======
echo off
SETLOCAL enabledelayedexpansion
cd ..
call setEnv.cmd

echo on
"%JAVA%" %MEM_ARGS% -classpath "%CLASSPATH%" twitter4j.examples.directmessage.SendDirectMessage %*

>>>>>>> riceisland
ENDLOCAL