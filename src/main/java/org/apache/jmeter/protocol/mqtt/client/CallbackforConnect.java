package org.apache.jmeter.protocol.mqtt.client;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class CallbackforConnect implements Callback<Void> {

	private String topics;
	private CallbackConnection connection;
	private CallbackforSubscribe cbs;
	private QoS qos;

	public CallbackforConnect(String topics, CallbackConnection connection,
			CallbackforSubscribe cbs, QoS qos) {
		super();
		this.topics = topics;
		this.connection = connection;
		this.cbs = cbs;
		this.qos = qos;
	}

	@Override
	public void onSuccess(Void value) {
		System.out.println("Connect sucessfully with topics " + topics);
		String[] topicArray = topics.split("\\s*,\\s*");
		Topic[] Tp = new Topic[topicArray.length];
		for (int i = 0; i < topicArray.length; i++)
			Tp[i] = new Topic(topicArray[i], qos);
		connection.subscribe(Tp, cbs);

	}

	@Override
	public void onFailure(Throwable value) {
		System.out.println(value);
	}

}
