package br.com.fast.aws.connections.examples.kinesis.model;

/**
 * @author Wagner Alves
 */
public class TransacaoDTO implements JSONConvertable<TransacaoDTO> {

    private long id;
    private long idConcessionaria;

    public TransacaoDTO(long id, long idConcessionaria) {
        super();
        this.id = id;
        this.idConcessionaria = idConcessionaria;
    }

    public long getId() {
        return id;
    }

    public long getIdConcessionaria() {
        return idConcessionaria;
    }

    public void increaseId(long id) {
        this.id = id + 1;
    }

}
