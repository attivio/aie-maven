@echo off
setlocal
call env.bat %1 %1

set arg_count=0
for %%x in (%*) do (
    set /A arg_count+=1
    if "%%x"=="-z" goto show_help
)

if %arg_count%==1 (
  goto skip_help
) else (
  goto show_help
)

:show_help
echo+
echo Usage
echo Stop the performance monitoring server:
echo %0 "host:port[,host:port,host:port...]"
echo e.g. %0 "config1:17000,config2:17000,config3:17000"
exit /b

:skip_help
set LOG_DIR=%ATTIVIO_PROJECT%\logs-perfmon
set AIE_ARGS=-Dattivio.log.directory="%LOG_DIR%"
mkdir %LOG_DIR%
set AIE_ARGS=-z %ZOOINFO% %AIE_ARGS%

echo Stopping performance monitoring server
aie-exec.exe perfmon-stop %AIE_ARGS%
