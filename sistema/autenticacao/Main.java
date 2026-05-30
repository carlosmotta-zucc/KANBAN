import model.Usuario;
import model.TipoUsuario;
import service.AutenticacaoService;
import exception.ValidacaoException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AutenticacaoService authService = new AutenticacaoService();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
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
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Escolha 1, 2 ou 3.\n");
            }
        }

        scanner.close();
        System.out.println("Sistema encerrado.");
    }

    private static void executarLogin(AutenticacaoService authService, Scanner scanner) {
        System.out.println("--- TELA DE LOGIN ---");
        System.out.print("Digite seu e-mail: ");
        String email = scanner.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = scanner.nextLine();

        try {
            Usuario usuario = authService.login(email, senha);
            System.out.println("\nLogin efetuado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
            System.out.println("Perfil: " + usuario.getTipo() + "\n");
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
}