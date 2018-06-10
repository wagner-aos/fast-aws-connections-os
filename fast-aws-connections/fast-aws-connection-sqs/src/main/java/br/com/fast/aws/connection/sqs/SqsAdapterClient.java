package br.com.fast.aws.connection.sqs;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.PurgeQueueRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import br.com.fast.aws.connection.commons.interfaces.JSONConvertable;

/**
 * @author Wagner.Alves
 */
public class SqsAdapterClient {

    private AmazonSQS sqsClient;

    private Integer numberOfMessagesToReceive;
    private Integer waitTimeSeconds;
    private Integer visibilityTimeout;
    private String sqsInboundQueue;
    private String sqsOutboundQueue;

    /**
     * SqsAdapterClient constructor
     * 
     * @param sqsClient
     * @param numberOfMessagesToReceive
     * @param waitTimeSeconds
     * @param visibilityTimeout
     */
    public SqsAdapterClient(AmazonSQS sqsClient, Integer numberOfMessagesToReceive, Integer waitTimeSeconds,
            Integer visibilityTimeout) {
        super();
        this.sqsClient = sqsClient;
        this.numberOfMessagesToReceive = numberOfMessagesToReceive;
        this.waitTimeSeconds = waitTimeSeconds;
        this.visibilityTimeout = visibilityTimeout;
    }

    /**
     * SqsAdapterClient constructor
     * 
     * @param sqsClient
     * @param numberOfMessagesToReceive
     * @param waitTimeSeconds
     * @param visibilityTimeout
     * @param sqsInboundQueue
     * @param sqsOutboundQueue
     */
    public SqsAdapterClient(AmazonSQS sqsClient, Integer numberOfMessagesToReceive, Integer waitTimeSeconds,
            Integer visibilityTimeout, String sqsInboundQueue, String sqsOutboundQueue) {
        super();
        this.sqsClient = sqsClient;
        this.numberOfMessagesToReceive = numberOfMessagesToReceive;
        this.waitTimeSeconds = waitTimeSeconds;
        this.visibilityTimeout = visibilityTimeout;
        this.sqsInboundQueue = sqsInboundQueue;
        this.sqsOutboundQueue = sqsOutboundQueue;
    }

    /**
     * Delivers a message to the specified queue.
     * 
     * @param jSONConvertable
     * @param nomeFila
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void enviar(JSONConvertable jSONConvertable, String nomeFila) {

        String myQueueUrl = sqsClient.getQueueUrl(nomeFila).getQueueUrl();

        SendMessageRequest sendMessageRequest = new SendMessageRequest(myQueueUrl, jSONConvertable.toJSON());
        sqsClient.sendMessage(sendMessageRequest);

    }

    /**
     * Delivers a message to the specified queue.
     * 
     * @param json
     * @param nomeFila
     * @throws Exception
     */
    public void enviar(String json, String nomeFila) {

        String myQueueUrl = sqsClient.getQueueUrl(nomeFila).getQueueUrl();

        SendMessageRequest sendMessageRequest = new SendMessageRequest(myQueueUrl, json);
        sqsClient.sendMessage(sendMessageRequest);

    }

    /**
     * Delivers a message to the specified queue with delay.
     * 
     * @param json
     * @param nomeFila
     * @param segundosDelay
     * @throws Exception
     */
    public void enviarComDelay(String json, String nomeFila, Integer segundosDelay) {

        String myQueueUrl = sqsClient.getQueueUrl(nomeFila).getQueueUrl();

        SendMessageRequest sendMessageRequest = new SendMessageRequest(myQueueUrl, json);
        sendMessageRequest.setDelaySeconds(segundosDelay);
        sqsClient.sendMessage(sendMessageRequest);

    }

