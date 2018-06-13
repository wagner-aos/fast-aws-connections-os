package br.com.fast.aws.connection.redis.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.RMap;

import br.com.fast.aws.connection.redis.RedisAdapterClient;
import br.com.fast.aws.connection.redis.RedisAdapterConfiguration;

public class RedisAdapterClientTest {

    private static String user = "x";
    private static String password = "x";
    private static String host = "fast-aws-connections.rkcdrm.0001.use1.cache.amazonaws.com";
    private static String port = "6379";
    private static String redisMap = "SequencialRedis";
    private static Long sequentialInit = 15L;

    private RedisAdapterClient<String, String> client;
    private String objetoParaTeste;

    @Before
    public void setUp() {

        // Config
        client = new RedisAdapterConfiguration<String, String>()
                .withUser(user)
                .withPassword(password)
                .withHost(host)
                .withPort(port)
                .withSequentialInit(sequentialInit)
                .withRedisMap(redisMap)
                .withRedisClient();

        // Setup
        client.tearDown();

        objetoParaTeste = "{ teste : teste no Redis}";
    }

    @Test
    public void deveGerarSequencialNoRedis() throws Exception {

        // gerando sequencial

        String concessionariaId = "10";
        long sequencial = client.gerarSequencial(concessionariaId);
        long sequencial2 = client.gerarSequencial(concessionariaId);
        long sequencial3 = client.gerarSequencial(concessionariaId);

        long sequencialEsperado1 = sequentialInit;
        long sequencialEsperado2 = sequentialInit + 1;
        long sequencialEsperado3 = sequentialInit + 2;

        assertEquals(sequencialEsperado1, sequencial);
        assertEquals(sequencialEsperado2, sequencial2);
        assertEquals(sequencialEsperado3, sequencial3);
    }

    @Test
    public void deveInserirERecuperarNoRedis() {

        // inserindo
        client.inserir("01", objetoParaTeste);
        System.out.println("Inserindo: " + objetoParaTeste);

        String result = (String) client.buscarPorId("01");

        assertEquals(objetoParaTeste, result);

    }

    @Test
    public void deveExcluirdoRedis() {

        client.inserir("02", objetoParaTeste);
        // apagando
        client.deletar("02");

        assertNull(client.buscarPorId("02"));

    }

    @Test
    public void deveExecutarTearDown() {
        boolean tearDown = client.tearDown();
        boolean tearDown2 = client.tearDown(redisMap);

        assertTrue(tearDown);
        assertTrue(tearDown2);
    }

    @Test
    public void deveRecuperarObjetoDoMap() {
        client.inserir("10", objetoParaTeste);
        RMap<String, String> esperado = client.getMap();
        assertEquals(objetoParaTeste, esperado.get("10"));
    }

    @After
    public void finalizaTestes() {
        // Finalizando Redis
        client.shutdown();
    }

}
