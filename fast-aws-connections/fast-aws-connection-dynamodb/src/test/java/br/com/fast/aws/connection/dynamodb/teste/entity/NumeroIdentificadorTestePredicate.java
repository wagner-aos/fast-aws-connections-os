package br.com.fast.aws.connection.dynamodb.teste.entity;

import java.util.List;
import java.util.function.Predicate;

public class NumeroIdentificadorTestePredicate implements Predicate<GestaoIdentificadorTesteEntity> {

    private List<Long> identificadores;

    public NumeroIdentificadorTestePredicate(List<Long> identificadores) {
        this.identificadores = identificadores;
    }

    @Override
    public boolean test(GestaoIdentificadorTesteEntity entity) {
        return !identificadores.contains(entity.getNuIdentificador());
    }

}
