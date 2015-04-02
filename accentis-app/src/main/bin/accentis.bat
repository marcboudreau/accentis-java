@rem **************************************************************************
@rem * Accentis
@rem *
@rem * Copyright (C) 2007-2012 MSB Software
@rem * 
@rem **************************************************************************

@echo off

set ARGS=
set SEP=

:begin
if "%~1" == "" goto done
set ARGS=%ARGS%%SEP%"%~1"
set SEP= 
shift
goto begin

:done

if defined JAVA_HOME (set JAVA_CMD="%JAVA_HOME%\bin\javaw.exe") else (set JAVA_CMD=javaw.exe)

JAVA_CMD -splash:splash.png -jar ..\lib\${project.build.finalName}.jar %ARGS%
if %errorlevel% == 9009 goto jNotFnd

goto :EOF

:jNotFnd
echo Windows could not find the program javaw.exe
echo To resolve this issue, either add the path to javaw.exe to the PATH environment variable,
echo or define the JAVA_HOME environment variable to refer to the root of the Java installation.
echo.
