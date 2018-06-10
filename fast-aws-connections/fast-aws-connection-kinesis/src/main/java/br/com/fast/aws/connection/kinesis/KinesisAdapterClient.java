package br.com.fast.aws.connection.kinesis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.UUID;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.model.PutRecordRequest;

import br.com.fast.aws.connection.commons.interfaces.JSONConvertable;

/**
 * @author Wagner.Alves
 */
public class KinesisAdapterClient {
    
    private AmazonKinesis client;
    private AWSCredentialsProvider awsCredentials;    

    private String awsRegion;
    private String streamName;
    private String applicationName;
    private Integer maxRecordsPerShard;
    private long idleTimeBetweenReadsInMillis;
    private long taskBackoffTimeInMillis;
    private InitialPositionInStream initialPositionInStream;


    /**
     * KinesisAdapterClient constructor
     * 
     * @param awsAccessKey
     * @param awsSecretKey
     * @param awsRegion
     * @param streamName
     * @param applicationName
     * @param maxRecordsPerShard
     * @param idleTimeBetweenReadsInMillis
     * @param taskBackoffTimeInMillis
     * @param initialPositionInStream
     */
    public KinesisAdapterClient(AWSCredentialsProvider awsCredentials, String awsRegion, String streamName, String applicationName,
            Integer maxRecordsPerShard, long idleTimeBetweenReadsInMillis, long taskBackoffTimeInMillis, InitialPositionInStream initialPositionInStream) {
        super();
        this.awsCredentials = awsCredentials;
        this.awsRegion = awsRegion;
        this.streamName = streamName;
        this.applicationName = applicationName;
        this.maxRecordsPerShard = maxRecordsPerShard;
        this.idleTimeBetweenReadsInMillis = idleTimeBetweenReadsInMillis;
        this.taskBackoffTimeInMillis = taskBackoffTimeInMillis;
        this.initialPositionInStream = initialPositionInStream;
    	if(this.initialPositionInStream == null) {
    		this.initialPositionInStream = InitialPositionInStream.TRIM_HORIZON;
    	}
    }

    /**
     * KinesisAdapterClient constructor
     * 
     * @param amazonKinesisClient
     * @param awsAccessKey
     * @param awsSecretKey
     * @param awsRegion
     * @param streamName
     */
    public KinesisAdapterClient(AmazonKinesis amazonKinesisClient, String awsRegion,
            String streamName) {
        this.client = amazonKinesisClient;
        this.awsRegion = awsRegion;
        this.streamName = streamName;
    }

    /**
     * Returns the KCL Configurated
     * 
     * @return KinesisClientLibConfiguration
     */
    public KinesisClientLibConfiguration getKCLConfig() {

        return new KinesisClientLibConfiguration(
                applicationName,
                streamName,
                awsCredentials,
                getWorkerId())
                        .withRegionName(awsRegion)
                        .withInitialPositionInStream(initialPositionInStream)
                        .withMaxRecords(maxRecordsPerShard)
                        .withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis)
                        .withTaskBackoffTimeMillis(taskBackoffTimeInMillis);

    }

    private static String getWorkerId() {
        // Definindo o workerId
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            System.out.println("Hostname do worker não pode ser determinado " + e);
        }
        String workerId;
        if (hostName != null) {
            workerId = hostName.concat(":").concat(UUID.randomUUID().toString());
        } else {
            workerId = UUID.randomUUID().toString();
        }
        return workerId;
    }

    /**
     * Send object to Kinesis
     * 
     * @param json
     * @param idEC
     */
    public void enviar(String json, String idEC) {
        enviarParaKinesis(json, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param json
     * @param idEC
     */
    public void enviar(String json, Integer idEC) {
        enviarParaKinesis(json, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param json
     * @param idEC
     * @param streamName
     */
    public void enviar(String json, String idEC, String streamName) {
        enviarParaKinesis(json, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param json
     * @param idEC
     * @param streamName
     */
    public void enviar(String json, Integer idEC, String streamName) {
        enviarParaKinesis(json, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param jSONConvertable
     * @param idEC
     */
    @SuppressWarnings("rawtypes")
    public void enviar(JSONConvertable jSONConvertable, Integer idEC) {
        enviarParaKinesis(jSONConvertable, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param jSONConvertable
     * @param idEC
     */
    @SuppressWarnings("rawtypes")
    public void enviar(JSONConvertable jSONConvertable, String idEC) {
        enviarParaKinesis(jSONConvertable, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param jSONConvertable
     * @param idEC
     * @param streamName
     */
    @SuppressWarnings("rawtypes")
    public void enviar(JSONConvertable jSONConvertable, String idEC, String streamName) {
        enviarParaKinesis(jSONConvertable, idEC, streamName);
    }

    /**
     * Send object to Kinesis
     * 
     * @param jSONConvertable
     * @param idEC
     * @param streamName
     */
    @SuppressWarnings("rawtypes")
    public void enviar(JSONConvertable jSONConvertable, Integer idEC, String streamName) {
        enviarParaKinesis(jSONConvertable, idEC, streamName);
    }

    @SuppressWarnings("rawtypes")
    private void enviarParaKinesis(JSONConvertable jSONConvertable, String idEC, String streamName) {
        String json = jSONConvertable.toJSON();
        enviar(json, idEC, streamName);
    }

    @SuppressWarnings("rawtypes")
    private void enviarParaKinesis(JSONConvertable jSONConvertable, Integer idEC, String streamName) {
        String json = jSONConvertable.toJSON();
        enviar(json, idEC, streamName);
    }

    private void enviarParaKinesis(String json, Integer idEC, String streamName) {
        // Cria o putRecord com os dados do kinesis e da passagem
        PutRecordRequest putRecordRequest;
        putRecordRequest = new PutRecordRequest()
                .withStreamName(streamName)
                .withPartitionKey("partitionKey-" + idEC)
                .withData(ByteBuffer.wrap(json.getBytes(Charset.forName("UTF-8"))));
        client.putRecord(putRecordRequest);
    }

    private void enviarParaKinesis(String json, String idEC, String streamName) {
        // Cria o putRecord com os dados do kinesis e da passagem
        PutRecordRequest putRecordRequest;
        putRecordRequest = new PutRecordRequest()
                .withStreamName(streamName)
                .withPartitionKey("partitionKey-" + idEC)
                .withData(ByteBuffer.wrap(json.getBytes(Charset.forName("UTF-8"))));
        client.putRecord(putRecordRequest);
    }

    public String getStreamName() {
        return streamName;
    }

}
