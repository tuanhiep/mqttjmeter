#!/bin/bash

loop_count=1
ramp_time=1
number_sample=1
i=0 # loop count number
step=200 # step for number of clients 
for ((c=100;c<=10000;c=c+$step))
do
	ANUMCLIENT[$i]=$c;
	i=$i+1;
done
AQOS=(0 1 2)
ARETAIN=(true false)
qos[0]=mqtt_at_most_once
qos[1]=mqtt_at_least_once
qos[2]=mqtt_extactly_once
if [ -d "TestPlans" ]; then
 	rm -r TestPlans
fi
	mkdir TestPlans
for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	echo "generating testplan.$SUFFIX.jmx"
	sed  's/__NUMCLIENT__/'$NUMCLIENT'/g' TestPlan.tmpl.jmx | sed 's/__QOS__/'${qos[$QOS]}'/g'  | sed 's/__RETAIN__/'$RETAIN'/g' | sed 's/__RAMP_TIME__/'$ramp_time'/g' | sed 's/__LOOP_COUNT__/'$loop_count'/g' | sed 's/__NUMBER_SAMPLE__/'$number_sample'/g' >TestPlans/testplan.$SUFFIX.jmx
done
done
done
