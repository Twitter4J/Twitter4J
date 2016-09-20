@rem JAVA_HOME=C:\Program Files\Java\jre1.6.0_10

for %%i in (..\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

set MEM_ARGS=-Xms30m -Xmx30m