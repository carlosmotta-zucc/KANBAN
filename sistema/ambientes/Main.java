import model.Ambiente;
import model.Perfil;
import model.TipoAmbiente;
import service.AmbienteService;
import exception.AcessoNegadoException;
import exception.ValidacaoException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AmbienteService ambienteService = new AmbienteService();
        Scanner scanner = new Scanner(System.in);

        // Perfil de quem está operando o sistema.
        // Troque pelo menu para testar o controle de acesso (KAN-03).
        Perfil perfilAtual = selecionarPerfil(scanner);

        boolean executando = true;
        while (executando) {
            System.out.println("=========================================");
            System.out.println("   CADASTRO DE SALAS E LABORATÓRIOS      ");
            System.out.println("   Perfil atual: " + perfilAtual);
            System.out.println("=========================================");
            System.out.println("1. Listar ambientes");
            System.out.println("2. Cadastrar ambiente   (somente Administrador)");
            System.out.println("3. Editar ambiente      (somente Administrador)");
            System.out.println("4. Desativar ambiente   (somente Administrador)");
            System.out.println("5. Reativar ambiente    (somente Administrador)");
            System.out.println("6. Trocar perfil");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            System.out.println();

            switch (opcao) {
                case "1":
                    listar(ambienteService);
                    break;
                case "2":
                    cadastrar(ambienteService, scanner, perfilAtual);
                    break;
                case "3":
                    editar(ambienteService, scanner, perfilAtual);
                    break;
                case "4":
                    desativar(ambienteService, scanner, perfilAtual);
                    break;
                case "5":
                    reativar(ambienteService, scanner, perfilAtual);
                    break;
                case "6":
                    perfilAtual = selecionarPerfil(scanner);
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

    private static Perfil selecionarPerfil(Scanner scanner) {
        System.out.println("--- SELECIONE O PERFIL DE ACESSO ---");
        Perfil[] perfis = Perfil.values();
        for (int i = 0; i < perfis.length; i++) {
            System.out.println((i + 1) + ". " + perfis[i]);
        }
        System.out.print("Opção: ");
        String entrada = scanner.nextLine().trim();
        System.out.println();
        try {
            int index = Integer.parseInt(entrada) - 1;
            if (index >= 0 && index < perfis.length) {
                return perfis[index];
            }
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Perfil inválido, assumindo 'Aluno' por padrão.\n");
        return Perfil.Aluno;
    }

    private static void listar(AmbienteService service) {
        System.out.println("--- AMBIENTES CADASTRADOS ---");
        List<Ambiente> ambientes = service.listarTodos();
        if (ambientes.isEmpty()) {
            System.out.println("Nenhum ambiente cadastrado.\n");
            return;
        }
        for (Ambiente a : ambientes) {
            System.out.println(a);
        }
        System.out.println();
    }

    private static void cadastrar(AmbienteService service, Scanner scanner, Perfil perfil) {
        System.out.println("--- CADASTRAR AMBIENTE ---");
        String nome = lerTexto(scanner, "Nome: ");
        TipoAmbiente tipo = lerTipo(scanner);
        int capacidade = lerInteiro(scanner, "Capacidade: ");
        String localizacao = lerTexto(scanner, "Localização (bloco/andar): ");

        try {
            Ambiente novo = service.cadastrar(perfil, nome, tipo, capacidade, localizacao);
            System.out.println("\nAmbiente cadastrado com sucesso!");
            System.out.println(novo + "\n");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    private static void editar(AmbienteService service, Scanner scanner, Perfil perfil) {
        System.out.println("--- EDITAR AMBIENTE ---");
        String id = lerTexto(scanner, "ID do ambiente: ");
        String nome = lerTexto(scanner, "Novo nome: ");
        TipoAmbiente tipo = lerTipo(scanner);
        int capacidade = lerInteiro(scanner, "Nova capacidade: ");
        String localizacao = lerTexto(scanner, "Nova localização: ");

        try {
            Ambiente editado = service.editar(perfil, id, nome, tipo, capacidade, localizacao);
            System.out.println("\nAmbiente atualizado com sucesso!");
            System.out.println(editado + "\n");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    private static void desativar(AmbienteService service, Scanner scanner, Perfil perfil) {
        System.out.println("--- DESATIVAR AMBIENTE ---");
        String id = lerTexto(scanner, "ID do ambiente: ");
        try {
            Ambiente desativado = service.desativar(perfil, id);
            System.out.println("\nAmbiente desativado com sucesso!");
            System.out.println(desativado + "\n");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    private static void reativar(AmbienteService service, Scanner scanner, Perfil perfil) {
        System.out.println("--- REATIVAR AMBIENTE ---");
        String id = lerTexto(scanner, "ID do ambiente: ");
        try {
            Ambiente reativado = service.reativar(perfil, id);
            System.out.println("\nAmbiente reativado com sucesso!");
            System.out.println(reativado + "\n");
        } catch (Exception e) {
            tratarErro(e);
        }
    }

    // --- Auxiliares de leitura ---
    private static String lerTexto(Scanner scanner, String rotulo) {
        System.out.print(rotulo);
        return scanner.nextLine();
    }

    private static int lerInteiro(Scanner scanner, String rotulo) {
        System.out.print(rotulo);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            // Retorna 0 para que a validação do service aponte o campo com problema (KAN-12).
            return 0;
        }
    }

    private static TipoAmbiente lerTipo(Scanner scanner) {
        System.out.println("Tipo do ambiente:");
        TipoAmbiente[] tipos = TipoAmbiente.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + tipos[i]);
        }
        System.out.print("Opção: ");
        String entrada = scanner.nextLine().trim();
        try {
            int index = Integer.parseInt(entrada) - 1;
            if (index >= 0 && index < tipos.length) {
                return tipos[index];
            }
        } catch (NumberFormatException ignored) {
        }
        return null; // validação no service aponta "tipo" como campo com problema
    }

    // --- Tratamento de erros padronizado ---
    private static void tratarErro(Exception e) {
        if (e instanceof AcessoNegadoException) {
            System.out.println("\nAcesso negado!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } else if (e instanceof ValidacaoException) {
            ValidacaoException ve = (ValidacaoException) e;
            System.out.println("\nErro de validação (KAN-12)!");
            System.out.println("Mensagem: " + ve.getMessage());
            System.out.println("Campo com problema: " + ve.getCampoComProblema() + "\n");
        } else if (e instanceof IllegalArgumentException) {
            System.out.println("\nNão foi possível concluir a operação!");
            System.out.println("Mensagem: " + e.getMessage() + "\n");
        } else {
            System.out.println("\nErro inesperado: " + e.getMessage() + "\n");
        }
    }
}
