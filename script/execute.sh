#!/bin/bash

target="azureuser@10.0.0.7" # target of the test
master="azureuser@10.0.0.4" # jmeter master of the test
#Generate number of clients (number of connection for each test)
i=0 # loop count number
step=200 # step for number of clients 
for ((c=100;c<=10000;c=c+$step))
do
	ANUMCLIENT[$i]=$c;
	i=$i+1;
done

# Quality of services
AQOS=(0 1 2)
ARETAIN=(true false)

# Jmeter server (slave )
JMETERSERVERS=10.0.0.5

if [ ! -z "$JMETERSERVERS" ]
then
    REMOTE="-R $JMETERSERVERS"
fi 
#----------------------------------Create Directory-----------------------------------------------#
if [ -d ~/METRICS ]
then 
  	rm -r ~/METRICS  
fi
	mkdir ~/METRICS ~/METRICS/CPU ~/METRICS/IO ~/METRICS/MEMO ~/METRICS/Logs ~/METRICS/Results ~/METRICS/Summarisers ~/METRICS/Summarisers/Total

#------------------------------------Run The Test-------------------------------------------------#

for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	echo "running testplan.$SUFFIX.jmx"
	# Start mesuring the metrics
	ssh $target ./getmetrics.sh
	#-------------------------------------------------/
	./../jmeter -n -t TestPlans/testplan.$SUFFIX.jmx -p specific.properties -l ~/METRICS/Results/result.$SUFFIX.jtl -j ~/METRICS/Logs/log.$SUFFIX.log $REMOTE
        cat Logs/log.$SUFFIX.log | grep 'Summariser' > ~/METRICS/Summarisers/summariser.$SUFFIX.log
        cat Logs/log.$SUFFIX.log | grep 'summary =' >> ~/METRICS/Summarisers/Total/total.$QOS.$RETAIN.log
	
	#Stop mesuring load at the target and get the result to local machine 

        ssh $target ./stopgetmetrics.sh
	scp $target:cpuload.log ~/METRICS/CPU/cpuload.$SUFFIX.log
	scp $target:memo.log ~/METRICS/MEMO/memo.$SUFFIX.log
	scp $target:io.log ~/METRICS/IO/io.$SUFFIX.log
	#-------------------------------------------------/
done
done
done

#echo Please read 16.7 Reducing resource requirements http://jmeter.apache.org/usermanual/best-practices.html
#echo Please read 2.4.3 2.4.7 http://jmeter.apache.org/usermanual/get-started.html
#echo Please read http://jmeter.apache.org/usermanual/jmeter_distributed_testing_step_by_step.pdf
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
