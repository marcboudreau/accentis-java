#!/bin/bash

accentis_home=`dirname $0`/..
accentis_home=`readlink -f ${accentis_home}`

if [ "$1" == "-debug" ]; then
	dbg_opts=-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y,timeout=5000
	shift
fi

ofx_file=`readlink -f "$1"`

java ${dbg_opts} -Daccentis.home="${accentis_home}" -jar "${accentis_home}/lib/${project.build.finalName}.jar" "${ofx_file}"
