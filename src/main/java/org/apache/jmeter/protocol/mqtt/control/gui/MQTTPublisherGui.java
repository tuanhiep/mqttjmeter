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
	/** Create a message of type long number */
	public static final String LONG = "mqtt_long_value"; //$NON-NLS-1$
	/** Create a Message of type integer number */
	public static final String INT = "mqtt_int_value"; //$NON-NLS-1$
	/** Create a Message of type double number */
	public static final String DOUBLE = "mqtt_double_value"; //$NON-NLS-1$
	/** Create a Message of type double number */
	public static final String FLOAT = "mqtt_float_value"; //$NON-NLS-1$
	/** Create a Message of type String */
	public static final String STRING = "mqtt_string_value"; //$NON-NLS-1$

	// These are the names of properties used to define the labels
	private static final String DEST_SETUP_STATIC = "mqtt_dest_setup_static"; // $NON-NLS-1$
	private static final String DEST_SETUP_DYNAMIC = "mqtt_dest_setup_dynamic"; // $NON-NLS-1$
	public static final String GENERATED_VALUE = "mqtt_generated_value"; // $NON-NLS-1$
	public static final String FIXED_VALUE = "mqtt_fixed_value";// $NON-NLS-1$
	public static final String PSEUDO = "mqtt_pseudo_random";// $NON-NLS-1$
	public static final String SECURE = "mqtt_secure_random";// $NON-NLS-1$
	public static final String EXACTLY_ONCE = "mqtt_extactly_once";// $NON-NLS-1$
	public static final String AT_LEAST_ONCE = "mqtt_at_least_once";// $NON-NLS-1$
	public static final String AT_MOST_ONCE = "mqtt_at_most_once";// $NON-NLS-1$
	// Button group resources
	private static final String[] DEST_SETUP_ITEMS = { DEST_SETUP_STATIC,DEST_SETUP_DYNAMIC };
	private final JLabeledRadioI18N destSetup = new JLabeledRadioI18N("mqtt_dest_setup", DEST_SETUP_ITEMS, DEST_SETUP_STATIC); // $NON-NLS-1$
	private static final String[] MSGTYPES_ITEMS = { TEXT_MSG_RSC,GENERATED_VALUE,FIXED_VALUE };
	private static final String[] VALTYPES_ITEMS = { INT,LONG,FLOAT,DOUBLE};
	private static final String[] FVALTYPES_ITEMS = {INT,LONG,FLOAT,DOUBLE,STRING};
	private static final String[] RANTYPES_ITEMS = {PSEUDO,SECURE};
	private static final String[] QTYPES_ITEMS = {EXACTLY_ONCE,AT_LEAST_ONCE,AT_MOST_ONCE};
	private final JLabeledTextField urlField = new JLabeledTextField(JMeterUtils.getResString("mqtt_provider_url")); //$NON-NLS-1$
	private final JLabeledTextField mqttDestination = new JLabeledTextField(JMeterUtils.getResString("mqtt_topic")); //$NON-NLS-1$
	private final JCheckBox useAuth = new JCheckBox(JMeterUtils.getResString("mqtt_use_auth"), false); //$NON-NLS-1$
	private final JLabeledTextField mqttUser = new JLabeledTextField(JMeterUtils.getResString("mqtt_user")); //$NON-NLS-1$
	private final JLabeledTextField mqttPwd = new JLabeledPasswordField(JMeterUtils.getResString("mqtt_pwd")); //$NON-NLS-1$
	private final JLabeledTextField iterations = new JLabeledTextField(	JMeterUtils.getResString("mqtt_itertions")); //$NON-NLS-1$
	private final JSyntaxTextArea textMessage = new JSyntaxTextArea(10, 50); // $NON-NLS-1$
	private final JLabeledRadioI18N msgChoice = new JLabeledRadioI18N("mqtt_message_type", MSGTYPES_ITEMS, TEXT_MSG_RSC); //$NON-NLS-1$

	// For messages content
	private final JCheckBox useTimeStamp = new JCheckBox(JMeterUtils.getResString("mqtt_use_time_stamp"), false); // $NON-NLS-1$
	private final JCheckBox useNumberSeq = new JCheckBox(JMeterUtils.getResString("mqtt_use_number_seq"), false); // $NON-NLS-1$
	private final JCheckBox isRetained = new JCheckBox(JMeterUtils.getResString("mqtt_send_as_retained_msg"), false); // $NON-NLS-1$
	private final JLabeledRadioI18N typeQoSValue = new JLabeledRadioI18N("mqtt_qos", QTYPES_ITEMS,EXACTLY_ONCE); //$NON-NLS-1$
	private final JLabeledRadioI18N typeGeneratedValue = new JLabeledRadioI18N("mqtt_type_of_generated_value", VALTYPES_ITEMS,INT); //$NON-NLS-1$
	private final JLabeledRadioI18N typeFixedValue = new JLabeledRadioI18N("mqtt_type_of_fixed_value", FVALTYPES_ITEMS,INT); //$NON-NLS-1$
	private final JLabeledTextField min = new JLabeledTextField(JMeterUtils.getResString("mqtt_min_value")); //$NON-NLS-1$
	private final JLabeledTextField max = new JLabeledTextField(JMeterUtils.getResString("mqtt_max_value")); //$NON-NLS-1$
	private final JLabeledTextField value = new JLabeledTextField(JMeterUtils.getResString("mqtt_value")); //$NON-NLS-1$
	private final JLabeledRadioI18N typeRandom = new JLabeledRadioI18N("mqtt_type_random", RANTYPES_ITEMS,PSEUDO); //$NON-NLS-1$
	private final JLabeledTextField seed = new JLabeledTextField(JMeterUtils.getResString("mqtt_seed_random")); //$NON-NLS-1$
	private final JLabel textArea = new JLabel(JMeterUtils.getResString("mqtt_text_area"));
	private final JTextScrollPane textPanel = new JTextScrollPane(textMessage);
	
	
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
		msgChoice.setLayout(new BoxLayout(msgChoice, BoxLayout.X_AXIS));
		mainPanel.add(msgChoice);
		mainPanel.add(useTimeStamp);
		mainPanel.add(useNumberSeq);
		mainPanel.add(isRetained);
