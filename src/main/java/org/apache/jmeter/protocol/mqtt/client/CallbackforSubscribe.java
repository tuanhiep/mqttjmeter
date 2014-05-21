package org.apache.jmeter.protocol.mqtt.client;

import org.fusesource.mqtt.client.Callback;

public class CallbackforSubscribe implements Callback<byte[]> {

	
	
	
	@Override
	public void onSuccess(byte[] qoses) {
				System.out.println("Subscribe sucessfully ");
			}

	@Override
	public void onFailure(Throwable value) {
		System.out.println("Did not subscribe");
		System.out.println(value);

		
	}

}
