set JAVA_HOME=C:\bea92\jdk150_06

for %%i in (..\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

set CLASSPATH=%CLASSPATH%;..\classes
set MEM_ARGS=-Xms10m -Xmx10m