package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// KAN-04: comprovante de que a reserva foi aceita (confirmação na tela + registro).
public class ConfirmacaoReserva {
    private static final DateTimeFormatter FMT_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String protocolo;
    private Reserva reserva;
    private LocalDateTime momentoConfirmacao;

    public ConfirmacaoReserva(String protocolo, Reserva reserva, LocalDateTime momentoConfirmacao) {
        this.protocolo = protocolo;
        this.reserva = reserva;
        this.momentoConfirmacao = momentoConfirmacao;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public LocalDateTime getMomentoConfirmacao() {
        return momentoConfirmacao;
    }

    @Override
    public String toString() {
        return "Protocolo " + protocolo +
                " | Confirmada em: " + momentoConfirmacao.format(FMT_DATA_HORA) +
                " | " + reserva;
    }
}
