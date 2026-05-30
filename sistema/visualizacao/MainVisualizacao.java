import model.Ambiente;
import model.AmbienteVisualizacao;
import model.SlotDisponibilidade;
import service.AmbienteService;
import service.VisualizacaoService;
import java.util.List;
import java.util.Scanner;

public class MainVisualizacao {
    public static void main(String[] args) {
        // Instancia o serviço de ambientes (KAN-03) e o serviço de visualização
        // (KAN-01)
        AmbienteService ambienteService = new AmbienteService();
        VisualizacaoService visualizacaoService = new VisualizacaoService(ambienteService);
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            System.out.println("=========================================");
            System.out.println("      VISUALIZAÇÃO DE SALAS (KAN-01)     ");
            System.out.println("=========================================");
            System.out.println("1. Listar todas as salas e seus horários");
            System.out.println("2. Filtrar apenas salas com horários disponíveis");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            System.out.println();

            switch (opcao) {
                case "1":
                    exibirSalas(visualizacaoService.obterVisaoDisponibilidade());
                    break;
                case "2":
                    exibirSalas(visualizacaoService.obterApenasDisponiveis());
                    break;
                case "3":
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Escolha 1, 2 ou 3.\n");
            }
        }

        scanner.close();
        System.out.println("Módulo de visualização encerrado.");
    }

    private static void exibirSalas(List<AmbienteVisualizacao> visao) {
        if (visao.isEmpty()) {
            System.out.println("Nenhum ambiente encontrado ou ativo.\n");
            return;
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-20s | %-12s | %-10s | %-15s | %-10s\n", "Nome", "Tipo", "Capacidade", "Horário", "Status");
        System.out.println("----------------------------------------------------------------------");
        for (AmbienteVisualizacao av : visao) {
            Ambiente a = av.getAmbiente();
            for (SlotDisponibilidade slot : av.getDisponibilidades()) {
                System.out.printf("%-20s | %-12s | %-10d | %-15s | %-10s\n",
                        a.getNome(),
                        a.getTipo(),
                        a.getCapacidade(),
                        slot.getHorario(),
                        slot.getStatus());
            }
        }
        System.out.println("----------------------------------------------------------------------\n");
    }
}