//---------------------------------------QoS --------------------------------------------//
		this.typeQoSValue.setLayout(new BoxLayout(typeQoSValue, BoxLayout.X_AXIS));
		mainPanel.add(this.typeQoSValue);
//----------------------------------Fixed Value Panel------------------------------------//	
		JPanel FPanel = new JPanel();
		typeFixedValue.setLayout(new BoxLayout(typeFixedValue, BoxLayout.Y_AXIS));
		FPanel.add(typeFixedValue);
		FPanel.add(value);
		mainPanel.add(FPanel);
		
//----------------------------------Generated Value Panel--------------------------------//		
		JPanel GPanel = new JPanel();
		typeGeneratedValue.setLayout(new BoxLayout(typeGeneratedValue, BoxLayout.Y_AXIS));
		GPanel.add(typeGeneratedValue);
		GPanel.add(min);		
		GPanel.add(max);
		this.typeRandom.setLayout(new BoxLayout(typeRandom, BoxLayout.Y_AXIS));
		GPanel.add(typeRandom);
		GPanel.add(seed);
		mainPanel.add(GPanel);
//---------------------------------------------------------------------------------------//
		JPanel messageContentPanel = new JPanel(new BorderLayout());
		messageContentPanel.add(this.textArea,	BorderLayout.NORTH);
		messageContentPanel.add(this.textPanel,BorderLayout.CENTER);
		mainPanel.add(messageContentPanel);
		useAuth.addChangeListener(this);
        msgChoice.addChangeListener(this);
		typeFixedValue.addChangeListener(this);
		typeQoSValue.addChangeListener(this);
		typeRandom.addChangeListener(this);
	
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
		msgChoice.setText(""); // $NON-NLS-1$
		updateConfig(USE_TEXT_RSC);
		msgChoice.setText(TEXT_MSG_RSC);
		iterations.setText("1"); // $NON-NLS-1$
		useAuth.setSelected(false);
		mqttUser.setEnabled(false);
		mqttPwd.setEnabled(false);
		destSetup.setText(DEST_SETUP_STATIC);
		this.textArea.setText("");
		
	}

	private void setupSamplerProperties(PublisherSampler sampler) {
		this.configureTestElement(sampler);
		sampler.setProviderUrl(urlField.getText());
		sampler.setDestination(mqttDestination.getText());
		sampler.setUsername(mqttUser.getText());
		sampler.setPassword(mqttPwd.getText());
		sampler.setTextMessage(textMessage.getText());
		sampler.setMessageChoice(msgChoice.getText());
		sampler.setIterations(iterations.getText());
		sampler.setUseAuth(useAuth.isSelected());
		sampler.setQuality(typeQoSValue.getText());
        sampler.setRetained(isRetained.isSelected());
        sampler.setTYPE_FIXED_VALUE(typeFixedValue.getText());
        sampler.setFIXED_VALUE(this.value.getText());
        sampler.setTYPE_RANDOM_VALUE(this.typeRandom.getText());
        sampler.setMAX_RANDOM_VALUE(this.max.getText());
        sampler.setMIN_RANDOM_VALUE(this.min.getText());
        sampler.setTYPE_GENERATED_VALUE(this.typeGeneratedValue.getText());
        sampler.setSEED(this.seed.getText());
        sampler.setUSE_TIMESTAMP(useTimeStamp.isSelected());
        sampler.setUSE_NUMBER_SEQUENCE(useNumberSeq.isSelected());
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
		msgChoice.setText(sampler.getMessageChoice());
		iterations.setText(sampler.getIterations());
		useAuth.setSelected(sampler.isUseAuth());
		mqttUser.setEnabled(useAuth.isSelected());
		mqttPwd.setEnabled(useAuth.isSelected());
		destSetup.setText(sampler.isDestinationStatic() ? DEST_SETUP_STATIC	: DEST_SETUP_DYNAMIC);
		updateChoice(msgChoice.getText());
	
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

	
		if (event.getSource() == msgChoice) {
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
		
		if(TEXT_MSG_RSC.equals(command)){
			
			this.typeGeneratedValue.setVisible(false);
			this.typeFixedValue.setVisible(false);
			this.max.setVisible(false);
			this.min.setVisible(false);
			this.value.setVisible(false);
			this.typeRandom.setVisible(false);
			this.seed.setVisible(false);
			this.textArea.setVisible(true);
			this.textPanel.setVisible(true);
			
			
										}
		else if(GENERATED_VALUE.equals(command)) {

			this.typeFixedValue.setVisible(false);
			this.value.setVisible(false);
			this.textArea.setVisible(false);
			this.textPanel.setVisible(false);
			this.typeGeneratedValue.setVisible(true);
			this.max.setVisible(true);
			this.min.setVisible(true);
			this.typeRandom.setVisible(true);
			this.seed.setVisible(true);
			
		} else if(FIXED_VALUE.equals(command)){

			this.typeGeneratedValue.setVisible(false);
			this.typeFixedValue.setVisible(true);
			this.max.setVisible(false);
			this.min.setVisible(false);
			this.value.setVisible(true);
			this.typeRandom.setVisible(false);
			this.seed.setVisible(false);
			this.textArea.setVisible(false);
			this.textPanel.setVisible(false);
											}				
		validate();
	}

}
