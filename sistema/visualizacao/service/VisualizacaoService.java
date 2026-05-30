package service;

import model.Ambiente;
import model.AmbienteVisualizacao;
import model.SlotDisponibilidade;
import java.util.ArrayList;
import java.util.List;

public class VisualizacaoService {
    private AmbienteService ambienteService;

    public VisualizacaoService(AmbienteService ambienteService) {
        this.ambienteService = ambienteService;
    }

    /**
     * Obtém todos os ambientes ativos cadastrados no KAN-03 e adiciona
     * faixas de horários simuladas para testes de visualização.
     */
    public List<AmbienteVisualizacao> obterVisaoDisponibilidade() {
        List<Ambiente> todosAmbientes = ambienteService.listarTodos();
        List<AmbienteVisualizacao> visao = new ArrayList<>();

        for (Ambiente a : todosAmbientes) {
            // Apenas exibe ambientes ativos
            if (a.isAtivo()) {
                AmbienteVisualizacao av = new AmbienteVisualizacao(a);
                
                // Simula horários com base no ID para variação nos testes
                int idNum = 0;
                try {
                    idNum = Integer.parseInt(a.getId());
                } catch (NumberFormatException ignored) {}

                if (idNum % 2 == 0) {
                    av.adicionarHorario("08:00 - 10:00", "Disponível");
                    av.adicionarHorario("10:00 - 12:00", "Reservado");
                    av.adicionarHorario("14:00 - 16:00", "Disponível");
                } else {
                    av.adicionarHorario("08:00 - 10:00", "Reservado");
                    av.adicionarHorario("10:00 - 12:00", "Reservado");
                    av.adicionarHorario("19:00 - 22:00", "Disponível");
                }
                visao.add(av);
            }
        }
        return visao;
    }

    /**
     * Filtra apenas os horários com status "Disponível" nos ambientes ativos.
     */
    public List<AmbienteVisualizacao> obterApenasDisponiveis() {
        List<AmbienteVisualizacao> todos = obterVisaoDisponibilidade();
        List<AmbienteVisualizacao> disponiveis = new ArrayList<>();

        for (AmbienteVisualizacao av : todos) {
            boolean temDisponivel = false;
            for (SlotDisponibilidade slot : av.getDisponibilidades()) {
                if (slot.getStatus().equalsIgnoreCase("Disponível")) {
                    temDisponivel = true;
                    break;
                }
            }

            if (temDisponivel) {
                // Cria um wrapper filtrado contendo somente os slots livres
                AmbienteVisualizacao filtrado = new AmbienteVisualizacao(av.getAmbiente());
                for (SlotDisponibilidade slot : av.getDisponibilidades()) {
                    if (slot.getStatus().equalsIgnoreCase("Disponível")) {
                        filtrado.adicionarHorario(slot.getHorario(), slot.getStatus());
                    }
                }
                disponiveis.add(filtrado);
            }
        }
        return disponiveis;
    }
}
