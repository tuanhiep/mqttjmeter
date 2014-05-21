package org.apache.jmeter.protocol.mqtt.client;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Listener;

public class ListenerforSubscribe implements Listener {

	@Override
	public void onConnected() {
		System.out.println("Subscriber is listening");

	}

	@Override
	public void onDisconnected() {
		System.out.println("Subscriber disabled listening");

	}

	@Override
	public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
//		String message = new String(body.getData());
//		System.out.println("Received: "+message);
		ack.run();

	}

	@Override
	public void onFailure(Throwable value) {
		System.out.println("Subscriber couldn't set up listener");
		System.out.println(value);
	}

}
