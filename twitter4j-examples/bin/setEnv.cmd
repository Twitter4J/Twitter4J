@rem JAVA_HOME=C:\Program Files\Java\jre1.6.0_10

if not "%JAVA_HOME%" == "" goto SET_CLASSPATH

set JAVA=java

:SET_CLASSPATH

if not "%JAVA%" == "" set JAVA=%JAVA_HOME%\bin\java

for %%i in (..\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i
for %%i in (..\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

set MEM_ARGS=-Xms30m -Xmx30m