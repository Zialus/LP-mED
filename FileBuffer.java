import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBuffer extends Buffer {
	private Path savePath;
	private boolean modified;

	FileBuffer(Path path){
		savePath = path;
		modified = false;
	}

	public boolean getModified(){
		return modified;
	}
	public void setModified(boolean bol){
		modified = bol;
	}

	public void save() throws IOException {
		if (modified) saveAs(savePath);
		modified = false;
	}

	public void saveAs(Path path) throws IOException { 

		BufferedWriter brw =  Files.newBufferedWriter(path);

		int numLines = getNumLines();
		//		System.out.println("numLines" + numLines);

		for (int i=0; i<numLines; i++) {
			StringBuilder stringbuilder = getNthLine(i);
			//			System.out.println("linha i =  " + i);	
			//			System.out.println(stringbuilder.toString());
			brw.write(stringbuilder.toString());
			brw.write("\n");
		}

		brw.close();

	}

	public void open(Path path) throws IOException {  

		BufferedReader brr = Files.newBufferedReader(path);
		System.out.println(path);

		//Ler o ficheiro linha a linha e enviar as linhas para o Buffer
		String tmp;
		while ((tmp = brr.readLine()) != null){
			insertStr(tmp);
			insertLn();
		}

		//Volar a colocar o cursor na posição 0,0 após preencher o Buffer(o que levaria o cursor a ir para o fim do buffer)
		Cursor curtmp = new Cursor(0,0);
		setCursor(curtmp);

		modified = true;
		
		brr.close();

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