#!/bin/bash

# *****************************************************************************
# * Accentis
# *
# * Copyright (C) 2007-2012 MSB Software
# *
# *****************************************************************************

if [ -z "${JAVA_HOME}" ]; then
	JAVA_CMD=javaw
else
	JAVA_CMD="${JAVA_HOME}/bin/javaw"
fi

which "${JAVA_CMD}" > /dev/null
if [ 0 -ne $? ]; then
	echo "Could not find the javaw program."
	echo "To resolve this issue, either add the path to javaw to the PATH environment variable,"
	echo "or define the JAVA_HOME environment variable to refer to the Java installation."
	echo ""
	
	exit
fi

${JAVA_CMD} -splash:splash.png -jar ../lib/${project.build.finalName}.jar "$@"