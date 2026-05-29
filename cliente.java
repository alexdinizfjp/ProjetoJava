package model;

public class Cliente extends Pessoa {
    private String idCliente;

    public Cliente(String nome, String cpf, String telefone, String idCliente) {
        super(nome, cpf, telefone);
        this.idCliente = idCliente;
    }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }
}