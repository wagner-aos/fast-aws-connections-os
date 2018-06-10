package br.com.fast.aws.connection.auth.provider;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;

/**
 * Classe para obtenção de credenciais a partir chaves da AWS passadas por application.properties
 * 
 * @author Wagner Alves
 */
public class ApplicationPropertiesCredentialsProvider implements AWSCredentialsProvider {

    private static final Logger LOG = Logger.getLogger(ApplicationPropertiesCredentialsProvider.class.getName());

    private String awsAccessKey;
    private String awsSecretKey;

    public ApplicationPropertiesCredentialsProvider(String awsAccessKey, String awsSecretKey) {
        super();
        this.awsAccessKey = StringUtils.trim(awsAccessKey);
        this.awsSecretKey = StringUtils.trim(awsSecretKey);
    }

    @Override
    public AWSCredentials getCredentials() {

        if (StringUtils.isNullOrEmpty(this.awsAccessKey) || StringUtils.isNullOrEmpty(this.awsSecretKey)) {
            String message = ">>> Unable to load AWS credentials from application.properties...";
            LOG.log(Level.FINE, message);
            throw new SdkClientException(message);
        } else {
            LOG.log(Level.FINE,
                    ">>> AccessKey: " + this.awsAccessKey + " - SecretKey: " + this.awsSecretKey + " found in Application Properties!");
        }

        return new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey);
    }

    @Override
    public void refresh() {
        // Method not used
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
