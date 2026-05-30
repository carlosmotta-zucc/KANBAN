package service;

import model.Ambiente;
import model.TipoAmbiente;
import model.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class OcupacaoService {
    private List<Ambiente> ambientes = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();

    // Define o dia operacional padrão em minutos: 12 horas = 720 minutos (ex: 08:00 às 20:00)
    private static final double MINUTOS_OPERACIONAIS_DIA = 720.0;

    public OcupacaoService() {
        // Inicializa com ambientes semelhantes ao KAN-03
        Ambiente a1 = new Ambiente("1", "Sala 101", TipoAmbiente.Sala, 40, "Bloco A");
        Ambiente a2 = new Ambiente("2", "Lab de Informática 1", TipoAmbiente.Laboratorio, 25, "Bloco B");
        Ambiente a3 = new Ambiente("3", "Lab de Química", TipoAmbiente.Laboratorio, 20, "Bloco C");
        Ambiente a4 = new Ambiente("4", "Lab de Redes", TipoAmbiente.Laboratorio, 18, "Bloco B");

        ambientes.add(a1);
        ambientes.add(a2);
        ambientes.add(a3);
        ambientes.add(a4);

        // Adiciona reservas mock para simular taxas de ocupação no dia de testes (30/05/2026)
        LocalDate dataTeste = LocalDate.of(2026, 5, 30);

        // Lab de Informática 1 ocupado das 08:00 às 12:00 (4 horas = 240 minutos) e das 14:00 às 16:00 (2 horas = 120 minutos) -> total 6 horas (50%)
        reservas.add(new Reserva("1", a2, "Carlos Professor", dataTeste, LocalTime.of(8, 0), LocalTime.of(12, 0), "Turma A"));
        reservas.add(new Reserva("2", a2, "Alice Professor", dataTeste, LocalTime.of(14, 0), LocalTime.of(16, 0), "Turma B"));

        // Lab de Química ocupado das 09:00 às 11:00 (2 horas = 120 minutos) -> total 2 horas (16.67%)
        reservas.add(new Reserva("3", a3, "Ana Professor", dataTeste, LocalTime.of(9, 0), LocalTime.of(11, 0), "Turma C"));

        // Sala 101 ocupada no dia seguinte 31/05/2026 das 13:30 às 17:30 (4 horas = 240 minutos)
        reservas.add(new Reserva("4", a1, "Roberto Professor", LocalDate.of(2026, 5, 31), LocalTime.of(13, 30), LocalTime.of(17, 30), "Turma D"));
    }

    public List<Ambiente> listarAmbientesAtivos() {
        List<Ambiente> ativos = new ArrayList<>();
        for (Ambiente a : ambientes) {
            if (a.isAtivo()) {
                ativos.add(a);
            }
        }
        return ativos;
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    public double calcularOcupacaoDiaria(Ambiente ambiente, LocalDate data) {
        long totalMinutosReservados = 0;

        for (Reserva r : reservas) {
            if (r.getAmbiente().getId().equals(ambiente.getId()) && r.getData().equals(data)) {
                Duration duracao = Duration.between(r.getHoraInicio(), r.getHoraFim());
                totalMinutosReservados += duracao.toMinutes();
            }
        }

        return (totalMinutosReservados / MINUTOS_OPERACIONAIS_DIA) * 100.0;
    }

    public int obterTotalReservas(Ambiente ambiente) {
        int contagem = 0;
        for (Reserva r : reservas) {
            if (r.getAmbiente().getId().equals(ambiente.getId())) {
                contagem++;
            }
        }
        return contagem;
    }

    public double obterTotalHoras(Ambiente ambiente) {
        long totalMinutos = 0;
        for (Reserva r : reservas) {
            if (r.getAmbiente().getId().equals(ambiente.getId())) {
                Duration duracao = Duration.between(r.getHoraInicio(), r.getHoraFim());
                totalMinutos += duracao.toMinutes();
            }
        }
        return totalMinutos / 60.0;
    }
}
