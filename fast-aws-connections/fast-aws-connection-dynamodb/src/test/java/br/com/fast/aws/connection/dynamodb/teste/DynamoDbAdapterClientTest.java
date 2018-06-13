package br.com.fast.aws.connection.dynamodb.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.Select;

import br.com.fast.aws.connection.commons.enumeration.TipoECEnum;
import br.com.fast.aws.connection.dynamodb.DynamoDbAdapterConfiguration;
import br.com.fast.aws.connection.dynamodb.teste.entity.ConcessionariaEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.EstabelecimentoComercialTesteEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.EstacionamentoEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.GestaoIdentificadorTesteEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.NumeroIdentificadorTestePredicate;

/**
 * 
 * @author Wagner Alves
 *
 */
public class DynamoDbAdapterClientTest extends DynamoDBBaseTeste {

    private static boolean isExecuting;

    @Before
    public void setUp() throws InterruptedException {

        if (!isExecuting) {

        	tableName = "Fast-AWS-Connections-GestaoIdentificador";
            tableNamePolymorphic = "Fast-AWS-Connections-EC-Polymorphic";

            isExecuting = true;

            client = new DynamoDbAdapterConfiguration<GestaoIdentificadorTesteEntity>()
                    .withAwsRegion("us-east-1")
                    .withAwsProfile("dev")
                    .withHost(dynamoEndpointHost)
                    .withPort(dynamoEndpointPort)
                    .withTableName(tableName)
                    .withUseEndpoint(useEndpoint)
                    .withAmazonDynamoDBClient();

            clientPolymorphic = new DynamoDbAdapterConfiguration<EstabelecimentoComercialTesteEntity>()
                    .withAwsRegion("us-east-1")
                    .withAwsProfile("dev")
                    .withHost(dynamoEndpointHost)
                    .withPort(dynamoEndpointPort)
                    .withTableName(tableNamePolymorphic)
                    .withUseEndpoint(useEndpoint)
                    .withAmazonDynamoDBClient();

            // Objeto para simular teste
            entity = geraEntityParaTeste();
            
            //TearDown
            gestaoIdentificadorTearDown();
            estabelecimentoComercialTearDown();
            
            //Waiting
            Thread.sleep(5000);
    
        }
    }

    @Test
    public void deveInserirERecuperarRegistroNoDynamo() {

        // Inserindo
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Buscando
        PaginatedQueryList<GestaoIdentificadorTesteEntity> list = client.executeQuery(GestaoIdentificadorTesteEntity.class,
                getQueryExpressionPorPlaca("ABC-1010"));

        GestaoIdentificadorTesteEntity entityRetornada = (GestaoIdentificadorTesteEntity) list.get(0);

        assertEquals(entity.getCodIdentificador(), entity.getCodIdentificador());
        assertEquals(entity.getPlaca(), entity.getPlaca());
        assertEquals(entity.getDataInclusao(), entity.getDataInclusao());
        assertEquals(entity.getIdCliente(), entity.getIdCliente());
        assertEquals(entity.getIdConta(), entity.getIdConta());
        assertEquals(entity.getNuIdentificador(), entity.getNuIdentificador());

        assertEquals(entityRetornada, entity);

        
        gestaoIdentificadorTearDown();

    }

    /**
     * Tests the client load and executeQuery method
     */
    @Test
    public void deveCarregarRegistroDoDynamo() {

        // Inserindo
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Buscando
        PaginatedQueryList<GestaoIdentificadorTesteEntity> list = client.executeQuery(GestaoIdentificadorTesteEntity.class,
                getQueryExpressionPorPlaca("ABC-1010"));

        GestaoIdentificadorTesteEntity obj = (GestaoIdentificadorTesteEntity) list.get(0);

        // Carregando
        GestaoIdentificadorTesteEntity result = (GestaoIdentificadorTesteEntity) client.load(GestaoIdentificadorTesteEntity.class,
                obj.getId());

        assertEquals(result.getNuIdentificador(), entity.getNuIdentificador());

        
        gestaoIdentificadorTearDown();

    }

    /**
     * Tests the client insert, delete and load methods.
     */
    @Test
    public void deveExcluirRegistroDoDynamo() {

        // Inserindo
        client.insert(entity);

        // Deletando
        client.delete(entity);

        // Tentando recuperar depois de apagado
        Object obj = client.load(GestaoIdentificadorTesteEntity.class, entity.getId());

        assertNull(obj);

        
    }

