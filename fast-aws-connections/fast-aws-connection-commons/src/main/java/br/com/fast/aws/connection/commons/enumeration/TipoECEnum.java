package br.com.fast.aws.connection.commons.enumeration;

import java.util.Arrays;

public enum TipoECEnum {

    CONCESSIONARIA("concessionaria", "ConcessionariaEntity"),
    ESTACIONAMENTO("estacionamento", "EstacionamentoEntity"),
    ABASTECIMENTO("abastecimento", "AbastecimentoEntity");

    private String descricao;
    private String entityName;

    TipoECEnum(String descricao, String entityName) {
        this.descricao = descricao;
        this.entityName = entityName;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEntityName() {
        return entityName;
    }

    public static TipoECEnum fromDescricao(String descricao) {
        return Arrays.stream(TipoECEnum.values())
                .filter(tipoEC -> tipoEC.getDescricao().equals(descricao))
                .findFirst().orElse(null);
    }
}