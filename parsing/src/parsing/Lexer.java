package parsing;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parsing.Token.Type;

/**
 *
 * TODO What does this type/class do?
 */
public class Lexer {

    private static final List<Character> LOWER_LETTERS_SY_G;
    private static final List<Character> UPPER_LETTERS_SY_G;
    private static final List<Character> WHITESPACES_SY_G;
    private static final List<Character> IDENT_START_SY_G;
    private static final List<Character> IDENT_SY_G;
    private static final List<Character> DIGIT_SY_G;

    private static final char EOF_SY = (char) -1;
    private static final char POINT_SY = '.';
    private static final char DOLLAR_SY = '$';
    private static final char LEFT_CURLY_SY = '{';
    private static final char RIGHT_CURLY_SY = '}';
    private static final char SEMICOLON_SY = ',';
    private static final char LEFT_SQUARE_SY = '[';
    private static final char RIGHT_SQUARE_SY = ']';
    private static final char LEFT_ROUND_SY = '(';
    private static final char RIGHT_ROUND_SY = ')';
    private static final char PLUS_SY = '+';

    static {
        LOWER_LETTERS_SY_G = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                                           'x', 'y', 'z');
        UPPER_LETTERS_SY_G = toUpperCase(LOWER_LETTERS_SY_G);
        WHITESPACES_SY_G = Arrays.asList(' ');
        IDENT_START_SY_G = LOWER_LETTERS_SY_G;
        IDENT_SY_G = union(LOWER_LETTERS_SY_G, UPPER_LETTERS_SY_G);
        DIGIT_SY_G = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    }

    private static List<Character> toUpperCase(List<Character> chars) {
        List<Character> result = new ArrayList<>();
        for (char c : chars) {
            result.add(Character.toUpperCase(c));
        }
        return result;
    }

    private static List<Character> union(List<Character> col1, List<Character> col2) {
        List<Character> result = new ArrayList<>();
        result.addAll(col1);
        result.addAll(col2);
        return result;
    }

    private Scanner scanner;
    private Token currentToken;

    public Lexer(InputStream stream) throws IOException {
        scanner = new Scanner(stream);
        scanner.next();
    }

    public Token next() throws IOException {
        Token result;

        ScannedChar currentChar = scanner.get();

        if (WHITESPACES_SY_G.contains(currentChar.getCargo())) {
            result = readWhiteSpaces();
        } else if (EOF_SY == currentChar.getCargo()) {
            result = readToken(Type.EOF);
        } else if (DOLLAR_SY == currentChar.getCargo()) {
            result = readToken(Type.DOLLAR);
        } else if (PLUS_SY == currentChar.getCargo()) {
            result = readToken(Type.PLUS);
        } else if (LEFT_CURLY_SY == currentChar.getCargo()) {
            result = readToken(Type.LEFT_CURLY);
        } else if (RIGHT_CURLY_SY == currentChar.getCargo()) {
            result = readToken(Type.RIGHT_CURLY);
        } else if (LEFT_SQUARE_SY == currentChar.getCargo()) {
            result = readToken(Type.LEFT_SQUARE);
        } else if (RIGHT_SQUARE_SY == currentChar.getCargo()) {
            result = readToken(Type.RIGHT_SQUARE);
        } else if (LEFT_ROUND_SY == currentChar.getCargo()) {
            result = readToken(Type.LEFT_ROUND);
        } else if (RIGHT_ROUND_SY == currentChar.getCargo()) {
            result = readToken(Type.RIGHT_ROUND);
        } else if (IDENT_START_SY_G.contains(currentChar.getCargo())) {
            result = readIdent();
        } else if (DIGIT_SY_G.contains(currentChar.getCargo())) {
            result = readNumber();
        } else if (POINT_SY == currentChar.getCargo()) {
            result = readToken(Type.POINT);
        } else if (SEMICOLON_SY == currentChar.getCargo()) {
            result = readToken(Type.SEMICOLON);
        } else {
            throw new RuntimeException("Parse exception at " + currentChar);
        }

        currentToken = result;
        return result;
    }

    public Token nextIgnoreWhiteSpaces() throws IOException {
        Token t = next();
        while (t.getType() == Type.WHITESPACE) {
            t = next();
        }
        return t;
    }

    public Token get() {
        return currentToken;
    }

    private Token readToken(Type type) throws IOException {
        Token result = new Token(scanner.get(), type);
        scanner.next();
        return result;
    }

    private Token readIdent() throws IOException {
        Token result = new Token(scanner.get(), Type.IDENT);
        ScannedChar currentChar = scanner.next();

        while (IDENT_SY_G.contains(currentChar.getCargo())) {
            result.appendCargo("" + currentChar.getCargo());
            currentChar = scanner.next();
        }
        return result;
    }

    private Token readNumber() throws IOException {
        Token result = new Token(scanner.get(), Type.NUMBER);
        ScannedChar currentChar = scanner.next();

        while (DIGIT_SY_G.contains(currentChar.getCargo())) {
            result.appendCargo("" + currentChar.getCargo());
            currentChar = scanner.next();
        }
        return result;
    }

    private Token readWhiteSpaces() throws IOException {
        Token result = new Token(scanner.get(), Type.WHITESPACE);
        ScannedChar currentChar = scanner.next();

        while (WHITESPACES_SY_G.contains(currentChar.getCargo())) {
            result.appendCargo("" + currentChar.getCargo());
            currentChar = scanner.next();
        }
        return result;
    }
}
