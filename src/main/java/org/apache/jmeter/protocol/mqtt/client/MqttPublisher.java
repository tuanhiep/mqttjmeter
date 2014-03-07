package org.apache.jmeter.protocol.mqtt.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
 
public class MqttPublisher extends AbstractJavaSamplerClient implements Serializable, Closeable {
    private static final long serialVersionUID = 1L;    
    private AtomicInteger total = new AtomicInteger(0);
    private FutureConnection connection;
    
    private int fireAndForget;
    
    public static void main(String[] args){
    	
    	String host = "tcps://localhost:8883";
    	String topic = "TEST.MQTT";
    	String message = "This is my test messsage.";
    	int aggregate = 10000;    	
    	MqttPublisher producer = new MqttPublisher();

    	try {
    		producer.setupTest(host, topic);
    		producer.produce(message, topic, aggregate);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
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
    
    public void setupTest(JavaSamplerContext context){
    	String host = context.getParameter( "HOST" );
    	String topic = context.getParameter( "TOPIC" );
    	
    	setupTest(host, topic);
        
    }
    
    public void setupTest(String host, String topic){
    	try {

            this.connection = createConnection(host);
        	
			this.connection.connect().await();
			
	    	
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
    }
    
    private FutureConnection createConnection(String host){
    	
    	try {
    		MQTT client = new MQTT();
			client.setHost(host);
			return client.futureConnection();
		} catch (URISyntaxException e) {
			getLogger().error(e.getMessage());
			return null;
		}
    	
    }
    
    private void produce(JavaSamplerContext context) throws Exception{
    	produce(context.getParameter("MESSAGE"), context.getParameter("TOPIC"), Integer.parseInt(context.getParameter("AGGREGATE")));
    }
    
    private void produce(String message, String topic, int aggregate) throws Exception{

    	for(int i = 0; i < aggregate; ++i){
    		
    		this.connection.publish(topic, message.getBytes(), QoS.EXACTLY_ONCE, false).await();
    		
    		total.incrementAndGet();
    	}
    }
    
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result = new SampleResult();
         
        try {
        	
            result.sampleStart(); // start stopwatch
            
        	produce(context);
 
            result.sampleEnd(); // stop stopwatch
            
            
            result.setSuccessful( true );
            result.setResponseMessage("Sent " + total.get() + " messages total");
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
/**
 * 
 * @return the boolean value which mean the connection of publisher exists or not
 */
    
    public boolean isConnected(){
    	return this.connection.isConnected();
    	
    }

	@Override
	public void close() throws IOException {
		
		if (this.connection!=null) this.connection.disconnect();
	
		
	}
}