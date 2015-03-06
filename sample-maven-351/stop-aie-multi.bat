@echo off
setlocal

set arg_count=0
for %%x in (%*) do Set /A arg_count+=1

set show_help=true
if %arg_count%==2 (
  set show_help=false
) else (
     set show_help=true
)

rem show usage for redundant aie-exec flags
if "%1"=="-n" set show_help=true
if "%1"=="-z" set show_help=true

if %show_help%==true (
   echo+
   echo Usage: 
   echo	%0 nodeName "host:port[,host:port,host:port...]"
   echo e.g. %0 node1 "config1:17000,config2:17000,config3:17000"
   echo+
   echo NOTE: double quotes are required when you are specifying more than one config server.
   exit /b
)

call env.bat %1 %2

rem get the nodename
set NODENAME=%1
set ATTIVIO_ARGS=-Dattivio.data.directory="%ATTIVIO_PROJECT%\data-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-Dattivio.log.directory="%ATTIVIO_PROJECT%\logs-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-project="%ATTIVIO_PROJECT%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-node %NODENAME% %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-zooKeeper %ZOOINFO% %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-n sample-maven-351 %ATTIVIO_ARGS%

ECHO Stopping multi-node AIE
attivio.exe -cmd stop -mode remote %ATTIVIO_ARGS%
