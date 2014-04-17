#!/bin/bash

ANUMCLIENT=( 100 1000 10000)
AQOS=(0 1 3)
ARETAIN=(true false)
qos[0]=mqtt_at_most_once
qos[1]=mqtt_at_least_once
qos[3]=mqtt_extactly_once
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
	sed  's/__NUMCLIENT__/'$NUMCLIENT'/g' TestPlan.tmpl.jmx | sed 's/__QOS__/'${qos[$QOS]}'/g'  | sed 's/__RETAIN__/'$RETAIN'/g' >TestPlans/testplan.$SUFFIX.jmx
done
done
done
