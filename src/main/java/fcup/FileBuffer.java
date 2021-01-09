package fcup;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

@Log
public class FileBuffer extends Buffer {
    private Path savePath;
    private boolean modified;
    private Deque<Command> commandList = new ArrayDeque<>();
    private int startRow;                   					// Primeira linha logica que aparece na janela
    private int lastRow;                                     // Ultima linha logica que aparece na janela

    FileBuffer(Path path){
        savePath = path;
        modified = false;
    }

    public boolean isModified(){
        return modified;
    }
    public void setModified(boolean bol){
        modified = bol;
    }

    public void save() {
        if (modified) saveAs(savePath);
        modified = false;
    }

    private void saveAs(Path path) {

        try (BufferedWriter brw = Files.newBufferedWriter(path)) {
            int numLines = getNumLines();
            log.info("numLines: " + numLines);

            for (int i = 0; i < numLines; i++) {
                StringBuilder sb = getNthLine(i);
                log.info("linha " + i + ": "+ sb.toString());
                brw.write(sb.toString());
                brw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void open(Path path) {

        try (BufferedReader brr = Files.newBufferedReader(path)) {
            log.info("File Path: " + path);

            //Ler o ficheiro linha a linha e enviar as linhas para o Buffer
            String tmp;
            while ((tmp = brr.readLine()) != null) {
                insertStr(tmp);
                insertLn();
            }

            //Volar a colocar o cursor na posição 0,0 após preencher o Buffer(o que levaria o cursor a ir para o fim do buffer)
            Cursor curtmp = new Cursor(0, 0);
            setCursor(curtmp);

            modified = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public Deque<Command> getCommandList() {
        return commandList;
    }

    public void setCommandList(Deque<Command> commandList) {
        this.commandList = commandList;
    }
}