package br.com.fast.aws.connection.auth.provider;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_ENV_VAR;
import static com.amazonaws.SDKGlobalConfiguration.ALTERNATE_ACCESS_KEY_ENV_VAR;
import static com.amazonaws.SDKGlobalConfiguration.ALTERNATE_SECRET_KEY_ENV_VAR;
import static com.amazonaws.SDKGlobalConfiguration.AWS_SESSION_TOKEN_ENV_VAR;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_ENV_VAR;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.util.StringUtils;

/**
 * Classe para obtenção de credenciais a partir chaves da AWS recuperadas
 * por variáveis de ambiente
 * 
 * @author Wagner Alves
 */
public class EnvVariableCredentialsProvider implements AWSCredentialsProvider {

    private static final Logger LOG = Logger.getLogger(EnvVariableCredentialsProvider.class.getName());

    @Override
    public AWSCredentials getCredentials() {
        String accessKey = System.getenv(ACCESS_KEY_ENV_VAR);
        
        if (accessKey == null) {
            accessKey = System.getenv(ALTERNATE_ACCESS_KEY_ENV_VAR);
        }

        String secretKey = System.getenv(SECRET_KEY_ENV_VAR);
        if (secretKey == null) {
            secretKey = System.getenv(ALTERNATE_SECRET_KEY_ENV_VAR);
        }

        accessKey = StringUtils.trim(accessKey);
        secretKey = StringUtils.trim(secretKey);
        String sessionToken = StringUtils.trim(System.getenv(AWS_SESSION_TOKEN_ENV_VAR));

        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            String message = ">>> Unable to load AWS credentials from Environment Variables " +
                    "(" + ACCESS_KEY_ENV_VAR + " (or " + ALTERNATE_ACCESS_KEY_ENV_VAR + ") and " +
                    SECRET_KEY_ENV_VAR + " (or " + ALTERNATE_SECRET_KEY_ENV_VAR + "))";

            LOG.log(Level.FINE, message);

            throw new SdkClientException(
                    message);
        }

        return sessionToken == null ? new BasicAWSCredentials(accessKey, secretKey)
                : new BasicSessionCredentials(accessKey, secretKey, sessionToken);
    }

    @Override
    public void refresh() {
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
