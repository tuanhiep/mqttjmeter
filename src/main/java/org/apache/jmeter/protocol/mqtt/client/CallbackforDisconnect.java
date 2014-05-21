package org.apache.jmeter.protocol.mqtt.client;

import org.fusesource.mqtt.client.Callback;

public class CallbackforDisconnect implements Callback<Void> {

	@Override
	public void onSuccess(Void value) {
		System.out.println("Disconnect sucessfully ");
	}

	@Override
	public void onFailure(Throwable value) {
		System.out.println("Did not disconnect sucessfully ");
		System.out.println(value);

	}

}
