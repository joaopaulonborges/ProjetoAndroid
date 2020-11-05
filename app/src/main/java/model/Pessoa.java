package model;

public abstract class Pessoa {
    private String id, nome, telefone, email, senha;
    private Boolean publicar;

    public Pessoa(){

    }

    public Pessoa(String id, String nome, String telefone, String email, String senha, Boolean publicar) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.publicar = publicar;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean getPublicar() { return publicar; }

    public void setPublicar(Boolean publicar) { this.publicar = publicar; }
}