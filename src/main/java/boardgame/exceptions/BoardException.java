package boardgame.exceptions;

public class BoardException extends RuntimeException{
    private static final Long serialVersionUID = 1L;

    public BoardException(String message) {
        super(message);
    }
}
