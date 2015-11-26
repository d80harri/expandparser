package parsing;

public class Token {

    public enum Type {
        WHITESPACE, IDENT, POINT, DOLLAR, SEMICOLON, EOF, NUMBER, PLUS, LEFT_ROUND, RIGHT_ROUND, EXCLIMATION, RANGE

    }

    private StringBuffer cargo;
    private int line;
    private int column;
    private Type type;
    private String context;

    public Token(String cargo, int line, int column, Type type, String context) {
        super();
        this.cargo = new StringBuffer(cargo);
        this.line = line;
        this.column = column;
        this.type = type;
        this.context = context;
    }

    public Token(ScannedChar currentChar, Type type) {
        this("" + currentChar.getCargo(), currentChar.getLine(), currentChar.getColumn(), type, currentChar.getContext());
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
    
    public String getContext() {
		return context;
	}

    @Override
    public String toString() {
        return "Token [cargo=" + cargo + ", line=" + line + ", column=" + column + ", type=" + type + "]";
    }
}
