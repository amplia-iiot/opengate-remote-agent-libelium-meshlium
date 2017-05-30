#!/bin/bash
if [ "$#" -gt 0 ];
then
	case "$1" in
		debug)
			remountrw
			java -jar -Dorg.slf4j.simpleLogger.logFile=/mnt/user/logs/OpenGate.log -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.levelInBrackets=true -Dorg.slf4j.simpleLogger.dateTimeFormat="[yyyy/MM/dd HH:mm:ss,SSS Z]" -Dorg.slf4j.simpleLogger.defaultLogLevel=debug /mnt/user/OpenGate/odmdevices-libelium-meshlium*.jar /mnt/lib/cfg/OpenGate &
			;;
		*)
			echo "Use: $0 [debug]" >&2
			exit 3
			;;
	esac
else
	remountrw
	java -jar -Dorg.slf4j.simpleLogger.logFile=/mnt/user/logs/OpenGate.log -Dorg.slf4j.simpleLogger.showDateTime=true -Dorg.slf4j.simpleLogger.levelInBrackets=true -Dorg.slf4j.simpleLogger.dateTimeFormat="[yyyy/MM/dd HH:mm:ss,SSS Z]" /mnt/user/OpenGate/odmdevices-libelium-meshlium*.jar /mnt/lib/cfg/OpenGate &
fi
exit 0
