package br.com.fast.aws.connection.dynamodb.teste.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.fast.aws.connection.commons.enumeration.TipoECEnum;

/**
 * @author Wagner.Alves
 */

@DynamoDBTable(tableName = "REPLACED_BY_VALUE_IN_PROPERTIES_FILE")
public class EstabelecimentoComercialTesteEntity {

    @DynamoDBHashKey
    private Integer id;

    @DynamoDBTypeConvertedEnum
    private TipoECEnum tipoEC;

    @DynamoDBRangeKey
    private Date dataInclusao;

    private Boolean registroAtual;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoECEnum getTipoEC() {
        return tipoEC;
    }

    public void setTipoEC(TipoECEnum tipoEC) {
        this.tipoEC = tipoEC;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        if (dataInclusao != null)
            this.dataInclusao = new Date(dataInclusao.getTime());
    }

    public Boolean getRegistroAtual() {
        return registroAtual;
    }

    public void setRegistroAtual(Boolean registroAtual) {
        this.registroAtual = registroAtual;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

}
