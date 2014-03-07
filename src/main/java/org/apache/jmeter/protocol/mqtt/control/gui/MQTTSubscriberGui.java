/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.mqtt.control.gui;

import java.awt.BorderLayout;

import javax.naming.Context;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.JLabeledRadioI18N;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.mqtt.sampler.SubscriberSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledPasswordField;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * This is the GUI for mqtt Subscriber <br>
 *
 */
public class MQTTSubscriberGui extends AbstractSamplerGui implements ChangeListener {

    private static final long serialVersionUID = 240L;

    private final JLabeledTextField urlField = new JLabeledTextField(JMeterUtils.getResString("mqtt_provider_url")); // $NON-NLS-1$
    private final JLabeledTextField mqttDestination = new JLabeledTextField(JMeterUtils.getResString("mqtt_topic")); // $NON-NLS-1$
    private final JLabeledTextField mqttDurableSubscriptionId = new JLabeledTextField(JMeterUtils.getResString("mqtt_durable_subscription_id")); // $NON-NLS-1$
    private final JLabeledTextField mqttClientId = new JLabeledTextField(JMeterUtils.getResString("mqtt_client_id")); // $NON-NLS-1$
    private final JLabeledTextField mqttUser = new JLabeledTextField(JMeterUtils.getResString("mqtt_user")); // $NON-NLS-1$
    private final JLabeledTextField mqttPwd = new JLabeledPasswordField(JMeterUtils.getResString("mqtt_pwd")); // $NON-NLS-1$
    private final JLabeledTextField iterations = new JLabeledTextField(JMeterUtils.getResString("mqtt_itertions")); // $NON-NLS-1$
    private final JCheckBox useAuth = new JCheckBox(JMeterUtils.getResString("mqtt_use_auth"), false); //$NON-NLS-1$
    private final JCheckBox readResponse = new JCheckBox(JMeterUtils.getResString("mqtt_read_response"), true); // $NON-NLS-1$
    private final JLabeledTextField timeout =  new JLabeledTextField(JMeterUtils.getResString("mqtt_timeout")); //$NON-NLS-1$
    private final JLabeledTextField separator =  new JLabeledTextField(JMeterUtils.getResString("mqtt_separator")); //$NON-NLS-1$

    //++ Do not change these strings; they are used in JMX files to record the button settings
    public static final String RECEIVE_RSC = "mqtt_subscriber_receive"; // $NON-NLS-1$
    public static final String ON_MESSAGE_RSC = "mqtt_subscriber_on_message"; // $NON-NLS-1$
    //--
    // Button group resources
    private static final String[] CLIENT_ITEMS = { RECEIVE_RSC, ON_MESSAGE_RSC };
    private final JLabeledRadioI18N clientChoice = new JLabeledRadioI18N("mqtt_client_type", CLIENT_ITEMS, RECEIVE_RSC); // $NON-NLS-1$
    private final JCheckBox stopBetweenSamples = new JCheckBox(JMeterUtils.getResString("mqtt_stop_between_samples"), true); // $NON-NLS-1$
    // These are the names of properties used to define the labels
    private static final String DEST_SETUP_STATIC = "mqtt_dest_setup_static"; // $NON-NLS-1$
    private static final String DEST_SETUP_DYNAMIC = "mqtt_dest_setup_dynamic"; // $NON-NLS-1$
    // Button group resources
    private static final String[] DEST_SETUP_ITEMS = { DEST_SETUP_STATIC, DEST_SETUP_DYNAMIC };
    private final JLabeledRadioI18N destSetup =  new JLabeledRadioI18N("mqtt_dest_setup", DEST_SETUP_ITEMS, DEST_SETUP_STATIC); // $NON-NLS-1$
    
    public MQTTSubscriberGui() {
        init();
    }

