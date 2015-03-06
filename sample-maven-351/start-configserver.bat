@echo off
setlocal
call env.bat %1 %2

set arg_count=0
for %%x in (%*) do (
    set /A arg_count+=1
    if "%%x"=="-d" goto show_help
    if "%%x"=="-z" goto show_help
)

if %arg_count%==2 (
  goto skip_help
) else if %arg_count%==4 (
  if "%3"=="installService" (
     goto skip_help
  )
  else (
       goto show_help
  )
)

:show_help
echo+
echo Usage
echo Start the configuration server:
echo %0 dataDir "host:port[,host:port,host:port...]"
echo e.g. %0 C:\config_data "config1:17000,config2:17000,config3:17000"
echo+
echo Install configuration server as a service:
echo %0 dataDir "host:port[,host:port,host:port...]" installService [domain\]userAccount
echo e.g. %0 C:\config_data "config1:17000,config2:17000,config3:17000" installService acme\jsmith
echo+
echo NOTE: double quotes are required when you are specifying more than one config server.
echo+
echo Uninstall the service:
echo %0 dataDir uninstallService
echo e.g. %0 C:\config_data uninstallService
exit /b

:skip_help
set DATADIR=%1
set LOG_DIR=%ATTIVIO_PROJECT%\logs-configserver
set AIE_ARGS=-Dattivio.log.directory="%LOG_DIR%"

set AIE_ARGS=-z %ZOOINFO% %AIE_ARGS%
set AIE_ARGS=-d %DATADIR% %AIE_ARGS%

rem Set the amount of memory for aie-exec.exe to use
rem set AIE_ARGS=-Xmx512m %AIE_ARGS%

rem handle service startup
set zoo_count=0
for %%x in (%~2) do set /A zoo_count+=1

if "%3"=="installService" (
  echo Installing configuration server
  set ATTIVIO_SERVICE=-service:install="AIE_configserver" -service:account=%4 -service:password
) else if %zoo_count% geq 1 (
 	  if "%~2"=="uninstallService" (	
       	     echo Uninstalling configuration server
     	     set ATTIVIO_SERVICE=-service:uninstall="AIE_configserver"
	  ) else (
      	    	 set ATTIVIO_SERVICE=
      		 echo Starting configuration server
      		 echo To stop the configuration server simply kill this process / window
	  )
)

aie-exec.exe configserver %AIE_ARGS% %ATTIVIO_SERVICE% 
