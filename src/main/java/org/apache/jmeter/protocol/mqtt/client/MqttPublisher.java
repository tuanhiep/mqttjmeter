package org.apache.jmeter.protocol.mqtt.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.mqtt.control.gui.MQTTPublisherGui;
import org.apache.jmeter.samplers.SampleResult;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class MqttPublisher extends AbstractJavaSamplerClient implements
		Serializable, Closeable {
	private static final long serialVersionUID = 1L;
	private AtomicInteger total = new AtomicInteger(0);
	private FutureConnection connection;

	// public static void main(String[] args){
	//
	// String host = "tcps://localhost:8883";
	// String topic = "TEST.MQTT";
	// String message = "This is my test messsage.";
	// int aggregate = 10000;
	// MqttPublisher producer = new MqttPublisher();
	//
	// try {
	// producer.setupTest(host, topic);
	// producer.produce(message, topic, aggregate);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("HOST", "tcp://localhost:1883");
		defaultParameters.addArgument("CLIENT_ID", "Hiep");
		defaultParameters.addArgument("TOPIC", "TEST.MQTT");
		defaultParameters.addArgument("AGGREGATE", "1");
		defaultParameters.addArgument("MESSAGE", "This is my test message");
		return defaultParameters;
	}

	public void setupTest(JavaSamplerContext context) {
		String host = context.getParameter("HOST");
		String topic = context.getParameter("TOPIC");

		setupTest(host, topic);

	}

	public void setupTest(String host, String topic) {
		try {

			this.connection = createConnection(host);

			this.connection.connect().await();

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	private FutureConnection createConnection(String host) {

		try {
			MQTT client = new MQTT();
			client.setHost(host);
			return client.futureConnection();
		} catch (URISyntaxException e) {
			getLogger().error(e.getMessage());
			return null;
		}

	}

	private void produce(JavaSamplerContext context) throws Exception {

		// ---------------------Type of message -------------------//

		if ("FIXED".equals(context.getParameter("TYPE_MESSAGE"))) {
			produce(context.getParameter("MESSAGE"),
					context.getParameter("TOPIC"),
					Integer.parseInt(context.getParameter("AGGREGATE")),
					context.getParameter("QOS"),
					context.getParameter("RETAINED"),
					context.getParameter("TYPE_VALUE"));

		} else if ("RANDOM".equals(context.getParameter("TYPE_MESSAGE"))) {

		} else if ("TEXT".equals(context.getParameter("TYPE_MESSAGE"))) {
			produce(context.getParameter("MESSAGE"),
					context.getParameter("TOPIC"),
					Integer.parseInt(context.getParameter("AGGREGATE")),
					context.getParameter("QOS"),
					context.getParameter("RETAINED"),
					context.getParameter("TYPE_VALUE"));
		}

	}

	private void produce(String message, String topic, int aggregate,
			String qos, String isRetained, String type_value) throws Exception {

		try {

			// Quality
			QoS quality = null;
			if (MQTTPublisherGui.EXACTLY_ONCE.equals(qos)) {
				quality = QoS.EXACTLY_ONCE;
			} else if (MQTTPublisherGui.AT_LEAST_ONCE.equals(qos)) {
				quality = QoS.AT_LEAST_ONCE;
			} else if (MQTTPublisherGui.AT_MOST_ONCE.equals(qos)) {
				quality = QoS.AT_MOST_ONCE;

			}

			// Retained
			boolean retained = false;
			if ("TRUE".equals(isRetained))
				retained = true;
			// Type of value in content of message
			System.out.println(type_value);
			if (MQTTPublisherGui.INT.equals(type_value)) {

				int msg = Integer.parseInt(message);
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic, Convert.int2ByteArray(msg),
							quality, retained).await();
					total.incrementAndGet();
				}
			} else if (MQTTPublisherGui.LONG.equals(type_value)) {
				long msg = Long.parseLong(message);
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic, Convert.long2ByteArray(msg),
							quality, retained).await();
					total.incrementAndGet();
				}
			} else if (MQTTPublisherGui.DOUBLE.equals(type_value)) {
				double msg = Double.parseDouble(message);
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic,
							Convert.double2ByteArray(msg), quality, retained)
							.await();
					total.incrementAndGet();
				}
			} else if (MQTTPublisherGui.FLOAT.equals(type_value)) {
				float msg = Float.parseFloat(message);
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic,
							Convert.float2ByteArray(msg), quality, retained)
							.await();
					total.incrementAndGet();
				}
			} else if (MQTTPublisherGui.STRING.equals(type_value)) {
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic,
							Convert.string2ByteArray(message), quality,
							retained).await();
					total.incrementAndGet();
				}
			} else if ("TEXT".equals(type_value)) {
				for (int i = 0; i < aggregate; ++i) {
					this.connection.publish(topic,
							Convert.string2ByteArray(message), quality,
							retained).await();
					total.incrementAndGet();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warn(e.getLocalizedMessage(), e);
		}
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();

		try {

			result.sampleStart(); // start stopwatch

			produce(context);

			result.sampleEnd(); // stop stopwatch

			result.setSuccessful(true);
			result.setResponseMessage("Sent " + total.get() + " messages total");
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

	/**
	 * 
	 * @return the boolean value which mean the connection of publisher exists
	 *         or not
	 */

	public boolean isConnected() {
		return this.connection.isConnected();

	}

	@Override
	public void close() throws IOException {

		if (this.connection != null)
			this.connection.disconnect();

	}
}