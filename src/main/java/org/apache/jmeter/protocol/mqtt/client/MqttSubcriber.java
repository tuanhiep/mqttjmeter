package org.apache.jmeter.protocol.mqtt.client;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MqttSubcriber extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final long TIMEOUT = 105000L;

	private FutureConnection connection;

	public static void main(String[] args){
		String host = "tcp://localhost:1883";
		String topic = "TEST.MQTT";
		String clientId = "me";
		boolean durable = false;
		int aggregate = 1000;

		MqttSubcriber consumer = new MqttSubcriber();
		try {
			consumer.setupTest(host, topic, durable, clientId);
			consumer.consume(aggregate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("HOST", "tcp://localhost:1883");
		defaultParameters.addArgument("CLIENT_ID", "${__time(YMDHMS)}${__threadNum}");
		defaultParameters.addArgument("TOPIC", "TEST.MQTT");
		defaultParameters.addArgument("AGGREGATE", "100");
		defaultParameters.addArgument("DURABLE", "false");
		return defaultParameters;
	}

	public void setupTest(JavaSamplerContext context){
		setupTest(context.getParameter( "HOST" ),
				context.getParameter( "TOPIC" ),
				Boolean.parseBoolean(context.getParameter("DURABLE")),
				context.getParameter( "CLIENT_ID" ));
	}

	private void setupTest(String host, String topic, boolean durable, String clientId){
		try {

			this.connection = createConnection(host, clientId, durable);

			this.connection.connect().await();

			this.connection.subscribe(new Topic[]{new Topic(topic, QoS.EXACTLY_ONCE)}).await();

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	private FutureConnection createConnection(String host, String clientId, boolean durable)
			throws URISyntaxException{

		MQTT client = new MQTT();
		client.setHost(host);
		client.setClientId(clientId);
		client.setCleanSession(!durable);
		return client.futureConnection();

	}

	private void consume(JavaSamplerContext context) throws Exception{
		consume(Integer.parseInt(context.getParameter("AGGREGATE")));
	}

	private void consume(int aggregate) throws Exception{

		for(int i = 1; i <= aggregate; ++i){
			Message msg = connection.receive().await(TIMEOUT, TimeUnit.MILLISECONDS);
			if(msg == null){
				getLogger().error("MQTT consumer timed out while waiting for a message. The test has been aborted.");
				return;
			}
			msg.ack();
			System.out.println(new String(msg.getPayload()));
		
//			getLogger().debug("consumed " + i);
		}
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();

		try {

			result.sampleStart(); // start stopwatch

			consume(context);

			result.sampleEnd(); // stop stopwatch


			result.setSuccessful( true );
			result.setResponseMessage("Received" + context.getParameter("AGGREGATE") + " messages");
			result.setResponseCode("OK");
		} catch (Exception e) {
			result.sampleEnd(); // stop stopwatch
			result.setSuccessful(false);
			result.setResponseMessage("Exception: " + e);

			// get stack trace as a String to return as document data
			java.io.StringWriter stringWriter = new java.io.StringWriter();
			e.printStackTrace( new java.io.PrintWriter(stringWriter) );
			result.setResponseData(stringWriter.toString(), null);
			result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
			result.setResponseCode("FAILED");
		}

		return result;
	}
}
