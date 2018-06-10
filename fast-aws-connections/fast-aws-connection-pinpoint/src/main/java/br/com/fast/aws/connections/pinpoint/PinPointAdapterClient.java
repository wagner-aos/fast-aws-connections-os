/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fast.aws.connections.pinpoint;

import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.model.AddressConfiguration;
import com.amazonaws.services.pinpoint.model.ChannelType;
import com.amazonaws.services.pinpoint.model.DirectMessageConfiguration;
import com.amazonaws.services.pinpoint.model.GCMMessage;
import com.amazonaws.services.pinpoint.model.MessageRequest;
import com.amazonaws.services.pinpoint.model.MessageResponse;
import com.amazonaws.services.pinpoint.model.MessageResult;
import com.amazonaws.services.pinpoint.model.SendMessagesRequest;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que determina as operações que podem ser feitas para o pinpoint.
 * @author stf_voliveira
 */
public class PinPointAdapterClient {
    private static final Logger LOGGER = Logger.getLogger("pinpoint-logger");
    private static final int SUCCESS_CODE = 200;
    private final AmazonPinpoint client;
    private final String applicationId;
    private final ChannelType channel = ChannelType.GCM;

    PinPointAdapterClient(AmazonPinpoint client, String applicationId) {
        this.client = client;
        this.applicationId = applicationId;
    }
    /**
     * Envia uma notificação push para um dispositivo móvel.
     * @param push Os dados da notificação que será enviada.
     * @return true se a mensagem foi enviada corretamente.
     * @throws IllegalStateException caso ocorra um erro no envio
     * @throws IllegalArgumentException caso o objeto de push seja enviado com o 
     * título, corpo ou deviceToken nulos.
     */
    public boolean send(PushNotification push) {
        if (push.isValid()) {
            MessageResponse response = client.sendMessages(
                new SendMessagesRequest()
                    .withApplicationId(applicationId)
                    .withMessageRequest(
                        new MessageRequest()
                            .withMessageConfiguration(buildMessage(push))
                            .addAddressesEntry(push.getDeviceToken(), 
                                               new AddressConfiguration().withChannelType(channel)))
            ).getMessageResponse();            
            
            Map<String, MessageResult> result = response.getResult();            
            if (result.isEmpty())
                throw new IllegalStateException("PinPoint returns an empty result");
            
            return verifyResult(result);
        } else 
            throw new IllegalArgumentException("Invalid PushNotification object! It has null values");
    }
    /**
     * Cria a mensagem direta para um dispositivo no padrão GCM
     * @param push O objeto com os dados da mensagem.
     * @return Uma mensagem do tipo GCM.
     */
    private DirectMessageConfiguration buildMessage(PushNotification push) {
        return new DirectMessageConfiguration().withGCMMessage(
                new GCMMessage()
                    .withTitle(push.getTitle())
                    .withBody(push.getBody())
                    .withData(push.getData()));
    }
    /**
     * Verifica se a mensagem foi enviada com sucesso.
     * @param map O mapa com o resultado do envio da notificação.
     * @return true se o status code de retorno for 200.
     */
    private boolean verifyResult(Map<String, MessageResult> map) {
        MessageResult result = map.entrySet().stream()
                                  .findFirst()
                                  .map(item -> item.getValue())
                                  .get();
        
        LOGGER.log(Level.INFO, "PinPoint push notification result: {0}", result);
        return SUCCESS_CODE == result.getStatusCode();
    }
}