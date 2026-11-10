package persistence;

// Falha de acesso a dados vista pela aplicação: a mensagem é segura para exibir na tela
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}