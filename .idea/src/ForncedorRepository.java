package repository;

import model.Fornecedor;
import java.util.ArrayList;
import java.util.List;

public class FornecedorRepository {
    private List<Fornecedor> fornecedores = new ArrayList<>();

    public void salvar(Fornecedor fornecedor) {
        fornecedores.add(fornecedor);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedores;
    }

    public Fornecedor buscarPorCnpj(String cnpj) {
        for (Fornecedor f : fornecedores) {
            if (f.getCnpj().equals(cnpj)) {
                return f;
            }
        }
        return null;
    }

    public boolean atualizar(Fornecedor fornecedorAtualizado) {
        Fornecedor fornecedorExistente = buscarPorCnpj(fornecedorAtualizado.getCnpj());
        if (fornecedorExistente != null) {
            int indice = fornecedores.indexOf(fornecedorExistente);
            fornecedores.set(indice, fornecedorAtualizado);
            return true;
        }
        return false;
    }

    public boolean deletar(String cnpj) {
        return fornecedores.removeIf(f -> f.getCnpj().equals(cnpj));
    }
}