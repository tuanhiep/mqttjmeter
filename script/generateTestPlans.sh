#!/bin/bash
i=0
for ((c=100;c<=10000;c=c+200))

do
	
	ANUMCLIENT[$i]=$c;
	i=$i+1;

done
#ANUMCLIENT=(10 100 1000 10000)
AQOS=(0 1 2)
ARETAIN=(true false)
qos[0]=mqtt_at_most_once
qos[1]=mqtt_at_least_once
qos[2]=mqtt_extactly_once
if [ ! -d "TestPlans" ]; then
  mkdir TestPlans
fi
for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	echo "generating testplan.$SUFFIX.jmx"
	sed  's/__NUMCLIENT__/'$NUMCLIENT'/g' TestPlan.tmpl.jmx | sed 's/__QOS__/'${qos[$QOS]}'/g'  | sed 's/__RETAIN__/'$RETAIN'/g' | sed 's/__RAMP_TIME__/'1'/g' >TestPlans/testplan.$SUFFIX.jmx
done
done
done
