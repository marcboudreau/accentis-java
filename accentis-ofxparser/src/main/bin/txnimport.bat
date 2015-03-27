@rem **************************************************************************
@rem *
@rem * txnimport.bat
@rem *
@rem * Launches the Accentis application.
@rem *
@rem * License text goes here...
@rem *
@rem **************************************************************************

@echo off

set ACCENTIS_HOME=%~dp0

if not "%~1" == "-debug" goto debugProcessed
set DGB_OPTS=-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y,timeout=5000
shift

:debugProcessed
 
set OFX_FILE=%~1

java %DBG_OPTS% -Daccentis.home="%ACCENTIS_HOME%" -jar "%ACCENTIS_HOME%\lib\${project.build.finalName}.jar" "%OFX_FILE%"
