package br.com.fast.aws.connections.examples.kinesis.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

import br.com.fast.aws.connection.kinesis.KinesisAdapterClient;

@Service
public class Consumer {

    @Autowired
    @Qualifier("KinesisAdapterClientConsumer")
    private KinesisAdapterClient client;

    @Autowired
    private IRecordProcessorFactory processorFactory;

    @EventListener(ContextRefreshedEvent.class)
    @Async
    public void createWorker() {
        new Worker.Builder()
                .recordProcessorFactory(processorFactory)
                .config(client.getKCLConfig())
                .build()
                .run();
    }
}
