package br.com.fast.aws.connection.sns;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.DeleteTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;

import br.com.fast.aws.connection.commons.interfaces.JSONConvertable;

/**
 * @author Wagner.Alves
 */
public class SnsAdapterClient {

    private AmazonSNS snsClient;

    private String topicARN;

    @SuppressWarnings("unused")
    private String concurrency;

    public SnsAdapterClient(AmazonSNS snsClient, String topicARN, String concurrency) {
        super();
        this.snsClient = snsClient;
        this.topicARN = topicARN;
        this.concurrency = concurrency;
    }

    /**
     * Delivers a message to the topic configured in the client.
     * 
     * @param jSONConvertable
     * @param nomeTopico
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void publish(JSONConvertable jSONConvertable) {

        System.out.println("publicando no tópico" + topicARN);

        PublishRequest publishRequest = new PublishRequest(topicARN, jSONConvertable.toJSON());
        PublishResult publishResult = snsClient.publish(publishRequest);

        System.out.println("mensagem publicada no tópico" + topicARN + "mensagem: " + publishResult.getMessageId());

    }

    /**
     * Delivers a message to the specified topic.
     * 
     * @param jSONConvertable
     * @param nomeTopico
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public void publish(JSONConvertable jSONConvertable, String topicNameARN) {

        System.out.println("publicando no tópico" + topicNameARN);

        PublishRequest publishRequest = new PublishRequest(topicNameARN, jSONConvertable.toJSON());
        PublishResult publishResult = snsClient.publish(publishRequest);

        System.out.println("mensagem publicada no tópico" + topicNameARN + "mensagem: " + publishResult.getMessageId());

    }

    /**
     * Delivers a message to the topic configured in the client.
     * 
     * @param json
     * @param nomeTopico
     * @throws Exception
     */
    public void publish(String json) {

        System.out.println("publicando no tópico" + topicARN);

        PublishRequest publishRequest = new PublishRequest(topicARN, json);
        PublishResult publishResult = snsClient.publish(publishRequest);

        System.out.println("mensagem publicada no tópico" + topicARN + "mensagem: " + publishResult.getMessageId());

    }

    /**
     * Delivers a message to the specified topic.
     * 
     * @param json
     * @param nomeTopico
     * @throws Exception
     */
    public void publish(String json, String topicNameARN) {

        System.out.println("publicando no tópico" + topicNameARN);

        PublishRequest publishRequest = new PublishRequest(topicNameARN, json);
        PublishResult publishResult = snsClient.publish(publishRequest);

        System.out.println("mensagem publicada no tópico" + topicNameARN + "mensagem: " + publishResult.getMessageId());

    }

    public SubscribeResult sQSsubscribeTopic(String topicARN, String endpoint) {
        SubscribeRequest subRequest = new SubscribeRequest(topicARN, "sqs", endpoint);
        SubscribeResult subscribeResult = snsClient.subscribe(subRequest);

        // get request id for SubscribeRequest from SNS metadata
        System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));

        ConfirmSubscriptionRequest confirmSubscriptionRequest = new ConfirmSubscriptionRequest();
        confirmSubscriptionRequest.setTopicArn(topicARN);

        ConfirmSubscriptionResult confirmSubscription = snsClient.confirmSubscription(confirmSubscriptionRequest);
        confirmSubscription.getSdkHttpMetadata().getHttpStatusCode();

        System.out.println("CONFIRM-SUBSCRIPTION: " + confirmSubscription);

        return subscribeResult;

    }

    public CreateTopicResult createTopic(String topicNameARN) {
        CreateTopicResult createTopicResult = snsClient.createTopic(topicNameARN);

        System.out.println("Topico Criado: " + createTopicResult.getTopicArn());
        return createTopicResult;
    }

    public DeleteTopicResult deleteTopic(String topicARN) {
        DeleteTopicResult deleteTopicResult = snsClient.deleteTopic(topicARN);

        System.out.println("Topico Criado: " + deleteTopicResult.getSdkResponseMetadata());
        return deleteTopicResult;
    }

}
