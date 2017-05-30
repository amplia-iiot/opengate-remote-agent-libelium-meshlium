#!/bin/bash
case "$1" in
	start)
		if [ -f /var/lock/OpenGate ]
		then
			exit 1
		else
			touch /var/lock/OpenGate
			/bin/OpenGate.sh &
		fi
		;;
	stop)
		rm /var/lock/OpenGate
		/usr/bin/killall OpenGate.sh
		PID=`ps aux | grep "OpenGate" | awk '{ print $2 }'`
        kill $PID
		;;
	*)
		echo "Use: $0 start|stop" >&2
		exit 3
		;;
esac
exit 0
