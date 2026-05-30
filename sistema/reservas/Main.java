import model.ConfirmacaoReserva;
import model.Laboratorio;
import model.Perfil;
import model.Reserva;
import service.ReservaService;
import exception.AcessoNegadoException;
import exception.ValidacaoException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Perfil perfilAtual;
    private static String nomeUsuario;

    public static void main(String[] args) {
        ReservaService reservaService = new ReservaService();
        Scanner scanner = new Scanner(System.in);

        identificarUsuario(scanner);

        boolean executando = true;
        while (executando) {
            System.out.println("=========================================");
            System.out.println("   RESERVA DE LABORATÓRIOS (KAN-02)      ");
            System.out.println("   Usuário: " + nomeUsuario + " (" + perfilAtual + ")");
            System.out.println("=========================================");
            System.out.println("1. Listar laboratórios disponíveis");
            System.out.println("2. Reservar laboratório   (somente Professor)");
            System.out.println("3. Listar reservas");
            System.out.println("4. Ver confirmações registradas (KAN-04)");
            System.out.println("5. Trocar usuário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            System.out.println();

            switch (opcao) {
                case "1":
                    listarLaboratorios(reservaService);
                    break;
                case "2":
                    reservar(reservaService, scanner);
                    break;
                case "3":
                    listarReservas(reservaService);
                    break;
                case "4":
                    listarConfirmacoes(reservaService);
                    break;
                case "5":
                    identificarUsuario(scanner);
                    break;
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!\n");
            }
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    // Simula o login do KAN-08: o perfil define o que o usuário pode fazer.
    private static void identificarUsuario(Scanner scanner) {
        System.out.println("--- IDENTIFICAÇÃO ---");
        Perfil[] perfis = Perfil.values();
        for (int i = 0; i < perfis.length; i++) {
            System.out.println((i + 1) + ". " + perfis[i]);
        }
        System.out.print("Selecione seu perfil: ");
        String entrada = scanner.nextLine().trim();

        perfilAtual = Perfil.Aluno;
        try {
            int index = Integer.parseInt(entrada) - 1;
            if (index >= 0 && index < perfis.length) {
                perfilAtual = perfis[index];
            }
        } catch (NumberFormatException ignored) {
        }

        System.out.print("Seu nome: ");
        String nome = scanner.nextLine().trim();
        nomeUsuario = nome.isEmpty() ? "(sem nome)" : nome;
        System.out.println();
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

        // Mostra as opções de laboratório antes da seleção (critério: "selecionar laboratório")
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
            ConfirmacaoReserva confirmacao = service.reservar(perfilAtual, nomeUsuario, labId, data, horaInicio, horaFim, turma);
            exibirConfirmacao(confirmacao);
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    // KAN-04: confirmação clara na tela de que a solicitação foi aceita
    private static void exibirConfirmacao(ConfirmacaoReserva confirmacao) {
        Reserva reserva = confirmacao.getReserva();
        System.out.println("\n=========================================");
        System.out.println("   RESERVA CONFIRMADA (KAN-04)           ");
        System.out.println("=========================================");
        System.out.println("Protocolo: " + confirmacao.getProtocolo());
        System.out.println("Status:    " + reserva.getStatus());
        System.out.println("Reserva:   " + reserva);
        System.out.println("Guarde o protocolo como comprovante da sua reserva.\n");
    }

    private static void listarConfirmacoes(ReservaService service) {
        System.out.println("--- CONFIRMAÇÕES REGISTRADAS (KAN-04) ---");
        List<ConfirmacaoReserva> confirmacoes = service.listarConfirmacoes();
        if (confirmacoes.isEmpty()) {
            System.out.println("Nenhuma confirmação registrada.\n");
            return;
        }
        for (ConfirmacaoReserva c : confirmacoes) {
            System.out.println(c);
        }
        System.out.println();
    }

    private static void listarReservas(ReservaService service) {
        System.out.println("--- RESERVAS ---");
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

    // --- Tratamento de erros padronizado (KAN-12) ---
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
