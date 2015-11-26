package parsing;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * TODO What does this type/class do?
 */
public class Scanner {

    private int currentLine = 0;
    private int currentColumn = 0;
    private int currentLocation = 0;

    private ScannedChar currentChar;
    private ScannerBuffer buffer = null;

    public Scanner(InputStream stream) throws IOException {
    	buffer = new ScannerBuffer(10, stream);
    }

    public ScannedChar next() throws IOException {
        ScannedChar result = null;
        int c = readNext();
        result = new ScannedChar(currentLine, currentColumn, currentLocation, (char) c, buffer.getContext());
        currentChar = result;
        return result;
    }

    public ScannedChar get() {
        return currentChar;
    }

    private int readNext() throws IOException {
        int result = -1;

        do {
            currentLocation++;
            currentColumn++;
            result = buffer.read();
            if (result == '\n') {
                currentLine++;
                currentColumn = 0;
            }
        } while (result == '\n');

        return result;
    }

}
