package service;

import exception.AcessoNegadoException;
import exception.ValidacaoException;
import model.Laboratorio;
import model.Perfil;
import model.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private List<Laboratorio> laboratorios = new ArrayList<>();
    private List<Reserva> reservas = new ArrayList<>();
    private int proximoIdReserva = 1;

    public ReservaService() {
        // Laboratórios de exemplo (no sistema real viriam do cadastro do KAN-03)
        laboratorios.add(new Laboratorio("1", "Lab de Informática 1", 25, "Bloco B"));
        laboratorios.add(new Laboratorio("2", "Lab de Química", 20, "Bloco C"));
        laboratorios.add(new Laboratorio("3", "Lab de Redes", 18, "Bloco B"));
    }

    // --- Controle de acesso: somente Professor reserva (KAN-02 + KAN-08) ---
    private void verificarPermissaoProfessor(Perfil perfil) {
        if (perfil != Perfil.Professor) {
            throw new AcessoNegadoException(
                    "Apenas professores podem reservar laboratórios para aula prática");
        }
    }

    // --- Reservar: seleciona laboratório, data, horário e turma ---
    public Reserva reservar(Perfil perfil, String professor, String laboratorioId,
                            String dataStr, String horaInicioStr, String horaFimStr, String turma) {
        verificarPermissaoProfessor(perfil);

        if (professor == null || professor.trim().isEmpty()) {
            throw new ValidacaoException("Nome do professor é obrigatório", "professor");
        }

        Laboratorio laboratorio = validarLaboratorio(laboratorioId);
        LocalDate data = validarData(dataStr);
        LocalTime horaInicio = validarHora(horaInicioStr, "horaInicio");
        LocalTime horaFim = validarHora(horaFimStr, "horaFim");

        if (!horaFim.isAfter(horaInicio)) {
            throw new ValidacaoException("O horário final deve ser depois do horário inicial", "horaFim");
        }
        if (turma == null || turma.trim().isEmpty()) {
            throw new ValidacaoException("Turma é obrigatória", "turma");
        }

        // KAN-06: não aprova reserva que cause choque de horário no mesmo laboratório
        verificarChoqueDeHorario(laboratorio, data, horaInicio, horaFim);

        Reserva reserva = new Reserva(String.valueOf(proximoIdReserva++), laboratorio,
                professor.trim(), data, horaInicio, horaFim, turma.trim());
        reservas.add(reserva);
        return reserva;
    }

    // --- KAN-06: impede reservas duplicadas no mesmo laboratório/data com horários sobrepostos ---
    private void verificarChoqueDeHorario(Laboratorio laboratorio, LocalDate data,
                                          LocalTime horaInicio, LocalTime horaFim) {
        for (Reserva r : reservas) {
            boolean mesmoLaboratorio = r.getLaboratorio().getId().equals(laboratorio.getId());
            boolean mesmaData = r.getData().equals(data);
            if (mesmoLaboratorio && mesmaData
                    && horariosSeSobrepoem(horaInicio, horaFim, r.getHoraInicio(), r.getHoraFim())) {
                throw new ValidacaoException(
                        "Conflito de horário: o laboratório já está reservado em "
                                + r.getHoraInicio().format(FMT_HORA) + "-" + r.getHoraFim().format(FMT_HORA)
                                + " (reserva " + r.getId() + ")", "horaInicio");
            }
        }
    }

    // Dois intervalos [inicioA, fimA) e [inicioB, fimB) se sobrepõem quando
    // inicioA < fimB e inicioB < fimA. Horários "encostados" (fim de um = início de outro) não conflitam.
    private boolean horariosSeSobrepoem(LocalTime inicioA, LocalTime fimA,
                                        LocalTime inicioB, LocalTime fimB) {
        return inicioA.isBefore(fimB) && inicioB.isBefore(fimA);
    }

    private Laboratorio validarLaboratorio(String laboratorioId) {
        if (laboratorioId == null || laboratorioId.trim().isEmpty()) {
            throw new ValidacaoException("Selecione um laboratório", "laboratorio");
        }
        Laboratorio laboratorio = buscarLaboratorio(laboratorioId);
        if (laboratorio == null) {
            throw new ValidacaoException("Laboratório não encontrado para o ID: " + laboratorioId, "laboratorio");
        }
        if (!laboratorio.isAtivo()) {
            throw new ValidacaoException("Laboratório está desativado e não pode ser reservado", "laboratorio");
        }
        return laboratorio;
    }

    private LocalDate validarData(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            throw new ValidacaoException("Data é obrigatória", "data");
        }
        LocalDate data;
        try {
            data = LocalDate.parse(dataStr.trim(), FMT_DATA);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("Data inválida. Use o formato dd/MM/aaaa", "data");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new ValidacaoException("A data não pode estar no passado", "data");
        }
        return data;
    }

    private LocalTime validarHora(String horaStr, String campo) {
        if (horaStr == null || horaStr.trim().isEmpty()) {
            throw new ValidacaoException("Horário é obrigatório", campo);
        }
        try {
            return LocalTime.parse(horaStr.trim(), FMT_HORA);
        } catch (DateTimeParseException e) {
            throw new ValidacaoException("Horário inválido. Use o formato HH:mm", campo);
        }
    }

    // --- Consultas ---
    public List<Laboratorio> listarLaboratoriosAtivos() {
        List<Laboratorio> ativos = new ArrayList<>();
        for (Laboratorio l : laboratorios) {
            if (l.isAtivo()) {
                ativos.add(l);
            }
        }
        return ativos;
    }

    public Laboratorio buscarLaboratorio(String id) {
        if (id == null) {
            return null;
        }
        for (Laboratorio l : laboratorios) {
            if (l.getId().equals(id.trim())) {
                return l;
            }
        }
        return null;
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }
}
