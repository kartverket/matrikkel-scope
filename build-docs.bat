@echo off
set JAVA=%JAVA_HOME%\bin\java
set cp=
for %%i in (ant\*.jar) do call ant\cp.bat %%i
for %%i in (lib\*.jar) do call ant\cp.bat %%i
set CP=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\jre\lib\rt.jar;%CP%
%JAVA% -classpath %CP% -Dant.home=ant org.apache.tools.ant.Main -emacs -buildfile build-docs.xml %1 %2 %3 %4 %5 %6


