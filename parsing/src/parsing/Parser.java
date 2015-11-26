package parsing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parsing.Token.Type;
import parsing.ast.Cardinality;
import parsing.ast.CompoundProperty;
import parsing.ast.Property;
import parsing.ast.PropertyList;
import semantic.PropertyTree;

/**
 *
 * TODO What does this type/class do?
 */
public class Parser {

    private final Lexer lexer;

    public Parser(InputStream stream) throws IOException {
        lexer = new Lexer(stream);
        lexer.nextIgnoreWhiteSpaces();
    }

    public Lexer getLexer() {
        return lexer;
    }

    public PropertyList parsePropertyList() throws IOException, ParsingException {
        PropertyList result = readPropertyList();
        lexer.nextIgnoreWhiteSpaces();
        readExpectedOrThrow(Type.EOF);
        return result;
    }

    public PropertyList readPropertyList() throws IOException, ParsingException {
        PropertyList result = new PropertyList();
        CompoundProperty currCompound = readCompoundProperty();

        result.add(currCompound);
        if (lexer.get().getType() == Type.SEMICOLON) {
            while (lexer.get().getType() == Type.SEMICOLON) {
                lexer.nextIgnoreWhiteSpaces();
                currCompound = readCompoundProperty();
                result.add(currCompound);
            }
        }
        if (lexer.get().getType() == Type.LEFT_ROUND) {
            lexer.next();
            PropertyList innerList = readPropertyList();
            for (CompoundProperty innerProperty : innerList) {
                result.add(new CompoundProperty(currCompound, innerProperty));
            }
            readExpectedOrThrow(Type.RIGHT_ROUND);
        }
        return result;
    }

    public CompoundProperty readCompoundProperty() throws IOException, ParsingException {
        CompoundProperty result = new CompoundProperty(readPropertyPath());

        Token t = lexer.get();

        if (t.getType() == Type.LEFT_SQUARE) {
            result.setCardinality(readCardinality());
        }

        return result;
    }

    private Cardinality readCardinality() throws ParsingException, IOException {
        Cardinality result = new Cardinality();

        readExpectedOrThrow(Type.LEFT_SQUARE);
        Token offset = readExpectedOrThrow(Type.NUMBER);
        result.setLeft(Integer.parseInt(offset.getCargo().toString()));

        readExpectedOrThrow(Type.SEMICOLON);
        readExpectedOrThrow(Type.PLUS);
        Token limit = readExpectedOrThrow(Type.NUMBER);
        result.setRight(result.getLeft() + Integer.parseInt(limit.getCargo().toString()));

        readExpectedOrThrow(Type.RIGHT_SQUARE);

        return result;
    }

    private List<Property> readPropertyPath() throws IOException, ParsingException {
        List<Property> result = new ArrayList<>();
        result.add(readProperty());
        while (lexer.get().getType() == Type.POINT) {
            lexer.next();
            result.add(readProperty());
        }
        return result;
    }

    private Property readProperty() throws IOException, ParsingException {
        Property result = new Property();
        Token ident = readExpectedOrThrow(Type.IDENT);
        result.setPropertyName(ident.getCargo().toString());

        return result;
    }

    private Token analyzeExpectedOrThrow(Type... expectedType) throws ParsingException {
        Token currentToken = lexer.get();
        if (!Arrays.asList(expectedType).contains(currentToken.getType())) {
            throw new ParsingException(currentToken, expectedType);
        }

        return currentToken;
    }

    private Token readExpectedOrThrow(Type... expectedTypes) throws IOException, ParsingException {
        Token result = analyzeExpectedOrThrow(expectedTypes);
        lexer.next();
        return result;
    }

    private Token readExpectedOrThrowIgnoringWhitespaces(Type... expectedTypes) throws IOException, ParsingException {
        Token result = readExpectedOrThrow(expectedTypes);
        lexer.nextIgnoreWhiteSpaces();
        return result;
    }

    public static void main(String[] args) throws IOException, ParsingException {
        Parser parser = new Parser(new ByteArrayInputStream("test.blubb, test.bla.sub, test.bla[1,+2], test.hallo[1,+2](one[17,+4], two)".getBytes()));
        PropertyList propertyList = parser.parsePropertyList();

        PropertyTree propertyTree = new PropertyTree();
        propertyTree.addPropertyList(propertyList);

        propertyTree.print(System.out);
    }

}
