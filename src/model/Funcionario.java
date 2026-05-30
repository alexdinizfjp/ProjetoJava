package model;

public class Funcionario extends Pessoa {
    private NivelAcesso nivelAcesso;
    private String matricula;
    private String senha;
    private NivelAcesso acesso;

    public Funcionario(String nome, String cpf, String telefone, String matricula, String senha, NivelAcesso nivelAcesso) {
        super(nome, cpf, telefone);
        this.matricula = matricula;
        this.senha = senha;
        this.nivelAcesso = nivelAcesso;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}