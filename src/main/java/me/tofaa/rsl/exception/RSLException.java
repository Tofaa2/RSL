package me.tofaa.rsl.exception;

public class RSLException extends RuntimeException {
    public RSLException() {
    }

    public RSLException(String message) {
        super(message);
    }

    public RSLException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSLException(Throwable cause) {
        super(cause);
    }

    public RSLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
