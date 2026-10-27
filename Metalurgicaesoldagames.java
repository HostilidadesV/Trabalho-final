
import java.util.ArrayList;
import java.util.Scanner;
 
/*
 * Sistema de gerenciamento de uma metalúrgica e oficina de soldagem
 * Trabalho de Programação Orientada a Objetos
 *
 * Classes do sistema:
 *  - Cliente
 *  - Funcionario
 *  - Servico (classe abstrata)
 *      - Soldagem   (herda de Servico)
 *      - Metalurgia (herda de Servico)
 *  - Oficina (guarda as listas e as regras do negócio)
 *  - Main (menu do programa)
 */
 
// ================= CLASSE CLIENTE =================
class Cliente {
    private String nome;
    private String telefone;
 
    public Cliente(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }
 
    public String getNome() {
        return nome;
    }
 
    public String getTelefone() {
        return telefone;
    }
 
    @Override
    public String toString() {
        return nome + " (tel: " + telefone + ")";
    }
}
 
// ================= CLASSE FUNCIONARIO =================
class Funcionario {
    private String nome;
    private String funcao; // soldador, torneiro, ajudante...
 
    public Funcionario(String nome, String funcao) {
        this.nome = nome;
        this.funcao = funcao;
    }
 
    public String getNome() {
        return nome;
    }
 
    public String getFuncao() {
        return funcao;
    }
 
    @Override
    public String toString() {
        return nome + " - " + funcao;
    }
}
 
// ================= CLASSE ABSTRATA SERVICO =================
// Todo serviço tem cliente, responsável e descrição,
// mas cada tipo de serviço calcula o valor de um jeito diferente.
abstract class Servico {
    private int codigo;
    private Cliente cliente;
    private Funcionario responsavel;
    private String descricao;
    private boolean concluido;
 
    public Servico(int codigo, Cliente cliente, Funcionario responsavel, String descricao) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.responsavel = responsavel;
        this.descricao = descricao;
        this.concluido = false; // todo serviço começa em andamento
    }
 
    // método abstrato: as subclasses são obrigadas a implementar
    public abstract double calcularValor();
 
    public void concluir() {
        this.concluido = true;
    }
 
    public int getCodigo() {
        return codigo;
    }
 
    public Cliente getCliente() {
        return cliente;
    }
 
    public Funcionario getResponsavel() {
        return responsavel;
    }
 
    public String getDescricao() {
        return descricao;
    }
 
    public boolean isConcluido() {
        return concluido;
    }
 
    @Override
    public String toString() {
        String status;
        if (concluido) {
            status = "Concluído";
        } else {
            status = "Em andamento";
        }
 
        return "Serviço " + codigo + " [" + status + "]\n"
                + "  Cliente: " + cliente.getNome() + "\n"
                + "  Responsável: " + responsavel.getNome() + "\n"
                + "  Descrição: " + descricao + "\n"
                + "  Valor: R$ " + String.format("%.2f", calcularValor());
    }
}
 
// ================= SOLDAGEM (herda de Servico) =================
class Soldagem extends Servico {
    private String tipoSolda; // MIG, TIG ou Eletrodo
    private double horas;
    private double valorHora;
 
    public Soldagem(int codigo, Cliente cliente, Funcionario responsavel,
                    String descricao, String tipoSolda, double horas) {
        super(codigo, cliente, responsavel, descricao);
        this.tipoSolda = tipoSolda;
        this.horas = horas;
 
        // o valor da hora depende do tipo de solda
        if (tipoSolda.equals("TIG")) {
            this.valorHora = 120.0;
        } else if (tipoSolda.equals("MIG")) {
            this.valorHora = 90.0;
        } else {
            this.valorHora = 70.0; // eletrodo revestido
        }
    }
 
    @Override
    public double calcularValor() {
        return horas * valorHora;
    }
 
    @Override
    public String toString() {
        return super.toString() + "\n"
                + "  Tipo: Soldagem " + tipoSolda
                + " (" + String.format("%.1f", horas) + " h a R$ "
                + String.format("%.2f", valorHora) + "/h)";
    }
}
 
// ================= METALURGIA (herda de Servico) =================
class Metalurgia extends Servico {
    private String material; // aço, alumínio, ferro...
    private double pesoKg;
    private double precoPorKg;
 
    public Metalurgia(int codigo, Cliente cliente, Funcionario responsavel,
                      String descricao, String material, double pesoKg, double precoPorKg) {
        super(codigo, cliente, responsavel, descricao);
        this.material = material;
        this.pesoKg = pesoKg;
        this.precoPorKg = precoPorKg;
    }
 
