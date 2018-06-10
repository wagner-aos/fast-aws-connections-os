package br.com.fast.aws.connection.sqs.teste.dto;

import br.com.fast.aws.connection.commons.interfaces.JSONConvertable;

public class PassagemDTO implements JSONConvertable<PassagemDTO> {

    private Long tagId;
    private String placa;
    private Long datahora;
    private Long passagemId;
    private int reenvio;
    private int motivoReenvio;
    private int praca;
    private int pista;
    private int catCadastrada;
    private int catDetectada;
    private int catCobrada;
    private int valor;
    private int motivoSemValor;
    private boolean nivelBateria;
    private boolean tagViolado;
    private boolean passAutomatica;
    private int motivoManual;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Long getDatahora() {
        return datahora;
    }

    public void setDatahora(Long datahora) {
        this.datahora = datahora;
    }

    public Long getPassagemId() {
        return passagemId;
    }

    public void setPassagemId(Long passagemId) {
        this.passagemId = passagemId;
    }

    public int getReenvio() {
        return reenvio;
    }

    public void setReenvio(int reenvio) {
        this.reenvio = reenvio;
    }

    public int getMotivoReenvio() {
        return motivoReenvio;
    }

    public void setMotivoReenvio(int motivoReenvio) {
        this.motivoReenvio = motivoReenvio;
    }

    public int getPraca() {
        return praca;
    }

    public void setPraca(int praca) {
        this.praca = praca;
    }

    public int getPista() {
        return pista;
    }

    public void setPista(int pista) {
        this.pista = pista;
    }

    public int getCatCadastrada() {
        return catCadastrada;
    }

    public void setCatCadastrada(int catCadastrada) {
        this.catCadastrada = catCadastrada;
    }

    public int getCatDetectada() {
        return catDetectada;
    }

    public void setCatDetectada(int catDetectada) {
        this.catDetectada = catDetectada;
    }

    public int getCatCobrada() {
        return catCobrada;
    }

    public void setCatCobrada(int catCobrada) {
        this.catCobrada = catCobrada;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getMotivoSemValor() {
        return motivoSemValor;
    }

    public void setMotivoSemValor(int motivoSemValor) {
        this.motivoSemValor = motivoSemValor;
    }

    public boolean isNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(boolean nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public boolean isTagViolado() {
        return tagViolado;
    }

    public void setTagViolado(boolean tagViolado) {
        this.tagViolado = tagViolado;
    }

    public boolean isPassAutomatica() {
        return passAutomatica;
    }

    public void setPassAutomatica(boolean passAutomatica) {
        this.passAutomatica = passAutomatica;
    }

    public int getMotivoManual() {
        return motivoManual;
    }

    public void setMotivoManual(int motivoManual) {
        this.motivoManual = motivoManual;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PassagemDTO: <br> [tagId=");
        builder.append(tagId);
        builder.append(",<br> placa=");
        builder.append(placa);
        builder.append(",<br> datahora=");
        builder.append(datahora);
        builder.append(",<br> passagemId=");
        builder.append(passagemId);
        builder.append(",<br> reenvio=");
        builder.append(reenvio);
        builder.append(",<br> motivoReenvio=");
        builder.append(motivoReenvio);
        builder.append(",<br> praca=");
        builder.append(praca);
        builder.append(",<br> pista=");
        builder.append(pista);
        builder.append(",<br> catCadastrada=");
        builder.append(catCadastrada);
        builder.append(",<br> catDetectada=");
        builder.append(catDetectada);
        builder.append(",<br> catCobrada=");
        builder.append(catCobrada);
        builder.append(",<br> valor=");
        builder.append(valor);
        builder.append(",<br> motivoSemValor=");
        builder.append(motivoSemValor);
        builder.append(",<br> nivelBateria=");
        builder.append(nivelBateria);
        builder.append(",<br> tagViolado=");
        builder.append(tagViolado);
        builder.append(",<br> passAutomatica=");
        builder.append(passAutomatica);
        builder.append(",<br> motivoManual=");
        builder.append(motivoManual);
        builder.append("]");
        return builder.toString();
    }
}
