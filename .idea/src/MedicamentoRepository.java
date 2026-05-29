package repository;

import model.Medicamento;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoRepository {
    private List<Medicamento> medicamentos = new ArrayList<>();

    public void salvar(Medicamento medicamento) {
        medicamentos.add(medicamento);
    }

    public List<Medicamento> listarTodos() {
        return medicamentos;
    }

    public Medicamento buscarPorId(String id) {
        for (Medicamento m : medicamentos) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public boolean atualizar(Medicamento medicamentoAtualizado) {
        Medicamento medicamentoExistente = buscarPorId(medicamentoAtualizado.getId());
        if (medicamentoExistente != null) {
            int indice = medicamentos.indexOf(medicamentoExistente);
            medicamentos.set(indice, medicamentoAtualizado);
            return true;
        }
        return false;
    }

    public boolean deletar(String id) {
        return medicamentos.removeIf(m -> m.getId().equals(id));
    }
}