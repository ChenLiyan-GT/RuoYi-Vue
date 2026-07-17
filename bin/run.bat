@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Web���̡�
echo.

cd %~dp0
cd ../ruoyi-admin/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

if exist "D:\openjdk-17\bin\java.exe" (
    set "JAVA_EXE=D:\openjdk-17\bin\java.exe"
) else if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

echo Using Java: %JAVA_EXE%
"%JAVA_EXE%" -jar %JAVA_OPTS% ruoyi-admin.jar

cd bin
pause