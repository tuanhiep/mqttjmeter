package org.apache.jmeter.protocol.mqtt.client;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
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
	public static int numSeq=0;
	private Random generator = new Random();	
	private SecureRandom secureGenerator= new SecureRandom(); 
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
		String clientId = context.getParameter("CLIENT_ID");

		if("TRUE".equals(context.getParameter("AUTH"))){			
		setupTest(host,clientId,context.getParameter("USER"),context.getParameter("PASSWORD"));
		
		}
		else{
			setupTest(host, clientId);
		}		
		

	}

	public void setupTest(String host, String clientId) {
		try {

			this.connection = createConnection(host,clientId);

			this.connection.connect().await();
			

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}
	public void setupTest(String host, String clientId, String user, String password) {
		try {

			this.connection = createConnection(host,clientId,user,password);

			this.connection.connect().await();
			

		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	private FutureConnection createConnection(String host,String clientId) {

		try {
			MQTT client = new MQTT();
			client.setHost(host);
			client.setClientId(clientId);
			return client.futureConnection();
		} catch (URISyntaxException e) {
			getLogger().error(e.getMessage());
			return null;
		}

	}
	private FutureConnection createConnection(String host,String clientId,String user, String password) {

		try {
			MQTT client = new MQTT();
			client.setHost(host);
			client.setUserName(user);
			client.setPassword(password);
			client.setClientId(clientId);
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
					context.getParameter("TIME_STAMP"),
					context.getParameter("NUMBER_SEQUENCE"),					
					context.getParameter("TYPE_VALUE"));

		} else if ("RANDOM".equals(context.getParameter("TYPE_MESSAGE"))) {

			produceRandomly(context.getParameter("SEED"),context.getParameter("MIN_RANDOM_VALUE"),
					context.getParameter("MAX_RANDOM_VALUE"),context.getParameter("TYPE_RANDOM_VALUE"),
					context.getParameter("TOPIC"),Integer.parseInt(context.getParameter("AGGREGATE")),
					context.getParameter("QOS"),context.getParameter("RETAINED"),
					context.getParameter("TIME_STAMP"),context.getParameter("NUMBER_SEQUENCE"),
					context.getParameter("TYPE_VALUE"));
									
		} else if ("TEXT".equals(context.getParameter("TYPE_MESSAGE"))) {
			produce(context.getParameter("MESSAGE"),
					context.getParameter("TOPIC"),
					Integer.parseInt(context.getParameter("AGGREGATE")),
					context.getParameter("QOS"),
					context.getParameter("RETAINED"),
					context.getParameter("TIME_STAMP"),
					context.getParameter("NUMBER_SEQUENCE"),					
					context.getParameter("TYPE_VALUE"));
		}

	}

	private void produce(String message, String topic, int aggregate,
			String qos, String isRetained, String useTimeStamp, String useNumberSeq,String type_value) throws Exception {

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
			
			for (int i = 0; i < aggregate; ++i) {
					byte[] payload = createPayload(message, useTimeStamp, useNumberSeq, type_value);	
					this.connection.publish(topic,payload,quality, retained).await();
					total.incrementAndGet();
				}
			

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().warn(e.getLocalizedMessage(), e);
		}
	}

	public void produceRandomly(String seed, String min, String max, String type_random,String topic, int aggregate,
			String qos, String isRetained, String useTimeStamp, String useNumberSeq,String type_value){
		

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
			
			for (int i = 0; i < aggregate; ++i) {
					byte[] payload = this.createRandomPayload(seed, min, max, type_random, useTimeStamp, useNumberSeq, type_value);	
					this.connection.publish(topic,payload,quality, retained).await();
					total.incrementAndGet();
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
	
    
	
	
	
	
	
	
	
	
	
	
	
	public byte[] createPayload(String message, String useTimeStamp, String useNumSeq ,String type_value) throws IOException, NumberFormatException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream d = new DataOutputStream(b);
// flags  	
    	byte flags=0x00;
		if("TRUE".equals(useTimeStamp)) flags|=0x80;
		if("TRUE".equals(useNumSeq)) flags|=0x40;
		if (MQTTPublisherGui.INT.equals(type_value)) flags|=0x20;
		if (MQTTPublisherGui.LONG.equals(type_value)) flags|=0x10;
		if (MQTTPublisherGui.FLOAT.equals(type_value)) flags|=0x08;
		if (MQTTPublisherGui.DOUBLE.equals(type_value)) flags|=0x04;
		if (MQTTPublisherGui.STRING.equals(type_value)) flags|=0x02;
		if(!"TEXT".equals(type_value)){
			d.writeByte(flags); 
		}		
// TimeStamp
		if("TRUE".equals(useTimeStamp)){
   		 Date date= new java.util.Date();
    	 d.writeLong(date.getTime());
    	                               }
// Number Sequence
		if("TRUE".equals(useNumSeq)){
   	     d.writeInt(numSeq++);   	
   	    
  	    }
// Value				
  	    if (MQTTPublisherGui.INT.equals(type_value)) {
  			d.writeInt(Integer.parseInt(message));  			
  		} else if (MQTTPublisherGui.LONG.equals(type_value)) {
  			d.writeLong(Long.parseLong(message));  		
  		} else if (MQTTPublisherGui.DOUBLE.equals(type_value)) {
  			d.writeDouble(Double.parseDouble(message));  		
  		} else if (MQTTPublisherGui.FLOAT.equals(type_value)) {
  			d.writeDouble(Float.parseFloat(message));  			
  		} else if (MQTTPublisherGui.STRING.equals(type_value)) {
  			d.write(message.getBytes());  			
  		} else if ("TEXT".equals(type_value)) {
  			d.write(message.getBytes());
  		}       	
		return b.toByteArray();
	}
  
    
    
    
    public byte[] createRandomPayload(String Seed,String min, String max, String type_random, String useTimeStamp, String useNumSeq ,String type_value) throws IOException, NumberFormatException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream d = new DataOutputStream(b);
		
		
		
// flags  	
    	byte flags=0x00;
		if("TRUE".equals(useTimeStamp)) flags|=0x80;
		if("TRUE".equals(useNumSeq)) flags|=0x40;
		if (MQTTPublisherGui.INT.equals(type_value)) flags|=0x20;
		if (MQTTPublisherGui.LONG.equals(type_value)) flags|=0x10;
		if (MQTTPublisherGui.FLOAT.equals(type_value)) flags|=0x08;
		if (MQTTPublisherGui.DOUBLE.equals(type_value)) flags|=0x04;
		if (MQTTPublisherGui.STRING.equals(type_value)) flags|=0x02;
		if(!"TEXT".equals(type_value)){
			d.writeByte(flags); 
		}		
// TimeStamp
		if("TRUE".equals(useTimeStamp)){
   		 Date date= new java.util.Date();
    	 d.writeLong(date.getTime());
    	                               }
// Number Sequence
		if("TRUE".equals(useNumSeq)){
   	     d.writeInt(numSeq++);   	
   	    
  	    }
// Value
		
	if(MQTTPublisherGui.PSEUDO.equals(type_random)){
		    generator.setSeed(Long.parseLong(Seed));
		    if (MQTTPublisherGui.INT.equals(type_value)) {	  	    	
	  			d.writeInt(generator.nextInt(Integer.parseInt(max)-Integer.parseInt(min))+Integer.parseInt(min));  
	   		} else if (MQTTPublisherGui.LONG.equals(type_value)) {	  			
	  			long Max= Long.parseLong(max);
	  			long Min= Long.parseLong(min);
	  			d.writeLong((Math.abs(generator.nextLong() % (Max - Min)) + Min));  		
	  		} else if (MQTTPublisherGui.DOUBLE.equals(type_value)) {
	  			double Max= Double.parseDouble(max);
	  			double Min= Double.parseDouble(min);
	  	      	d.writeDouble((Min+(Max-Min)*generator.nextDouble()));
	  		} else if (MQTTPublisherGui.FLOAT.equals(type_value)) {
	  			float Max= Float.parseFloat(max);
	  			float Min= Float.parseFloat(min);
	  			d.writeFloat((Min+(Max-Min)*generator.nextFloat()));			
	   		} 
			}
	else if(MQTTPublisherGui.SECURE.equals(type_random)){
		
		 secureGenerator.setSeed(Long.parseLong(Seed));
		 if (MQTTPublisherGui.INT.equals(type_value)) {	  	    	
	  			d.writeInt(secureGenerator.nextInt(Integer.parseInt(max)-Integer.parseInt(min))+Integer.parseInt(min));  
	  		} else if (MQTTPublisherGui.LONG.equals(type_value)) {	  			
	  			long Max= Long.parseLong(max);
	  			long Min= Long.parseLong(min);
	  			d.writeLong((Math.abs(secureGenerator.nextLong() % (Max - Min)) + Min));  		
	  		} else if (MQTTPublisherGui.DOUBLE.equals(type_value)) {
	  			double Max= Double.parseDouble(max);
	  			double Min= Double.parseDouble(min);
	  	      	d.writeDouble((Min+(Max-Min)*secureGenerator.nextDouble()));
	  		} else if (MQTTPublisherGui.FLOAT.equals(type_value)) {
	  			float Max= Float.parseFloat(max);
	  			float Min= Float.parseFloat(min);
	  			d.writeFloat((Min+(Max-Min)*secureGenerator.nextFloat()));			
	   		} 
		
		
	}
  	  
  	    
		return b.toByteArray();
	}
	
}