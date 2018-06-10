package br.com.fast.aws.connection.dynamodb.teste;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.Select;
import com.amazonaws.util.DateUtils;

import br.com.fast.aws.connection.commons.enumeration.TipoECEnum;
import br.com.fast.aws.connection.dynamodb.DynamoDbAdapterClient;
import br.com.fast.aws.connection.dynamodb.teste.entity.ConcessionariaEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.EstabelecimentoComercialTesteEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.EstacionamentoEntity;
import br.com.fast.aws.connection.dynamodb.teste.entity.GestaoIdentificadorTesteEntity;

public class DynamoDBBaseTeste {

    protected boolean useEndpoint = false;
    protected String dynamoEndpointHost = "dynamodb";
    protected Integer dynamoEndpointPort = 8000;

    protected static String tableName;
    protected static String tableNamePolymorphic;

    protected static DynamoDbAdapterClient<GestaoIdentificadorTesteEntity> client;
    protected static DynamoDbAdapterClient<GestaoIdentificadorTesteEntity> clientDax;
    protected static GestaoIdentificadorTesteEntity entity;

    protected static DynamoDbAdapterClient<EstabelecimentoComercialTesteEntity> clientPolymorphic;

    // ****** METODOS AUXILIARES *******

    protected DynamoDBQueryExpression<GestaoIdentificadorTesteEntity> getQueryExpressionPorPlaca(String placa) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":placa", new AttributeValue().withS(placa));

        return new DynamoDBQueryExpression<GestaoIdentificadorTesteEntity>()
                .withKeyConditionExpression("placa = :placa")
                .withIndexName("_GIPlacaIndex")
                .withConsistentRead(false)
                .withScanIndexForward(false)
                .withExpressionAttributeValues(eav)
                .withSelect(Select.ALL_ATTRIBUTES);

    }

    protected DynamoDBScanExpression getScanExpressionPorPlaca(String placa) {

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":placa", new AttributeValue().withS(placa));

        return new DynamoDBScanExpression()
                .withFilterExpression("placa = :placa")
                .withIndexName("_GIPlacaIndex")
                .withConsistentRead(false)
                .withExpressionAttributeValues(eav)
                .withSelect(Select.ALL_ATTRIBUTES);

    }

    protected ScanRequest getScanRequestForTest(String tableName) {

        Date dataLiberacao = new Date();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":dataLiberacao", new AttributeValue().withS(DateUtils.formatISO8601Date(dataLiberacao)));
        eav.put(":liberadoOperacao", new AttributeValue().withN("0"));
        eav.put(":registroAtual", new AttributeValue().withN("1"));

        return new ScanRequest()
                // .withFilterExpression("dataLiberacao = :dataLiberacao and liberadoOperacao = :liberadoOperacao and
                // registroAtual = :registroAtual")
                // .withExpressionAttributeValues(eav)
                .withConsistentRead(false);
    }

    protected String geraNomeDeTabelaDinamico(String tableName) {
        Calendar data = Calendar.getInstance();
        String result = tableName.concat(String.valueOf(data.getTimeInMillis()));
        System.out.println("Nome de Tabela gerado: " + result);
        return result;

    }

    protected GestaoIdentificadorTesteEntity geraEntityParaTeste() {

        GestaoIdentificadorTesteEntity entity = new GestaoIdentificadorTesteEntity();
        entity.setCodIdentificador("11111111");
        entity.setPlaca("ABC-1010");
        entity.setDataInclusao(new Date());
        entity.setIdCliente(45);
        entity.setIdConta(345345);
        entity.setNuIdentificador(4523523452345L);

        return entity;
    }

    protected GestaoIdentificadorTesteEntity geraEntityParaTeste(String placa) {

        GestaoIdentificadorTesteEntity entity = new GestaoIdentificadorTesteEntity();
        entity.setCodIdentificador("11111111");
        entity.setPlaca(placa);
        entity.setDataInclusao(new Date());
        entity.setIdCliente(45);
        entity.setIdConta(345345);
        entity.setNuIdentificador(4523523452345L);

        return entity;
    }

    protected ConcessionariaEntity geraConcessionariaParaTeste(Integer idEC, TipoECEnum tipo, String codConcessionaria) {

        ConcessionariaEntity c = new ConcessionariaEntity();
        c.setId(idEC);
        c.setTipoEC(tipo);
        // c.setAgenciaReguladora(AgenciaReguladoraEnum.ARTESP);
        c.setCadastroAutomatico(true);
        c.setCodConcessionaria(codConcessionaria);
        c.setCodLoteLiberacaoEspecial(2);
        // c.setConta(new ContaEntity(3));
        c.setDataEncerramento(new Date());
        c.setDataLiberacao(new Date());
        c.setDiasRepassePos(4);
        c.setDiasRepassePre(5);
        c.setDiasSlaReEnvio(6);
        c.setLiberadoOperacao(false);
        // c.setMaiorTarifaPorCategoria(new ArrayList<MaiorTarifaEntity>());
        // c.setModalidadeRepassePos(ModalidadeRepassePosEnum.TRANSACAO);
        // c.setPracas(new ArrayList<PracaEntity>());
        c.setRegistroAtual(true);
        c.setSegundosBloqueioLeituraDuplicada(7);
        // c.setSlaEnvio(new SLAConfigEntity());
        // c.setTipoRepasse(TipoRepasseEnum.BRUTO);
        c.setUtilizaPap(false);
        c.setValidaPlaca(true);
        c.setDataInclusao(new Date());

        return c;

    }

    protected EstacionamentoEntity geraEstacionamentoParaTeste(Integer idEC, TipoECEnum tipo, String codEstacionamento) {

        EstacionamentoEntity e = new EstacionamentoEntity();
        e.setId(idEC);
        e.setTipoEC(tipo);
        // c.setAgenciaReguladora(AgenciaReguladoraEnum.ARTESP);
        e.setCodEstacionamento(codEstacionamento);
        e.setCodLoteLiberacaoEspecial(2);
        // c.setConta(new ContaEntity(3));
        e.setDataEncerramento(new Date());
        e.setDataLiberacao(new Date());
        e.setDiasRepassePos(4);
        e.setDiasRepassePre(5);
        e.setDiasSlaReEnvio(6);
        e.setLiberadoOperacao(false);
        // c.setMaiorTarifaPorCategoria(new ArrayList<MaiorTarifaEntity>());
        // c.setModalidadeRepassePos(ModalidadeRepassePosEnum.TRANSACAO);
        // c.setPracas(new ArrayList<PracaEntity>());
        e.setRegistroAtual(true);
        // c.setSlaEnvio(new SLAConfigEntity());
        // c.setTipoRepasse(TipoRepasseEnum.BRUTO);
        e.setDataInclusao(new Date());

        return e;

    }

}
