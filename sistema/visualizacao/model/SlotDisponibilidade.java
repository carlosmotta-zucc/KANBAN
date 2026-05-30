package model;

public class SlotDisponibilidade {
    private String horario;
    private String status;

    public SlotDisponibilidade(String horario, String status) {
        this.horario = horario;
        this.status = status;
    }

    public String getHorario() {
        return horario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
