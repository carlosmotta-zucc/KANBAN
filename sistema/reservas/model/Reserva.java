package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Reserva {
    public static final String STATUS_RESERVADA = "Reservada";
    public static final String STATUS_CANCELADA = "Cancelada";
    // KAN-10: reservas especiais passam por aprovação do administrador
    public static final String STATUS_PENDENTE = "Pendente";
    public static final String STATUS_APROVADA = "Aprovada";
    public static final String STATUS_RECUSADA = "Recusada";

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
    private boolean especial;

    public Reserva(String id, Laboratorio laboratorio, String professor,
                   LocalDate data, LocalTime horaInicio, LocalTime horaFim, String turma) {
        this(id, laboratorio, professor, data, horaInicio, horaFim, turma, false);
    }

    public Reserva(String id, Laboratorio laboratorio, String professor,
                   LocalDate data, LocalTime horaInicio, LocalTime horaFim, String turma, boolean especial) {
        this.id = id;
        this.laboratorio = laboratorio;
        this.professor = professor;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.turma = turma;
        this.especial = especial;
        // KAN-10: reserva especial nasce pendente de aprovação; a normal já fica reservada
        this.status = especial ? STATUS_PENDENTE : STATUS_RESERVADA;
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

    public boolean isEspecial() {
        return especial;
    }

    public boolean isPendente() {
        return STATUS_PENDENTE.equals(status);
    }

    // Ocupa o horário (e portanto conta para conflito do KAN-06) enquanto não for cancelada/recusada.
    // Pendente e Aprovada seguram o horário; só Cancelada/Recusada o liberam.
    public boolean isAtiva() {
        return !STATUS_CANCELADA.equals(status) && !STATUS_RECUSADA.equals(status);
    }

    // KAN-05: cancela a reserva, liberando o ambiente (disponibilidade atualizada)
    public void cancelar() {
        this.status = STATUS_CANCELADA;
    }

    // KAN-10: decisão do administrador sobre uma reserva especial pendente
    public void aprovar() {
        this.status = STATUS_APROVADA;
    }

    public void recusar() {
        this.status = STATUS_RECUSADA;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + laboratorio.getNome() +
                " | Data: " + data.format(FMT_DATA) +
                " | Horário: " + horaInicio.format(FMT_HORA) + "-" + horaFim.format(FMT_HORA) +
                " | Turma: " + turma +
                " | Professor: " + professor +
                " | Status: " + status +
                (especial ? " | Especial" : "");
    }
}
