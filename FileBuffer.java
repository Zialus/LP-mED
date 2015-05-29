import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBuffer extends Buffer {
	private Path savePath;
	private boolean modified;
	
	FileBuffer(Path path){
		savePath = path;
	}

	public void save() throws IOException {
		if (modified) saveAs(savePath);
		modified=false;
	}

	public void saveAs(Path path) throws IOException { 
		
		//File file = new File(path.toString());		
		//FileWriter fw = new FileWriter(file.getAbsolutePath());
		
		BufferedWriter brw =  Files.newBufferedWriter(path);
		
		int numLines = getNumLines();
		System.out.println("NUMLINES" + numLines);
		
		
		for (int i = 0; i < numLines;i++) {
			StringBuilder stringbuilder = getNthLine(i);
			System.out.println("linha i =  " + i);	
			System.out.println(stringbuilder.toString());
			brw.write(stringbuilder.toString());
			brw.write("\n");
		}	
		brw.close();

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
