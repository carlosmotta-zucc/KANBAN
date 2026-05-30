package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    public static final String STATUS_RESERVADA = "Reservada";
    public static final String STATUS_CANCELADA = "Cancelada";

    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private String id;
    private Laboratorio laboratorio;
    private String professor;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String turma;
    private String status;

    public Reserva(String id, Laboratorio laboratorio, String professor,
                   LocalDate data, LocalTime horaInicio, LocalTime horaFim, String turma) {
        this.id = id;
        this.laboratorio = laboratorio;
        this.professor = professor;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.turma = turma;
        this.status = STATUS_RESERVADA;
    }

    public String getId() {
        return id;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
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

    public boolean isAtiva() {
        return STATUS_RESERVADA.equals(status);
    }

    // KAN-05: cancela a reserva, liberando o ambiente (disponibilidade atualizada)
    public void cancelar() {
        this.status = STATUS_CANCELADA;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + laboratorio.getNome() +
                " | Data: " + data.format(FMT_DATA) +
                " | Horário: " + horaInicio.format(FMT_HORA) + "-" + horaFim.format(FMT_HORA) +
                " | Turma: " + turma +
                " | Professor: " + professor +
                " | Status: " + status;
    }
}
