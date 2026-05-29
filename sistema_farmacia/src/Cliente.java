package model;

public class Cliente extends Pessoa {
    private String id;

    public Cliente(String nome, String cpf, String telefone, String id) {
        super(nome, cpf, telefone);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
