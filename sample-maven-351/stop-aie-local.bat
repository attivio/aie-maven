@echo off
setlocal
call env.bat %1 %2

set NODENAME=local
set ATTIVIO_ARGS=-Dattivio.data.directory="%ATTIVIO_PROJECT%\data-%NODENAME%" %ATTIVIO_ARGS%
set ATTIVIO_ARGS=-Dattivio.log.directory="%ATTIVIO_PROJECT%\logs-%NODENAME%" %ATTIVIO_ARGS%

ECHO Stopping local (non multi-node) AIE
attivio.exe -cmd stop -mode local -project="%ATTIVIO_PROJECT%" %ATTIVIO_ARGS% sample-maven-351.xml 
