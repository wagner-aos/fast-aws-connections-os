package br.com.fast.aws.connection.dynamodb;

import static com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder.standard;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class DynamoDbAdapterConfiguration<T> extends AwsBaseConfiguration {

    private AmazonDynamoDB dbClient;
    private String tableName;
    private ClientConfiguration clientConfiguration;
    private EndpointConfiguration endpointConfiguration;
    
    public DynamoDbAdapterConfiguration<T> withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withUseEndpoint(boolean useEndpoint) {
        this.useEndpoint = useEndpoint;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withHost(String host) {
        this.host = host;
        return this;
    }

    public DynamoDbAdapterConfiguration<T> withPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public DynamoDbAdapterConfiguration<T> withTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
    /**
     * Metodo que valida a configuração base e a propriedade tablename
     * @throws IlegalArgumentException Caso o nome da tabela seja nulo.
     */
    private void validateTableName() {
        validateBaseConfiguration();
        if (this.tableName == null)
            throw new IllegalArgumentException("Table name não pode ser nulo");
    }

    public DynamoDbAdapterConfiguration<T> withClientConfiguration(ClientConfiguration clientConfiguration) {

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

    public DynamoDbAdapterConfiguration<T> withEndpointConfiguration(EndpointConfiguration endpointConfiguration) {

        this.endpointConfiguration = endpointConfiguration;

        if (useEndpoint) {
            if (this.endpointConfiguration == null) {
                this.endpointConfiguration = new EndpointConfiguration(resolveEndpoint(), "");
            }
        }

        return this;
    }
 
    /**
     * Returns a normal DynamoDbAdapterClient.
     * -
     * AWSCredentialsProvider implementation that provides credentials by looking at the:
     * AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY) environment variables.
     * -
     * If the AWS_SESSION_TOKEN environment variable is also set then temporary credentials will be used.
     * AWSCredentialsProvider implementation that provides credentials by looking at the
     * aws.accessKeyId and aws.secretKey Java system properties.
     * -
     * Creates a new profile credentials provider that returns the AWS security credentials configured for the
     * default profile.
     * Loading the credential file is deferred until the getCredentials() method is called.
     * 
     * @return DynamoDbAdapterClient<T>
     */
    public DynamoDbAdapterClient<T> withAmazonDynamoDBClient() {
        validateTableName();
        AWSCredentialsProviderChain credentials = getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile);        
        AmazonDynamoDBClientBuilder builder = standard().withCredentials(credentials);

        dbClient = useEndpoint ? builder.withEndpointConfiguration(endpointConfiguration).build()
                               : builder.withRegion(awsRegion).build();

    
        return new DynamoDbAdapterClient<>(dbClient, tableName);
    }

   
}