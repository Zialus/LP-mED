package fcup;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log
public class Editor {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Pode-se correr o programa com um ficheiro");
            System.out.println("java -jar <caminho/para/o_ficheiro.jar> <caminho/para/o_ficheiro.txt>");
            System.out.println("ou varios");
            System.out.println("java -jar <caminho/para/o_ficheiro.jar> <caminho/para/o_ficheiro1.txt> <etc.txt>");

            System.exit(0);
        }

        FileSystem defaultFileSystem = FileSystems.getDefault();

        List<FileBuffer> fileBufferList = Arrays.stream(args)
                .map(defaultFileSystem::getPath)
                .map(path -> {
                    fcup.FileBuffer fb = new fcup.FileBuffer(path);
                    fb.open(path);
                    return fb;
                })
                .collect(Collectors.toList());

        log.info(fileBufferList.toString());

        BufferView buff = new BufferView(fileBufferList);
        buff.startTerm();
    }
}