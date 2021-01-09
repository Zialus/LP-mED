package fcup;

import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

@Log
public class Testar {

    public static void main(String[] args) {

        log.info("---------------Just testing---------------");

        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers

        File temp;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        log.info("Temp file: " + temp.getAbsolutePath());

        Path path = temp.toPath();

        FileBuffer fb = new FileBuffer(path);

        fb.open(path);

        lista.add(fb);

        log.info("File List: " + lista);

        BufferView buff;
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