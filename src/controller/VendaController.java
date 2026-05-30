package controller;

import model.Cliente;
import model.Funcionario;
import model.Medicamento;
import repository.MedicamentoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaController {
    // O controller precisa do repositório de medicamentos para checar e atualizar o estoque
    private MedicamentoRepository medicamentoRepository;

    // Lista interna para armazenar o histórico de todas as vendas realizadas
    private List<VendaSimples> historicoVendas = new ArrayList<>();

    public VendaController(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    // Lógica de realizar a venda
    public boolean realizarVenda(Medicamento medicamento, int quantidade, Cliente cliente, Funcionario funcionario) {
        // Regra de Negócio 1: Verificar se tem estoque suficiente
        if (medicamento.getQuantidadeEstoque() < quantidade) {
            return false; // Venda cancelada por falta de estoque
        }

        // Regra de Negócio 2: Verificar se o medicamento está vencido antes de vender
        if (medicamento.getDataValidade().isBefore(LocalDate.now())) {
            return false; // Venda cancelada: produto vencido!
        }

        // Se passou nas validações, atualiza o estoque (baixa no estoque)
        int novoEstoque = medicamento.getQuantidadeEstoque() - quantidade;
        medicamento.setQuantidadeEstoque(novoEstoque);
        medicamentoRepository.atualizar(medicamento);

        // Registra a venda no histórico
        double valorTotal = medicamento.getPrecoVenda() * quantidade;
        VendaSimples novaVenda = new VendaSimples(LocalDateTime.now(), medicamento, quantidade, cliente, funcionario, valorTotal);
        historicoVendas.add(novaVenda);

        // Emissão do Comprovante (Saída no console)
        emitirComprovante(novaVenda);

        return true;
    }

    private void emitirComprovante(VendaSimples venda) {
        System.out.println("\n=== COMPROVANTE DE VENDA ===");
        System.out.println("Data/Hora: " + venda.dataHora);
        System.out.println("Atendente: " + venda.funcionario.getNome());
        System.out.println("Cliente: " + (venda.cliente != null ? venda.cliente.getNome() : "Não Identificado"));
        System.out.println("----------------------------");
        System.out.println("Medicamento: " + venda.medicamento.getNome());
        System.out.println("Qtd: " + venda.quantidade + " x R$ " + venda.medicamento.getPrecoVenda());
        System.out.println("TOTAL: R$ " + venda.valorTotal);
        System.out.println("============================\n");
    }

    // ====== SEÇÃO DE RELATÓRIOS ======

    // Relatório 1: Histórico de Vendas
    public void relatorioVendas() {
        System.out.println("\n--- RELATÓRIO DE VENDAS ---");
        for (VendaSimples v : historicoVendas) {
            System.out.println(v.dataHora + " - " + v.medicamento.getNome() + " | Qtd: " + v.quantidade + " | Total: R$ " + v.valorTotal);
        }
    }

    // Relatório 2: Produtos Vencidos
    public void relatorioProdutosVencidos() {
        System.out.println("\n--- PRODUTOS VENCIDOS ---");
        for (Medicamento m : medicamentoRepository.listarTodos()) {
            if (m.getDataValidade().isBefore(LocalDate.now())) {
                System.out.println("ID: " + m.getId() + " | " + m.getNome() + " | Venceu em: " + m.getDataValidade());
            }
        }
    }

    // Relatório 3: Lucro Mensal (Baseado nas vendas efetuadas)
    public void relatorioLucroMensal() {
        double receitaTotal = 0;
        double custoTotal = 0;

        for (VendaSimples v : historicoVendas) {
            // Só calcula se for do mês e ano atual
            if (v.dataHora.getMonth() == LocalDateTime.now().getMonth() &&
                    v.dataHora.getYear() == LocalDateTime.now().getYear()) {

                receitaTotal += v.valorTotal;
                custoTotal += v.medicamento.getPrecoCusto() * v.quantidade;
            }
        }

        double lucro = receitaTotal - custoTotal;
        System.out.println("\n--- FECHAMENTO DO MÊS ATUAL ---");
        System.out.println("Faturamento Bruto: R$ " + receitaTotal);
        System.out.println("Custo das Mercadorias: R$ " + custoTotal);
        System.out.println("Lucro Líquido: R$ " + lucro);
    }

    // Classe interna auxiliar para estruturar os dados da venda sem complicar com muitos arquivos
    private static class VendaSimples {
        LocalDateTime dataHora;
        Medicamento medicamento;
        int quantidade;
        Cliente cliente;
        Funcionario funcionario;
        double valorTotal;

        public VendaSimples(LocalDateTime dataHora, Medicamento medicamento, int quantidade, Cliente cliente, Funcionario funcionario, double valorTotal) {
            this.dataHora = dataHora;
            this.medicamento = medicamento;
            this.quantidade = quantidade;
            this.quantidade = quantidade;
            this.cliente = cliente;
            this.funcionario = funcionario;
            this.valorTotal = valorTotal;
        }
    }
}