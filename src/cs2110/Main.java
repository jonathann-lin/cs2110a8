package cs2110;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import cs2110.Expression.UnassignedVariableException;
import static cs2110.Parser.*;
import static cs2110.TokenType.*;

public class Main {

    /**
     * A Map of keywords to generate Tokens while lexing
     */
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("if", IF);
        keywords.put("then", THEN);
        keywords.put("else", ELSE);
        keywords.put("while", WHILE);
        keywords.put("do", DO);
    }

    /**
     * Some tokens are in the form [c][=], so this is just a helper method for cleaner code
     * (This is alot more useful if >=, <=, and != are introduced)
     */
    private static Token equalsHelper(int c, TokenType equals) {
        if (c == -1) {
            return new Token(EOF);
        }
        if (c == '=') {
            return new Token(equals);
        }
        return new Token(TokenType.EOF);
    }

    /**
     * Consumes the next full token from <code>reader</code>, with any whitespace before it.
     * Returns the token through a <code>Token</code> object.
     */
    private static Token scanToken(Reader reader) throws IOException {
        int c = reader.read();
        if (c == -1) return new Token(TokenType.EOF);

        while (Character.isWhitespace(c)) c = reader.read();

        return switch ((char) c) {
            case '(' -> new Token(LPAREN);
            case ')' -> new Token(RPAREN);
            case '{' -> new Token(LBRACE);
            case '}' -> new Token(RBRACE);
            case '+' -> new Token(PLUS);
            case '-' -> new Token(MINUS);
            case '*' -> new Token(MULTIPLY);
            case '/' -> new Token(DIVIDE);
            case ';' -> new Token(SEMICOLON);
            case ':' -> equalsHelper(reader.read(), ASSIGN);
            case '<' -> new Token(LESS);
            case '>' -> new Token(GREATER);
            case '=' -> equalsHelper(reader.read(), EQUAL);
            default -> {
                if (Character.isDigit(c) || c == '.') {
                    StringBuilder num = new StringBuilder();
                    do {
                        num.append((char) c);
                        reader.mark(1);
                    } while ((c = reader.read()) != -1 && (Character.isDigit(c) || c == '.'));

                    if (c != -1) {
                        reader.reset();
                    }

                    yield new Token(TokenType.NUMBER, Double.valueOf(num.toString()));
                }

                if (Character.isAlphabetic(c)) {
                    StringBuilder word = new StringBuilder();
                    do {
                        word.append(Character.toLowerCase((char) c));
                        reader.mark(1);
                    } while ((c = reader.read()) != -1 && Character.isAlphabetic(c));

                    if (c != -1) {
                        reader.reset();
                    }

                    if (keywords.containsKey(word.toString())) {
                        yield new Token(keywords.get(word.toString()), null);
                    }
                    yield new Token(TokenType.VAR, word.toString());
                }

                yield new Token(TokenType.EOF);
            }
        };

    }

    /**
     * Returns a queue of tokens from a <code>program</code> string.
     * Also used in <code>ParsingTest.java</code>
     */
    public static Queue<Token> tokenize(String program) throws IOException {
        StringReader stringReader = new StringReader(program);
        Queue<Token> tokens = new ArrayDeque<>();

        Token curr = scanToken(stringReader);
        while (curr.tokenType() != TokenType.EOF) {
            tokens.add(curr);
            curr = scanToken(stringReader);
        }
        return tokens;
    }

    /**
     * Given a <code>program</code> string, returns the AST form.
     * Throws a <code>MalformedExpressionException</code> if the string
     * isn't grammatically correct.
     */
    private static Expression<VarTable> getAST(String program) throws
            IOException, MalformedExpressionException {
        Queue<Token> tokens = tokenize(program);
        //        for (Token t : tokens) {
        //            System.out.println(t);
        //        }
        Parser parser = new Parser(tokens);
        return parser.parseProgram();
    }

    /**
     * Calls <code>Main.run</code> via file input, found in <code>path</code>
     */
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    /**
     * Calls <code>Main.run</code> through a command line prompt that takes in programs.
     * Programs must be one-line.
     */
    private static void runCommandLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a program below: ");
        while (true) {
            System.out.print("> ");
            String line = br.readLine();
            if (line == null) break;
            run(line);
        }
    }

    /**
     * Given a program string, runs and prints out a simplified expression and the variable table
     */
    private static void run(String program) throws IOException {
        Expression<VarTable> c = null;
        try {
            c = getAST(program);
            VarTable result = c.eval(VarTable.empty());
            System.out.println("\nSimplified Expression: ");
            System.out.println(c.simplify(VarTable.empty()).infixString());

            System.out.println("\nVarTable: ");
            for (String var : result.varSet()) {
                System.out.println(var + " " + result.getValue(var));
            }
        } catch (MalformedExpressionException e) {
            System.out.println("Invalid Program: " + e.getMessage());
        } catch (UnassignedVariableException e) {
            System.out.println("\nSimplified Expression: ");
            System.out.println(c.simplify(VarTable.empty()).infixString());
        }
    }

    /**
     * Runs the full program. If a file name is inputted, then the program runs with a file as input.
     * Otherwise, a command line prompt is given.
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            runCommandLine();
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            System.err.println("Usage: java [script]");
            System.exit(1);
        }
    }
}
