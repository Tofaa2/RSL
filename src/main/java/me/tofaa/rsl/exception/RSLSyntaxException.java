package me.tofaa.rsl.exception;

public class RSLSyntaxException extends RSLException {

    public RSLSyntaxException() {
    }

    public RSLSyntaxException(String message) {
        super(message);
    }

    public RSLSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSLSyntaxException(Throwable cause) {
        super(cause);
    }

    public RSLSyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