    @Override
    public double calcularValor() {
        return pesoKg * precoPorKg;
    }
 
    @Override
    public String toString() {
        return super.toString() + "\n"
                + "  Tipo: Metalurgia - " + material
                + " (" + String.format("%.2f", pesoKg) + " kg a R$ "
                + String.format("%.2f", precoPorKg) + "/kg)";
    }
}
 
// ================= CLASSE OFICINA =================
// Guarda as listas de clientes, funcionários e serviços
class Oficina {
    private String nome;
    private ArrayList<Cliente> clientes = new ArrayList<>();
    private ArrayList<Funcionario> funcionarios = new ArrayList<>();
    private ArrayList<Servico> servicos = new ArrayList<>();
    private int proximoCodigo = 1;
 
    public Oficina(String nome) {
        this.nome = nome;
    }
 
    public String getNome() {
        return nome;
    }
 
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
 
    public ArrayList<Funcionario> getFuncionarios() {
        return funcionarios;
    }
 
    public ArrayList<Servico> getServicos() {
        return servicos;
    }
 
    public void cadastrarCliente(Cliente c) {
        clientes.add(c);
    }
 
    public void cadastrarFuncionario(Funcionario f) {
        funcionarios.add(f);
    }
 
    public void adicionarServico(Servico s) {
        servicos.add(s);
    }
 
    // gera o código do próximo serviço (1, 2, 3...)
    public int gerarCodigo() {
        int codigo = proximoCodigo;
        proximoCodigo++;
        return codigo;
    }
 
    public Servico buscarServico(int codigo) {
        for (Servico s : servicos) {
            if (s.getCodigo() == codigo) {
                return s;
            }
        }
        return null; // não achou
    }
 
    // soma o valor só dos serviços que já foram concluídos
    public double calcularFaturamento() {
        double total = 0;
        for (Servico s : servicos) {
            if (s.isConcluido()) {
                total = total + s.calcularValor();
            }
        }
        return total;
    }
}
 
// ================= CLASSE PRINCIPAL =================
public class Main {
 
    static Scanner sc = new Scanner(System.in);
    static Oficina oficina = new Oficina("Metalúrgica Ferro Forte");
 
