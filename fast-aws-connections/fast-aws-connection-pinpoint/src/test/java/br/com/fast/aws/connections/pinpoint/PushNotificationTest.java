/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fast.aws.connections.pinpoint;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Classe de teste para validar o envio de notificações push.
 * @author stf_voliveira
 */
public class PushNotificationTest {
    private final String token = "cVrl9iKBo6E:APA91bHqgjxhpn5sf5DLqgRwtbLeUCKFWWDxH0-999OgJXlq4MqhcZEhPtanWh0L0VB3S0BMW4NpyjmeptKVzL4w_hHpbkxDkNp-6CEz4HghvII6CTfvVnal0h39vXmxdxz_ako9P7eo";
    private static final String APPLICATION_ID = "df32808a9aad4387a1ed204458391779";
    private static PinPointAdapterClient adapter;
    
    @BeforeClass
    public static void setup() {
        adapter = new PinPointClientConfiguration()
                .withApplicationId(APPLICATION_ID)
                .withAwsRegion("us-east-1")
                .withAwsProfile("dev")
                .build();
    }
    
    @Test
    public void deveEnviarUmaNotificacao() {
        PushNotification push = new PushNotification("Teste", "Teste de envio 1", token);
        adapter.send(push);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deveLancarUmaExcecaoSeNaoInformarOsDadosDePush() {
        PushNotification push = new PushNotification(null, null, null);
        adapter.send(push);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deveLancarUmaExcecaoSeNaoInformarOTitulo() {
        PushNotification push = new PushNotification(null, "Teste de envio 1", token);
        adapter.send(push);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deveLancarUmaExcecaoSeNaoInformarOCorpo() {
        PushNotification push = new PushNotification("Teste", null, token);
        adapter.send(push);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deveLancarUmaExcecaoSeNaoInformarOToken() {
        PushNotification push = new PushNotification("Teste", "Teste de envio 1", null);
        adapter.send(push);
    }
}