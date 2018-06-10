package br.com.fast.aws.connection.lambda.teste;

import org.junit.Before;
import org.junit.Test;

import br.com.fast.aws.connection.commons.exceptions.FastAWSConnectionsException;
import br.com.fast.aws.connection.lambda.LambdaAdapterClient;
import br.com.fast.aws.connection.lambda.LambdaAdapterConfiguration;

/**
 * @author Wagner.Alves
 */
public class LambdaAdapterClientTest {

    private static boolean useEndpoint = false;

    private LambdaAdapterClient client;

    private String functionName;
    private String jsonPayload;
    private String awsProfile = "dev";
    private String awsRegion;
    private String awsAccessKey;
    private String awsSecretKey;

    @Before
    public void setUp() {
        // Client
        client = lambdaAdapterClient();

    }

    private LambdaAdapterClient lambdaAdapterClient() {
        return new LambdaAdapterConfiguration()
                .withAwsProfile(awsProfile)
                .withAwsRegion(awsRegion)
                .withAwsAccessKey(awsAccessKey)
                .withAwsSecretKey(awsSecretKey)
                .withUseEndpoint(useEndpoint)
                .withAWSLambdaClient();
    }

    @Test
    public void deveInvocarApideCEP() throws FastAWSConnectionsException {

        functionName = "api-cep-endereco-" + awsProfile + "-cep";
        jsonPayload = "{\"pathParameters\": {\"cep\": \"18047205\" }, \"httpMethod\": \"GET\"}";

        String invokeResult = client.invoke(functionName, jsonPayload);

        System.out.println(invokeResult);

    }

    @Test
    public void deveInvocarApideCepEstados() throws FastAWSConnectionsException {

        functionName = "api-cep-endereco-" + awsProfile + "-estados";
        jsonPayload = "{\"httpMethod\": \"GET\"}";

        String invokeResult = client.invoke(functionName, jsonPayload);

        System.out.println(invokeResult);

    }

    @Test
    public void deveInvocarApideCepPorEstado() throws FastAWSConnectionsException {

        functionName = "api-cep-endereco-" + awsProfile + "-cidadesPorEstado";
        jsonPayload = "{\"pathParameters\": {\"estado\": \"SP\" }, \"httpMethod\": \"GET\"}";

        String invokeResult = client.invoke(functionName, jsonPayload);

        System.out.println(invokeResult);

    }

    @Test
    public void deveConsultarApiDeCadastro() throws FastAWSConnectionsException {

        functionName = "api-cadastro-" + awsProfile + "-consultarDadosClientePorNumeroDocumento";
        jsonPayload = "{\"pathParameters\": {\"numeroDocumento\": \"82736166000135\" }, \"httpMethod\": \"GET\"}";

        String invokeResult = client.invoke(functionName, jsonPayload);

        System.out.println(invokeResult);

    }

}
