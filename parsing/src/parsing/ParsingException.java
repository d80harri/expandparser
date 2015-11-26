package parsing;

import java.util.Arrays;

import parsing.Token.Type;

/**
 *
 * TODO What does this type/class do?
 */
public class ParsingException extends Exception {

    public ParsingException(Token currentToken, Type[] expectedType) {
        super("Found token " + currentToken + " but expected one of" + Arrays.toString(expectedType) + " near of \"" + currentToken.getContext() + "\"");
    }

}
