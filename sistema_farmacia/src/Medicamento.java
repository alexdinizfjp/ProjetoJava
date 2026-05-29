package model;

import java.time.LocalDate;

public class Medicamento {
    private String id;
    private String nome;
    private String principioAtivo;
    private double precoCusto;
    private double precoVenda;
    private int quantidadeEstoque;
    private LocalDate dataValidade;

    public Medicamento(String id, String nome, String principioAtivo, double precoCusto, double precoVenda, int quantidadeEstoque, LocalDate dataValidade) {
        this.id = id;
        this.nome = nome;
        this.principioAtivo = principioAtivo;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.dataValidade = dataValidade;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPrincipioAtivo() { return principioAtivo; }
    public void setPrincipioAtivo(String principioAtivo) { this.principioAtivo = principioAtivo; }

    public double getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }

    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
}
