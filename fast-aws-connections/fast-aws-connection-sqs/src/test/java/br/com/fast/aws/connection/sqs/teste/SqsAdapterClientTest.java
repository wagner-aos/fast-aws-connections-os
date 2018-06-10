package br.com.fast.aws.connection.sqs.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.Message;

import br.com.fast.aws.connection.sqs.SqsAdapterClient;
import br.com.fast.aws.connection.sqs.SqsAdapterConfiguration;
import br.com.fast.aws.connection.sqs.teste.dto.PassagemDTO;

public class SqsAdapterClientTest {

    private boolean useEndpoint = false;
    private String host = "elasticmq";
    private Integer port = 9324;
    private String queueName = "Fast-AWS-Connections-Queue";
    private String filaQueNaoExiste = "QR89324JMOISDA3";

    private SqsAdapterClient client;
    private PassagemDTO dto;

    private String awsProfile = "dev";
    private String awsRegion = "us-east-1";

    @Before
    public void setUp() throws InterruptedException {

        // Client
        client = new SqsAdapterConfiguration()
                .withAwsRegion(awsRegion)
                .withAwsProfile(awsProfile)
                .withHost(host)
                .withPort(port)
                .withSqsInboundQueue(queueName)
                .withSqsOutboundQueue(queueName)
                .withUseEndpoint(useEndpoint)
                .withNumberOfMessagesToReceive(5)
                .withVisibilityTimeout(10)
                .withWaitTimeSeconds(5)
                .withSQSProducer();

        // Objeto para o teste
        PassagemDTO dto = new PassagemDTO();
        dto.setPassagemId(1010L);
        dto.setTagId(2000L);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);

        this.dto = dto;

        client.createQueue(queueName);

    }

    @Test
    public void deveInserirERecuperarNoSQS() throws Exception {

        // Inserindo JSONConvertable ou JSON
        for (int i = 0; i < 1; i++) {
            client.enviar(dto, queueName);
            client.enviar(dto.toJSON(), queueName);
            System.out.println("Inserido: " + dto.toJSON());
        }

        // Recuperando
        int count = 1;
        List<Message> messages = new ArrayList<Message>();
        while (messages.isEmpty() && count <= 5) {
            Thread.sleep(3000);
            messages.addAll(client.receber(queueName));
            System.out.println("Mensagens recuperadas: " + messages.size());
            count++;
        }

        PassagemDTO dto = new PassagemDTO().createFromJSON(messages.get(0).getBody(), PassagemDTO.class);

        // Assert
        assertEquals(this.dto.getPassagemId(), dto.getPassagemId());

    }

    @Test
    public void deveInserirENoSQSComDelayENaoConseguirRecuperarMensagem() throws Exception {
        Integer segundosDelay = 30;
        // Inserindo JSONConvertable ou JSON
        client.enviarComDelay(dto.toJSON(), queueName, segundosDelay);
        System.out.println("Inserido: " + dto.toJSON());

        List<Message> messages = client.receber(queueName);

        assertTrue(messages.isEmpty());

    }

    @Test
    public void deveInserirERecuperarNoSQSComDelay() throws Exception {
        Integer segundosDelay = 10;
        // Inserindo JSONConvertable ou JSON
        client.enviarComDelay(this.dto.toJSON(), queueName, segundosDelay);
        System.out.println("Inserido: " + dto.toJSON());

        Thread.sleep(10000);

        List<Message> messages = client.receber(queueName);
        PassagemDTO dto = (PassagemDTO) new PassagemDTO().createFromJSON(messages.get(0).getBody(), PassagemDTO.class);

        assertEquals(this.dto.getPassagemId(), dto.getPassagemId());
    }

    @Test
    public void deveRemoverMensagem() throws InterruptedException {
        // tearDown
        client.tearDown(queueName);
        // inserindo
        client.enviar(dto, queueName);
        // recuperar
        List<Message> lista = client.receber(queueName);
        // removendo
        DeleteMessageResult deleteMessageResult = client.remover(lista.get(0), queueName);
        int httpStatusCode = deleteMessageResult.getSdkHttpMetadata().getHttpStatusCode();
        // Assert
        assertEquals(200, httpStatusCode);

    }

    @Test(expected = AmazonSQSException.class)
    public void deveLancarExcecaoAoInserirERecuperarNoSQS() throws Exception {
        client.enviar(dto, filaQueNaoExiste);
    }

    @Test(expected = AmazonSQSException.class)
    public void deveLancarExcecaoAoExecutarTearDown() {
        try {
            client.tearDown(filaQueNaoExiste);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deveCriarEDeletarUmaQueue() throws InterruptedException {
        String queueName = this.queueName.concat("TESTE");

        CreateQueueResult createQueueResult = client.createQueue(queueName);
        int httpStatusCodeCreate = createQueueResult.getSdkHttpMetadata().getHttpStatusCode();
        assertEquals(200, httpStatusCodeCreate);

        DeleteQueueResult deleteQueueResult = client.deleteQueue(queueName);
        int httpStatusCodeDelete = deleteQueueResult.getSdkHttpMetadata().getHttpStatusCode();
        assertEquals(200, httpStatusCodeDelete);
    }

    @Test
    public void deveListarQueues() {
        client.getSqsInboundQueue();
        client.getSqsOutboundQueue();
        client.listQueues();
    }

    @After
    public void endTest() throws Exception {
        client.tearDown(queueName);
    }

}
