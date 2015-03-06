@echo off
setlocal
call env.bat

rem Set the amount of memory for AIE to use
rem set ATTIVIO_MEM=1g

set arg_count=0
for %%x in (%*) do Set /A arg_count+=1

set show_help=true
if %arg_count%==0 (
  set show_help=false
) else if %arg_count%==1 (
  if "%1"=="uninstallService" (
     set show_help=false   
  )
)else if %arg_count%==2 (
  if "%1"=="installService" (
     set show_help=false   
  )
)

if %show_help%==true (
   echo+
   echo Usage: 
   echo Start the local node:
   echo	%0
   echo+
   echo Install node as a service:
   echo	%0 installService [domain\]userAccount
   echo e.g. %0 installService acme\jsmith
   echo+
   echo Uninstall the service:
   echo %0 uninstallService
   echo e.g. %0 uninstallService
   exit /b
)

set NODENAME=local

rem uncomment the next line to enable the java debugger on port 8000
rem set ATTIVIO_ARGS=-J-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 %ATTIVIO_ARGS%

set ATTIVIO_ARGS=-Dattivio.data.directory="%ATTIVIO_PROJECT%\data-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-Dattivio.log.directory="%ATTIVIO_PROJECT%\logs-%NODENAME%" %ATTIVIO_ARGS%

rem handle service startup
if "%1"=="installService" (
  echo Installing local AIE service
  set ATTIVIO_SERVICE=-service:install="AIE_sample-maven-351_%NODENAME%" -service:account=%2 -service:password
) else if "%1"=="uninstallService" (
  echo Uninstalling local AIE service
  set ATTIVIO_SERVICE=-service:uninstall="AIE_sample-maven-351_%NODENAME%"
) else (
  set ATTIVIO_SERVICE=
)

echo Starting local (non multi-node) AIE
attivio.exe -cmd start -mode local -project="%ATTIVIO_PROJECT%" %ATTIVIO_ARGS% %ATTIVIO_SERVICE% sample-maven-351.xml