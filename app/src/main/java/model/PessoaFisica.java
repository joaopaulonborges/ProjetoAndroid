package model;

import java.io.Serializable;

public class PessoaFisica extends Pessoa implements Serializable {
    private String cpf;

    public PessoaFisica(){

    }

    public PessoaFisica(String id, String nome, String telefone, String email, String senha, String cpf, Boolean publicar) {
        super(id, nome, telefone, email, senha, publicar);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}