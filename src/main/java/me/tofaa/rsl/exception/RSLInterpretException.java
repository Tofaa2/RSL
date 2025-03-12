package me.tofaa.rsl.exception;

public class RSLInterpretException extends RSLException {

    public RSLInterpretException() {
    }

    public RSLInterpretException(String message) {
        super(message);
    }

    public RSLInterpretException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSLInterpretException(Throwable cause) {
        super(cause);
    }

    public RSLInterpretException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
