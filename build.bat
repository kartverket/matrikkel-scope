@echo off
set JAVA=%JAVA_HOME%\bin\java
set cp=
for %%i in (ant\*.jar) do call ant\cp.bat %%i
set CP=lib\junit.jar;%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\jre\lib\rt.jar;%CP%
%JAVA% -classpath %CP% -Dant.home=ant org.apache.tools.ant.Main -emacs -buildfile build.xml %1 %2 %3 %4 %5 %6

