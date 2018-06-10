package br.com.fast.aws.connection.dynamodb.teste.entity;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Bruno.Delphim
 */

@DynamoDBDocument
public class CadastroTecnicoTesteEntity extends EstabelecimentoComercialTesteEntity {

    private Date dataEncerramento;
    private Date dataLiberacao;

    private Boolean liberadoOperacao;
    private Integer diasSlaReEnvio;
    private Integer diasRepassePre;
    private Integer diasRepassePos;

    public boolean isLiberadoOperacao() {
        return liberadoOperacao;
    }

    public void setLiberadoOperacao(boolean liberadoOperacao) {
        this.liberadoOperacao = liberadoOperacao;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        if (dataEncerramento != null)
            this.dataEncerramento = new Date(dataEncerramento.getTime());
    }

    public Date getDataLiberacao() {
        return dataLiberacao;
    }

    public void setDataLiberacao(Date dataLiberacao) {
        if (dataLiberacao != null)
            this.dataLiberacao = new Date(dataLiberacao.getTime());
    }

    public Integer getDiasSlaReEnvio() {
        return diasSlaReEnvio;
    }

    public void setDiasSlaReEnvio(Integer diasSlaReEnvio) {
        this.diasSlaReEnvio = diasSlaReEnvio;
    }

    public Integer getDiasRepassePre() {
        return diasRepassePre;
    }

    public void setDiasRepassePre(Integer diasRepassePre) {
        this.diasRepassePre = diasRepassePre;
    }

    public Integer getDiasRepassePos() {
        return diasRepassePos;
    }

    public void setDiasRepassePos(Integer diasRepassePos) {
        this.diasRepassePos = diasRepassePos;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(this);
    }

}