    /**
     * Tests the client insert, scan and batchDelete methods.
     */
    @Test
    public void deveExcluirRegistroDoDynamoEmLote() {

        // Insere 10 objetos
        for (int i = 0; i < 10; i++) {
            client.insert(geraEntityParaTeste());
        }

        // Recupera
        List<GestaoIdentificadorTesteEntity> lista = client.scan(GestaoIdentificadorTesteEntity.class,
                getScanExpressionPorPlaca("WAG-1010"));

        // Deletando
        client.batchDelete(lista);

        // Tentando recuperar depois de apagado
        // Deve retornar lista vazia
        List<GestaoIdentificadorTesteEntity> listaResult = client.scan(GestaoIdentificadorTesteEntity.class,
                getScanExpressionPorPlaca("WAG-1010"));

        assertEquals(0, listaResult.size());

        
    }

    /**
     * Tests the client insert and executeQuery methods.
     */
    @Test
    public void deveRetornarRegistroPeloNumeroDaPlacaComQuery() {

        // Insere
        entity.setPlaca("WAG-1010");
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Recupera
        PaginatedQueryList<GestaoIdentificadorTesteEntity> resultList = client.executeQuery(GestaoIdentificadorTesteEntity.class,
                getQueryExpressionPorPlaca("WAG-1010"));

        // Assert
        assertEquals(resultList.get(0), entity);

        
        gestaoIdentificadorTearDown();

    }

    /**
     * Tests the client insert and scan methods.
     */
    @Test
    public void deveRetornarRegistroPeloNumeroDaPlacaComScan() {

        // Insere
        entity.setPlaca("WAG-1010");
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Recupera
        List<GestaoIdentificadorTesteEntity> resultList = client.scan(GestaoIdentificadorTesteEntity.class,
                getScanExpressionPorPlaca("WAG-1010"));

        // Assert
        assertEquals(resultList.get(0), entity);

        
        gestaoIdentificadorTearDown();
    }

    /**
     * Tests the client insert and scanPaginated methods.
     */
    @Test
    public void deveRetornarRegistroPeloNumeroDaPlacaComScanPaginated() {

        // Insere
        entity.setPlaca("WAG-1010");
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Recupera
        List<GestaoIdentificadorTesteEntity> resultList = client.scanPaginated(GestaoIdentificadorTesteEntity.class,
                getScanExpressionPorPlaca("WAG-1010"));

        // Assert
        assertEquals(resultList.get(0), entity);

        
        gestaoIdentificadorTearDown();
    }

    /**
     * Tests the client insert and scanPaginated methods.
     */
    @Test
    public void deveRetornarRegistroPeloNumeroDaPlacaComQueryPaginated() {

        // Insere
        entity.setPlaca("WAG-1010");
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Recupera
        List<GestaoIdentificadorTesteEntity> resultList = client.queryPaginated(GestaoIdentificadorTesteEntity.class,
                getQueryExpressionPorPlaca("WAG-1010"));

        // Assert
        assertEquals(resultList.get(0), entity);

        
        gestaoIdentificadorTearDown();
    }

    /**
     * Tests the client insert and scanPage methods with Predicate.
     */
    @Test
    public void deveRetornarRegistrosComFiltroPredicate() {

        // Adicionando Identificadores
        GestaoIdentificadorTesteEntity identificador1 = new GestaoIdentificadorTesteEntity();
        identificador1.setNuIdentificador(1L);
        GestaoIdentificadorTesteEntity identificador2 = new GestaoIdentificadorTesteEntity();
        identificador2.setNuIdentificador(2L);
        GestaoIdentificadorTesteEntity identificador3 = new GestaoIdentificadorTesteEntity();
        identificador3.setNuIdentificador(3L);
        GestaoIdentificadorTesteEntity identificador4 = new GestaoIdentificadorTesteEntity();
        identificador4.setNuIdentificador(4L);
        GestaoIdentificadorTesteEntity identificador5 = new GestaoIdentificadorTesteEntity();
        identificador5.setNuIdentificador(5L);

        client.insert(identificador1);
        client.insert(identificador2);
        client.insert(identificador3);
        client.insert(identificador4);
        client.insert(identificador5);

        // Buscando identificadores
        // Predicate
        List<Long> numeros = Arrays.asList(1L, 3L, 5L, 7L, 9L);
        NumeroIdentificadorTestePredicate predicate = new NumeroIdentificadorTestePredicate(numeros);

        // Expression
        DynamoDBScanExpression expression = new DynamoDBScanExpression()
                .withConsistentRead(false);

        List<GestaoIdentificadorTesteEntity> listaDeIdentificadores = client.scanPage(GestaoIdentificadorTesteEntity.class, expression);

        List<GestaoIdentificadorTesteEntity> listaDeIdentificadoresComPredicate = client.scanPage(GestaoIdentificadorTesteEntity.class,
                expression, predicate);

        // Assert
        assertEquals(5, listaDeIdentificadores.size());

        // Assert
        assertEquals(2, listaDeIdentificadoresComPredicate.size());

        
        gestaoIdentificadorTearDown();

    }

