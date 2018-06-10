/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fast.aws.connections.pinpoint;

import br.com.fast.aws.connection.auth.configuration.AwsBaseConfiguration;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import org.apache.commons.lang.StringUtils;

/**
 * Classe para configuração de conexão com o pingpoint
 * @author stf_voliveira
 */
public final class PinPointClientConfiguration extends AwsBaseConfiguration {    
    private String applicationId;
    
    public PinPointClientConfiguration withAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
        return this;
    }

    public PinPointClientConfiguration withAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
        return this;
    }
    
    public PinPointClientConfiguration withAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }

    public PinPointClientConfiguration withAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
        return this;
    }
    
    public PinPointClientConfiguration withApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }
    /**
     * Constrói um adapter para o pinpoint configurado com as credenciais.
     * @return O client para conexão com o pinpoint.
     * @throws IllegalArgumentException Caso o método withApplicationId não tenha
     * sido chamado ou enviado um argumento nulo.
     */
    public PinPointAdapterClient build() {
        if (applicationId == null || StringUtils.isEmpty(applicationId))
            throw new IllegalArgumentException("ApplicationID cannot be null");
        
        AWSCredentialsProviderChain credentials = getCredentialsProviderChain(awsAccessKey, awsSecretKey, awsProfile);
        AmazonPinpoint client = AmazonPinpointClientBuilder.standard()
                .withCredentials(credentials)
                .withRegion(awsRegion)
                .build();
        return new PinPointAdapterClient(client, applicationId);
    }
}
