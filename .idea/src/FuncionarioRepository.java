package repository;

import model.Funcionario;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {
    private List<Funcionario> funcionarios = new ArrayList<>();

    public void salvar(Funcionario funcionario) {
        funcionarios.add(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarios;
    }

    public Funcionario buscarPorMatricula(String matricula) {
        for (Funcionario f : funcionarios) {
            if (f.getMatricula().equals(matricula)) {
                return f;
            }
        }
        return null;
    }

    public boolean atualizar(Funcionario funcionarioAtualizado) {
        Funcionario funcionarioExistente = buscarPorMatricula(funcionarioAtualizado.getMatricula());
        if (funcionarioExistente != null) {
            int indice = funcionarios.indexOf(funcionarioExistente);
            funcionarios.set(indice, funcionarioAtualizado);
            return true;
        }
        return false;
    }

    public boolean deletar(String matricula) {
        return funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
    }
}