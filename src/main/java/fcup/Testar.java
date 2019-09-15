package fcup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Testar {

    public static void main() {

        System.out.println("-----------Testar Geral-----------------");
        test_geral();

    }

    private static void test_geral() {
        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers


        File temp = null;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Temp file : " + temp.getAbsolutePath());

        Path path = temp.toPath();

        FileBuffer fb = new FileBuffer(path);

        try {
            fb.open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lista.add(fb);

        System.out.println(lista);

        BufferView buff = null;
        try {
            buff = new BufferView(lista);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            buff.startTerm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}