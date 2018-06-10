/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fast.aws.connections.pinpoint;

import java.util.Map;

/**
 * Classe que define os atributos para envio de uma notificação push.
 * @author stf_voliveira
 */
public class PushNotification {
    private final String title;
    private final String body;
    private final String deviceToken;
    private Map<String, String> data;
    /**
     * Cria um objeto de notificação push com as informações necessárias
     * @param title O título da mensagem
     * @param body O conteúdo da mensagem
     * @param deviceToken O token do dispositivo móvel registrado no firebase.
     */
    public PushNotification(String title, String body, String deviceToken) {
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
    }
    /**
     * Cria um objeto de notificação push com as informações necessárias
     * @param title O título da mensagem
     * @param body O conteúdo da mensagem
     * @param deviceToken O token do dispositivo móvel registrado no firebase.
     * @param data O dados que serão enviados para o aplicativo.
     */
    public PushNotification(String title, String body, String deviceToken, Map<String, String> data) {
        this(title, body, deviceToken);
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    public boolean isValid() {
        return title != null && body != null && deviceToken != null;
    }
}