    //@Test
    public void testeScanPolymorphic() throws ClassNotFoundException, InterruptedException {
        // Insere
        clientPolymorphic.insert(geraConcessionariaParaTeste(01, TipoECEnum.CONCESSIONARIA, "1010"));
        clientPolymorphic.insert(geraEstacionamentoParaTeste(02, TipoECEnum.ESTACIONAMENTO, "1011"));
        
        //Waiting
        System.out.println("Waiting...");
        Thread.sleep(5000);
        
        @SuppressWarnings("unchecked")
        List<EstabelecimentoComercialTesteEntity> list = clientPolymorphic.scanPolymorphic(
                EstabelecimentoComercialTesteEntity.class,
                getScanRequestForTest(tableNamePolymorphic));
        
        ConcessionariaEntity concessionariaEsperada = (ConcessionariaEntity) list.get(1);
        EstacionamentoEntity estacionamentoEsperado = (EstacionamentoEntity) list.get(0);

        assertEquals(2, list.size());
        assertEquals("1010", concessionariaEsperada.getCodConcessionaria());
        assertEquals("1011", estacionamentoEsperado.getCodEstacionamento());

        
        estabelecimentoComercialTearDown();

    }
    
    @Test
    public void testeQueryPolymorphic() throws ClassNotFoundException {

    	Integer idConcessionaria = 1;
    	Integer idEstacionamento = 2;
        clientPolymorphic.insert(geraConcessionariaParaTeste(idConcessionaria, TipoECEnum.CONCESSIONARIA, "1010"));
        clientPolymorphic.insert(geraEstacionamentoParaTeste(idEstacionamento, TipoECEnum.ESTACIONAMENTO, "1011"));

        
        ConcessionariaEntity concessionariaEsperada =(ConcessionariaEntity)  this.queryTestEstabelecimento(idConcessionaria);
        EstacionamentoEntity estacionamentoEsperado = (EstacionamentoEntity)  this.queryTestEstabelecimento(idEstacionamento);

        assertEquals(TipoECEnum.CONCESSIONARIA, concessionariaEsperada.getTipoEC());
        assertEquals(TipoECEnum.ESTACIONAMENTO, estacionamentoEsperado.getTipoEC());
        
        assertEquals("1010", concessionariaEsperada.getCodConcessionaria());
        assertEquals("1011", estacionamentoEsperado.getCodEstacionamento());

        System.out.println("....");

        
        estabelecimentoComercialTearDown();

    }
   
    @Test
    public void testeScanPolymorphicPaginated() throws ClassNotFoundException {
        // Insere
        for (int x = 0; x < 10; x++) {
            clientPolymorphic.insert(geraConcessionariaParaTeste(x, TipoECEnum.CONCESSIONARIA, "10" + x));
            clientPolymorphic.insert(geraEstacionamentoParaTeste(1000 + x, TipoECEnum.ESTACIONAMENTO, "20" + x));
        }

        @SuppressWarnings("unchecked")
        List<EstabelecimentoComercialTesteEntity> list = clientPolymorphic.scanPolymorphicPaginated(
                EstabelecimentoComercialTesteEntity.class,
                getScanRequestForTest(tableNamePolymorphic));

        assertEquals(20, list.size());

        System.out.println("....");

        
        estabelecimentoComercialTearDown();

    }

    @Test
    public void deveExecutarFindAll() {

        // Inserindo
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        List<GestaoIdentificadorTesteEntity> list = client.findAll(GestaoIdentificadorTesteEntity.class);

        Assert.assertEquals(1, list.size());

        
        gestaoIdentificadorTearDown();

    }

