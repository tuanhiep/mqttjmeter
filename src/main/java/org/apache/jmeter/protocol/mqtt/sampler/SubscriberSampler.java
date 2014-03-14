/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at
 
    http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License. 

  Copyright 2014 University Joseph Fourier, LIG Laboratory, ERODS Team

*/

package org.apache.jmeter.protocol.mqtt.sampler;
import java.util.Date;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.mqtt.client.MqttSubscriber;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class SubscriberSampler extends BaseMQTTSampler implements Interruptible, ThreadListener, TestStateListener{

	
	private static final long serialVersionUID = 240L;
	private static final Logger log = LoggingManager.getLoggerForClass();
	private static final String DURABLE_SUBSCRIPTION_ID = "mqtt.durableSubscriptionId"; // $NON-NLS-1$
    private static final String DURABLE_SUBSCRIPTION_ID_DEFAULT = "";
    private static final String CLIENT_ID = "mqtt.clientId"; // $NON-NLS-1$
    private static final String CLIENT_ID_DEFAULT = ""; // $NON-NLS-1$
    private static final String TIMEOUT = "mqtt.timeout"; // $NON-NLS-1$
    private static final String TIMEOUT_DEFAULT = ""; // $NON-NLS-1$
    // This was the old value that was checked for
    public transient MqttSubscriber subscriber = null;
    private JavaSamplerContext context = null;

    
    
    public SubscriberSampler() {
        super();
    }    
	@Override
	public void testEnded() {
			}
	@Override
	public void testEnded(String arg0) {
		testEnded();
	}
	@Override
	public void testStarted() {
	}
	@Override
	public void testStarted(String arg0) {
		 testStarted();
	}
	// ------------------------------ For Thread ---------------------------------//
	
	private void logThreadStart() {
		if (log.isDebugEnabled()) {
			log.debug("Thread started " + new Date());
			log.debug("MQTTSampler: [" + Thread.currentThread().getName()
					+ "], hashCode=[" + hashCode() + "]");
		}
	}
	
	@Override
	public void threadStarted() {
	    
		logThreadStart();		
		if(subscriber==null){
			
			try {
			    subscriber = new MqttSubscriber();
			} catch (Exception e) {
				log.warn(e.getLocalizedMessage(), e);
			}
			}
		String host = getProviderUrl();
		String topic = getDestination();
		String aggregate = "" + getIterationCount();
		String clientId= getClientId();		
		String timeout= this.getTimeout();
		Arguments parameters = new Arguments();
		parameters.addArgument("HOST", host);
		parameters.addArgument("CLIENT_ID", clientId);
		parameters.addArgument("TOPIC", topic);
		parameters.addArgument("AGGREGATE", aggregate);
		parameters.addArgument("DURABLE", "false");
		parameters.addArgument("TIMEOUT", timeout);
		
		if(this.isUseAuth()) {
			parameters.addArgument("AUTH","TRUE");
			parameters.addArgument("USER",getUsername());
			parameters.addArgument("PASSWORD",getPassword());
		} else parameters.addArgument("AUTH","FALSE");
		
		context = new JavaSamplerContext(parameters);
		subscriber.setupTest(context);
		}
	
	@Override
	public void threadFinished() {

		log.debug("Thread ended " + new Date());
		if (this.subscriber != null) {

			try {
				this.subscriber.close();
							
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getLocalizedMessage(), e);
			}
			

		}
	}

	

	@Override
	public boolean interrupt() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SampleResult sample() {
		
		return this.subscriber.runTest(context);
	}

	public void setDurableSubscriptionId(String durableSubscriptionId) {
		setProperty(DURABLE_SUBSCRIPTION_ID, durableSubscriptionId, DURABLE_SUBSCRIPTION_ID_DEFAULT); 
	}

	public void setClientID(String clientId) {
		   setProperty(CLIENT_ID, clientId, CLIENT_ID_DEFAULT);
		
	}
	public void setTimeout(String timeout) {
		 setProperty(TIMEOUT, timeout, TIMEOUT_DEFAULT);  
	}
	public String getDurableSubscriptionId() {
		return getPropertyAsString(DURABLE_SUBSCRIPTION_ID);
	}
	public String getClientId() {
		 return getPropertyAsString(CLIENT_ID, CLIENT_ID_DEFAULT);
	}
	public String getTimeout() {
		return getPropertyAsString(TIMEOUT, TIMEOUT_DEFAULT);
	}
}
