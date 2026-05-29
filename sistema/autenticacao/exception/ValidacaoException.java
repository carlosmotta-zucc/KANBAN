package exception;

public class ValidacaoException extends RuntimeException {
    private String campoComProblema;

    public ValidacaoException(String message) {
        super(message);
    }

    public ValidacaoException(String message, String campoComProblema) {
        super(message);
        this.campoComProblema = campoComProblema;
    }

    public ValidacaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidacaoException(Throwable cause) {
        super(cause);
    }

    public String getCampoComProblema() {
        return campoComProblema;
    }
}
