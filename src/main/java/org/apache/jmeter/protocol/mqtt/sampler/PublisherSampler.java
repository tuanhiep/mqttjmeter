package org.apache.jmeter.protocol.mqtt.sampler;

import java.io.IOException;
import java.util.Date;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.protocol.mqtt.client.MqttPublisher;
import org.apache.jmeter.protocol.mqtt.control.gui.MQTTPublisherGui;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PublisherSampler extends BaseMQTTSampler implements ThreadListener {

	private static final long serialVersionUID = 233L;
	private static final Logger log = LoggingManager.getLoggerForClass();
	// ++ These are JMX file names and must not be changed
	private static final String INPUT_FILE = "mqtt.input_file"; //$NON-NLS-1$
	private static final String RANDOM_PATH = "mqtt.random_path"; //$NON-NLS-1$
	private static final String TEXT_MSG = "mqtt.text_message"; //$NON-NLS-1$
	private static final String CONFIG_CHOICE = "mqtt.config_choice"; //$NON-NLS-1$
	private static final String MESSAGE_CHOICE = "mqtt.config_msg_type"; //$NON-NLS-1$
	public transient MqttPublisher producer = null;
	// These static variables are only used to convert existing files
	private static final String USE_FILE_LOCALNAME = JMeterUtils
			.getResString(MQTTPublisherGui.USE_FILE_RSC);
	private static final String USE_RANDOM_LOCALNAME = JMeterUtils
			.getResString(MQTTPublisherGui.USE_RANDOM_RSC);
	private JavaSamplerContext context = null;

	/**
	 * Constructor
	 */

	public PublisherSampler() {
	}

	// ---------------------Get/Set Property--------------------------//

	public void setInputFile(String file) {
		setProperty(INPUT_FILE, file);

	}

	public void setTextMessage(String message) {
		setProperty(TEXT_MSG, message);

	}

	public void setRandomPath(String path) {
		setProperty(RANDOM_PATH, path);

	}

	public void setConfigChoice(String choice) {
		setProperty(CONFIG_CHOICE, choice);

	}

	public void setMessageChoice(String choice) {
		setProperty(MESSAGE_CHOICE, choice);

	}

	public String getTextMessage() {
		return getPropertyAsString(TEXT_MSG);
	}

	public String getInputFile() {
		return getPropertyAsString(INPUT_FILE);
	}

	public String getRandomPath() {
		return getPropertyAsString(RANDOM_PATH);
	}

	/**
	 * To get the configuration choice
	 */

	public String getConfigChoice() {
		// Allow for the old JMX file which used the local language string
		String config = getPropertyAsString(CONFIG_CHOICE);
		if (config.equals(USE_FILE_LOCALNAME)
				|| config.equals(MQTTPublisherGui.USE_FILE_RSC)) {
			return MQTTPublisherGui.USE_FILE_RSC;
		}
		if (config.equals(USE_RANDOM_LOCALNAME)
				|| config.equals(MQTTPublisherGui.USE_RANDOM_RSC)) {
			return MQTTPublisherGui.USE_RANDOM_RSC;
		}
		return config; // will be the 3rd option, which is not checked
						// specifically
	}

	/**
	 * To get the message choice
	 * 
	 * @return
	 */
	public String getMessageChoice() {
		return getPropertyAsString(MESSAGE_CHOICE);
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

		if (producer == null) {

			try {
				producer = new MqttPublisher();

			} catch (Exception e) {
				log.warn(e.getLocalizedMessage(), e);
			}
		}
	
			String host = getProviderUrl();
			String topic = getDestination();
			String aggregate = "" + getIterationCount();
			String message = getTextMessage();
			Arguments parameters = new Arguments();
			parameters.addArgument("HOST", host);
			parameters.addArgument("CLIENT_ID", "Hiep");
			parameters.addArgument("TOPIC", topic);
			parameters.addArgument("AGGREGATE", aggregate);
			parameters.addArgument("MESSAGE", message);
			this.context = new JavaSamplerContext(parameters);
			this.producer.setupTest(this.context);

	}

	@Override
	public void threadFinished() {
		log.debug("Thread ended " + new Date());
		if (producer != null) {

			try {
				this.producer.close();

			} catch (IOException e) {
				e.printStackTrace();
				log.warn(e.getLocalizedMessage(), e);
			}

		}

	}

	// -------------------------Sample------------------------------------//

	@Override
	public SampleResult sample() {

		
		return this.producer.runTest(context);
	}

	}
