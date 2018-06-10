package br.com.fast.aws.connection.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class S3AdapterConfiguration extends AwsBaseConfiguration {
	
	private AmazonS3 s3Client;

    private String bucketName;
    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;
    
    public S3AdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public S3AdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public S3AdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }
    
    public S3AdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public S3AdapterConfiguration withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public S3AdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public S3AdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public S3AdapterConfiguration withBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public S3AdapterConfiguration withClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        return this;
    }
    
    public S3AdapterConfiguration withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
    	this.endpointConfiguration = endpointConfiguration;
    	
    	if(useEndpoint) {
    		if(this.endpointConfiguration == null) {
    			this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
    		}
    	}
    	
    	return this;
    }

    /**
     * Returns a S3AdapterClient
     * 
     * @return
     */
    public S3AdapterClient withAmazonS3Client() {

        if (this.useEndpoint && this.host == null) {
            throw new IllegalArgumentException("Host não pode ser nulo");
        }
        if (this.useEndpoint && this.port == null) {
            throw new IllegalArgumentException("Port não pode ser nulo");
        }        

        if (this.useEndpoint) {

            s3Client = AmazonS3Client.builder()
            		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withEndpointConfiguration(endpointConfiguration)
                    .withClientConfiguration(clientConfiguration)
                    .build();

        } else {

            s3Client = AmazonS3Client.builder()
            		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withRegion(awsRegion)
                    .withClientConfiguration(clientConfiguration)
                    .build();
        }

        return new S3AdapterClient(s3Client);
    }

}