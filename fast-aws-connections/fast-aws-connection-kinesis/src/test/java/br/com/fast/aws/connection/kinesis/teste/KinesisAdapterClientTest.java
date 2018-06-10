package br.com.fast.aws.connection.kinesis.teste;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;

import br.com.fast.aws.connection.commons.testes.dto.PassagemDTO;
import br.com.fast.aws.connection.kinesis.KinesisAdapterClient;
import br.com.fast.aws.connection.kinesis.KinesisAdapterConfiguration;

public class KinesisAdapterClientTest {

	private String applicationName = "FAWSKinesisApplicationTESTE";
	@SuppressWarnings("unused")
	private String inboundStreamName = "FastAWSConnectionsStream";
	private String outboundStreamName = "FastAWSConnectionsStream";
	private Integer maxRecordsPerShard = 1000;
	private long idleTimeBetweenReadsInMillis = 3000;
	private long taskBackoffTimeInMillis = 500;

	private KinesisAdapterClient clientProducer;
	private KinesisAdapterClient clientListener;

	private PassagemDTO passagemDTO;
	private Integer idECInteger;
	private String idECString;

	@Before
	public void setUp() {

		// Client
		clientProducer = new KinesisAdapterConfiguration()
        		.withAwsProfile("dev")
				.withAwsRegion("us-east-1")
				.withStreamName(outboundStreamName)
				.withKinesisProducerClient();

		clientListener = new KinesisAdapterConfiguration()
        		.withAwsProfile("dev")
				.withAwsRegion("us-east-1")
				.withStreamName(outboundStreamName)
				.withApplicationName(applicationName)
				.withIdleTimeBetweenReadsInMillis(idleTimeBetweenReadsInMillis)
				.withMaxRecordsPerShard(maxRecordsPerShard)
				.withTaskBackoffTimeInMillis(taskBackoffTimeInMillis)
				.withKinesisConsumerClient();

		// Objeto para teste
		passagemDTO = new PassagemDTO();
		passagemDTO.setPassagemId(1010L);
		passagemDTO.setTagId(2000L);
		passagemDTO.setDatahora(new Date().getTime());
		passagemDTO.setPassAutomatica(true);

	}

	@Test
	public void deveEnviarParaOKinesisComIdECInteger() {
		idECInteger = 1010;
		clientProducer.enviar(passagemDTO, idECInteger);
	}

	@Test
	public void deveEnviarParaOKinesisComIdECString() {
		idECString = "1011";
		clientProducer.enviar(passagemDTO, idECString);
	}

	@Test
	public void deveEnviarParaOKinesisComIdECStringEStringJson() {
		idECString = "1011";
		String json = passagemDTO.toJSON();
		clientProducer.enviar(json, idECInteger);
	}

	@Test
	public void deveInicilizarKCL() {
		KinesisClientLibConfiguration kclConfig = clientListener.getKCLConfig();

		assertEquals(kclConfig.getIdleTimeBetweenReadsInMillis(), idleTimeBetweenReadsInMillis);
		assertEquals(kclConfig.getTaskBackoffTimeMillis(), taskBackoffTimeInMillis);
	}

}
