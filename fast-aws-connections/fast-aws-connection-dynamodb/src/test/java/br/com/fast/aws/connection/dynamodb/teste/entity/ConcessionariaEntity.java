package br.com.fast.aws.connection.dynamodb.teste.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * @author Bruno.Delphim
 */

@DynamoDBDocument
public class ConcessionariaEntity extends CadastroTecnicoTesteEntity {

    private String codConcessionaria;
    private Integer codLoteLiberacaoEspecial;

    private Integer diasRepasseValePedagio;
    private Integer segundosBloqueioLeituraDuplicada;
    private Boolean validaPlaca;
    private Boolean utilizaPap;
    private Boolean cadastroAutomatico = false;

    public ConcessionariaEntity() {

    }

    public ConcessionariaEntity(Integer id) {
        this.setId(id);
    }

    public String getCodConcessionaria() {
        return codConcessionaria;
    }

    public void setCodConcessionaria(String codConcessionaria) {
        this.codConcessionaria = codConcessionaria;
    }

    public Integer getCodLoteLiberacaoEspecial() {
        return codLoteLiberacaoEspecial;
    }

    public void setCodLoteLiberacaoEspecial(Integer codLoteLiberacaoEspecial) {
        this.codLoteLiberacaoEspecial = codLoteLiberacaoEspecial;
    }

    public Integer getDiasRepasseValePedagio() {
        return diasRepasseValePedagio;
    }

    public void setDiasRepasseValePedagio(Integer diasRepasseValePedagio) {
        this.diasRepasseValePedagio = diasRepasseValePedagio;
    }

    public Integer getSegundosBloqueioLeituraDuplicada() {
        return segundosBloqueioLeituraDuplicada;
    }

    public void setSegundosBloqueioLeituraDuplicada(Integer segundosBloqueioLeituraDuplicada) {
        this.segundosBloqueioLeituraDuplicada = segundosBloqueioLeituraDuplicada;
    }

    public Boolean isValidaPlaca() {
        return validaPlaca;
    }

    public void setValidaPlaca(Boolean validaPlaca) {
        this.validaPlaca = validaPlaca;
    }

    public Boolean isUtilizaPap() {
        return utilizaPap;
    }

    public void setUtilizaPap(Boolean utilizaPap) {
        this.utilizaPap = utilizaPap;
    }

    public Boolean getCadastroAutomatico() {
        return cadastroAutomatico;
    }

    public void setCadastroAutomatico(Boolean cadastroAutomatico) {
        this.cadastroAutomatico = cadastroAutomatico;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConcessionariaEntity [codConcessionaria=");
        builder.append(codConcessionaria);
        builder.append(", codLoteLiberacaoEspecial=");
        builder.append(codLoteLiberacaoEspecial);
        builder.append(", pracas=");
        builder.append(", agenciaReguladora=");
        builder.append(", diasRepasseValePedagio=");
        builder.append(diasRepasseValePedagio);
        builder.append(", segundosBloqueioLeituraDuplicada=");
        builder.append(segundosBloqueioLeituraDuplicada);
        builder.append(", validaPlaca=");
        builder.append(validaPlaca);
        builder.append(", utilizaPap=");
        builder.append(utilizaPap);
        builder.append(", maiorTarifaPorCategoria=");
        builder.append(", cadastroAutomatico=");
        builder.append(cadastroAutomatico);
        builder.append("]");
        return builder.toString();
    }

}
