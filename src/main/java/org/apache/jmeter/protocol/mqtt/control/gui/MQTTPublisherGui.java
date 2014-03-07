package org.apache.jmeter.protocol.mqtt.control.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.JLabeledRadioI18N;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.mqtt.sampler.PublisherSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledPasswordField;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * @author strongman
 * 
 */
public class MQTTPublisherGui extends AbstractSamplerGui implements
		ChangeListener {

	private static final long serialVersionUID = 240L;
	private static final String ALL_FILES = "*.*"; //$NON-NLS-1$
	/** Take source from the named file */
	public static final String USE_FILE_RSC = "mqtt_use_file"; //$NON-NLS-1$
	/** Take source from a random file */
	public static final String USE_RANDOM_RSC = "mqtt_use_random_file"; //$NON-NLS-1$
	/** Take source from the text area */
	private static final String USE_TEXT_RSC = "mqtt_use_text"; //$NON-NLS-1$
	/** Create a TextMessage */
	public static final String TEXT_MSG_RSC = "mqtt_text_message"; //$NON-NLS-1$
	/** Create a MapMessage */
	public static final String MAP_MSG_RSC = "mqtt_map_message"; //$NON-NLS-1$
	/** Create an ObjectMessage */
	public static final String OBJECT_MSG_RSC = "mqtt_object_message"; //$NON-NLS-1$
	/** Create a BytesMessage */
	public static final String BYTES_MSG_RSC = "mqtt_bytes_message"; //$NON-NLS-1$
	// Button group resources when Bytes Message is selected
	private static final String[] CONFIG_ITEMS_BYTES_MSG = { USE_FILE_RSC,USE_RANDOM_RSC };
	// Button group resources
	private static final String[] CONFIG_ITEMS = { USE_FILE_RSC,USE_RANDOM_RSC, USE_TEXT_RSC };
	// These are the names of properties used to define the labels
	private static final String DEST_SETUP_STATIC = "mqtt_dest_setup_static"; // $NON-NLS-1$
	private static final String DEST_SETUP_DYNAMIC = "mqtt_dest_setup_dynamic"; // $NON-NLS-1$
	// Button group resources
	private static final String[] DEST_SETUP_ITEMS = { DEST_SETUP_STATIC,DEST_SETUP_DYNAMIC };
	private final JLabeledRadioI18N destSetup = new JLabeledRadioI18N("mqtt_dest_setup", DEST_SETUP_ITEMS, DEST_SETUP_STATIC); // $NON-NLS-1$
	private static final String[] MSGTYPES_ITEMS = { TEXT_MSG_RSC, MAP_MSG_RSC,	OBJECT_MSG_RSC, BYTES_MSG_RSC };
	private final JLabeledTextField urlField = new JLabeledTextField(JMeterUtils.getResString("mqtt_provider_url")); //$NON-NLS-1$
	private final JLabeledTextField mqttDestination = new JLabeledTextField(JMeterUtils.getResString("mqtt_topic")); //$NON-NLS-1$
	private final JCheckBox useAuth = new JCheckBox(JMeterUtils.getResString("mqtt_use_auth"), false); //$NON-NLS-1$
	private final JLabeledTextField mqttUser = new JLabeledTextField(JMeterUtils.getResString("mqtt_user")); //$NON-NLS-1$
	private final JLabeledTextField mqttPwd = new JLabeledPasswordField(JMeterUtils.getResString("mqtt_pwd")); //$NON-NLS-1$
	private final JLabeledTextField iterations = new JLabeledTextField(	JMeterUtils.getResString("mqtt_itertions")); //$NON-NLS-1$
	private final FilePanel messageFile = new FilePanel(JMeterUtils.getResString("mqtt_file"), ALL_FILES); //$NON-NLS-1$
	private final FilePanel randomFile = new FilePanel(	JMeterUtils.getResString("mqtt_random_file"), ALL_FILES); //$NON-NLS-1$
	private final JSyntaxTextArea textMessage = new JSyntaxTextArea(10, 50); // $NON-NLS-1$
	private final JLabeledRadioI18N msgChoice = new JLabeledRadioI18N("mqtt_message_type", MSGTYPES_ITEMS, TEXT_MSG_RSC); //$NON-NLS-1$
	private final JLabeledRadioI18N configChoice = new JLabeledRadioI18N("mqtt_config", CONFIG_ITEMS, USE_TEXT_RSC); //$NON-NLS-1$

	public MQTTPublisherGui() {
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);
		JPanel mainPanel = new VerticalPanel();
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(urlField);
		mainPanel.add(createDestinationPane());
		mainPanel.add(createAuthPane());
		mainPanel.add(iterations);
		configChoice.setLayout(new BoxLayout(configChoice, BoxLayout.X_AXIS));
		mainPanel.add(configChoice);
		msgChoice.setLayout(new BoxLayout(msgChoice, BoxLayout.X_AXIS));
		mainPanel.add(msgChoice);
		mainPanel.add(messageFile);
		mainPanel.add(randomFile);
		JPanel messageContentPanel = new JPanel(new BorderLayout());
		messageContentPanel.add(new JLabel(JMeterUtils.getResString("mqtt_text_area")),	BorderLayout.NORTH);
		messageContentPanel.add(new JTextScrollPane(textMessage),BorderLayout.CENTER);
		mainPanel.add(messageContentPanel);
		useAuth.addChangeListener(this);
		configChoice.addChangeListener(this);
		msgChoice.addChangeListener(this);
		
	}

	/**
	 * 
	 * @return JPanel Panel with checkbox to choose  user and password
	 */
	private Component createAuthPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(useAuth);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(mqttUser);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(mqttPwd);
		return panel;
	}

	/**
	 * 
	 * @return JPanel that contains destination infos
	 */
	private Component createDestinationPane() {
		JPanel panel = new JPanel(new BorderLayout(3, 0));
		panel.add(mqttDestination, BorderLayout.WEST);
		destSetup.setLayout(new BoxLayout(destSetup, BoxLayout.X_AXIS));
		panel.add(destSetup, BorderLayout.CENTER);
		return panel;
	}

	/**
	 * To Clear the GUI
	 */
	@Override
	public void clearGui() {
		super.clearGui();
		urlField.setText(""); // $NON-NLS-1$
		mqttDestination.setText(""); // $NON-NLS-1$
		mqttUser.setText(""); // $NON-NLS-1$
		mqttPwd.setText(""); // $NON-NLS-1$
		textMessage.setInitialText(""); // $NON-NLS-1$
		messageFile.setFilename(""); // $NON-NLS-1$
		randomFile.setFilename(""); // $NON-NLS-1$
		msgChoice.setText(""); // $NON-NLS-1$
		configChoice.setText(USE_TEXT_RSC);
		updateConfig(USE_TEXT_RSC);
		msgChoice.setText(TEXT_MSG_RSC);
		iterations.setText("1"); // $NON-NLS-1$
		useAuth.setSelected(false);
		mqttUser.setEnabled(false);
		mqttPwd.setEnabled(false);
		destSetup.setText(DEST_SETUP_STATIC);
	}

	private void setupSamplerProperties(PublisherSampler sampler) {
		this.configureTestElement(sampler);
		sampler.setProviderUrl(urlField.getText());
		sampler.setDestination(mqttDestination.getText());
		sampler.setUsername(mqttUser.getText());
		sampler.setPassword(mqttPwd.getText());
		sampler.setTextMessage(textMessage.getText());
		sampler.setInputFile(messageFile.getFilename());
		sampler.setRandomPath(randomFile.getFilename());
		sampler.setConfigChoice(configChoice.getText());
		sampler.setMessageChoice(msgChoice.getText());
		sampler.setIterations(iterations.getText());
		sampler.setUseAuth(useAuth.isSelected());

	}
		
	
	/**
	 * the implementation loads the URL and the soap action for the request.
	 */
	@Override
	public void configure(TestElement el) {
		super.configure(el);
		PublisherSampler sampler = (PublisherSampler) el;
		urlField.setText(sampler.getProviderUrl());
		mqttDestination.setText(sampler.getDestination());
		mqttUser.setText(sampler.getUsername());
		mqttPwd.setText(sampler.getPassword());
		textMessage.setInitialText(sampler.getTextMessage());
		textMessage.setCaretPosition(0);
		messageFile.setFilename(sampler.getInputFile());
		randomFile.setFilename(sampler.getRandomPath());
		configChoice.setText(sampler.getConfigChoice());
		msgChoice.setText(sampler.getMessageChoice());
		iterations.setText(sampler.getIterations());
		useAuth.setSelected(sampler.isUseAuth());
		mqttUser.setEnabled(useAuth.isSelected());
		mqttPwd.setEnabled(useAuth.isSelected());
		destSetup.setText(sampler.isDestinationStatic() ? DEST_SETUP_STATIC	: DEST_SETUP_DYNAMIC);
		updateChoice(msgChoice.getText());
		updateConfig(sampler.getConfigChoice());
	}
	
	@Override
	public TestElement createTestElement() {
		PublisherSampler sampler = new PublisherSampler();
		setupSamplerProperties(sampler);
		return sampler;
	}
	/**
	 * To Update the parameter of field in the GUI
	 * 
	 * @param command
	 */

	private void updateConfig(String command) {

		if (command.equals(USE_TEXT_RSC)) {
			textMessage.setEnabled(true);
			messageFile.enableFile(false);
			randomFile.enableFile(false);
		} else if (command.equals(USE_RANDOM_RSC)) {
			textMessage.setEnabled(false);
			messageFile.enableFile(false);
			randomFile.enableFile(true);
		} else {
			textMessage.setEnabled(false);
			messageFile.enableFile(true);
			randomFile.enableFile(false);
		}

	}

	@Override
	public String getLabelResource() {
		return "mqtt_publisher"; //$NON-NLS-1$
	}

	@Override
	public void modifyTestElement(TestElement s) {
		PublisherSampler sampler = (PublisherSampler) s;
		setupSamplerProperties(sampler);
		sampler.setDestinationStatic(destSetup.getText().equals(DEST_SETUP_STATIC));

	}

	/**
	 * When we change some parameter by clicking on the GUI
	 */
	@Override
	public void stateChanged(ChangeEvent event) {

		if (event.getSource() == configChoice) {
			updateConfig(configChoice.getText());
		} else if (event.getSource() == msgChoice) {
			updateChoice(msgChoice.getText());
		} else if (event.getSource() == useAuth) {
			mqttUser.setEnabled(useAuth.isSelected());
			mqttPwd.setEnabled(useAuth.isSelected());
		}
	}

	/**
	 * To Update the choice of message to send
	 * 
	 * @param command
	 */
	private void updateChoice(String command) {

		String oldChoice = configChoice.getText();
		if (BYTES_MSG_RSC.equals(command)) {
			String newChoice = USE_TEXT_RSC.equals(oldChoice) ? USE_FILE_RSC : oldChoice;
			configChoice.resetButtons(CONFIG_ITEMS_BYTES_MSG, newChoice);
			textMessage.setEnabled(false);
		} else {
			configChoice.resetButtons(CONFIG_ITEMS, oldChoice);
			textMessage.setEnabled(true);
		}
		validate();
	}

}
