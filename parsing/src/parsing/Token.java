package parsing;

public class Token {

    public enum Type {
        WHITESPACE, IDENT, POINT, DOLLAR, LEFT_CURLY, RIGHT_CURLY, SEMICOLON, EOF, LEFT_SQUARE, NUMBER, PLUS, RIGHT_SQUARE, LEFT_ROUND, RIGHT_ROUND

    }

    private StringBuffer cargo;
    private int line;
    private int column;
    private Type type;

    public Token(String cargo, int line, int column, Type type) {
        super();
        this.cargo = new StringBuffer(cargo);
        this.line = line;
        this.column = column;
        this.type = type;
    }

    public Token(ScannedChar currentChar, Type type) {
        this("" + currentChar.getCargo(), currentChar.getLine(), currentChar.getColumn(), type);
    }

    public void appendCargo(String cargo) {
        this.cargo.append(cargo);
    }

    public StringBuffer getCargo() {
        return cargo;
    }

    public void setCargo(StringBuffer cargo) {
        this.cargo = cargo;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Token [cargo=" + cargo + ", line=" + line + ", column=" + column + ", type=" + type + "]";
    }
}
