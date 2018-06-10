package br.com.fast.aws.connection.sns;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class SnsAdapterConfiguration extends AwsBaseConfiguration {
	
	private AmazonSNS snsClient;
	
    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;

    private String topicARN;
    private String concurrency;
    
    public SnsAdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public SnsAdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public SnsAdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }
    
    public SnsAdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public SnsAdapterConfiguration withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public SnsAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public SnsAdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }

    public SnsAdapterConfiguration withTopicARN(String topicARN) {
        this.topicARN = topicARN;
        return this;
    }
    
    public String getTopicARN() {
    	return topicARN;
    }

    public String getConcurrency() {
        return concurrency;
    }

    public SnsAdapterConfiguration withConcurrency(String concurrency) {
        this.concurrency = concurrency;
        return this;
    }
    
    public SnsAdapterConfiguration withClientConfiguration(ClientConfiguration clientConfiguration) {

    	this.clientConfiguration = clientConfiguration;
    	
    	if(useEndpoint) {
    		if(this.clientConfiguration == null) {
    			this.clientConfiguration = new ClientConfiguration();
    		}
    		this.clientConfiguration.setProxyHost(host);
    		this.clientConfiguration.setProxyPort(port);
    	}
    	
    	return this;
    }
    
    public SnsAdapterConfiguration withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
    	
    	this.endpointConfiguration = endpointConfiguration;
    	
    	if(useEndpoint) {
    		if(this.endpointConfiguration == null) {
    			this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
    		}
    	}
    	
    	return this;
    }


    /**
     * Returns a SnsAdapterClient for SNS.
     * 
     * @return SnsAdapterClient
     */
    public SnsAdapterClient withSNSClient() {

    	validateBaseConfiguration();

        if (this.useEndpoint) {

            snsClient = AmazonSNSClient.builder()
            		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withEndpointConfiguration(endpointConfiguration)
                    .withClientConfiguration(clientConfiguration)
                    .build();

        } else {

            snsClient = AmazonSNSClient.builder()
            		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withRegion(awsRegion)
                    .withClientConfiguration(clientConfiguration)
                    .build();
        }

        return new SnsAdapterClient(snsClient, topicARN, concurrency);

    }

}