    @Test
    public void deveExcluirTodosRegistrosDeUmaTabela() {

        int quantidadeRegistrosInseridos = 0;

        // Adicionando Identificadores
        GestaoIdentificadorTesteEntity identificador1 = new GestaoIdentificadorTesteEntity();
        identificador1.setNuIdentificador(1L);
        GestaoIdentificadorTesteEntity identificador2 = new GestaoIdentificadorTesteEntity();
        identificador2.setNuIdentificador(2L);
        GestaoIdentificadorTesteEntity identificador3 = new GestaoIdentificadorTesteEntity();
        identificador3.setNuIdentificador(3L);
        GestaoIdentificadorTesteEntity identificador4 = new GestaoIdentificadorTesteEntity();
        identificador4.setNuIdentificador(4L);
        GestaoIdentificadorTesteEntity identificador5 = new GestaoIdentificadorTesteEntity();

        client.insert(identificador1);
        client.insert(identificador2);
        client.insert(identificador3);
        client.insert(identificador4);
        client.insert(identificador5);

        // Teste inserção
        quantidadeRegistrosInseridos = client.findAll(GestaoIdentificadorTesteEntity.class).size();

        Assert.assertEquals(5, quantidadeRegistrosInseridos);

        // Teste DeleteAll
        client.deleteAll(GestaoIdentificadorTesteEntity.class);

        quantidadeRegistrosInseridos = client.findAll(GestaoIdentificadorTesteEntity.class).size();

        Assert.assertEquals(0, quantidadeRegistrosInseridos);

    }

    @Test
    public void deveExecutarScanCount() {

        // Adicionando Identificadores
        GestaoIdentificadorTesteEntity identificador1 = new GestaoIdentificadorTesteEntity();
        identificador1.setNuIdentificador(1L);
        GestaoIdentificadorTesteEntity identificador2 = new GestaoIdentificadorTesteEntity();
        identificador2.setNuIdentificador(2L);
        GestaoIdentificadorTesteEntity identificador3 = new GestaoIdentificadorTesteEntity();
        identificador3.setNuIdentificador(3L);
        GestaoIdentificadorTesteEntity identificador4 = new GestaoIdentificadorTesteEntity();
        identificador4.setNuIdentificador(4L);
        GestaoIdentificadorTesteEntity identificador5 = new GestaoIdentificadorTesteEntity();
        identificador5.setNuIdentificador(5L);

        client.insert(identificador1);
        client.insert(identificador2);
        client.insert(identificador3);
        client.insert(identificador4);
        client.insert(identificador5);

        int countScan = client.countScan(GestaoIdentificadorTesteEntity.class, new DynamoDBScanExpression());
        Assert.assertEquals(5, countScan);

        
        gestaoIdentificadorTearDown();

    }

    @Test
    public void deveExecutarQueryCount() throws InterruptedException {

        // Insere
        entity.setPlaca("WAG-1010");
        client.insert(entity);
        System.out.println("Inserido: " + entity.toString());

        // Recupera
        PaginatedQueryList<GestaoIdentificadorTesteEntity> resultList = client.executeQuery(GestaoIdentificadorTesteEntity.class,
                getQueryExpressionPorPlaca("WAG-1010"));
        // Assert
        assertEquals(resultList.get(0), entity);

        // Count
        int countQuery = client.countQuery(GestaoIdentificadorTesteEntity.class, getQueryExpressionPorPlaca("WAG-1010"));
        // Assert
        Assert.assertEquals(1, countQuery);

        
        gestaoIdentificadorTearDown();

    }

    @Test
    public void deveDescreverTabela() {
        String describeTableJson = client.describeTableToJSON(tableName);
        System.out.println(describeTableJson);

        String describeTableYAML = client.describeTableToYAML(tableName);
        System.out.println(describeTableYAML);

    }

  
	@SuppressWarnings("unchecked")
	private EstabelecimentoComercialTesteEntity queryTestEstabelecimento(Integer idEstabelecimento) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":id", new AttributeValue().withN(idEstabelecimento.toString()));

		QueryRequest queryExpression = new QueryRequest().withKeyConditionExpression("id = :id")
				.withConsistentRead(false).withScanIndexForward(false).withExpressionAttributeValues(eav)
				.withSelect(Select.ALL_ATTRIBUTES);

		List<EstabelecimentoComercialTesteEntity> list = clientPolymorphic
				.queryPolymorphic(EstabelecimentoComercialTesteEntity.class, queryExpression);

		return list.stream().findFirst().orElse(null);
	}

	@After
    public void deleteTable() {
       

    }

    private void gestaoIdentificadorTearDown() {
        client.deleteAll(GestaoIdentificadorTesteEntity.class);
    }

    private void estabelecimentoComercialTearDown() {
        clientPolymorphic.deleteAll(EstabelecimentoComercialTesteEntity.class);
    }

}
