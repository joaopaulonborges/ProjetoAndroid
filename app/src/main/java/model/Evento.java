package model;

public class Evento {

    private String id, titulo, descricao, logradouro, numero, bairro, cidade, estado, hora, minuto, localizacao, data;
    private Integer dia, mes, ano, tipo;
    private PessoaJuridica pessoaJuridica;

    public Evento(){

    }

    public Evento(String id, String titulo, String descricao, String logradouro, String numero, String bairro, String cidade, String estado, String hora, String minuto, Integer dia, Integer mes, Integer ano, Integer tipo, PessoaJuridica pessoaJuridica, String localizacao, String data) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.hora = hora;
        this.minuto = minuto;
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.tipo = tipo;
        this.pessoaJuridica = pessoaJuridica;
        this.localizacao = localizacao;
        this.data = data;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) { this.tipo = tipo; }

    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) { this.pessoaJuridica = pessoaJuridica; }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }
}