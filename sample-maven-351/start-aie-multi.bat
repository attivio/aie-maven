@echo off
setlocal
call env.bat %1 %2

rem Set the amount of memory for AIE to use
rem set ATTIVIO_MEM=1g

set arg_count=0
for %%x in (%*) do (
    set /A arg_count+=1
    if "%%x"=="-n" goto show_help
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
echo Start a node:
echo %0 nodeName "host:port[,host:port,host:port...]"
echo e.g. %0 node1 "config1:17000,config2:17000,config3:17000"
echo+
echo Install node as a service:
echo %0 nodeName "host:port[,host:port,host:port...]" installService [domain\]userAccount
echo e.g. %0 node1 "config1:17000,config2:17000,config3:17000" installService acme\jsmith
echo+
echo NOTE: double quotes are required when you are specifying more than one config server.
echo+
echo Uninstall the service:
echo %0 nodeName uninstallService
echo e.g. %0 node1 uninstallService
exit /b

:skip_help
set NODENAME=%1
rem uncomment the next line to enable the java debugger on port 8000
rem note that on multinode systems you may need to vary this port for each node if running on the same physical host
rem set ATTIVIO_ARGS=-J-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 %ATTIVIO_ARGS%

set ATTIVIO_ARGS=-Dattivio.data.directory="%ATTIVIO_PROJECT%\data-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-Dattivio.log.directory="%ATTIVIO_PROJECT%\logs-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-project="%ATTIVIO_PROJECT%" %ATTIVIO_ARGS%
set AIE_ARGS=-node %NODENAME%

set AIE_ARGS=-zooKeeper %ZOOINFO% %AIE_ARGS%
set AIE_ARGS=-n sample-maven-351 %AIE_ARGS%

rem handle service startup
set zoo_count=0
for %%x in (%~2) do set /A zoo_count+=1

if "%3"=="installService" (
  echo Installing multi-node AIE service
  set ATTIVIO_SERVICE=-service:install="AIE_multi_%NODENAME%" -service:account=%4 -service:password
) else if %zoo_count% geq 1 (
 	  if "%~2"=="uninstallService" (	
       	     echo Uninstalling multi-node AIE service
     	     set ATTIVIO_SERVICE=-service:uninstall="AIE_multi_%NODENAME%"
	  )  else (
		set ATTIVIO_SERVICE=
	        echo Starting multi-node AIE		
	  )
)

attivio.exe -cmd start -mode remote %ATTIVIO_ARGS% %AIE_ARGS% %ATTIVIO_SERVICE% 
