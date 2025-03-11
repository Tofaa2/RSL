package me.tofaa.rsl.lexer;

public record Token(String value, TokenType type) {

    @Override
    public String toString() {
        return "{ type: " + type.name() + ", value: " + value + " }";
    }
}
