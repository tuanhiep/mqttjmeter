#!/bin/bash

echo Please read 16.7 Reducing resource requirements http://jmeter.apache.org/usermanual/best-practices.html
echo Please read 2.4.3 2.4.7 http://jmeter.apache.org/usermanual/get-started.html
echo Please read http://jmeter.apache.org/usermanual/jmeter_distributed_testing_step_by_step.pdf

ANUMCLIENT=( 100 1000 10000)
AQOS=(0 1 2)
ARETAIN=(true false)

JMETERSERVERS=192.168.0.10,192.168.0.11,192.168.0.12,192.168.0.13,192.168.0.14

if [ ! -z "$JMETERSERVERS" ]
then
    REMOTE='--remotestart $JMETERSERVERS'
fi 

for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	running testplan.$SUFFIX.jmx
	$JMETER_HOME/bin/jmeter --nongui --testfile testplan.$SUFFIX.jmx --logfile result.$SUFFIX.jtl --jmeterlogfile log.$SUFFIX.log $REMOTE
done
done
done


# 2.4.3 Non-GUI Mode (Command Line mode)

#    For non-interactive testing, you may choose to run JMeter without the GUI. To do so, use the following command options:

#    -n This specifies JMeter is to run in non-gui mode
#    -t [name of JMX file that contains the Test Plan].
#    -l [name of JTL file to log sample results to].
#    -j [name of JMeter run log file].
#    -r Run the test in the servers specified by the JMeter property "remote_hosts"
#    -R [list of remote servers] Run the test in the specified remote servers

#    The script also lets you specify the optional firewall/proxy server information:

#    -H [proxy server hostname or ip address]
#    -P [proxy server port]

#    Example : jmeter -n -t my_test.jmx -l log.jtl -H my.proxy.server -P 8000

#    If the property jmeterengine.stopfail.system.exit is set to true (default is false), then JMeter will invoke System.exit(1) if it cannot stop all threads. Normally this is not necessary.
