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
    private String context;

    public ScannedChar(int line, int column, int location, char cargo) {
        super();
        this.line = line;
        this.column = column;
        this.location = location;
        this.cargo = cargo;
    }

    public ScannedChar(int line, int column, int location,
			char cargo, String context) {
		this(line, column, location, cargo);
		this.context = context;
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
    
    public String getContext() {
		return context;
	}

    @Override
    public String toString() {
        return "ScannedChar [line=" + line + ", column=" + column + ", location=" + location + ", cargo=" + cargo + "]";
    }

}