    /**
     * Retrieves one or more messages (up to 10), from the specified queue. Using the WaitTimeSeconds parameter enables
     * long-poll support. For more information, see Amazon SQS Long Polling in the Amazon SQS Developer Guide.
     * Short poll is the default behavior where a weighted random set of machines is sampled on a ReceiveMessage call.
     * Thus, only the messages on the sampled machines are returned. If the number of messages in the queue is small
     * (fewer than 1,000), you most likely get fewer messages than you requested per ReceiveMessage call. If the number
     * of messages in the queue is extremely small, you might not receive any messages in a particular ReceiveMessage
     * response. If this happens, repeat the request.
     * 
     * @param queueName
     * @return List<Message>
     */
    public List<Message> receber(String queueName) {

        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(conformeQueueUrl);
        receiveMessageRequest
                .withMaxNumberOfMessages(numberOfMessagesToReceive)
                .withWaitTimeSeconds(waitTimeSeconds)
                .withVisibilityTimeout(visibilityTimeout);
        return sqsClient.receiveMessage(receiveMessageRequest).getMessages();
    }

    /**
     * Retrieves one or more messages (up to 10), from the specified queue. Using the WaitTimeSeconds parameter enables
     * long-poll support. For more information, see Amazon SQS Long Polling in the Amazon SQS Developer Guide.
     * Short poll is the default behavior where a weighted random set of machines is sampled on a ReceiveMessage call.
     * Thus, only the messages on the sampled machines are returned. If the number of messages in the queue is small
     * (fewer than 1,000), you most likely get fewer messages than you requested per ReceiveMessage call. If the number
     * of messages in the queue is extremely small, you might not receive any messages in a particular ReceiveMessage
     * response. If this happens, repeat the request.
     * 
     * @param queueName
     * @param visibilityTimeout
     * @return
     */
    public List<Message> receber(String queueName, Integer visibilityTimeout) {

        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(conformeQueueUrl);
        receiveMessageRequest
                .withMaxNumberOfMessages(numberOfMessagesToReceive)
                .withWaitTimeSeconds(waitTimeSeconds)
                .withVisibilityTimeout(visibilityTimeout);
        return sqsClient.receiveMessage(receiveMessageRequest).getMessages();
    }

    /**
     * Retrieves one or more messages (up to 10), from the specified queue. Using the WaitTimeSeconds parameter enables
     * long-poll support. For more information, see Amazon SQS Long Polling in the Amazon SQS Developer Guide.
     * Short poll is the default behavior where a weighted random set of machines is sampled on a ReceiveMessage call.
     * Thus, only the messages on the sampled machines are returned. If the number of messages in the queue is small
     * (fewer than 1,000), you most likely get fewer messages than you requested per ReceiveMessage call. If the number
     * of messages in the queue is extremely small, you might not receive any messages in a particular ReceiveMessage
     * response. If this happens, repeat the request.
     * 
     * @param queueName
     * @param visibilityTimeout
     * @param waitTimeSeconds
     * @return
     */
    public List<Message> receber(String queueName, Integer visibilityTimeout, Integer waitTimeSeconds) {

        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(conformeQueueUrl);
        receiveMessageRequest
                .withMaxNumberOfMessages(numberOfMessagesToReceive)
                .withWaitTimeSeconds(waitTimeSeconds)
                .withVisibilityTimeout(visibilityTimeout);
        return sqsClient.receiveMessage(receiveMessageRequest).getMessages();
    }

    /**
     * Retrieves one or more messages (up to 10), from the specified queue. Using the WaitTimeSeconds parameter enables
     * long-poll support. For more information, see Amazon SQS Long Polling in the Amazon SQS Developer Guide.
     * Short poll is the default behavior where a weighted random set of machines is sampled on a ReceiveMessage call.
     * Thus, only the messages on the sampled machines are returned. If the number of messages in the queue is small
     * (fewer than 1,000), you most likely get fewer messages than you requested per ReceiveMessage call. If the number
     * of messages in the queue is extremely small, you might not receive any messages in a particular ReceiveMessage
     * response. If this happens, repeat the request.
     * 
     * @param queueName
     * @param visibilityTimeout
     * @param waitTimeSeconds
     * @param numberOfMessagesToReceive
     * @return
     */
    public List<Message> receber(String queueName, Integer visibilityTimeout, Integer waitTimeSeconds, Integer numberOfMessagesToReceive) {

        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(conformeQueueUrl);
        receiveMessageRequest
                .withMaxNumberOfMessages(numberOfMessagesToReceive)
                .withWaitTimeSeconds(waitTimeSeconds)
                .withVisibilityTimeout(visibilityTimeout);
        return sqsClient.receiveMessage(receiveMessageRequest).getMessages();
    }

