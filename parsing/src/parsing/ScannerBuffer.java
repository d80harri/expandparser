package parsing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ScannerBuffer {
	private LRUCache<Character> before;
	private char current;
	private LRUCache<Character> after;
	
	private InputStream stream;
	
	public ScannerBuffer(int i, InputStream stream) throws IOException {
		before = new LRUCache<>(i);
		after = new LRUCache<>(i);
		this.stream = stream;
		
		fillAfterBuffer(i, stream);
	}
	
	private void fillAfterBuffer(int i, InputStream stream2) throws IOException {
		Character fromAfter = null;
		while (fromAfter == null) {
			fromAfter = after.add((char)stream.read());
		}
		current = fromAfter;
	}

	public char read() throws IOException {
		char result = this.current;
		char c = (char)stream.read();
		Character current = after.add(c);
		if (current != null) {
			before.add(this.current);
			this.current = current.charValue();
		}
		return result;
	}

	public String getContext() {
		List<Character> before = this.before.getFullBuffer();
		List<Character> after = this.after.getFullBuffer();
		return toString(before) + current + toString(after);
	}

	private String toString(List<Character> charList) {
		StringBuffer result = new StringBuffer();
		
		for (Character c : charList) {
			result.append(c);
		}
		
		return result.toString();
	}

	public static void main(String[] args) throws IOException {
		ScannerBuffer buffer = new ScannerBuffer(5, new ByteArrayInputStream("0123456789"
								.getBytes()));
		System.out.println(buffer.read());
	}
}
