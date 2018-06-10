package br.com.fast.aws.connection.hornetq.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsOperations;

import br.com.fast.aws.connection.commons.testes.dto.PassagemDTO;
import br.com.fast.aws.connection.hornetq.HornetqAdapterConfiguration;

public class HornetQAdapterClientTest {

    private String user = "fast";
    private String password = "fast@123";
    private String host = "localhost";
    private String port = "5445";
    private boolean useEndpoint = true;
    // private long receiveTimeout = 20;
    private String topicName = "passagensprocessadas.01010";

    private JmsOperations jmsTemplate;
    private PassagemDTO dto;

    @Before
    public void setUp() throws Exception {

        // Client
        jmsTemplate = new HornetqAdapterConfiguration()
                .withTopicName(topicName)
                .withUser(user)
                .withPassword(password)
                .withHost(host)
                .withPort(port)
                .withUseEndpoint(useEndpoint)
                .withJmsTemplate();

        // Objeto para o teste
        PassagemDTO dto = new PassagemDTO();
        dto.setPassagemId(1010L);
        dto.setTagId(2000L);
        dto.setDatahora(new Date().getTime());
        dto.setPassAutomatica(true);

        this.dto = dto;

    }

    @Test
    public void deveEnviarEReceberMensagemParaTopico() throws InterruptedException {

        // Envia
        for (int i = 0; i < 100; i++) {

            // jmsTemplate.convertAndSend(dto.toJSON());
        }

        Thread.sleep(3000);

        // Recebe
        Object jsonRecebido = jmsTemplate.receiveAndConvert(topicName);
        PassagemDTO dtoRecebido = (PassagemDTO) new PassagemDTO().createFromJSON((String) jsonRecebido, PassagemDTO.class);

        System.out.println("RECEBIDO: " + jsonRecebido);

        // Assert
        assertEquals(this.dto.getPassagemId(), dtoRecebido.getPassagemId());
        assertEquals(this.dto.getTagId(), dtoRecebido.getTagId());
        assertEquals(this.dto.isPassAutomatica(), dtoRecebido.isPassAutomatica());

    }

    // @Test
    public void deveRemoverTodasAsMensagensDaFila() {
        // Envia mensagens para a fila
        for (int i = 0; i < 10; i++) {
            jmsTemplate.convertAndSend(dto.toJSON());
        }

        // Limpa a fila
        clearTopic();

        // Assert

        assertNull(jmsTemplate.receiveAndConvert());

    }

    private void clearTopic() {
        Object jsonRecebido = jmsTemplate.receiveAndConvert();
        if (jsonRecebido != null) {
            clearTopic();
        }
        System.out.println("Todas as mensagens foram deletadas!!!");
    }

}
