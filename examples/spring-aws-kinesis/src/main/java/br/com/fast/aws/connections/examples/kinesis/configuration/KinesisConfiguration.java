package br.com.fast.aws.connections.examples.kinesis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;

import br.com.fast.aws.connection.kinesis.KinesisAdapterClient;
import br.com.fast.aws.connection.kinesis.KinesisAdapterConfiguration;

@Configuration
public class KinesisConfiguration {

    // AWS CREDENTIALS
    @Value("${aws.profile}")
    private String awsProfile;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    // AWS Kinesis
    @Value("${aws.kinesis.application.name}")
    private String applicationName;

    @Value("${aws.kinesis.stream.name}")
    private String streamName;

    @Value("${aws.kinesis.max.records.read.per.shard}")
    private Integer maxRecordsPerShard;

    @Value("${aws.kinesis.idle.time.between.reads.millis}")
    private long idleTimeBetweenReadsInMillis;

    @Value("${aws.kinesis.task.backoff.time.millis}")
    private long taskBackoffTimeInMillis;

    @Value("${aws.kinesis.initial.position.stream}")
    private String initialPositionInStream;

    @Bean(name = "KinesisAdapterClientConsumer")
    public KinesisAdapterClient consumer() {
        return new KinesisAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withApplicationName(applicationName)
                .withStreamName(streamName)
                .withInitialPositionInStream(InitialPositionInStream.valueOf(initialPositionInStream))
                .withMaxRecordsPerShard(maxRecordsPerShard)
                .withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis)
                .withTaskBackoffTimeInMillis(taskBackoffTimeInMillis)
                .withKinesisConsumerClient();
    }

    @Bean(name = "KinesisAdapterClientProducer")
    public KinesisAdapterClient producer() {
        return new KinesisAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withStreamName(streamName)
                .withKinesisProducerClient();
    }

    
}