    /**
     * Removes a message from SQS
     * 
     * @param message
     * @param queueName
     * @return
     */
    public DeleteMessageResult remover(Message message, String queueName) {
        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
        String messageReceiptHandle = message.getReceiptHandle();
        return sqsClient.deleteMessage(new DeleteMessageRequest(conformeQueueUrl, messageReceiptHandle));

    }

    /**
     * Purge all queue messages, but you can only execute one purge every 60 seconds.
     * 
     * @param queueName
     */
    public void purgeQueue(String queueName) {
        String queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        PurgeQueueRequest purgeQueueRequest = new PurgeQueueRequest();
        purgeQueueRequest.withQueueUrl(queueUrl);
        sqsClient.purgeQueue(purgeQueueRequest);
    }

    /**
     * Delete all queue messages
     * 
     * @param queueName
     * @throws InterruptedException
     */
    public void tearDown(String queueName) throws InterruptedException {

        String conformeQueueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(conformeQueueUrl)
                .withMaxNumberOfMessages(numberOfMessagesToReceive);
        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            String messageReceiptHandle = message.getReceiptHandle();
            sqsClient.deleteMessage(new DeleteMessageRequest(conformeQueueUrl, messageReceiptHandle));
        }
        if (!messages.isEmpty()) {
            Thread.sleep(3000);
            tearDown(queueName);
        }

    }

    /**
     * Purge all queue messages, but you can only execute one purge every 60 seconds
     * 
     * @param queueName
     * @throws InterruptedException
     */
    public void tearDownPurge(String queueName) {
        purgeQueue(queueName);
    }

    /**
     * Creates a queue
     * 
     * @param queueName
     * @return
     * @throws InterruptedException
     */
    public CreateQueueResult createQueue(String queueName) throws InterruptedException {
        CreateQueueResult createQueueResult;
        createQueueResult = sqsClient.createQueue(queueName);
        Thread.sleep(3000);
        return createQueueResult;
    }

    /**
     * Deletes a Queue from SQS
     * 
     * @param queueName
     * @return
     * @throws InterruptedException
     */
    public DeleteQueueResult deleteQueue(String queueName) throws InterruptedException {
        DeleteQueueResult deleteQueueResult = new DeleteQueueResult();
        boolean queueExists = true;
        while (!sqsClient.listQueues(queueName).getQueueUrls().isEmpty() && queueExists) {

            GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
            GetQueueUrlResult queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest);
            DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest(queueUrl.getQueueUrl());

            if (!sqsClient.listQueues(queueName).getQueueUrls().isEmpty()) {
                deleteQueueResult = sqsClient.deleteQueue(deleteQueueRequest);
                queueExists = false;
            }
            Thread.sleep(3000);

        }

        if (sqsClient.listQueues(queueName).getQueueUrls().isEmpty()) {
            return deleteQueueResult;
        }
        return deleteQueueResult;
    }

    /**
     * Lists the Queues
     * 
     * @return
     */
    public ListQueuesResult listQueues() {
        return sqsClient.listQueues();
    }

    public String getSqsInboundQueue() {
        if (sqsInboundQueue == null) {
            throw new IllegalArgumentException("Nome da fila de inbound não foi passado ao builder de conexão!");
        }
        return sqsInboundQueue;
    }

    public String getSqsOutboundQueue() {
        if (sqsOutboundQueue == null) {
            throw new IllegalArgumentException("Nome da fila de outbound não foi passado ao builder de conexão!");
        }
        return sqsOutboundQueue;
    }

}
