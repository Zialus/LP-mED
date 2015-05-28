import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBuffer extends Buffer {
	private Path savePath;
	private boolean modified;

	public void save() throws IOException {
		if (modified) saveAs(savePath);
		modified=false;
	}

	public void saveAs(Path path) throws IOException { 

		BufferedWriter brw = Files.newBufferedWriter(path);
		int numLines = getNumLines();

		for (int i = 0; i < numLines;i++) {
			StringBuilder stringbuilder = getNthLine(i);
			brw.write(stringbuilder.toString());   
		}

	}

	public boolean getModified(){
		return modified;
	}
	public void setModified(boolean bol){
		modified = bol;
	}


	public void open(Path path) throws IOException {  

		BufferedReader brr = Files.newBufferedReader(path);
		System.out.println(path);

		// Ler o buffer linha a linha
		String tmp;
		while ((tmp = brr.readLine()) != null){
			insertStr(tmp);
			insertLn();
		}

		Cursor cur = getCursor();	
		cur.setC(0);	
		cur.setL(0);

		modified = true;

	}

	@Override
	public void insertChar(char c) {
		super.insertChar(c);
		modified = true; // marcar modificação
	}

	@Override
	public void deleteChar() {
		super.deleteChar();
		modified = true; // marcar modificação
	}

}
