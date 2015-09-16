package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Helper to get all the content of a file.
 * 
 * @author Jean Jung
 */
public class EntireFileReader extends BufferedReader {
	
	private File file; 

	/**
	 * {@inheritDoc}
	 * @param in
	 * @throws FileNotFoundException 
	 */
	public EntireFileReader(File in) throws FileNotFoundException {
		super(new FileReader(in));
		this.file = in; 
	}
	
	/**
	 * Read all the lines of the file and returns this on an {@link List}. 
	 * @return a {@link List} with all the lines of the file.
	 * @throws IOException
	 */
	public Set<String> readAllLines() throws IOException {
		
		Set<String> lines = new TreeSet<String>();
		
		String linha;
		
		while ((linha = this.readLine()) != null) {
			
			lines.add(linha);
		}
		
		return lines; 
	}
	
	/**
	 * Read all the content of the file into a String.
	 * @return A interned String with the file content.
	 * @throws IOException
	 */
	public String readFileContent() throws IOException {
		
		String content = "";
		char[] buff = new char[4096];
		try {
			
			int c = 0;
			do {
				
				c = this.read(buff);
				
				if (c > 0) 
					content = content.concat(new String(buff, 0, c));
				
			} while (c != -1);
			
		} finally {
			try { this.close(); } catch (IOException e) { e.printStackTrace();}
		}
		
		return content.intern(); // prevent the StringBuffer storage for convenience with performance issues.
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
}
