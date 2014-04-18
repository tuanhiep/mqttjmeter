#!/bin/bash

#echo Please read 16.7 Reducing resource requirements http://jmeter.apache.org/usermanual/best-practices.html
#echo Please read 2.4.3 2.4.7 http://jmeter.apache.org/usermanual/get-started.html
#echo Please read http://jmeter.apache.org/usermanual/jmeter_distributed_testing_step_by_step.pdf
#ANUMCLIENT=(10 100 1000 10000)
i=0 # loop count number
step=200 # step for number of clients 
for ((c=100;c<=10000;c=c+$step))
do
	ANUMCLIENT[$i]=$c;
	i=$i+1;
done
AQOS=(0 1 2)
ARETAIN=(true false)

JMETERSERVERS=localhost

if [ ! -z "$JMETERSERVERS" ]
then
    REMOTE="-R $JMETERSERVERS"
fi 
if [ -d "Logs" ]
then 
  	rm -r Logs   
fi
	mkdir Logs
if [ -d "Results" ]
then 
 	rm -r Results 
fi	
	mkdir Results
if [ -d "Summarisers" ]
then
        rm -r Summarisers
fi
	mkdir Summarisers
cd Summarisers
if [ -d "Total" ]
then
        rm -r Total
fi
	mkdir Total
	cd ../
for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	echo "running testplan.$SUFFIX.jmx"
	./../jmeter -n -t TestPlans/testplan.$SUFFIX.jmx -p specific.properties -l Results/result.$SUFFIX.jtl -j Logs/log.$SUFFIX.log #$REMOTE
        cat Logs/log.$SUFFIX.log | grep 'Summariser' > Summarisers/summariser.$SUFFIX.log
        cat Logs/log.$SUFFIX.log | grep 'summary =' >> Summarisers/Total/total.$QOS.$RETAIN.log
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
