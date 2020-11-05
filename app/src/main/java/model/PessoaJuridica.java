package model;

import java.io.Serializable;

public class PessoaJuridica extends Pessoa implements Serializable {
    private String cnpj;

    public PessoaJuridica(){

    }

    public PessoaJuridica(String id, String nome, String telefone, String email, String senha, String cnpj, Boolean publicar) {
        super(id, nome, telefone, email, senha, publicar);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}