    public static void main(String[] args) {
        int opcao;
 
        do {
            mostrarMenu();
            opcao = lerInt("Escolha uma opção: ");
 
            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    cadastrarFuncionario();
                    break;
                case 3:
                    novaSoldagem();
                    break;
                case 4:
                    novaMetalurgia();
                    break;
                case 5:
                    listarClientes();
                    break;
                case 6:
                    listarFuncionarios();
                    break;
                case 7:
                    listarServicos();
                    break;
                case 8:
                    concluirServico();
                    break;
                case 9:
                    mostrarFaturamento();
                    break;
                case 0:
                    System.out.println("Encerrando o sistema. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
 
        sc.close();
    }
 
    // ---------- menu ----------
    static void mostrarMenu() {
        System.out.println();
        System.out.println("===== " + oficina.getNome() + " =====");
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Cadastrar funcionário");
        System.out.println("3 - Novo serviço de soldagem");
        System.out.println("4 - Novo serviço de metalurgia");
        System.out.println("5 - Listar clientes");
        System.out.println("6 - Listar funcionários");
        System.out.println("7 - Listar serviços");
        System.out.println("8 - Concluir serviço");
        System.out.println("9 - Ver faturamento");
        System.out.println("0 - Sair");
    }
 
    // ---------- cadastros ----------
    static void cadastrarCliente() {
        String nome = lerTexto("Nome do cliente: ");
        String telefone = lerTexto("Telefone: ");
        oficina.cadastrarCliente(new Cliente(nome, telefone));
        System.out.println("Cliente cadastrado com sucesso!");
    }
 
    static void cadastrarFuncionario() {
        String nome = lerTexto("Nome do funcionário: ");
        String funcao = lerTexto("Função (soldador, torneiro...): ");
        oficina.cadastrarFuncionario(new Funcionario(nome, funcao));
        System.out.println("Funcionário cadastrado com sucesso!");
    }
 
    // ---------- novos serviços ----------
    static void novaSoldagem() {
        Cliente cliente = escolherCliente();
        if (cliente == null) {
            return;
        }
        Funcionario func = escolherFuncionario();
        if (func == null) {
            return;
        }
 
        String descricao = lerTexto("Descrição do serviço: ");
 
        System.out.println("Tipo de solda: 1 - MIG | 2 - TIG | 3 - Eletrodo");
        int op = lerInt("Escolha o tipo: ");
        String tipo;
        if (op == 1) {
            tipo = "MIG";
        } else if (op == 2) {
            tipo = "TIG";
        } else if (op == 3) {
            tipo = "Eletrodo";
        } else {
            System.out.println("Tipo de solda inválido!");
            return;
        }
 
        double horas = lerDouble("Horas de trabalho: ");
        if (horas <= 0) {
            System.out.println("As horas precisam ser maiores que zero!");
            return;
        }
 
        // polimorfismo: a variável é do tipo Servico, mas o objeto é uma Soldagem
        Servico s = new Soldagem(oficina.gerarCodigo(), cliente, func, descricao, tipo, horas);
        oficina.adicionarServico(s);
        System.out.println("Serviço criado! Código: " + s.getCodigo());
    }
 
    static void novaMetalurgia() {
        Cliente cliente = escolherCliente();
        if (cliente == null) {
            return;
        }
        Funcionario func = escolherFuncionario();
        if (func == null) {
            return;
        }
 
        String descricao = lerTexto("Descrição do serviço (ex: fabricação de grade): ");
        String material = lerTexto("Material (aço, alumínio, ferro...): ");
        double peso = lerDouble("Peso em kg: ");
        double preco = lerDouble("Preço por kg (R$): ");
        if (peso <= 0 || preco <= 0) {
            System.out.println("Peso e preço precisam ser maiores que zero!");
            return;
        }
 
        // aqui também: variável Servico guardando um objeto Metalurgia
        Servico s = new Metalurgia(oficina.gerarCodigo(), cliente, func, descricao, material, peso, preco);
        oficina.adicionarServico(s);
        System.out.println("Serviço criado! Código: " + s.getCodigo());
    }
 
    // ---------- escolher cliente / funcionário ----------
    static Cliente escolherCliente() {
        if (oficina.getClientes().isEmpty()) {
            System.out.println("Cadastre um cliente primeiro!");
            return null;
        }
        listarClientes();
        int numero = lerInt("Número do cliente: ");
        if (numero < 1 || numero > oficina.getClientes().size()) {
            System.out.println("Cliente inválido!");
            return null;
        }
        return oficina.getClientes().get(numero - 1);
    }
 
    static Funcionario escolherFuncionario() {
        if (oficina.getFuncionarios().isEmpty()) {
            System.out.println("Cadastre um funcionário primeiro!");
            return null;
        }
        listarFuncionarios();
        int numero = lerInt("Número do funcionário responsável: ");
        if (numero < 1 || numero > oficina.getFuncionarios().size()) {
            System.out.println("Funcionário inválido!");
            return null;
        }
        return oficina.getFuncionarios().get(numero - 1);
    }
 
    // ---------- listagens ----------
    static void listarClientes() {
        ArrayList<Cliente> lista = oficina.getClientes();
        if (lista.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println((i + 1) + " - " + lista.get(i));
            }
        }
    }
 
    static void listarFuncionarios() {
        ArrayList<Funcionario> lista = oficina.getFuncionarios();
        if (lista.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println((i + 1) + " - " + lista.get(i));
            }
        }
    }
 
    static void listarServicos() {
        ArrayList<Servico> lista = oficina.getServicos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum serviço cadastrado.");
        } else {
            for (Servico s : lista) {
                System.out.println(s); // chama o toString de Soldagem ou de Metalurgia
                System.out.println("--------------------------");
            }
        }
    }
 
    // ---------- concluir serviço e faturamento ----------
    static void concluirServico() {
        if (oficina.getServicos().isEmpty()) {
            System.out.println("Nenhum serviço cadastrado.");
            return;
        }
        listarServicos();
        int codigo = lerInt("Código do serviço que foi concluído: ");
        Servico s = oficina.buscarServico(codigo);
 
        if (s == null) {
            System.out.println("Serviço não encontrado!");
        } else if (s.isConcluido()) {
            System.out.println("Esse serviço já estava concluído.");
        } else {
            s.concluir();
            System.out.println("Serviço " + codigo + " concluído!");
        }
    }
 
    static void mostrarFaturamento() {
        double total = oficina.calcularFaturamento();
        System.out.println("Faturamento (serviços concluídos): R$ " + String.format("%.2f", total));
    }
 
    // ---------- métodos para ler os dados do teclado ----------
    // Uso sempre nextLine() para não dar problema de "pular" a leitura
    static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine();
    }
 
    static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido!");
            }
        }
    }
 
    static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                // troca a vírgula por ponto para aceitar "5,5"
                String texto = sc.nextLine().trim().replace(",", ".");
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            }
        }
    }
}
 