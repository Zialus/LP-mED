import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class Editor {

	public static void main(String[] args) throws IOException {

		ArrayList<FileBuffer> lista = new ArrayList<FileBuffer>();  // Lista de Buffers

		for (int i = 0; i < args.length; i++) {
			Path path = FileSystems.getDefault().getPath(args[i]);
			FileBuffer fb = new FileBuffer(path);

			try {
				fb.open(path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			lista.add(fb);
		}

		System.out.println(lista.toString());
		BufferView buff = new BufferView(lista);	

		buff.StartTerm();

	}
}