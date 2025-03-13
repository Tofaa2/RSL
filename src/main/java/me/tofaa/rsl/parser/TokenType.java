package me.tofaa.rsl.parser;

public enum TokenType {

    NULL,

    FOR,
    WHILE,
    BREAK,
    RANGE_EXCLUSIVE,
    RANGE_INCLUSIVE,

    // Logic
    IS,
    NOT,
    AND,
    OR,
    GREATER_THAN,
    GREATER_THAN_OR_EQ,
    LESS_THAN,
    LESS_THAN_OR_EQ,
    IF,
    ELSE,
    ELIF,

    NUMBER,
    IDENTIFIER,
    EQUALS,
    ARITHMENTIC,
    VARIABLE,
    CONSTANT,
    L_PAREN,
    R_PAREN,
    SEMICOLON,
    FN,
    RETURN,
    L_BRACKET,
    R_BRACKET,
    STRING,
    DOT,
    COLON,
    COMMA,
    L_BRACE,
    R_BRACE,
    BOOL,

    EOF,

    ;

}
