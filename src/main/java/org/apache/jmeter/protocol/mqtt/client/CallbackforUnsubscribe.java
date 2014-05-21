package org.apache.jmeter.protocol.mqtt.client;

import org.fusesource.mqtt.client.Callback;

public class CallbackforUnsubscribe implements Callback<Void> {

	@Override
	public void onSuccess(Void value) {
		System.out.println("Unsubscribe sucessfully ");

	}

	@Override
	public void onFailure(Throwable value) {
		System.out.println("Did not unsubscribe sucessfully ");
		System.out.println(value);

	}

}
