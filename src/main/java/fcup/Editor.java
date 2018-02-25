package fcup;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class Editor {

    public static void main(String[] args) throws IOException {

        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers

        if (args.length ==0){
            System.out.println("Pode-se correr o programa com um ficheiro");
            System.out.println("java -jar <caminho/para/o_ficheiro.jar> <caminho/para/o_ficheiro.txt>");
            System.out.println("ou varios");
            System.out.println("java -jar <caminho/para/o_ficheiro.jar> <caminho/para/o_ficheiro1.txt> <etc.txt>");

            System.exit(0);
        }


        for (String arg : args) {
            Path path = FileSystems.getDefault().getPath(arg);
            FileBuffer fb = new FileBuffer(path);

            fb.open(path);

            lista.add(fb);
        }

        System.out.println(lista.toString());
        BufferView buff = new BufferView(lista);

        buff.startTerm();

    }
}