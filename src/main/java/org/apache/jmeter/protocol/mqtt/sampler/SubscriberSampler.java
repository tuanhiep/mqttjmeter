package org.apache.jmeter.protocol.mqtt.sampler;



import org.apache.jmeter.protocol.mqtt.control.gui.MQTTSubscriberGui;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.util.JMeterUtils;

public class SubscriberSampler extends BaseMQTTSampler implements Interruptible, ThreadListener, TestStateListener{

	
	private static final long serialVersionUID = 240L;
	private static final String DURABLE_SUBSCRIPTION_ID = "mqtt.durableSubscriptionId"; // $NON-NLS-1$
    private static final String DURABLE_SUBSCRIPTION_ID_DEFAULT = "";
    private static final String CLIENT_ID = "mqtt.clientId"; // $NON-NLS-1$
    private static final String CLIENT_ID_DEFAULT = ""; // $NON-NLS-1$
    private static final String CLIENT_CHOICE = "mqtt.client_choice"; // $NON-NLS-1$
    private static final String STOP_BETWEEN = "mqtt.stop_between_samples"; // $NON-NLS-1$
    private static final String TIMEOUT = "mqtt.timeout"; // $NON-NLS-1$
    private static final String TIMEOUT_DEFAULT = ""; // $NON-NLS-1$
    private static final String SEPARATOR = "mqtt.separator"; // $NON-NLS-1$
    private static final String SEPARATOR_DEFAULT = ""; // $NON-NLS-1$
    // This was the old value that was checked for
    private static final String RECEIVE_STR = JMeterUtils.getResString(MQTTSubscriberGui.RECEIVE_RSC); // $NON-NLS-1$

    
    
    public SubscriberSampler() {
        super();
    }
    
    
    
    
	@Override
	public void testEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEnded(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testStarted(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void threadStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean interrupt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SampleResult sample() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	public void setDurableSubscriptionId(String durableSubscriptionId) {
		setProperty(DURABLE_SUBSCRIPTION_ID, durableSubscriptionId, DURABLE_SUBSCRIPTION_ID_DEFAULT); 
	}

	public void setClientID(String clientId) {
		   setProperty(CLIENT_ID, clientId, CLIENT_ID_DEFAULT);
		
	}

	public void setClientChoice(String choice) {
		setProperty(CLIENT_CHOICE, choice);
		
	}

	public void setStopBetweenSamples(boolean selected) {
		 setProperty(STOP_BETWEEN, selected, false);   
		
	}

	public void setTimeout(String timeout) {
		 setProperty(TIMEOUT, timeout, TIMEOUT_DEFAULT);  
		
	}

	public void setSeparator(String text) {
		setProperty(SEPARATOR, text, SEPARATOR_DEFAULT);
		
	}

	public String getDurableSubscriptionId() {
		return getPropertyAsString(DURABLE_SUBSCRIPTION_ID);
	}

	public String getClientId() {
		 return getPropertyAsString(CLIENT_ID, CLIENT_ID_DEFAULT);
	}

	public String getClientChoice() {
		 String choice = getPropertyAsString(CLIENT_CHOICE);
        // Convert the old test plan entry (which is the language dependent string) to the resource name
        if (choice.equals(RECEIVE_STR)){
            choice = MQTTSubscriberGui.RECEIVE_RSC;
        } else if (!choice.equals(MQTTSubscriberGui.RECEIVE_RSC)){
            choice = MQTTSubscriberGui.ON_MESSAGE_RSC;
        }
        return choice;
	}

	public boolean isStopBetweenSamples() {
		 return getPropertyAsBoolean(STOP_BETWEEN, false);
	}

	public String getTimeout() {
		return getPropertyAsString(TIMEOUT, TIMEOUT_DEFAULT);
	}

	public String getSeparator() {
		return getPropertyAsString(SEPARATOR, SEPARATOR_DEFAULT);
	}

	


}
