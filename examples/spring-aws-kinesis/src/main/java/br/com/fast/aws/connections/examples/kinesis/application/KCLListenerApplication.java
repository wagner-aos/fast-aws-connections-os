package br.com.fast.aws.connections.examples.kinesis.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import br.com.fast.aws.connections.examples.kinesis.model.TransacaoDTO;
import br.com.fast.aws.connections.examples.kinesis.producer.Producer;

/**
 * @author Wagner Alves
 */

@SpringBootApplication
@ComponentScan(basePackages = "br.com.fast.aws.connections.examples.kinesis")
@EnableAsync
public class KCLListenerApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(KCLListenerApplication.class, args);
    }

    @Autowired
    private Producer producer;

    @Value("${aws.kinesis.producer.send.messages.at.starting}")
    private boolean sendMesssagesAtStarting;
    
    @Value("${aws.kinesis.producer.send.messages.start.after.seconds}")
    private long sendMesssagesStartAfterSeconds;
    
    @Value("${aws.kinesis.purge.stream}")
    private boolean purgeStream;

    @Value("${aws.kinesis.producer.send.message.initial.amount}")
    private long initialAmount;

    /**
     * Os dois metodos abaixo foram criados para inserção inicial de objetos na stream
     * -
     * purgeStream = true -> esvazia os objetos da stream
     * -
     * initialAmount -> quantidade de objetos para trafegar na stream
     */

    @Override
    public void run(String... args) throws Exception {
    	
        if (!purgeStream && sendMesssagesAtStarting) {
        	Thread.sleep(sendMesssagesStartAfterSeconds);    	
            producer.sendToKinesis(createMessagesToSend());
        }
    }

    private List<TransacaoDTO> createMessagesToSend() {
        List<TransacaoDTO> lista = new ArrayList<>();
        if (initialAmount < 1) {
            initialAmount = 1;
        }
        for (int i = 0; i < initialAmount; i++) {
            TransacaoDTO dto = new TransacaoDTO(i, 1010);
            lista.add(dto);
        }
        return lista;
    }

}
