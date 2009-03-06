set JAVA_HOME=C:\Program Files\Java\jre1.5.0_17

for %%i in (..\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i
for %%i in (..\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

set MEM_ARGS=-Xms10m -Xmx10m