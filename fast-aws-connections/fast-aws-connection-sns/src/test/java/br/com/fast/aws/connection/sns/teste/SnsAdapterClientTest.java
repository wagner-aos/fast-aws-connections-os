package br.com.fast.aws.connection.sns.teste;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;

import br.com.fast.aws.connection.auth.chain.AWSAuth;
import br.com.fast.aws.connection.sns.SnsAdapterClient;
import br.com.fast.aws.connection.sns.SnsAdapterConfiguration;
import br.com.fast.aws.connection.sns.teste.dto.PassagemDTO;

public class SnsAdapterClientTest {
	
    private boolean useEndpoint = false;
    private SnsAdapterClient client;
    private String host = "sns";
    private Integer port = 9324;
    private String topicNameTeste = "topic-fast-aws-connection-sns2";
    private String topicNameTesteARN;

    @SuppressWarnings("unused")
    private String topicSubscriptionARN;

    private String sqsQueueName = "filaDeTesteParaSNS2";

    @SuppressWarnings("unused")
    private String sqsQueueARN;

    private AmazonSQS sqsClient;
    private PassagemDTO dto;

    @Before
    public void setUp() throws InterruptedException {

        // Client
        client = new SnsAdapterConfiguration()
                .withAwsRegion("us-east-1")
                .withAwsProfile("dev")
                .withHost(host)
                .withPort(port)
                .withUseEndpoint(useEndpoint)
                .withTopicARN(topicNameTesteARN)
                .withSNSClient();

        sqsClient = createSQSClient();

        // Objeto para o teste
        PassagemDTO dto = new PassagemDTO();
        dto.setPassagemId(1010L);
        dto.setTagId(2000L);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);

        this.dto = dto;

        CreateTopicResult createTopic = client.createTopic(topicNameTeste);
        topicNameTesteARN = createTopic.getTopicArn();

    }

    @Test
    public void deveEnviarParaOTopico() {
        client.publish(dto.toJSON(), topicNameTesteARN);
    }

    @Test
    public void deveFazerSubscribe() throws InterruptedException {
        // Criando fila de SQS para teste de Subscribe no SNS.
        @SuppressWarnings("unused")
        CreateQueueResult createQueue = sqsClient.createQueue(sqsQueueName);

        Thread.sleep(6000);

        GetQueueUrlRequest request = new GetQueueUrlRequest().withQueueName(sqsQueueName);
        GetQueueUrlResult queueUrlResult = sqsClient.getQueueUrl(request);
        String queueURL = queueUrlResult.getQueueUrl();

        GetQueueAttributesResult queueAttributes = sqsClient.getQueueAttributes(queueURL, Arrays.asList("QueueArn"));
        Map<String, String> attributes = queueAttributes.getAttributes();
        Collection<String> values = attributes.values();
        String queueARN = values.toString();
        // Thread.sleep(5000);
        System.out.println("SQS QUEUE_ARN: " + queueARN);

        // SubscribeResult subscribeResult = client.sQSsubscribeTopic(topicNameTesteARN, queueARN);

        // System.out.println("SUBSCRIBE-RESULT: " + subscribeResult.getSubscriptionArn());
    }

    @After
    public void deleteTopic() {
        client.deleteTopic(topicNameTesteARN);
    }

    private AmazonSQS createSQSClient() {

        if (useEndpoint) {
            String sqsEndpoint = "http://".concat(host).concat(":").concat(port.toString());

            EndpointConfiguration endpointConfiguration = new EndpointConfiguration(sqsEndpoint, "");

            return AmazonSQSClient.builder()
            		.withCredentials(new AWSAuth().getCredentialsProviderChain(null, null, "dev"))
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();

        } else {

            return AmazonSQSClient.builder()
            		.withCredentials(new AWSAuth().getCredentialsProviderChain(null, null, "dev"))
                    .withRegion("us-east-1")
                    .build();
        }

    }

}
