import model.Usuario;
import service.AutenticacaoService;
import exception.ValidacaoException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AutenticacaoService authService = new AutenticacaoService();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("  SISTEMA DE AGENDAMENTO - TELA DE LOGIN  ");
        System.out.println("=========================================");

        boolean logado = false;

        while (!logado) {
            System.out.print("Digite seu e-mail (ou 'sair' para encerrar): ");
            String email = scanner.nextLine();

            if (email.equalsIgnoreCase("sair")) {
                break;
            }

            System.out.print("Digite sua senha: ");
            String senha = scanner.nextLine();

            try {
                Usuario usuario = authService.login(email, senha);
                System.out.println("Login efetuado com sucesso!");
                System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
                System.out.println("Perfil: " + usuario.getTipo());
                logado = true; // Encerra o loop após o login com sucesso
            } catch (ValidacaoException e) {
                System.out.println("Erro de valida\u00E7\u00E3o (KAN-12)!");
                System.out.println("Mensagem: " + e.getMessage());
                System.out.println("Campo com problema: " + e.getCampoComProblema() + "\n");
            } catch (IllegalArgumentException e) {
                System.out.println("Falha na autentica\u00E7\u00E3o!");
                System.out.println("Mensagem: " + e.getMessage() + "\n");
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage() + "\n");
            }
        }

        scanner.close();
        System.out.println("\nSistema encerrado.");
    }
}