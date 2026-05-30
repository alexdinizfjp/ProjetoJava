import controller.AutenticadorController;
import controller.VendaController;
import model.NivelAcesso;
import repository.ClienteRepository;
import repository.FuncionarioRepository;
import repository.MedicamentoRepository;

import java.util.Scanner;

public class Main {
    private static FuncionarioRepository funcionarioRepository = new FuncionarioRepository();
    private static MedicamentoRepository medicamentoRepository = new MedicamentoRepository();
    private static ClienteRepository clienteRepository = new ClienteRepository();

    private static AutenticadorController autenticador = new AutenticadorController(funcionarioRepository);
    private static VendaController vendaController = new VendaController(medicamentoRepository);

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("    BEM-VINDO AO SISTEMA FARMA+ v1.0     ");
        System.out.println("=========================================");

        boolean executando = true;
        while (executando) {
            // Se não houver ninguém logado, força a tela de login
            if (autenticador.getFuncionarioLogado() == null) {
                telaLogin();
            } else {
                exibirMenuPrincipal();
            }
        }
    }

    private static void telaLogin() {
        System.out.println("\n--- TELA DE ACESSO ---");
        System.out.print("Digite a Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Digite a Senha: ");
        String senha = scanner.nextLine();

        if (autenticador.fazerLogin(matricula, senha)) {
            System.out.println("\n✅ Login realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + autenticador.getFuncionarioLogado().getNome() + "!");
        } else {
            System.out.println("\n❌ Matrícula ou senha incorretas. Tente novamente.");
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n================ MENU PRINCIPAL ================");
        System.out.println("1. Vendas (Efetuar Venda)");
        System.out.println("2. CRUD Medicamentos");
        System.out.println("3. CRUD Clientes");
        System.out.println("4. CRUD Fornecedores");

        // Menu dinâmico: Só mostra opções restritas para quem for ADMIN
        if (autenticador.ehAdmin()) {
            System.out.println("5. CRUD Funcionários (Restrito)");
            System.out.println("6. Relatórios Gerenciais (Restrito)");
        }

        System.out.println("0. Sair / Logout");
        System.out.println("================================================");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                efetuarVenda();
                break;
            case "2":
                menuMedicamentos();
                break;
            case "3":
                menuClientes();
                break;
            case "4":
                menuFornecedores();
                break;
            case "5":
                if (autenticador.ehAdmin()) {
                    menuFuncionarios();
                } else {
                    System.out.println("❌ Opção inválida!");
                }
                break;
            case "6":
                if (autenticador.ehAdmin()) {
                    menuRelatorios();
                } else {
                    System.out.println("❌ Opção inválida!");
                }
                break;
            case "0":
                autenticador.fazerLogout();
                System.out.println("👋 Logout efetuado. Até logo!");
                break;
            default:
                System.out.println("❌ Opção inválida, tente novamente.");
        }
    }

    private static void efetuarVenda() {
        System.out.println("\n--- EFETUAR VENDA ---");
        System.out.print("Digite o ID do Medicamento: ");
        String idMedicamento = scanner.nextLine();

        model.Medicamento med = medicamentoRepository.buscarPorId(idMedicamento);
        if (med == null) {
            System.out.println("❌ Medicamento não encontrado no sistema!");
            return;
        }

        System.out.println("👉 Produto Selecionado: " + med.getNome() + " | Estoque: " + med.getQuantidadeEstoque() + " | Preço: R$" + med.getPrecoVenda());
        System.out.print("Digite a Quantidade: ");
        int quantidade = Integer.parseInt(scanner.nextLine());

        System.out.print("ID do Cliente (Pressione ENTER para não identificar): ");
        String idCliente = scanner.nextLine();
        model.Cliente cliente = null;
        if (!idCliente.isEmpty()) {
            cliente = clienteRepository.buscarPorId(idCliente);
            if (cliente == null) {
                System.out.println("⚠️ Cliente não cadastrado. Prosseguindo como 'Não Identificado'.");
            }
        }

        model.Funcionario funcionarioLogado = autenticador.getFuncionarioLogado();
        boolean vendaSucesso = vendaController.realizarVenda(med, quantidade, cliente, funcionarioLogado);

        if (vendaSucesso) {
            System.out.println("✅ Transação concluída com sucesso!");
        } else {
            System.out.println("❌ Falha na venda: Verifique o estoque ou a validade do produto.");
        }
    }

    private static void menuMedicamentos() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n--- GERENCIAMENTO DE MEDICAMENTOS ---");
            System.out.println("1. Cadastrar Novo Medicamento");
            System.out.println("2. Listar Todos os Medicamentos");
            System.out.println("3. Buscar Medicamento por ID");
            System.out.println("4. Excluir Medicamento");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    cadastrarMedicamento();
                    break;
                case "2":
                    listarMedicamentos();
                    break;
                case "3":
                    buscarMedicamento();
                    break;
                case "4":
                    excluirMedicamento();
                    break;
                case "0":
                    noMenu = false;
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
    }

    private static void cadastrarMedicamento() {
        System.out.println("\n--- CADASTRAR MEDICAMENTO ---");
        System.out.print("ID do Medicamento: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Princípio Ativo: ");
        String principio = scanner.nextLine();
        System.out.print("Preço de Custo (Ex: 10.50): ");
        double custo = Double.parseDouble(scanner.nextLine());
        System.out.print("Preço de Venda (Ex: 25.00): ");
        double venda = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantidade Inicial em Estoque: ");
        int estoque = Integer.parseInt(scanner.nextLine());
        System.out.print("Data de Validade (Ano-Mês-Dia, Ex: 2027-12-31): ");
        String dataStr = scanner.nextLine();
        java.time.LocalDate validade = java.time.LocalDate.parse(dataStr);

        model.Medicamento novo = new model.Medicamento(id, nome, principio, custo, venda, estoque, validade);
        medicamentoRepository.salvar(novo);
        System.out.println("✅ Medicamento cadastrado com sucesso!");
    }

    private static void listarMedicamentos() {
        System.out.println("\n--- LISTA DE MEDICAMENTOS ---");
        java.util.List<model.Medicamento> lista = medicamentoRepository.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("Nenhum medicamento cadastrado.");
            return;
        }

        for (model.Medicamento m : lista) {
            System.out.println("ID: " + m.getId() + " | Nome: " + m.getNome() + " | Preço: R$" + m.getPrecoVenda() + " | Estoque: " + m.getQuantidadeEstoque());
        }
    }

    private static void buscarMedicamento() {
        System.out.println("\n--- BUSCAR MEDICAMENTO ---");
        System.out.print("Digite o ID do medicamento: ");
        String id = scanner.nextLine();

        model.Medicamento m = medicamentoRepository.buscarPorId(id);
        if (m != null) {
            System.out.println("👉 Encontrado: " + m.getNome() + " - R$" + m.getPrecoVenda());
        } else {
            System.out.println("❌ Medicamento não encontrado.");
        }
    }

    private static void excluirMedicamento() {
        System.out.println("\n--- EXCLUIR MEDICAMENTO ---");
        System.out.print("Digite o ID do medicamento: ");
        String id = scanner.nextLine();

        if (medicamentoRepository.deletar(id)) {
            System.out.println("✅ Medicamento removido com sucesso!");
        } else {
            System.out.println("❌ ID não encontrado.");
        }
    }

    private static void menuClientes() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n--- GERENCIAMENTO DE CLIENTES ---");
            System.out.println("1. Cadastrar Novo Cliente");
            System.out.println("2. Listar Todos os Clientes");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.println("\n--- CADASTRAR CLIENTE ---");
                    System.out.print("ID/Código: ");
                    String id = scanner.nextLine();
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Telefone: ");
                    String tel = scanner.nextLine();

                    model.Cliente novo = new model.Cliente(nome, cpf, tel, id);
                    clienteRepository.salvar(novo);
                    System.out.println("✅ Cliente cadastrado com sucesso!");
                    break;
                case "2":
                    System.out.println("\n--- LISTA DE CLIENTES ---");
                    for (model.Cliente c : clienteRepository.listarTodos()) {
                        System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpf());
                    }
                    break;
                case "0":
                    noMenu = false;
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
    }

    private static void menuFornecedores() {
        System.out.println("\n--- GERENCIAMENTO DE FORNECEDORES ---");
        System.out.println("⚠️ Funcionalidade de consulta rápida (Simulação)");
        System.out.println("1. Listar Fornecedores Homologados");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        String opcao = scanner.nextLine();
        if (opcao.equals("1")) {
            System.out.println("\n--- FORNECEDORES HOMOLOGADOS ---");
            System.out.println("CNPJ: 11.222.333/0001-44 | Distribuidora Galênica de Medicamentos LTDA");
            System.out.println("CNPJ: 55.666.777/0001-88 | MedFarma Atacado e Logística S/A");
        }
    }

    private static void menuFuncionarios() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n--- GERENCIAMENTO DE FUNCIONÁRIOS (RESTRITO) ---");
            System.out.println("1. Cadastrar Novo Funcionário");
            System.out.println("2. Listar Quadro de Funcionários");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.println("\n--- CADASTRAR FUNCIONÁRIO ---");
                    System.out.print("Nome Completo: ");
                    String nome = scanner.nextLine();
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    System.out.print("Telefone: ");
                    String tel = scanner.nextLine();
                    System.out.print("Matrícula (Login): ");
                    String mat = scanner.nextLine();
                    System.out.print("Senha de Acesso: ");
                    String senha = scanner.nextLine();

                    System.out.println("Nível de Acesso (1 - Administrador | 2 - Atendente): ");
                    String nivelOp = scanner.nextLine();
                    model.NivelAcesso nivel = nivelOp.equals("1") ? model.NivelAcesso.ADMINISTRADOR : model.NivelAcesso.ADMINISTRADOR.ATENDENTE;

                    // Se o seu Enum for chamado direto, use: model.NivelAcesso.ADMINISTRADOR ou model.NivelAcesso.ATENDENTE
                    model.NivelAcesso nivelFinal = nivelOp.equals("1") ? NivelAcesso.ADMINISTRADOR : NivelAcesso.ATENDENTE;

                    model.Funcionario novoFunc = new model.Funcionario(nome, cpf, tel, mat, senha, nivelFinal);
                    funcionarioRepository.salvar(novoFunc);
                    System.out.println("✅ Funcionário cadastrado com sucesso!");
                    break;

                case "2":
                    System.out.println("\n--- QUADRO DE FUNCIONÁRIOS ---");
                    for (model.Funcionario f : funcionarioRepository.listarTodos()) {
                        System.out.println("Matrícula: " + f.getMatricula() + " | Nome: " + f.getNome() + " | Cargo: " + f.getNivelAcesso());
                    }
                    break;
                case "0":
                    noMenu = false;
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
    }

    private static void menuRelatorios() {
        System.out.println("\n--- RELATÓRIOS GERENCIAIS ---");
        System.out.println("1. Relatório de Estoque Atual");
        System.out.println("2. Histórico de Vendas");
        System.out.println("3. Produtos Vencidos");
        System.out.println("4. Lucro Mensal");
        System.out.print("Escolha o relatório: ");
        String op = scanner.nextLine();

        switch (op) {
            case "1":
                System.out.println("\n--- ESTOQUE ATUAL ---");
                listarMedicamentos();
                break;
            case "2":
                vendaController.relatorioVendas();
                break;
            case "3":
                vendaController.relatorioProdutosVencidos();
                break;
            case "4":
                vendaController.relatorioLucroMensal();
                break;
            default:
                System.out.println("Voltando...");
        }
    }
}