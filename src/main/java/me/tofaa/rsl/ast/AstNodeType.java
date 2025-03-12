package me.tofaa.rsl.ast;

public enum AstNodeType {

    // STMT
    PROGRAM,
    VAR_DECL,
    FUNC_DECL,

    // EXPR,
    NUM_LITERAL,
    BOOL_LITERAL,
    NULL_LITERAL,
    IDENTIFIER,
    BINARY_EXPR,
    UNARY_EXPR,
    CALL_EXPR,
    ASSIGNMENT_EXPR,


}
