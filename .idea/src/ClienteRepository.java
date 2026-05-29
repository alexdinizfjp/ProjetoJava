package repository;

import model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private List<Cliente> clientes = new ArrayList<>();

    public void salvar(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Cliente> listarTodos() {
        return clientes;
    }

    public Cliente buscarPorId(String idCliente) {
        for (Cliente c : clientes) {
            if (c.getId().equals(idCliente)) {
                return c;
            }
        }
        return null;
    }

    public boolean atualizar(Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorId(clienteAtualizado.getId());
        if (clienteExistente != null) {
            int indice = clientes.indexOf(clienteExistente);
            clientes.set(indice, clienteAtualizado);
            return true;
        }
        return false;
    }

    public boolean deletar(String idCliente) {
        return clientes.removeIf(c -> c.getId().equals(idCliente));
    }
}