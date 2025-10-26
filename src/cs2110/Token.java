package cs2110;

/**
 * An enumeration of TokenTypes. Lists all possible types of Tokens available to the parser.
 */
enum TokenType {
    // Enums will be new to you. Examples of how to use them are in Parser.java

    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,

    LPAREN,
    RPAREN,

    LBRACE,
    RBRACE,

    ASSIGN,
    SEMICOLON,

    NUMBER,
    VAR,

    GREATER,
    LESS,
    EQUAL,

    TRUE,
    FALSE,

    IF,
    THEN,
    ELSE,

    WHILE,
    DO,

    EOF
}

/**
 * Representation of a Token and its associated literal value
 * The literal value is null for all TokenTypes except for NUMBER and VAR
 */
public record Token(TokenType tokenType, Object literal) {

    /**
     * Creates a Token without a literal value (should be used for everything
     * except for NUMBER and VAR)
     */
    public Token(TokenType tokenType) {
        this(tokenType, null);
    }

}
