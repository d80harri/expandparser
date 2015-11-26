package parsing;

/**
 *
 * TODO What does this type/class do?
 */
public class ScannedChar {

    private int line;
    private int column;
    private int location;
    private char cargo;

    public ScannedChar(int line, int column, int location, char cargo) {
        super();
        this.line = line;
        this.column = column;
        this.location = location;
        this.cargo = cargo;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getLocation() {
        return location;
    }

    public char getCargo() {
        return cargo;
    }

    @Override
    public String toString() {
        return "ScannedChar [line=" + line + ", column=" + column + ", location=" + location + ", cargo=" + cargo + "]";
    }

}
