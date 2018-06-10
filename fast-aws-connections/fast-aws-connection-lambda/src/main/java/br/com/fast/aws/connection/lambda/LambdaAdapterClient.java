package br.com.fast.aws.connection.lambda;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.Response;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.LogType;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fast.aws.connection.commons.exceptions.FastAWSConnectionsException;

/**
 * @author Wagner.Alves
 */
public class LambdaAdapterClient {

    private static final Logger LOG = Logger.getLogger(LambdaAdapterClient.class.getName());

    private AWSLambda client;

    /**
     * LambdaAdapterClient constructor
     * 
     * @param client
     */
    public LambdaAdapterClient(AWSLambda client) {
        this.client = client;
    }

    public String invoke(String functionName, String jsonPayload) throws FastAWSConnectionsException {

        InvokeRequest invokeRequest = new InvokeRequest()
                .withInvocationType(InvocationType.RequestResponse)
                .withFunctionName(functionName)
                .withLogType(LogType.Tail)
                .withPayload(jsonPayload);

        try {
            InvokeResult invokeResult = client.invoke(invokeRequest);
            return invokeResultToString(invokeResult);

        } catch (Exception e) {
            LOG.log(Level.FINE, e.getMessage());
            throw new FastAWSConnectionsException(e.getMessage());
        }
    }

    public String invoke(String functionName, ByteBuffer byteBufferPayload) throws FastAWSConnectionsException {

        InvokeRequest invokeRequest = new InvokeRequest()
                .withInvocationType(InvocationType.RequestResponse)
                .withFunctionName(functionName)
                .withLogType(LogType.Tail)
                .withPayload(byteBufferPayload);

        try {
            InvokeResult invokeResult = client.invoke(invokeRequest);
            return invokeResultToString(invokeResult);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
            throw new FastAWSConnectionsException(e.getMessage());
        }

    }

    private String invokeResultToString(InvokeResult invokeResult) throws FastAWSConnectionsException {

        try {
            return new String(invokeResult.getPayload().array(), "UTF-8");
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
            throw new FastAWSConnectionsException(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private String getJson(String rawJson) throws FastAWSConnectionsException {

        ObjectMapper mapper = new ObjectMapper();
        Response<?> response = null;
        try {
            response = mapper.readValue(rawJson, Response.class);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage());
            throw new FastAWSConnectionsException(e.getMessage());
        }

        return response.toString();
    }

}
