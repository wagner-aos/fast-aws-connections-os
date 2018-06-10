package br.com.fast.aws.connection.redshift;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.redshift.AmazonRedshift;
import com.amazonaws.services.redshift.AmazonRedshiftClientBuilder;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class RedshiftAdapterConfiguration extends AwsBaseConfiguration {
	
	private AmazonRedshift redshiftClient;
	
    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;
    
    public RedshiftAdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public RedshiftAdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public RedshiftAdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }
    
    public RedshiftAdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public RedshiftAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public RedshiftAdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }
    
    public RedshiftAdapterConfiguration withClientConfiguration(ClientConfiguration clientConfiguration) {
    	this.clientConfiguration = clientConfiguration;
    	return this;
    }
    
    public RedshiftAdapterConfiguration withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
    	this.endpointConfiguration = endpointConfiguration;
    	
    	if(useEndpoint) {
    		if(this.endpointConfiguration == null) {
    			this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
    		}
    	}
    	
    	return this;
    }

    /**
     * Returns a RedshiftAdapterClient
     * 
     * @return RedshiftAdapterClient
     */
    public RedshiftAdapterClient withClient() {

    	validateBaseConfiguration();

        redshiftClient = AmazonRedshiftClientBuilder.standard()
        		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                .withRegion(awsRegion)
                .withClientConfiguration(clientConfiguration)
                .withEndpointConfiguration(endpointConfiguration)
                .build();

        return new RedshiftAdapterClient(redshiftClient);

    }

}
