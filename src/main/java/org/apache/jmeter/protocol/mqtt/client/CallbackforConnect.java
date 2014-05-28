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
	private int size;

	public CallbackforConnect(String topics, CallbackConnection connection,
			CallbackforSubscribe cbs, QoS qos, int size) {
		super();
		this.topics = topics;
		this.connection = connection;
		this.cbs = cbs;
		this.qos = qos;
		this.size=size;
	}

	@Override
	public void onSuccess(Void value) {
		
		if(size==1) {
			Topic[] Tp = new Topic[1];
			Tp[0] = new Topic(topics, qos);
			connection.subscribe(Tp, cbs);
			System.out.println("Connect sucessfully with only one topic " + topics);
		}
		else if (size>1){
			String[] topicArray = topics.split("\\s*,\\s*");
			Topic[] Tp = new Topic[topicArray.length];
			for (int i = 0; i < topicArray.length; i++)
				Tp[i] = new Topic(topicArray[i], qos);
			connection.subscribe(Tp, cbs);
			System.out.println("Connect sucessfully with these topics " + topics);
		}
		

	}

	@Override
	public void onFailure(Throwable value) {
		System.out.println(value);
	}

}
