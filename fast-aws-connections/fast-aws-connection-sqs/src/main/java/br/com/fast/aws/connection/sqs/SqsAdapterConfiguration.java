package br.com.fast.aws.connection.sqs;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class SqsAdapterConfiguration extends AwsBaseConfiguration {

    private AmazonSQS sqsClient;
    private String concurrency;
    private Integer numberOfMessagesToReceive;
    private Integer waitTimeSeconds;
    private Integer visibilityTimeout;
    private String sqsInboundQueue;
    private String sqsOutboundQueue;
    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;
    private int acknowledge = Session.CLIENT_ACKNOWLEDGE;

    public SqsAdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public SqsAdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public SqsAdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }
    
    public SqsAdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public SqsAdapterConfiguration withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public SqsAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public SqsAdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getSqsInboundQueue() {
        return sqsInboundQueue;
    }

    public SqsAdapterConfiguration withSqsInboundQueue(String sqsInboundQueue) {
        this.sqsInboundQueue = sqsInboundQueue;
        return this;
    }

    public String getSqsOutboundQueue() {
        return sqsOutboundQueue;
    }

    public SqsAdapterConfiguration withSqsOutboundQueue(String sqsOutboundQueue) {
        this.sqsOutboundQueue = sqsOutboundQueue;
        return this;
    }

    public SqsAdapterConfiguration withConcurrency(String concurrency) {
        this.concurrency = concurrency;
        return this;
    }

    public Integer getNumberOfMessagesToReceive() {
        return numberOfMessagesToReceive;
    }

    public SqsAdapterConfiguration withNumberOfMessagesToReceive(Integer numberOfMessagesToReceive) {
        this.numberOfMessagesToReceive = numberOfMessagesToReceive;
        return this;
    }

    public Integer getWaitTimeSeconds() {
        return waitTimeSeconds;
    }

    public SqsAdapterConfiguration withWaitTimeSeconds(Integer waitTimeSeconds) {
        this.waitTimeSeconds = waitTimeSeconds;
        return this;
    }

    public Integer getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public SqsAdapterConfiguration withVisibilityTimeout(Integer visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
        return this;
    }

    public SqsAdapterConfiguration withClientConfiguration(ClientConfiguration clientConfiguration) {

        this.clientConfiguration = clientConfiguration;

        if (useEndpoint) {
            if (this.clientConfiguration == null) {
                this.clientConfiguration = new ClientConfiguration();
            }
            this.clientConfiguration.setProxyHost(host);
            this.clientConfiguration.setProxyPort(port);
        }

        return this;
    }

    public SqsAdapterConfiguration withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
        this.endpointConfiguration = endpointConfiguration;

        if (useEndpoint) {
            if (this.endpointConfiguration == null) {
                this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
            }
        }

        return this;
    }

    public SqsAdapterConfiguration withAcknowledge(int acknowledge) {
        this.acknowledge = acknowledge;
        return this;
    }

    /**
     * Returns a DefaultJmsListenerContainerFactory for SQS Listener
     * 
     * @param clientAcknowledge
     * @return
     */
    public DefaultJmsListenerContainerFactory withSQSListener() {

        validateBaseConfiguration();

        if (this.concurrency == null) {
            throw new IllegalArgumentException("Concurrency não pode ser nulo");
        }

        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency(concurrency);
        factory.setSessionAcknowledgeMode(acknowledge);
        return factory;
    }

    public ConnectionFactory connectionFactory() {

        if (useEndpoint) {

            return SQSConnectionFactory.builder()
                    .withEndpoint(resolveEndpoint())
                    .withClientConfiguration(clientConfiguration)
                    .withAWSCredentialsProvider(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .build();
        } else {

            return SQSConnectionFactory.builder()
                    .withRegion(Region.getRegion(Regions.fromName(awsRegion)))
                    .withAWSCredentialsProvider(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .build();
        }
    }

    /**
     * Returns a SqsAdapterClient for SQS producer
     * 
     * @return
     */
    public SqsAdapterClient withSQSProducer() {

        validateBaseConfiguration();

        if (useEndpoint) {

            sqsClient = AmazonSQSClient.builder()
                    .withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();

        } else {

            sqsClient = AmazonSQSClient.builder()
                    .withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withRegion(Regions.fromName(awsRegion))
                    .build();
        }

        return new SqsAdapterClient(sqsClient, numberOfMessagesToReceive, waitTimeSeconds, visibilityTimeout, sqsInboundQueue,
                sqsOutboundQueue);

    }

}
