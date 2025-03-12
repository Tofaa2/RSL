package me.tofaa.rsl.lexer;

import me.tofaa.rsl.Utils;
import me.tofaa.rsl.ast.*;
import me.tofaa.rsl.exception.RSLSyntaxTreeException;
import me.tofaa.rsl.exception.RSLTokenizeException;

import java.util.ArrayList;
import java.util.List;

public class RSLParser {

    private final List<Token> tokens;

    public RSLParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public RSLParser(Lexer l) {
        this.tokens = l.tokenize();
    }

    public ProgramStatement create() {
        var program = new ProgramStatement(new ArrayList<>());
        while (notEof()) {
            program.body().add(parseStatement());
        }
        return program;
    }

    private Statement parseStatement() {
        switch (currentToken().type()) {
            case TokenType.VARIABLE, TokenType.CONSTANT -> {
                return parseVarDecl();
            }
            default -> {
                return parseExpr();
            }
        }
    }

    // if var and non asigned, do null
    private VarDeclStatement parseVarDecl() {
        boolean isConstant = advance().type() == TokenType.CONSTANT;
        var identifier = advanceExpect(TokenType.IDENTIFIER, "Expected identifier name following a declaration statement (var | const)").value();
        if (currentToken().type() == TokenType.SEMICOLON) {
            advance();
            if (isConstant) {
                throw new RSLSyntaxTreeException("Attempted to specify an uninitialized constant. Use var instead of const");
            }
            return new VarDeclStatement(false, identifier, NullLiteralExpression.INSTANCE);
        }
        advanceExpect(TokenType.EQUALS, "Expected equals token in variable declaration statement");
        var decl = new VarDeclStatement(isConstant, identifier, parseExpr());
        advanceExpect(TokenType.SEMICOLON, "Expected semicolon after variable declaration.");
        return decl;
    }

    private Expression parseExpr() {
        return parseAssignmentExpr();

    }

    private Expression parseObjectExpr() {
        if (currentToken().type() != TokenType.L_BRACE) {
            return parseAdditiveExpr();
        }
        advance();
        var properties = new ArrayList<PropertyLiteralExpression>();
        while (notEof() && currentToken().type() != TokenType.R_BRACE) {

            var key = advanceExpect(TokenType.IDENTIFIER, "Expected key for object.").value();

            // Shorthand key <-> pair where you just define the key.
            if (currentToken().type() == TokenType.COMMA) {
                advance();
                properties.add(new PropertyLiteralExpression(key, NullLiteralExpression.INSTANCE));
                continue;
            }
            else if (currentToken().type() == TokenType.R_BRACE) {
                properties.add(new PropertyLiteralExpression(key, NullLiteralExpression.INSTANCE));
                continue;
            }

            // Full key: value, parsing
            advanceExpect(TokenType.COLON, "Missing colon declaration for non-shorthand variable declaration for objects. Use (key:value) syntax");
            var value = parseExpr();
            properties.add(new PropertyLiteralExpression(key, value));
            if (currentToken().type() != TokenType.R_BRACE) {
                advanceExpect(TokenType.COMMA, "Expected comma or end of object declaration }.");
            }
        }

        advanceExpect(TokenType.R_BRACE, "Expected object closing brace }.");
        return new ObjectLiteralExpression(properties);
    }

    private Expression parseAssignmentExpr() {
        var left = parseObjectExpr();
        if (currentToken().type() == TokenType.EQUALS) {
            advance();
            var value = parseAssignmentExpr();
            return new AssignmentExpression(left, value);
        }
        return left;
    }

    // Left precedence based additive expression parsing
    private Expression parseAdditiveExpr() {
        var left = parseMultiplicativeExpr();
        while (currentToken().value().equals("+") || currentToken().value().equals("-")) {
            var operator = advance().value();
            var right = parseMultiplicativeExpr();
            left = new BinaryExpression(
                    left, right, operator
            );
        }
        return left;
    }

    // Left precedence based multiplicative expression parsing
    private Expression parseMultiplicativeExpr() {
        var left = parsePrimaryExpr();
        while (currentToken().value().equals("/") || currentToken().value().equals("*") || currentToken().value().equals("%")) {
            var operator = advance().value();
            var right = parsePrimaryExpr();
            left = new BinaryExpression(
                    left, right, operator
            );
        }
        return left;
    }


    private Expression parsePrimaryExpr() {
        var token = currentToken().type();

        switch (token) {
            case IDENTIFIER -> {
                return new IdentifierExpression(advance().value());
            }
            case NULL -> {
                advance();
                return NullLiteralExpression.INSTANCE;
            }
            case NUMBER -> {
                return new NumericLiteralExpression(Utils.numFromString(advance().value()));
            }
            case BOOL -> {
                return new BooleanLiteral(Boolean.parseBoolean(advance().value()));
            }
            case L_PAREN -> {
                advance();
                var value = this.parseExpr();
                advanceExpect(TokenType.R_PAREN, "Unexpected token found. Expected closing parenthesis");
                return value;
            }

            default -> {
                throw new RSLSyntaxTreeException("Unexpected token found at: " + currentToken());
            }
        }

    }

    private Token advanceExpect(TokenType required, String msg) {
        var prev = advance();
        if (prev.type() != required) {
            throw new RSLTokenizeException(msg + " Found: " + prev.type());
        }
        return prev;
    }

    private Token currentToken() {
        return this.tokens.getFirst();
    }

    private Token advance() {
        var current = currentToken();
        tokens.removeFirst();
        return current;
    }

    private boolean notEof() {
        return tokens.getFirst().type() != TokenType.EOF;
    }


}
