package me.tofaa.rsl.exception;

public class RSLTokenizeException extends RSLException {
    public RSLTokenizeException() {
    }

    public RSLTokenizeException(String message) {
        super(message);
    }

    public RSLTokenizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSLTokenizeException(Throwable cause) {
        super(cause);
    }

    public RSLTokenizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
