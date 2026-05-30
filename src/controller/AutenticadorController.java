package controller;

import model.Funcionario;
import model.NivelAcesso;
import repository.FuncionarioRepository;

public class AutenticadorController {
    private FuncionarioRepository funcionarioRepository;
    private Funcionario funcionarioLogado;

    // CONSTRUTOR CORRIGIDO: O nome do parâmetro agora bate exatamente com a atribuição
    public AutenticadorController(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        // Cria o administrador padrão na memória para permitir o primeiro login
        inicializarAdminPadrao();
    }

    private void inicializarAdminPadrao() {
        // CORRIGIDO: Alinhado para buscar pela matrícula correta e usando o Enum correto
        if (funcionarioRepository.buscarPorMatricula("123") == null) {
            Funcionario admin = new Funcionario(
                    "Administrador Padrão",
                    "000.000.000-00",
                    "99999-9999",
                    "123",
                    "admin",
                    NivelAcesso.ADMINISTRADOR
            );
            funcionarioRepository.salvar(admin);
        }
    }

    // Lógica do Login
    public boolean fazerLogin(String matricula, String senha) {
        Funcionario f = funcionarioRepository.buscarPorMatricula(matricula);

        // Validação de segurança: evita NullPointerException se o funcionário não existir
        if (f != null && f.getSenha().equals(senha)) {
            this.funcionarioLogado = f; // Guarda o funcionário que está operando o sistema
            return true;
        }
        return false;
    }

    public void fazerLogout() {
        this.funcionarioLogado = null;
    }

    public Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }

    // Método auxiliar para checar se o usuário atual é Administrador
    public boolean ehAdmin() {
        return funcionarioLogado != null &&
                funcionarioLogado.getNivelAcesso() == NivelAcesso.ADMINISTRADOR;
    }
}
