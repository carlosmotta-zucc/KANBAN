package model;

import java.util.ArrayList;
import java.util.List;

public class AmbienteVisualizacao {
    private Ambiente ambiente;
    private List<SlotDisponibilidade> disponibilidades;

    public AmbienteVisualizacao(Ambiente ambiente) {
        this.ambiente = ambiente;
        this.disponibilidades = new ArrayList<>();
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public List<SlotDisponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public void adicionarHorario(String horario, String status) {
        this.disponibilidades.add(new SlotDisponibilidade(horario, status));
    }
}
