package org.apache.jmeter.protocol.mqtt.sampler;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;

public abstract class BaseMQTTSampler extends AbstractSampler {
    private static final long serialVersionUID = 240L;
    //++ These are JMX file attribute names and must not be changed
    private static final String PROVIDER_URL = "mqtt.provider_url"; // $NON-NLS-1$
    // N.B. Cannot change value, as that is used in JMX files
    private static final String DEST = "mqtt.topic"; // $NON-NLS-1$
    private static final String PRINCIPAL = "mqtt.security_principle"; // $NON-NLS-1$
    private static final String CREDENTIALS = "mqtt.security_credentials"; // $NON-NLS-1$
    private static final String ITERATIONS = "mqtt.iterations"; // $NON-NLS-1$
    private static final String USE_AUTH = "mqtt.authenticate"; // $NON-NLS-1$
    // Is Destination setup static? else dynamic
    private static final String DESTINATION_STATIC = "mqtt.destination_static"; // $NON-NLS-1$
    private static final boolean DESTINATION_STATIC_DEFAULT = true; // default to maintain compatibility
    //-- End of JMX file attribute names
    // See BUG 45460. We need to keep the resource in order to interpret existing files
    private static final String REQUIRED = JMeterUtils.getResString("mqtt_auth_required"); // $NON-NLS-1$
    private static final String READ_RESPONSE = "mqtt.read_response"; // $NON-NLS-1$
   
    /**
     * Constructor
     */
    public BaseMQTTSampler() {
	}

    @Override
	public SampleResult sample(Entry e) {
    	 return this.sample();
	}
	public abstract SampleResult sample() ;
	
	  // ------------- get/set properties ----------------------//
	
	 /**
     * 
     * @param url the provider URL
     */
    public void setProviderUrl(String url) {
        setProperty(PROVIDER_URL, url);
    }
    
    /**
     * 
     * @return the provider URL
     */
    public String getProviderUrl() {
        return getPropertyAsString(PROVIDER_URL);
    }
    /**
     * set the destination (topic or queue name)
     *
     * @param dest the destination
     */
    public void setDestination(String dest) {
        setProperty(DEST, dest);
    }
    /**
     * return the destination (topic or queue name)
     *
     * @return the destination
     */
    public String getDestination() {
        return getPropertyAsString(DEST);
    }
    
    /**
     * set the username to login into the mqtt server if needed
     *
     * @param user
     */
    public void setUsername(String user) {
        setProperty(PRINCIPAL, user);
    }

    /**
     * return the username used to login to the mqtt server
     *
     * @return the username used to login to the mqtt server
     */
    public String getUsername() {
        return getPropertyAsString(PRINCIPAL);
    }
    /**
     * Set the password to login to the mqtt server
     *
     * @param pwd
     */
    public void setPassword(String pwd) {
        setProperty(CREDENTIALS, pwd);
    }

    /**
     * return the password used to login to the mqtt server
     *
     * @return the password used to login to the mqtt server
     */
    public String getPassword() {
        return getPropertyAsString(CREDENTIALS);
    }
    /**
     * set the number of iterations the sampler should aggregate
     *
     * @param count
     */
    public void setIterations(String count) {
        setProperty(ITERATIONS, count);
    }

    /**
     * get the iterations as string
     *
     * @return the number of iterations
     */
    public String getIterations() {
        return getPropertyAsString(ITERATIONS);
    }
    
    /**
     * return the number of iterations as int instead of string
     *
     * @return the number of iterations as int instead of string
     */
    public int getIterationCount() {
        return getPropertyAsInt(ITERATIONS);
    }

    /**
     * Set whether authentication is required for mqtt server
     *
     * @param useAuth
     */
    public void setUseAuth(boolean useAuth) {
        setProperty(USE_AUTH, useAuth);
    }
    /**
     * 
     *
     * @return whether mqtt server requires authentication
     */
    public boolean isUseAuth() {
        final String useAuth = getPropertyAsString(USE_AUTH);
        return useAuth.equalsIgnoreCase("true") || useAuth.equals(REQUIRED); // $NON-NLS-1$
    }
    /**
     * if the sampler should use a static destination, call the method with true
     *
     * @param isStatic
     */
    public void setDestinationStatic(boolean isStatic) {
        setProperty(DESTINATION_STATIC, isStatic, DESTINATION_STATIC_DEFAULT);
    }

    /**
     * return whether the sampler should use a static destination.
     *
     * @return  whether the sampler should use a static destination.
     */
    public boolean isDestinationStatic(){
        return getPropertyAsBoolean(DESTINATION_STATIC, DESTINATION_STATIC_DEFAULT);
    }
    
    /**
     * set whether the sampler should read the response or not
     *
     * @param read whether the sampler should read the response or not
     */
    public void setReadResponse(String read) {
        setProperty(READ_RESPONSE, read);
    }
    /**
     * return whether the sampler should read the response as a boolean value
     *
     * @return whether the sampler should read the response as a boolean value
     */
    public boolean getReadResponseAsBoolean() {
        return getPropertyAsBoolean(READ_RESPONSE);
    }
	
}
