package br.com.fast.aws.connection.dynamodb.teste.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class EstacionamentoEntity extends CadastroTecnicoTesteEntity {

    private String codEstacionamento;
    private Integer codLoteLiberacaoEspecial;

    public EstacionamentoEntity() {

    }

    public String getCodEstacionamento() {
        return codEstacionamento;
    }

    public void setCodEstacionamento(String codEstacionamento) {
        this.codEstacionamento = codEstacionamento;
    }

    public Integer getCodLoteLiberacaoEspecial() {
        return codLoteLiberacaoEspecial;
    }

    public void setCodLoteLiberacaoEspecial(Integer codLoteLiberacaoEspecial) {
        this.codLoteLiberacaoEspecial = codLoteLiberacaoEspecial;
    }

}