    @Override
    public String getLabelResource() {
        return "mqtt_subscriber_title"; // $NON-NLS-1$
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    @Override
    public TestElement createTestElement() {
        SubscriberSampler sampler = new SubscriberSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement s) {
        SubscriberSampler sampler = (SubscriberSampler) s;
        this.configureTestElement(sampler);
        sampler.setProviderUrl(urlField.getText());
        sampler.setDestination(mqttDestination.getText());
        sampler.setDurableSubscriptionId(mqttDurableSubscriptionId.getText());
        sampler.setClientID(mqttClientId.getText());
        sampler.setUsername(mqttUser.getText());
        sampler.setPassword(mqttPwd.getText());
        sampler.setUseAuth(useAuth.isSelected());
        sampler.setIterations(iterations.getText());
        sampler.setReadResponse(String.valueOf(readResponse.isSelected()));
        sampler.setClientChoice(clientChoice.getText());
        sampler.setStopBetweenSamples(stopBetweenSamples.isSelected());
        sampler.setTimeout(timeout.getText());
        sampler.setDestinationStatic(destSetup.getText().equals(DEST_SETUP_STATIC));
        sampler.setSeparator(separator.getText());
    }

    /**
     * init() adds jndiICF to the mainPanel. The class reuses logic from
     * SOAPSampler, since it is common.
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JPanel mainPanel = new VerticalPanel();
        add(mainPanel, BorderLayout.CENTER);      
        urlField.setToolTipText(Context.PROVIDER_URL);
        mqttUser.setToolTipText(Context.SECURITY_PRINCIPAL);
        mqttPwd.setToolTipText(Context.SECURITY_CREDENTIALS);
        mainPanel.add(urlField);
        mainPanel.add(createDestinationPane());
        mainPanel.add(mqttDurableSubscriptionId);
        mainPanel.add(mqttClientId);
        mainPanel.add(useAuth);
        mainPanel.add(mqttUser);
        mainPanel.add(mqttPwd);
        mainPanel.add(iterations);
        mainPanel.add(readResponse);
        mainPanel.add(timeout);        
        JPanel choice = new HorizontalPanel();
        choice.add(clientChoice);
        choice.add(stopBetweenSamples);
        mainPanel.add(choice);
        mainPanel.add(separator);           
        useAuth.addChangeListener(this);
    }

    /**
     * the implementation loads the URL and the soap action for the request.
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        SubscriberSampler sampler = (SubscriberSampler) el;  
        urlField.setText(sampler.getProviderUrl());        
        mqttDestination.setText(sampler.getDestination());
        mqttDurableSubscriptionId.setText(sampler.getDurableSubscriptionId());
        mqttClientId.setText(sampler.getClientId());    
        mqttUser.setText(sampler.getUsername());
        mqttPwd.setText(sampler.getPassword());
        iterations.setText(sampler.getIterations());
        useAuth.setSelected(sampler.isUseAuth());
        mqttUser.setEnabled(useAuth.isSelected());
        mqttPwd.setEnabled(useAuth.isSelected());
        readResponse.setSelected(sampler.getReadResponseAsBoolean());
        clientChoice.setText(sampler.getClientChoice());
        stopBetweenSamples.setSelected(sampler.isStopBetweenSamples());
        timeout.setText(sampler.getTimeout());
        separator.setText(sampler.getSeparator());
        destSetup.setText(sampler.isDestinationStatic() ? DEST_SETUP_STATIC : DEST_SETUP_DYNAMIC);
    }

    @Override
    public void clearGui(){
        super.clearGui();
        urlField.setText(""); // $NON-NLS-1$
        mqttDestination.setText(""); // $NON-NLS-1$
        mqttDurableSubscriptionId.setText(""); // $NON-NLS-1$
        mqttClientId.setText(""); // $NON-NLS-1$     
        mqttUser.setText(""); // $NON-NLS-1$
        mqttPwd.setText(""); // $NON-NLS-1$
        iterations.setText("1"); // $NON-NLS-1$
        timeout.setText(""); // $NON-NLS-1$
        separator.setText(""); // $NON-NLS-1$
        useAuth.setSelected(false);
        mqttUser.setEnabled(false);
        mqttPwd.setEnabled(false);
        readResponse.setSelected(true);
        clientChoice.setText(RECEIVE_RSC);
        stopBetweenSamples.setSelected(false);
        destSetup.setText(DEST_SETUP_STATIC);
    }

    /**
     * When the state of a widget changes, it will notify the gui. the method
     * then enables or disables certain parameters.
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == useAuth) {
            mqttUser.setEnabled(useAuth.isSelected());
            mqttPwd.setEnabled(useAuth.isSelected());
        }
    }
    
    private JPanel createDestinationPane() {
        JPanel pane = new JPanel(new BorderLayout(3, 0));
        pane.add(mqttDestination, BorderLayout.CENTER);
        destSetup.setLayout(new BoxLayout(destSetup, BoxLayout.X_AXIS));
        pane.add(destSetup, BorderLayout.EAST);
        return pane;
    }
}