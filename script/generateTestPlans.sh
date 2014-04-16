#!/bin/bash

ANUMCLIENT=( 100 1000 10000)
AQOS=(mqtt_at_most_once mqtt_at_least_once mqtt_extactly_once)
ARETAIN=(true false)

for NUMCLIENT in ${ANUMCLIENT[*]}
do
for QOS in ${AQOS[*]}
do
for RETAIN in ${ARETAIN[*]}
do
        SUFFIX=$NUMCLIENT.$QOS.$RETAIN
	echo "generating testplan.$SUFFIX.jmx"
	sed  's/__NUMCLIENT__/'$NUMCLIENT'/g' TestPlan.tmpl.jmx | sed 's/__QOS__/'$QOS'/g'  | sed 's/__RETAIN__/'$RETAIN'/g' > testplan.$SUFFIX.jmx
done
done
done
