package painel.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private String id;
    private Ambiente ambiente;
    private String professor;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String turma;
    private String status;

    public Reserva(String id, Ambiente ambiente, String professor,
                   LocalDate data, LocalTime horaInicio, LocalTime horaFim, String turma) {
        this.id = id;
        this.ambiente = ambiente;
        this.professor = professor;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.turma = turma;
        this.status = "Reservada";
    }

    public String getId() {
        return id;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public String getProfessor() {
        return professor;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public String getTurma() {
        return turma;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + ambiente.getNome() +
                " | Data: " + data.format(FMT_DATA) +
                " | Horário: " + horaInicio.format(FMT_HORA) + "-" + horaFim.format(FMT_HORA) +
                " | Turma: " + turma +
                " | Professor: " + professor +
                " | Status: " + status;
    }
}
