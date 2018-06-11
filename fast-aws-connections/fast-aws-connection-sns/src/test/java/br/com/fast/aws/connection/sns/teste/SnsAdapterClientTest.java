package br.com.fast.aws.connection.sns.teste;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import br.com.fast.aws.connection.auth.chain.AWSAuth;
import br.com.fast.aws.connection.sns.SnsAdapterClient;
import br.com.fast.aws.connection.sns.SnsAdapterConfiguration;
import br.com.fast.aws.connection.sns.teste.dto.PassagemDTO;

public class SnsAdapterClientTest {
	
    private boolean useEndpoint = false;
    private SnsAdapterClient client;
    private String host = "sns";
    private Integer port = 9324;
    private String topicName = "FastAWSConnetionsTopic";
    private String topicNameARN;

    private String sqsQueueName = "FastAWSConnetionsSQSTopicSubscribe";

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
                //.withTopicARN(topicNameARN)
                .withSNSClient();

        sqsClient = createSQSClient();

        // Objeto para o teste
        PassagemDTO dto = new PassagemDTO();
        dto.setPassagemId(1010L);
        dto.setTagId(2000L);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);

        this.dto = dto;
        
        topicNameARN = client.getTopicArn(topicName);

    }

    @Test
    public void deveEnviarParaOTopicoEreceberNoSQS() throws InterruptedException {
        //Send to topic
        client.publish(dto.toJSON(), topicNameARN);
        
        Thread.sleep(5000);
        
        //TODO-- I need implement subscription confirmation
        
        //Receive from SQS Queue
        ReceiveMessageResult receiveMessage = sqsClient.receiveMessage(sqsQueueName);
        List<Message> messages = receiveMessage.getMessages();
        
        //Message message = messages.get(0);
        
        //System.out.println(message.getBody());
       
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
