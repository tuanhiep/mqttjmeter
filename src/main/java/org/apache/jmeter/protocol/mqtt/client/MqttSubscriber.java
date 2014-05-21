/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License. 

  Copyright 2014 University Joseph Fourier, LIG Laboratory, ERODS Team

 */

package org.apache.jmeter.protocol.mqtt.client;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Random;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.mqtt.control.gui.MQTTSubscriberGui;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttSubscriber extends AbstractJavaSamplerClient implements
		Serializable {
	private static final long serialVersionUID = 1L;
	private CallbackConnection[] connectionArray;
	private static final String mycharset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String getClientId(String clientPrefix, int suffixLength) {
		Random rand = new Random(System.nanoTime() * System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		sb.append(clientPrefix);
		for (int i = 0; i < suffixLength; i++) {
			int pos = rand.nextInt(mycharset.length());
			sb.append(mycharset.charAt(pos));
		}
		return sb.toString();
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("HOST", "tcp://localhost:1883");
		defaultParameters.addArgument("CLIENT_ID",
				"${__time(YMDHMS)}${__threadNum}");
		defaultParameters.addArgument("TOPIC", "TEST.MQTT");
		defaultParameters.addArgument("AGGREGATE", "100");
		defaultParameters.addArgument("DURABLE", "false");
		return defaultParameters;
	}

	public void setupTest(JavaSamplerContext context) {
		String host = context.getParameter("HOST");
		String clientId = context.getParameter("CLIENT_ID");
		if ("TRUE".equalsIgnoreCase(context.getParameter("RANDOM_SUFFIX"))) {
			clientId = MqttPublisher.getClientId(clientId,
					Integer.parseInt(context.getParameter("SUFFIX_LENGTH")));
		}
		if ("FALSE".equals(context.getParameter("PER_TOPIC"))) {
			String topic = context.getParameter("TOPIC");
			if ("TRUE".equals(context.getParameter("AUTH"))) {
				setupTest(host, clientId, topic, context.getParameter("USER"),
						context.getParameter("PASSWORD"), 1,
						Boolean.parseBoolean(context.getParameter("DURABLE")),
						context.getParameter("QOS"));
			} else {
				setupTest(host, clientId, topic, 1,
						Boolean.parseBoolean(context.getParameter("DURABLE")),
						context.getParameter("QOS"));
			}
		} else if ("TRUE".equals(context.getParameter("PER_TOPIC"))) {
			String topics = context.getParameter("TOPIC");
			String[] topicArray = topics.split("\\s*,\\s*");
			int size = topicArray.length;
			if ("TRUE".equals(context.getParameter("AUTH"))) {
				setupTest(host, clientId, topics, context.getParameter("USER"),
						context.getParameter("PASSWORD"), size,
						Boolean.parseBoolean(context.getParameter("DURABLE")),
						context.getParameter("QOS"));
			} else {
				setupTest(host, clientId, topics, size,
						Boolean.parseBoolean(context.getParameter("DURABLE")),
						context.getParameter("QOS"));
			}
		}
	}

	private void setupTest(String host, String clientId, String topic,
			String user, String password, int size, boolean durable,
			String quality) {
		try {
			// Quality
			QoS qos = null;
			if (MQTTSubscriberGui.EXACTLY_ONCE.equals(quality)) {
				qos = QoS.EXACTLY_ONCE;
			} else if (MQTTSubscriberGui.AT_LEAST_ONCE.equals(quality)) {
				qos = QoS.AT_LEAST_ONCE;
			} else if (MQTTSubscriberGui.AT_MOST_ONCE.equals(quality)) {
				qos = QoS.AT_MOST_ONCE;
			}

			this.connectionArray = new CallbackConnection[size];
			JMeterContext jmcx = JMeterContextService.getContext();
			if (size == 1) {
				this.connectionArray[0] = createConnection(host, clientId
						+ jmcx.getThreadNum(), durable, user, password);
				this.connectionArray[0].listener(new ListenerforSubscribe());
				CallbackforSubscribe cbs = new CallbackforSubscribe();
				CallbackforConnect cbc = new CallbackforConnect(topic,
						connectionArray[0], cbs, qos);
				this.connectionArray[0].connect(cbc);

			} else if (size > 1) {

				for (int j = 0; j < size; j++) {
					this.connectionArray[j] = createConnection(host, clientId
							+ jmcx.getThreadNum() + j, durable, user, password);
					this.connectionArray[j]
							.listener(new ListenerforSubscribe());
					CallbackforSubscribe cbs = new CallbackforSubscribe();
					CallbackforConnect cbc = new CallbackforConnect(topic,
							connectionArray[j], cbs, qos);
					this.connectionArray[j].connect(cbc);

				}
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	private void setupTest(String host, String clientId, String topic,
			int size, boolean durable, String quality) {
		try {
			// Quality
			QoS qos = null;
			if (MQTTSubscriberGui.EXACTLY_ONCE.equals(quality)) {
				qos = QoS.EXACTLY_ONCE;
			} else if (MQTTSubscriberGui.AT_LEAST_ONCE.equals(quality)) {
				qos = QoS.AT_LEAST_ONCE;
			} else if (MQTTSubscriberGui.AT_MOST_ONCE.equals(quality)) {
				qos = QoS.AT_MOST_ONCE;
			}
			this.connectionArray = new CallbackConnection[size];
			JMeterContext jmcx = JMeterContextService.getContext();
			if (size == 1) {
				this.connectionArray[0] = createConnection(host, clientId
						+ jmcx.getThreadNum(), durable);
				this.connectionArray[0].listener(new ListenerforSubscribe());
				CallbackforSubscribe cbs = new CallbackforSubscribe();
				CallbackforConnect cbc = new CallbackforConnect(topic,
						connectionArray[0], cbs, qos);
				this.connectionArray[0].connect(cbc);
				System.out.println("Hello setup Test");
			} else if (size > 1) {
				for (int j = 0; j < size; j++) {
					this.connectionArray[j] = createConnection(host, clientId
							+ jmcx.getThreadNum() + j, durable);
					this.connectionArray[j]
							.listener(new ListenerforSubscribe());
					CallbackforSubscribe cbs = new CallbackforSubscribe();
					CallbackforConnect cbc = new CallbackforConnect(topic,
							connectionArray[j], cbs, qos);
					this.connectionArray[j].connect(cbc);
				}
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	private CallbackConnection createConnection(String host, String clientId,
			boolean durable) throws URISyntaxException {

		MQTT client = new MQTT();
		client.setHost(host);
		client.setClientId(clientId);
		client.setCleanSession(!durable);
		return client.callbackConnection();
	}

	private CallbackConnection createConnection(String host, String clientId,
			boolean durable, String user, String password)
			throws URISyntaxException {
		MQTT client = new MQTT();
		client.setHost(host);
		client.setClientId(clientId);
		client.setUserName(user);
		client.setPassword(password);
		client.setCleanSession(!durable);
		return client.callbackConnection();
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		try {
			result.sampleStart(); // start stopwatch
			Thread.sleep(Long.parseLong(context.getParameter("TIMEOUT")));
			result.sampleEnd(); // stop stopwatch
			result.setSuccessful(true);
			result.setResponseMessage("Received "
					+ context.getParameter("AGGREGATE") + " messages");
			result.setResponseCode("OK");
		} catch (Exception e) {
			result.sampleEnd(); // stop stopwatch
			result.setSuccessful(false);
			result.setResponseMessage("Exception: " + e);
			// get stack trace as a String to return as document data
			java.io.StringWriter stringWriter = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(stringWriter));
			result.setResponseData(stringWriter.toString(), null);
			result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
			result.setResponseCode("FAILED");
		}
		return result;
	}

	public void close(JavaSamplerContext context) {

		// Quality
		QoS qos = null;
		if (MQTTSubscriberGui.EXACTLY_ONCE.equals(context.getParameter("QOS"))) {
			qos = QoS.EXACTLY_ONCE;
		} else if (MQTTSubscriberGui.AT_LEAST_ONCE.equals(context
				.getParameter("QOS"))) {
			qos = QoS.AT_LEAST_ONCE;
		} else if (MQTTSubscriberGui.AT_MOST_ONCE.equals(context
				.getParameter("QOS"))) {
			qos = QoS.AT_MOST_ONCE;
		}
		// Topic
		String topics = context.getParameter("TOPIC");
		String[] topicArray = topics.split("\\s*,\\s*");
		Topic[] Tp = new Topic[topicArray.length];
		UTF8Buffer[] Topics = new UTF8Buffer[topicArray.length];
		for (int i = 0; i < topicArray.length; i++) {
			Tp[i] = new Topic(topicArray[i], qos);
			Topics[i] = new UTF8Buffer(Tp[i].toString());
		}
		if (this.connectionArray != null) {
			for (int p = 0; p < this.connectionArray.length; p++) {
				if (this.connectionArray[p] != null) {
					this.connectionArray[p].unsubscribe(Topics,
							new CallbackforUnsubscribe());
					this.connectionArray[p]
							.disconnect(new CallbackforDisconnect());
				}

				this.connectionArray[p] = null;
			}
		}
		this.connectionArray = null;
	}

}
