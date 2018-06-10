package br.com.fast.aws.connection.lambda;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class LambdaAdapterConfiguration extends AwsBaseConfiguration {

    private AWSLambda awsLambdaClient;

    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;

    public LambdaAdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public LambdaAdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public LambdaAdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }

    public LambdaAdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public LambdaAdapterConfiguration withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public LambdaAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public LambdaAdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    public void setClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public LambdaAdapterConfiguration withClientConfiguration(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        return this;
    }

    public LambdaAdapterConfiguration withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {
        this.endpointConfiguration = endpointConfiguration;

        if (useEndpoint) {
            if (this.endpointConfiguration == null) {
                this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
            }
        }

        return this;
    }

    public LambdaAdapterClient withAWSLambdaClient() {

        if (this.useEndpoint && this.host == null) {
            throw new IllegalArgumentException("Host não pode ser nulo");
        }
        if (this.useEndpoint && this.port == null) {
            throw new IllegalArgumentException("Port não pode ser nulo");
        }

        if (this.useEndpoint) {

            awsLambdaClient = AWSLambdaClientBuilder.standard()
                    .withRegion(awsRegion)
                    .withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .withEndpointConfiguration(endpointConfiguration)
                    .withClientConfiguration(clientConfiguration)
                    .build();

        } else {

            awsLambdaClient = AWSLambdaClientBuilder.standard()
                    .withRegion(awsRegion)
                    .withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                    .build();
        }

        return new LambdaAdapterClient(awsLambdaClient);
    }

}
