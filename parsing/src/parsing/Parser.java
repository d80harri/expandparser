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

	public PropertyList parsePropertyList() throws IOException,
			ParsingException {
		PropertyList result = readPropertyList();
		lexer.nextIgnoreWhiteSpaces();
		readExpectedOrThrow(Type.EOF);
		return result;
	}

	public PropertyList readPropertyList() throws IOException, ParsingException {
		PropertyList result = new PropertyList();
		CompoundProperty currCompound = readCompoundProperty();

		result.add(currCompound);
		while (lexer.get().getType() == Type.SEMICOLON
				|| lexer.get().getType() == Type.LEFT_ROUND) {
			if (lexer.get().getType() == Type.SEMICOLON) {
				lexer.nextIgnoreWhiteSpaces();
				currCompound = readCompoundProperty();
				result.add(currCompound);
			} else if (lexer.get().getType() == Type.LEFT_ROUND) {
				lexer.next();
				PropertyList innerList = readPropertyList();
				for (CompoundProperty innerProperty : innerList) {
					result.add(new CompoundProperty(currCompound, innerProperty));
				}
				readExpectedOrThrow(Type.RIGHT_ROUND);
			}
		}
		return result;
	}

	public CompoundProperty readCompoundProperty() throws IOException,
			ParsingException {
		CompoundProperty result = new CompoundProperty(readPropertyPath());

		Token t = lexer.get();

		if (t.getType() == Type.EXCLIMATION) {
			readExpectedOrThrow(Type.EXCLIMATION);
			result.setCardinality(readCardinality());
		}

		return result;
	}

	private Cardinality readCardinality() throws ParsingException, IOException {
		Cardinality result = new Cardinality();

		Token firstNumberToken = readExpectedOrThrow(Type.NUMBER);
		Token t = readExpectedOrThrow(Type.RANGE, Type.LEFT_ROUND, Type.PLUS);
		
		if (t.getType() == Type.RANGE) {
			result.setLeft(Integer.parseInt(firstNumberToken.getCargo().toString()));
			Token rightToken = readExpectedOrThrow(Type.NUMBER);
			result.setRight(Integer.parseInt(rightToken.getCargo().toString()));
		} else if (t.getType() == Type.LEFT_ROUND) {
			Token pageSizeToken = firstNumberToken;
			Token pageNumberToken = readExpectedOrThrow(Type.NUMBER);

			int pageSize = Integer
					.parseInt(pageSizeToken.getCargo().toString());
			int pageNumber = Integer.parseInt(pageNumberToken.getCargo()
					.toString());

			result.setLeft(pageSize * pageNumber);
			result.setRight(result.getLeft() + pageSize - 1);
			readExpectedOrThrow(Type.RIGHT_ROUND);
		} else if (t.getType() == Type.PLUS) {
			result.setLeft(Integer.parseInt(firstNumberToken.getCargo().toString()));
			Token rightToken = readExpectedOrThrow(Type.NUMBER);
			result.setRight(result.getLeft() + Integer.parseInt(rightToken.getCargo().toString()));
		}
		
		return result;
	}

	private List<Property> readPropertyPath() throws IOException,
			ParsingException {
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

	private Token analyzeExpectedOrThrow(Type... expectedType)
			throws ParsingException {
		Token currentToken = lexer.get();
		if (!Arrays.asList(expectedType).contains(currentToken.getType())) {
			throw new ParsingException(currentToken, expectedType);
		}

		return currentToken;
	}

	private Token readExpectedOrThrow(Type... expectedTypes)
			throws IOException, ParsingException {
		Token result = analyzeExpectedOrThrow(expectedTypes);
		lexer.next();
		return result;
	}

	private Token readExpectedOrThrowIgnoringWhitespaces(Type... expectedTypes)
			throws IOException, ParsingException {
		Token result = readExpectedOrThrow(expectedTypes);
		lexer.nextIgnoreWhiteSpaces();
		return result;
	}

	// Laufzeitkomplexität: 4887215.9090...E-10 x + 235,733...
	// wobei x = {Anzahl der Zeichen}
	// Berechnet durch folgende Messwerte:
	// x       t in mills
	// 880000	1237
	// 1760000 1666
	// 2640000 1702
	// 3520000 1834
	// 4400000 1947
	// 5280000 2296
	// 6160000 2931
	// 7040000 2885
	// 7920000 4176
	// 8800000 5243
	// 9680000 5119
	// 10560000 3526
	// 11440000 6977
	// 12320000 5964
	// 13200000 7642
	public static void main(String[] args) throws IOException, ParsingException {
		String parseString = "test.blubb, test.bla.sub, test.bla!1+2, test.hallo!1..2(one!17+4, two), test.other!10(0)";
		StringBuffer buffer = new StringBuffer(parseString);
		
		for (int i=0; i<5000; i++) {
			buffer.append("," + parseString);
		}
		
		long start = System.currentTimeMillis();
		Parser parser = new Parser(
				new ByteArrayInputStream(buffer.toString()
								.getBytes()));
		// Parser parser = new Parser(new
		// ByteArrayInputStream("test.other%10[2]".getBytes()));
		PropertyList propertyList = parser.parsePropertyList();
		System.out.println("PARSED IN " + (System.currentTimeMillis() - start) + " milliseconds");

		PropertyTree propertyTree = new PropertyTree();
		propertyTree.addPropertyList(propertyList);

		propertyTree.print(System.out);
	}

}
