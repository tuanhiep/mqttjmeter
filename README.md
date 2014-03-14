mqtt-jmeter
===========

This is the plugin for Jmeter to Test MQTT protocol

Tuan Hiep
ERODS Team
LIG- Grenoble-France


# Introduction

The MQTT Plugin in Jmeter is used for the injection testing of MQTT server. It permits the complete
test correspond many scenarios, which depend on type of messages, type of connections. Thanks to it's
interface graphic, the fact of testing mqtt protocol is taken easily.


# How to install MQTT plugin in Jmeter

From the repository: https://github.com/tuanhiep/mqtt-jmeter  
Get the source code, go to mqtt-jemeter folder and and use the command maven in terminal (Ubuntu):

	mvn clean install package

to obtain the file **mqtt-jmeter.jar** in **mqtt-jemeter/target**.  
Put the **mqtt-jemeter.jar** in the folder **lib/ext** of Jmeter
(to be downloaded on http://jmeter.apache.org/download_jmeter.cgi ).

Remind that, it's necessary to update the file **ApacheJMeter_core.jar** in the repository lib/ext of Jmeter.
Update the file messages.properties in the folder :/org/apache/jmeter/resources/
in ApacheJMeter_core.jar by new file messages.properties from
https://github.com/tuanhiep/mqtt-jmeter/tree/master/ressource

#  How to use MQTT plugin in Jmeter

##  MQTT Publisher

The interface graphic of Jmeter:

![Alt text](images/Main_Interface_Jmeter.png)

Right-click “Thread” and choose : Add → Sampler → MQTT Publisher

![Alt text](images/MQTT_Publisher.png)

In the principal interface of MQTT Publisher we have the fields:

Name : Name of the MQTT Publisher
Comments: Your comments
Provider URL: the address of MQTT server example: tcp://localhost:1883
Client Id: Your Id in the session with MQTT server example: Noel De Palma
Topic: The topic's name you want to publish
Use Authorization check box : Necessary in the case the connection needs the username and
password
User: Your username
Password: Your password
Number of samples to aggregate : In other way, the number of messages you want to publish to
the MQTT sever in this MQTT Publisher thread, with the value like the configuration below.
Message Type: You can choose : Text, Generated Value, Fixed Value (more detail below)


