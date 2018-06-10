package br.com.fast.aws.connection.kinesis;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;

/**
 * @author Wagner.Alves
 */
public class KinesisAdapterConfiguration extends AwsBaseConfiguration {
	
	private AmazonKinesis amazonKinesisClient;

    private String streamName;
    private String applicationName;
    private Integer maxRecordsPerShard;
    private Long idleTimeBetweenReadsInMillis;
    private Long taskBackoffTimeInMillis;
    private InitialPositionInStream initialPositionInStream;
    
    public KinesisAdapterConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public KinesisAdapterConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }

    public KinesisAdapterConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }
    
    public KinesisAdapterConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }

    public KinesisAdapterConfiguration withHost(String host) {
        this.host = host;
        return this;
    }

    public KinesisAdapterConfiguration withPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getStreamName() {
        return streamName;
    }

    public KinesisAdapterConfiguration withStreamName(String streamName) {
        this.streamName = streamName;
        return this;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public KinesisAdapterConfiguration withApplicationName(String applicationName) {
        this.applicationName = applicationName;
        return this;
    }

    public Integer getMaxRecordsPerShard() {
        return maxRecordsPerShard;
    }

    public KinesisAdapterConfiguration withMaxRecordsPerShard(Integer maxRecordsPerShard) {
        this.maxRecordsPerShard = maxRecordsPerShard;
        return this;
    }

    public long getIdleTimeBetweenReadsInMillis() {
        return idleTimeBetweenReadsInMillis;
    }

    public KinesisAdapterConfiguration withIdleTimeBetweenReadsInMillis(long idleTimeBetweenReadsInMillis) {
        this.idleTimeBetweenReadsInMillis = idleTimeBetweenReadsInMillis;
        return this;
    }

    public long getTaskBackoffTimeInMillis() {
        return taskBackoffTimeInMillis;
    }

    public KinesisAdapterConfiguration withTaskBackoffTimeInMillis(long taskBackoffTimeInMillis) {
        this.taskBackoffTimeInMillis = taskBackoffTimeInMillis;
        return this;
    }
    
    /**
     * Initial position in the stream when the application starts up for the first time.
     * Position can be one of LATEST (most recent data) or TRIM_HORIZON (oldest available data)
     * Default: TRIM_HORIZON
     */
    public KinesisAdapterConfiguration withInitialPositionInStream(InitialPositionInStream initialPositionInStream) {
    	this.initialPositionInStream = initialPositionInStream;
    	return this;
    }

    /**
     * Returns a Kinesis KinesisAdapterClient for KCL configuration.
     * 
     * @return KinesisAdapterClient
     */
    public KinesisAdapterClient withKinesisConsumerClient() {

        if (this.awsRegion == null) {
            throw new IllegalArgumentException("AWS Region não pode ser nulo");
        }
        if (this.streamName == null) {
            throw new IllegalArgumentException("Stream name não pode ser nulo");
        }
        if (this.applicationName == null) {
            throw new IllegalArgumentException("applicationName não pode ser nulo");
        }
        if (this.maxRecordsPerShard == null || this.maxRecordsPerShard < 0) {
            throw new IllegalArgumentException("maxRecordsPerShard não pode ser nulo ou menor que zero");
        }
        if (this.idleTimeBetweenReadsInMillis == null || this.idleTimeBetweenReadsInMillis < 0) {
            throw new IllegalArgumentException("idleTimeBetweenReadsInMillis não pode ser nulo ou menor que zero");
        }
        if (this.taskBackoffTimeInMillis == null || this.taskBackoffTimeInMillis < 0) {
            throw new IllegalArgumentException("taskBackoffTimeInMillis name não pode ser nulo ou menor que zero");
        }
 
        return new KinesisAdapterClient(
        		getCredentialsProviderChain(awsAccessKey, awsSecretKey,awsProfile),
        		awsRegion, streamName, applicationName, maxRecordsPerShard, idleTimeBetweenReadsInMillis, taskBackoffTimeInMillis, initialPositionInStream);
    }

    /**
     * Returns a Kinesis KinesisAdapterClient for producer configuration.
     * 
     * @return KinesisAdapterClient
     */
    public KinesisAdapterClient withKinesisProducerClient() {

        if (this.awsRegion == null) {
            throw new IllegalArgumentException("AWS Region não pode ser nulo");
        }
        if (this.streamName == null) {
            throw new IllegalArgumentException("Stream name não pode ser nulo");
        }

        amazonKinesisClient = AmazonKinesisClient.builder()
        		.withCredentials(getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile))
                .withRegion(awsRegion)
                .build();

        return new KinesisAdapterClient(amazonKinesisClient, awsRegion, streamName);

    }

}
