package br.com.fast.aws.connections.examples.kinesis.producer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.fast.aws.connection.kinesis.KinesisAdapterClient;
import br.com.fast.aws.connections.examples.kinesis.model.TransacaoDTO;

/**
 * @author Wagner Alves
 */
@Service
public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class);

    @Autowired
    @Qualifier("KinesisAdapterClientProducer")
    private KinesisAdapterClient producer;

    @Value("${aws.kinesis.stream.name}")
    private String streamName;

    @Value("${aws.kinesis.producer.send.message.interval}")
    private Long sendMessageInterval;

    @Value("${aws.kinesis.purge.stream}")
    private boolean purgeStream;

    public void sendToKinesis(List<TransacaoDTO> transacoesDTO) throws Exception {
    	sendListOfMessages(transacoesDTO);
    }

    public void sendToKinesis(TransacaoDTO... transacoesDTO) throws Exception {
    	List<TransacaoDTO> list = new ArrayList<>();
    	for(TransacaoDTO dto: transacoesDTO) {
    		list.add(dto);
    	}
    	sendListOfMessages(list);
    }
    
    private void sendListOfMessages(List<TransacaoDTO> transacoesDTO) throws Exception {
    	if (!purgeStream) {
	        for (TransacaoDTO dto : transacoesDTO) {
	        	TransacaoDTO dtoToSend = new TransacaoDTO(dto.getId(), dto.getIdConcessionaria());
                dtoToSend.increaseId(dto.getId());
	            sendMessage(dto);
	        }
    	}
    }

    private void sendMessage(TransacaoDTO dto) throws Exception {
        try {

            log.info("Producer.sendMessage-ID: " + dto.getId());
            // Thread Sleep
            Thread.sleep(sendMessageInterval);

            producer.enviar(dto.toJSON(), String.valueOf(dto.getIdConcessionaria()));

            log.info("Producer.sendMessage-ID: " + dto.getId() + " - SENT");

        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new Exception(e);
        }
    }

}
