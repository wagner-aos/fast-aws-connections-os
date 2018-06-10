package br.com.fast.aws.connection.hornetq;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;

import br.com.fast.aws.connection.auth.configuration.BaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class HornetqAdapterConfiguration extends BaseConfiguration {

    private String queueName;
    private String topicName;
    private ConnectionFactory connectioFactory;
    private boolean pubSubDomain;
    private Integer acknowledgeMode;
    private boolean subscriptionDurable;
    private boolean sessionTransacted;
    private String clientId;
    private String defaultDestinationName;
    private int deliveryMode;
    private boolean messageIdEnabled;
    private boolean messageTimestampEnabled;
    private long receiveTimeout;
    private boolean pubSubNoLocal;
    private boolean explicitQosEnabled;

    private Formatter formatter = new Formatter();

    @Override
    public String getUser() {
        return user;
    }

    public HornetqAdapterConfiguration withUser(String user) {
        this.user = user;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public HornetqAdapterConfiguration withPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean isUseEndpoint() {
        return useEndpoint;
    }

    public HornetqAdapterConfiguration withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    @Override
    public String getHost() {
        return host;
    }

    public HornetqAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    @Override
    public String getPort() {
        return port;
    }

    public HornetqAdapterConfiguration withPort(String port) {
        this.port = port;
        return this;
    }

    public String getQueueName() {
        return queueName;
    }

    public HornetqAdapterConfiguration withQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getTopicName() {
        return topicName;
    }

    public HornetqAdapterConfiguration withTopicName(String topicName) {
        this.topicName = topicName;
        return this;
    }

    public ConnectionFactory getConnectioFactory() {
        return connectioFactory;
    }

    public HornetqAdapterConfiguration withConnectioFactory(ConnectionFactory connectioFactory) {
        this.connectioFactory = connectioFactory;
        return this;
    }

    public boolean isPubSubDomain() {
        return pubSubDomain;
    }

    public HornetqAdapterConfiguration withPubSubDomain(boolean pubSubDomain) {
        this.pubSubDomain = pubSubDomain;
        return this;
    }

    public Integer getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public HornetqAdapterConfiguration withAcknowledgeMode(Integer acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
        return this;
    }

    public boolean isSubscriptionDurable() {
        return subscriptionDurable;
    }

    public HornetqAdapterConfiguration withSubscriptionDurable(boolean subscriptionDurable) {
        this.subscriptionDurable = subscriptionDurable;
        return this;
    }

    public boolean isSessionTransacted() {
        return sessionTransacted;
    }

    public HornetqAdapterConfiguration withSessionTransacted(boolean sessionTransacted) {
        this.sessionTransacted = sessionTransacted;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public HornetqAdapterConfiguration withClientId() {
        this.clientId = "durable-client-" + formatter.format("%1$d", System.currentTimeMillis());
        return this;
    }

    public String getDefaultDestinationName() {
        return defaultDestinationName;
    }

    public HornetqAdapterConfiguration withDefaultDestinationName(String defaultDestinationName) {
        this.defaultDestinationName = defaultDestinationName;
        return this;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public HornetqAdapterConfiguration withDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
        return this;
    }

    public boolean isMessageIdEnabled() {
        return messageIdEnabled;
    }

    public HornetqAdapterConfiguration withMessageIdEnabled(boolean messageIdEnabled) {
        this.messageIdEnabled = messageIdEnabled;
        return this;
    }

    public boolean isMessageTimestampEnabled() {
        return messageTimestampEnabled;
    }

    public HornetqAdapterConfiguration withMessageTimestampEnabled(boolean messageTimestampEnabled) {
        this.messageTimestampEnabled = messageTimestampEnabled;
        return this;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public HornetqAdapterConfiguration withReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
        return this;
    }

    public boolean isPubSubNoLocal() {
        return pubSubNoLocal;
    }

    public HornetqAdapterConfiguration withPubSubNoLocal(boolean pubSubNoLocal) {
        this.pubSubNoLocal = pubSubNoLocal;
        return this;
    }

    public boolean isExplicitQosEnabled() {
        return explicitQosEnabled;
    }

    public HornetqAdapterConfiguration withExplicitQosEnabled(boolean explicitQosEnabled) {
        this.explicitQosEnabled = explicitQosEnabled;
        return this;
    }

    /**
     * Returns a DefaultJmsListenerContainerFactory for HornetQ Topic Listener
     * 
     * @param connectionFactory
     * @return DefaultJmsListenerContainerFactory
     * @throws JMSException
     */
    public DefaultJmsListenerContainerFactory withListener() throws JMSException {

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(returnUserCredentials());
        factory.setPubSubDomain(pubSubDomain);
        factory.setSessionAcknowledgeMode(acknowledgeMode);
        factory.setSubscriptionDurable(subscriptionDurable);
        factory.setSessionTransacted(sessionTransacted);
        if (clientId != null) {
            factory.setClientId(clientId);
        }
        return factory;
    }

    /**
     * Returns a JmsTemplate for HornetQ Topic Producer.
     * 
     * @param connectionFactory
     * @return
     * @throws JMSException
     */
    public JmsTemplate withJmsTemplate() throws JMSException {

        JmsTemplate jmsTemplate = new JmsTemplate(returnUserCredentials());
        jmsTemplate.setSessionTransacted(sessionTransacted);
        jmsTemplate.setDefaultDestinationName(topicName);
        jmsTemplate.setPubSubDomain(pubSubDomain);
        jmsTemplate.setDeliveryMode(deliveryMode);
        jmsTemplate.setMessageIdEnabled(messageIdEnabled);
        jmsTemplate.setMessageTimestampEnabled(messageTimestampEnabled);
        jmsTemplate.setPubSubNoLocal(pubSubNoLocal);
        jmsTemplate.setExplicitQosEnabled(explicitQosEnabled);
        jmsTemplate.setReceiveTimeout(receiveTimeout);
        return jmsTemplate;
    }

    public ConnectionFactory connectionFactory() throws JMSException {

        hostPortValidate();

        Map<String, Object> params = new HashMap<>();
        params.put(TransportConstants.HOST_PROP_NAME, this.host);
        params.put(TransportConstants.PORT_PROP_NAME, this.port);
        TransportConfiguration transportConfiguration = new TransportConfiguration(
                NettyConnectorFactory.class.getName(), params);

        HornetQConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF,
                transportConfiguration);

        return (ConnectionFactory) cf;
    }

    private UserCredentialsConnectionFactoryAdapter returnUserCredentials() throws JMSException {

        clientValidate();

        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        adapter.setTargetConnectionFactory(connectioFactory);
        adapter.setUsername(user);
        adapter.setPassword(password);
        return adapter;
    }

    private void clientValidate() {
        if (this.user == null) {
            throw new IllegalArgumentException("Usuario não pode ser nulo");
        }
        if (this.password == null) {
            throw new IllegalArgumentException("Password não pode ser nulo");
        }

    }

    private void hostPortValidate() {
        if (this.host == null) {
            throw new IllegalArgumentException("Host não pode ser nulo");
        }
        if (this.port == null) {
            throw new IllegalArgumentException("Port não pode ser nulo");
        }

    }

}
