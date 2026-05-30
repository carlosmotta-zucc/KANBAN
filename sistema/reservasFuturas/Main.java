import model.Laboratorio;
import model.Reserva;
import model.Usuario;
import model.TipoUsuario;
import service.ReservaService;
import service.AutenticacaoService;
import exception.AcessoNegadoException;
import exception.ValidacaoException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Usuario usuarioLogado;

    public static void main(String[] args) {
        AutenticacaoService authService = new AutenticacaoService();
        ReservaService reservaService = new ReservaService();
        Scanner scanner = new Scanner(System.in);

        boolean executando = true;
        while (executando) {
            if (usuarioLogado == null) {
                executando = menuAutenticacao(authService, scanner);
            } else {
                executando = menuReservas(reservaService, scanner);
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
            usuarioLogado = authService.login(email, senha);
            System.out.println("\nLogin efetuado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuarioLogado.getNome() + "!");
            System.out.println("Perfil: " + usuarioLogado.getTipo() + "\n");
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

    private static boolean menuReservas(ReservaService service, Scanner scanner) {
        System.out.println("=========================================");
        System.out.println("   RESERVA DE LABORATÓRIOS (KAN-09)      ");
        System.out.println("   Usuário: " + usuarioLogado.getNome() + " (" + usuarioLogado.getTipo() + ")");
        System.out.println("=========================================");
        System.out.println("1. Listar laboratórios disponíveis");
        System.out.println("2. Reservar laboratório (somente Professor)");
        System.out.println("3. Listar todas as reservas");
        System.out.println("4. Listar minhas reservas futuras");
        System.out.println("5. Logout");
        System.out.println("0. Sair do sistema");
        System.out.print("Escolha uma opção: ");

        String opcao = scanner.nextLine().trim();
        System.out.println();

        switch (opcao) {
            case "1":
                listarLaboratorios(service);
                break;
            case "2":
                reservar(service, scanner);
                break;
            case "3":
                listarReservas(service);
                break;
            case "4":
                listarReservasFuturas(service);
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

    private static void listarLaboratorios(ReservaService service) {
        System.out.println("--- LABORATÓRIOS DISPONÍVEIS ---");
        List<Laboratorio> labs = service.listarLaboratoriosAtivos();
        if (labs.isEmpty()) {
            System.out.println("Nenhum laboratório disponível.\n");
            return;
        }
        for (Laboratorio l : labs) {
            System.out.println(l);
        }
        System.out.println();
    }

    private static void reservar(ReservaService service, Scanner scanner) {
        System.out.println("--- RESERVAR LABORATÓRIO ---");

        List<Laboratorio> labs = service.listarLaboratoriosAtivos();
        for (Laboratorio l : labs) {
            System.out.println(l);
        }

        System.out.print("ID do laboratório: ");
        String labId = scanner.nextLine();
        System.out.print("Data (dd/MM/aaaa): ");
        String data = scanner.nextLine();
        System.out.print("Hora de início (HH:mm): ");
        String horaInicio = scanner.nextLine();
        System.out.print("Hora de término (HH:mm): ");
        String horaFim = scanner.nextLine();
        System.out.print("Turma: ");
        String turma = scanner.nextLine();

        try {
            Reserva reserva = service.reservar(usuarioLogado.getTipo(), usuarioLogado.getNome(), labId, data, horaInicio, horaFim, turma);
            System.out.println("\nReserva realizada com sucesso!");
            System.out.println(reserva + "\n");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    private static void listarReservas(ReservaService service) {
        System.out.println("--- TODAS AS RESERVAS ---");
        List<Reserva> reservas = service.listarReservas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva registrada.\n");
            return;
        }
        for (Reserva r : reservas) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void listarReservasFuturas(ReservaService service) {
        System.out.println("--- MINHAS RESERVAS FUTURAS ---");
        List<Reserva> reservasFuturas = service.listarReservasFuturas(usuarioLogado.getNome());
        if (reservasFuturas.isEmpty()) {
            System.out.println("Você não possui reservas futuras registradas.\n");
            return;
        }
        for (Reserva r : reservasFuturas) {
            System.out.println(r);
        }
        System.out.println();
    }

    private static void tratarErro(Exception e) {
        if (e instanceof AcessoNegadoException) {
            System.out.println("\nAcesso negado!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } else if (e instanceof ValidacaoException) {
            ValidacaoException ve = (ValidacaoException) e;
            System.out.println("\nErro de validação (KAN-12)!");
            System.out.println("Mensagem: " + ve.getMessage());
            System.out.println("Campo com problema: " + ve.getCampoComProblema() + "\n");
        } else {
            System.out.println("\nErro inesperado: " + e.getMessage() + "\n");
        }
    }
}
