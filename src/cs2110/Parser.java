package cs2110;

import java.util.Queue;

/**
 * A Parser object that consumes tokens and processes it into an Expression
 */
public class Parser {

    private Queue<Token> tokens;

    /**
     * Creates a Parser object with a queue of tokens
     */
    public Parser(Queue<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parses arithmetic units like variables, numbers, and parentheses that surround
     * arithmetic expressions.
     * U ::= x | n | -n | (A)
     */
    Expression<Double> parseArithmeticUnit() throws MalformedExpressionException {
        if (tokens.isEmpty()) {
            throw new MalformedExpressionException("Arithmetic unit expected");
        }
        Token token = tokens.remove();
        switch (token.tokenType()) {
            // TODO 3.3: Return a Variable
            case VAR -> {
                throw new UnsupportedOperationException();
            }
            case NUMBER -> {
                return new Constant<>((Double) token.literal());
            }
            case MINUS -> {
                if (tokens.isEmpty() || tokens.peek().tokenType() != TokenType.NUMBER)
                    throw new MalformedExpressionException("Negative number expected");
                token = tokens.remove();
                return new Constant<>(-1 * (Double) token.literal());
            }
            case LPAREN -> {
                Expression<Double> aExpr = parseArithmeticExpr();
                if (!tokens.isEmpty() && tokens.peek().tokenType() == TokenType.RPAREN) {
                    tokens.remove();
                    return aExpr;
                } else {
                    throw new MalformedExpressionException("Parentheses expected");
                }
            }
        };
        throw new MalformedExpressionException("Unexpected token in place of arithmetic unit");
    }

    /**
     * Parses arithmetic terms that can use either multiply or divide.
     * T ::= U | U * T | U / T
     */
    Expression<Double> parseArithmeticTerm() throws MalformedExpressionException {
        Expression<Double> expr = parseArithmeticUnit();
        // TODO 3.7: Implement multiplication and division parsing.

        return expr;
    }

    /**
     * Parses arithmetic expressions. To ensure that order of operations are well-respected, we
     * need to split the work amongst a helper function (which parses T).
     * A ::= T | T + A | T - A
     */
    Expression<Double> parseArithmeticExpr() throws MalformedExpressionException {
        Expression<Double> expr = parseArithmeticTerm();
        // TODO 3.7: Implement addition and subtraction parsing.

        return expr;
    }

    /**
     * Parse boolean expressions and consume tokens
     */
    Expression<Boolean> parseBooleanExpr() throws MalformedExpressionException {
        if (tokens.isEmpty()) {
            throw new MalformedExpressionException("Boolean expression expected");
        }
        Token token = tokens.peek();
        if (token.tokenType() == TokenType.TRUE) {
            tokens.remove();
            return new Constant<>(true);
        } else if (token.tokenType() == TokenType.FALSE) {
            tokens.remove();
            return new Constant<>(false);
        } else if (token.tokenType() == TokenType.LPAREN) {
            tokens.remove();
            Expression<Boolean> bExpr = parseBooleanExpr();
            if (!tokens.isEmpty() && tokens.peek().tokenType() == TokenType.RPAREN) {
                tokens.remove();
                return bExpr;
            } else throw new MalformedExpressionException("Parentheses expected");
        }

        Expression<Double> leftExpr = parseArithmeticExpr();
        if (tokens.isEmpty()) throw new MalformedExpressionException("Boolean operator expected");
        token = tokens.remove();

        // TODO 4.1: Implement this method according to its specifications.
        if (token.tokenType() != TokenType.GREATER && token.tokenType() != TokenType.LESS
                && token.tokenType() != TokenType.EQUAL) {
            throw new MalformedExpressionException("Boolean operator expected");
        }

        throw new UnsupportedOperationException();
    }

    /**
     * Helper method to parse [keyword] {P}
     */
    Expression<VarTable> parseControlCommandHelper(TokenType keyword, String keywordMsg)
            throws MalformedExpressionException {
        if (tokens.isEmpty() || tokens.remove().tokenType() != keyword) {
            throw new MalformedExpressionException(keywordMsg);
        }
        if (tokens.isEmpty() || tokens.remove().tokenType() != TokenType.LBRACE) {
            throw new MalformedExpressionException("Left brace expected");
        }
        Expression<VarTable> command = parseProgram();
        if (tokens.isEmpty() || tokens.remove().tokenType() != TokenType.RBRACE) {
            throw new MalformedExpressionException("Right brace expected");
        }
        return command;
    }

    /**
     * Parse through any non-sequential command.
     * C ::= x:=P | if B then {P} else {P} | while B do {P} else {P}
     */
    Expression<VarTable> parseCommand() throws MalformedExpressionException {
        if (tokens.isEmpty()) {
            throw new MalformedExpressionException("Command expected");
        }
        Token token = tokens.remove();

        switch (token.tokenType()) {
            case VAR -> {
                if (!tokens.isEmpty() && tokens.remove().tokenType() == TokenType.ASSIGN) {
                    // TODO 3.3: Implement this method according to its specifications.
                    throw new UnsupportedOperationException();
                } else {
                    throw new MalformedExpressionException("Assignment operator expected");
                }
            }
            case IF -> {
                Expression<Boolean> booleanExpr = parseBooleanExpr();
                Expression<VarTable> command1 = parseControlCommandHelper(TokenType.THEN,
                        "Then token expected");
                Expression<VarTable> command2 = parseControlCommandHelper(TokenType.ELSE,
                        "Else token expected");
                // TODO 4.2: Return If
                throw new UnsupportedOperationException();
            }
            case WHILE -> {
                Expression<Boolean> booleanExpr = parseBooleanExpr();
                Expression<VarTable> command1 = parseControlCommandHelper(TokenType.DO,
                        "Do token expected");
                Expression<VarTable> command2 = parseControlCommandHelper(TokenType.THEN,
                        "Then token expected");
                // TODO 4.2: Return While
                throw new UnsupportedOperationException();
            }
            default -> throw new MalformedExpressionException("Unexpected token in place of command");
        }
    }

    /**
     * Parse through a program.
     * P ::= C | C ; P
     */
    Expression<VarTable> parseProgram() throws MalformedExpressionException {
        Expression<VarTable> command = parseCommand();
        // TODO 3.8: Implement this method according to its specifications.

        return command;
    }

    /**
     * A class for an exception thrown during token parsing
     */
    public static class MalformedExpressionException extends Exception {
        MalformedExpressionException(String message) {
            super(message);
        }
    }

}
