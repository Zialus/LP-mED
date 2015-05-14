import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBuffer extends Buffer {
	private Path savePath; // null= não definido
	private boolean modified;

	public void save() throws IOException {
		if (modified) saveAs(savePath);
	}

	public void saveAs(Path path) throws IOException { 

		BufferedWriter brw = Files.newBufferedWriter(path);
		int numLines = getNumLines();

		for (int i = 0; i < numLines;i++) {
			StringBuilder stringbuilder = getNthLine(i);
			brw.write(stringbuilder.toString());   
		}

	}

	public void open(Path path) throws IOException {  

		List<String> teste = Files.readAllLines(path); // cria lista com todas as linhas que vieram do ficheiro path

		for (String string : teste) {
			insertStr(string); // passa as strings para a LineList do buffer
		}

	}

	@Override
	public void insertChar(char c) {
		super.insertChar(c);
		modified = true; // marcar modificação
	}
	
	public void deleteChar() {
		super.deleteChar();
		modified = true; // marcar modificação
	}



}