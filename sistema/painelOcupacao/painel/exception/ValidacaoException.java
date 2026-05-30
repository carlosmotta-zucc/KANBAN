package painel.exception;

public class ValidacaoException extends RuntimeException {
    private String campoComProblema;

    public ValidacaoException(String message) {
        super(message);
    }

    public ValidacaoException(String message, String campoComProblema) {
        super(message);
        this.campoComProblema = campoComProblema;
    }

    public String getCampoComProblema() {
        return campoComProblema;
    }
}
