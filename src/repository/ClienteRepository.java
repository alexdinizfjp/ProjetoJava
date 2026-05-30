package repository;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteRepository {
    private List<Cliente> listaClientes = new ArrayList<>();

    public void salvar(Cliente cliente) {
        listaClientes.add(cliente);
    }

    public List<Cliente> listarTodos() {
        return listaClientes;
    }

    public Cliente buscarPorId(String id) {
        for (Cliente c : listaClientes) {
            if (c.getId().equals(id)) { // Corrigido para getId()
                return c;
            }
        }
        return null;
    }

    public boolean deletar(String id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            listaClientes.remove(cliente);
            return true;
        }
        return false;
    }
}