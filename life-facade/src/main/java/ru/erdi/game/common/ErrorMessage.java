package ru.erdi.game.common;

import ru.erdi.game.facade.types.Error;

public class ErrorMessage extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Error error;

    public ErrorMessage() {
        super();
    }
    
    public ErrorMessage(String message) {
        super(message);
    }
    
    public ErrorMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorMessage(String message, Error error) {
        super(message);
        this.error = error;
    }
    
    public ErrorMessage(Error error) {
        this.error = error;
    }

    public ErrorMessage(String message, Error error, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public Error getFaultInfo() {
        return this.error;
    }
}
