import painel.model.Ambiente;
import painel.model.Reserva;
import model.Usuario;
import model.TipoUsuario;
import painel.service.OcupacaoService;
import service.AutenticacaoService;
import painel.exception.AcessoNegadoException;
import painel.exception.ValidacaoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Usuario usuarioLogado;
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        AutenticacaoService authService = new AutenticacaoService();
        OcupacaoService ocupacaoService = new OcupacaoService();
        Scanner scanner = new Scanner(System.in);

        boolean executando = true;
        while (executando) {
            if (usuarioLogado == null) {
                executando = menuAutenticacao(authService, scanner);
            } else {
                executando = menuPainel(ocupacaoService, scanner);
            }
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static boolean menuAutenticacao(AutenticacaoService authService, Scanner scanner) {
        System.out.println("=========================================");
        System.out.println("          SISTEMA DE AGENDAMENTO         ");
        System.out.println("=========================================");
        System.out.println("1. Login");
        System.out.println("2. Cadastrar Novo Usuário");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine().trim();
        System.out.println();

        switch (opcao) {
            case "1":
                executarLogin(authService, scanner);
                break;
            case "2":
                executarCadastro(authService, scanner);
                break;
            case "3":
                return false;
            default:
                System.out.println("Opção inválida! Escolha 1, 2 ou 3.\n");
        }
        return true;
    }

    private static void executarLogin(AutenticacaoService authService, Scanner scanner) {
        System.out.println("--- TELA DE LOGIN ---");
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        try {
            Usuario usuario = authService.login(email, senha);

            // Valida se o perfil tem acesso ao painel de ocupação (KAN-11)
            if (usuario.getTipo() != TipoUsuario.Gestor && usuario.getTipo() != TipoUsuario.Administrador) {
                throw new AcessoNegadoException("Apenas gestores ou administradores podem acessar o painel de ocupação");
            }

            usuarioLogado = usuario;
            System.out.println("\nLogin efetuado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
            System.out.println("Perfil: " + usuarioLogado.getTipo() + "\n");
        } catch (AcessoNegadoException e) {
            System.out.println("\nAcesso negado!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } catch (ValidacaoException e) {
            System.out.println("\nErro de validação (KAN-12)!");
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("Campo com problema: " + e.getCampoComProblema() + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nFalha na autenticação!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\nErro inesperado: " + e.getMessage() + "\n");
        }
    }

    private static void executarCadastro(AutenticacaoService authService, Scanner scanner) {
        System.out.println("--- CADASTRO DE NOVO USUÁRIO ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.println("Escolha o tipo de usuário:");
        TipoUsuario[] tipos = TipoUsuario.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i]);
        }
        System.out.print("Opção: ");
        String opcaoTipo = scanner.nextLine().trim();

        TipoUsuario tipoSelecionado = null;
        try {
            int index = Integer.parseInt(opcaoTipo) - 1;
            if (index >= 0 && index < tipos.length) {
                tipoSelecionado = tipos[index];
            }
        } catch (NumberFormatException ignored) {}

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        try {
            Usuario novoUsuario = authService.cadastrar(nome, email, tipoSelecionado, senha);
            System.out.println("\nUsuário cadastrado com sucesso!");
            System.out.println("ID gerado: " + novoUsuario.getId());
            System.out.println("Nome: " + novoUsuario.getNome());
            System.out.println("Perfil: " + novoUsuario.getTipo() + "\n");
        } catch (ValidacaoException e) {
            System.out.println("\nErro de validação no cadastro (KAN-12)!");
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("Campo com problema: " + e.getCampoComProblema() + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\nFalha ao realizar cadastro!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("\nErro inesperado: " + e.getMessage() + "\n");
        }
    }

    private static boolean menuPainel(OcupacaoService service, Scanner scanner) {
        System.out.println("=========================================");
        System.out.println("     PAINEL DE OCUPAÇÃO (KAN-11)         ");
        System.out.println("     Usuário: " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipo() + ")");
        System.out.println("=========================================");
        System.out.println("1. Taxa de ocupação consolidada por ambiente");
        System.out.println("2. Taxa de ocupação detalhada por data");
        System.out.println("3. Listar ambientes");
        System.out.println("4. Listar todas as reservas");
        System.out.println("5. Logout");
        System.out.println("0. Sair do sistema");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine().trim();
        System.out.println();

        switch (opcao) {
            case "1":
                exibirOcupacaoConsolidada(service);
                break;
            case "2":
                exibirOcupacaoPorData(service, scanner);
                break;
            case "3":
                listarAmbientes(service);
                break;
            case "4":
                listarReservas(service);
                break;
            case "5":
                usuarioLogado = null;
                System.out.println("Logout efetuado com sucesso.\n");
                break;
            case "0":
                return false;
            default:
                System.out.println("Opção inválida!\n");
        }
        return true;
    }

    private static void exibirOcupacaoConsolidada(OcupacaoService service) {
        System.out.println("--- TAXA DE OCUPAÇÃO CONSOLIDADA ---");
        List<Ambiente> ambientes = service.listarAmbientesAtivos();
        if (ambientes.isEmpty()) {
            System.out.println("Nenhum ambiente ativo cadastrado.\n");
            return;
        }

        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-25s | %-12s | %-10s | %-15s\n", "ID", "Ambiente", "Tipo", "Reservas", "Horas Ocupadas");
        System.out.println("----------------------------------------------------------------------------------");
        for (Ambiente a : ambientes) {
            int reservas = service.obterTotalReservas(a);
            double horas = service.obterTotalHoras(a);
            System.out.printf("%-5s | %-25s | %-12s | %-10d | %-15.1f hrs\n",
                    a.getId(), a.getNome(), a.getTipo(), reservas, horas);
        }
        System.out.println("----------------------------------------------------------------------------------\n");
    }

    private static void exibirOcupacaoPorData(OcupacaoService service, Scanner scanner) {
        System.out.println("--- TAXA DE OCUPAÇÃO POR DATA ---");
        System.out.print("Digite a data para análise (dd/MM/aaaa): ");
        String dataStr = scanner.nextLine().trim();

        LocalDate data;
        try {
            if (dataStr.isEmpty()) {
                throw new ValidacaoException("A data é obrigatória", "data");
            }
            data = LocalDate.parse(dataStr, FMT_DATA);
        } catch (DateTimeParseException e) {
            System.out.println("\nErro de validação (KAN-12)!");
            System.out.println("Mensagem: Data inválida. Use o formato dd/MM/aaaa");
            System.out.println("Campo com problema: data\n");
            return;
        } catch (ValidacaoException e) {
            System.out.println("\nErro de validação (KAN-12)!");
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("Campo com problema: " + e.getCampoComProblema() + "\n");
            return;
        }

        List<Ambiente> ambientes = service.listarAmbientesAtivos();
        if (ambientes.isEmpty()) {
            System.out.println("Nenhum ambiente ativo cadastrado.\n");
            return;
        }

        System.out.println("\n----------------------------------------------------------------------");
        System.out.printf("%-5s | %-25s | %-12s | %-20s\n", "ID", "Ambiente", "Tipo", "Taxa Ocupação (%)");
        System.out.println("----------------------------------------------------------------------");
        for (Ambiente a : ambientes) {
            double taxa = service.calcularOcupacaoDiaria(a, data);
            System.out.printf("%-5s | %-25s | %-12s | %-20.2f%%\n",
                    a.getId(), a.getNome(), a.getTipo(), taxa);
        }
        System.out.println("----------------------------------------------------------------------\n");
    }

    private static void listarAmbientes(OcupacaoService service) {
        System.out.println("--- LISTA DE AMBIENTES ATIVOS ---");
        List<Ambiente> ambientes = service.listarAmbientesAtivos();
        if (ambientes.isEmpty()) {
            System.out.println("Nenhum ambiente ativo encontrado.\n");
            return;
        }
        for (Ambiente a : ambientes) {
            System.out.println(a);
        }
        System.out.println();
    }

    private static void listarReservas(OcupacaoService service) {
        System.out.println("--- LISTA DE RESERVAS ---");
        List<Reserva> reservas = service.listarReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva registrada no painel.\n");
            return;
        }
        for (Reserva r : reservas) {
            System.out.println(r);
        }
        System.out.println();
    }
}
