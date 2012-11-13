#!/bin/sh
# Copyright (c) 2006 Harri Kaimio
# Based on Freemind startup script, copyright (c) 2004 Joerg Mueller
# 
# This file is part of Photovault.
#
# Photovault is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# Photovault is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Photovault; if not, write to the Free Software Foundation,
# Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA

_debug() {
	if [ -n "${DEBUG}" ]
	then
		echo "DEBUG:   $1" >&2 
		shift
		for text in "$@"
		do
			echo "         ${text}" >&2
		done
	fi
}

_error() {
	echo "ERROR:   $1" >&2
	shift
	for text in "$@"
	do
		echo "         ${text}" >&2
	done
}

findjava() {
	# We try hard to find the proper 'java' command
	if [ -n "${JAVACMD}" ] && [ -x "${JAVACMD}" ]
	then
		_debug "Using \$JAVACMD to find java virtual machine."
	elif [ -n "${JAVA_BINDIR}" ] && [ -x "${JAVA_BINDIR}/java" ]
	then
		JAVACMD="${JAVA_BINDIR}/java"
		_debug "Using \$JAVA_BINDIR to find java virtual machine."
	elif [ -n "${JAVA_HOME}" ] && [ -x "${JAVA_HOME}/bin/java" ]
	then
		JAVACMD="${JAVA_HOME}/bin/java"
		_debug "Using \$JAVA_HOME to find java virtual machine."
	else
		JAVACMD=$(which java)
		if [ -n "${JAVACMD}" ] && [ -x "${JAVACMD}" ]
		then
			_debug "Using \$PATH to find java virtual machine."
		elif [ -x /usr/bin/java ]
		then
			_debug "Using /usr/bin/java to find java virtual machine."
			JAVACMD=/usr/bin/java
		fi
	fi

	# if we were successful, we return 0 else we complain and return 1
	if [ -n "${JAVACMD}" ] && [ -x "${JAVACMD}" ]
	then
		_debug "Using '$JAVACMD' as java virtual machine..."
		if [ -n "${DEBUG}" ]
		then
			"$JAVACMD" -version
		fi
		return 0
	else
		_error "Couldn't find a java virtual machine," \
		       "define JAVACMD, JAVA_BINDIR, JAVA_HOME or PATH."
		return 1
	fi
}

_source() {
	if [ -f "$1" ]
	then
		_debug "Sourcing '$1'."
		. "$1"
	fi
}

_debug "Photovault parameters are '${@}'."
_debug "$(uname -a)"

findjava
if [ $? -ne 0 ]
then
	exit 1
fi

pvpath=$(dirname "$0")
pvpath="${pvpath%/bin}"

for jar in "${PVAULT_BASE_DIR}" \
	"${pvpath}" "${pvpath}/share/photovault" "${pvpath}/photovault"
do
	if [ -f "${jar}/lib/photovault.jar" ]
	then
		pvdir="${jar}"
		_debug "Photovault Directory is '${jar}'."
		break
	fi
done

if [ -z "${pvdir}" ]
then
	_error "Couldn't find Photovault under '${freepath}'."
	exit 1
fi


_debug "Calling: '${JAVACMD} -Xmx384M -jar ${jar}/lib/photovault.jar $@"
"${JAVACMD}" -Xmx384M "-Dpv.basedir=${pvdir}" -jar "${jar}/lib/photovault.jar" "$@"
