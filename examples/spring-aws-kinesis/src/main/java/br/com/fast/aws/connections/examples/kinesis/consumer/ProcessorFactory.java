package br.com.fast.aws.connections.examples.kinesis.consumer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;

import br.com.fast.aws.connections.examples.kinesis.model.JSONConvertable;
import br.com.fast.aws.connections.examples.kinesis.model.TransacaoDTO;
import br.com.fast.aws.connections.examples.kinesis.producer.Producer;

/**
 * @author Wagner Alves
 */
@Service
public class ProcessorFactory implements IRecordProcessorFactory {

    private static final Logger log = LoggerFactory.getLogger(ProcessorFactory.class);

    @Autowired
    private Producer producer;

    @Value("${aws.kinesis.task.backoff.time.millis}")
    private long taskBackoffTimeInMillis;

    private static final int NUM_RETRIES = 10;

    @Override
    public IRecordProcessor createProcessor() {
        return this.new RecordProcessor();
    }

    private class RecordProcessor implements IRecordProcessor {

        private String kinesisShardId;

        @Override
        public void initialize(InitializationInput initializationInput) {
            log.info("Iniciando com ShardId: {}", initializationInput.getShardId());
            kinesisShardId = initializationInput.getShardId();
        }

        @Override
        public void processRecords(ProcessRecordsInput processRecordsInput) {

            List<Record> records = processRecordsInput.getRecords();
            log.info("Processando {} records do shardId: {}", records.size(), kinesisShardId);

            try {

                List<TransacaoDTO> transacoes = records.stream()
                        .map(r -> converterParaDTO(new String(r.getData().array(), StandardCharsets.UTF_8)))
                        .collect(Collectors.toList());

                producer.sendToKinesis(transacoes);

                IRecordProcessorCheckpointer checkpointer = processRecordsInput.getCheckpointer();
                checkpoint(checkpointer);

                log.info("Fim de processamento da shard '{}'", kinesisShardId);

            } catch (Exception e) {
                log.error("Erro durante o envio da mensagem ou execução do Checkpoint!!!", e);
            }
        }

        @Override
        public void shutdown(ShutdownInput shutdownInput) {
            log.info("Shutting down da shard '{}', motivo: {}", kinesisShardId, shutdownInput.getShutdownReason());
            if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
                checkpoint(shutdownInput.getCheckpointer());
            }
        }

        /**
         * This method will checkpoint the progress at the last data record that was delivered to the record processor.
         * Upon fail over (after a successful checkpoint() call), the new/replacement RecordProcessor instance will
         * receive data records whose sequenceNumber > checkpoint position (for each partition key).
         * In steady state, applications should checkpoint periodically (e.g. once every 5 minutes).
         * Calling this API too frequently can slow down the application (because it puts pressure on the underlying
         * checkpoint storage layer).
         * -
         * Throws:
         * ThrottlingException - Can't store checkpoint. Can be caused by checkpointing too frequently. Consider
         * increasing the throughput/capacity of the checkpoint store or reducing checkpoint frequency.
         * -
         * ShutdownException - The record processor instance has been shutdown. Another instance may have started
         * processing some of these records already. The application should abort processing via this RecordProcessor
         * instance.
         * -
         * InvalidStateException - Can't store checkpoint. Unable to store the checkpoint in the DynamoDB table (e.g.
         * table doesn't exist).
         * -
         * KinesisClientLibDependencyException - Encountered an issue when storing the checkpoint. The application can
         * backoff and retry.
         * 
         * @param checkpointer
         */
        private void checkpoint(IRecordProcessorCheckpointer checkpointer) {

            log.info("Checkpointing shard", kinesisShardId);

            for (int i = 0; i < NUM_RETRIES; i++) {
                try {
                    checkpointer.checkpoint();
                    break;
                } catch (ShutdownException se) {
                    // Ignore checkpoint if the processor instance has been shutdown (fail over).
                    log.info("caught shutdown exception skipping checkpoint", se);
                    break;
                } catch (ThrottlingException e) {
                    // Backoff and re-attempt checkpoint upon transient failures
                    if (i >= (NUM_RETRIES - 1)) {
                        log.error("checkpoint failed after {} attempts.", i + 1, e);
                        break;
                    } else {
                        log.info("Transient issue when checkpointing attempt {} of {}", i + 1, NUM_RETRIES, e);
                    }
                } catch (InvalidStateException e) {
                    // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
                    log.error("cannot save checkpoint dynamodb table", e);
                    break;
                }

                try {
                    Thread.sleep(taskBackoffTimeInMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.debug("interrupted sleep", e);
                }
            }
        }

        private TransacaoDTO converterParaDTO(String json) {
            return JSONConvertable.createFromJsonStatic(json, TransacaoDTO.class);
        }

    }

}