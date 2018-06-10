package br.com.fast.aws.connection.auth.chain;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import br.com.fast.aws.connection.auth.provider.ApplicationPropertiesCredentialsProvider;
import br.com.fast.aws.connection.auth.provider.EnvVariableCredentialsProvider;
import br.com.fast.aws.connection.auth.provider.RoleBasedCredentialsProfile;

/**
 * Chain of Responsibility e Strategy para obtenção de credencial para conexão com a AWS.
 * Tenta obter credencial por meio de:
 * -
 * 1-EC2 Instance Metadata 
 * 2-Propriedades passadas pelo ClientConfiguration 'application.properties'
 * 3-Variáveis de Ambiente
 * 4-Roles configuradas por profile nos arquivos .aws/credentials e .aws/config
 * 5-Static Profile, por profile do arquivo .aws/credentials
 * 
 * @author Wagner Alves
 */
public class AWSAuth {

    private static final Logger LOG = Logger.getLogger(AWSAuth.class.getName());

    public AWSCredentialsProviderChain getCredentialsProviderChain(String awsAccessKey, String awsSecretKey, String awsProfile) {

        AWSCredentialsProviderChain providerChain = new AWSCredentialsProviderChain(
                new InstanceProfileCredentialsProvider(false),
                new ApplicationPropertiesCredentialsProvider(awsAccessKey, awsSecretKey),
                new EnvVariableCredentialsProvider(),
                new RoleBasedCredentialsProfile(awsProfile)) {

            @Override
            public AWSCredentials getCredentials() {
                try {
                    return super.getCredentials();
                } catch (AmazonClientException ace) {
                    LOG.log(Level.FINE, ">>> AWS Credentials could not be obtained!");
                }
                return null;
            }
        };

        providerChain.setReuseLastProvider(true);
        return providerChain;

    }

}
