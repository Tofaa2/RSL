package me.tofaa.rsl.exception;

public class RSLSyntaxTreeException extends RSLException{
    public RSLSyntaxTreeException() {
    }

    public RSLSyntaxTreeException(String message) {
        super(message);
    }

    public RSLSyntaxTreeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RSLSyntaxTreeException(Throwable cause) {
        super(cause);
    }

    public RSLSyntaxTreeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
