package fcup;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class Testar {

    public static void main() {

        System.out.println("-----------Testar Geral-----------------");
        test_geral();

        System.out.println("-----------Testar Move Prev-------------");
        test_move_prev();

        System.out.println("-----------Testar Move Next-------------");
        test_move_next();

        System.out.println("-----------Testar Delete Char-----------");
        test_delete_char();

    }

    private static void test_geral() {
        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers


        File temp = null;
        try {
            temp = File.createTempFile("temp-file-name", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
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
        }

        try {
            buff.startTerm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test_move_prev() {
        Buffer buff_teste = new Buffer("123456789abcdef");
        Cursor cursor1 = new Cursor(0,2);
        buff_teste.setCursor(cursor1);
        buff_teste.insertChar('\n');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));

        Cursor cursor2 = new Cursor(1,1);
        buff_teste.setCursor(cursor2);

        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.movePrev();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
    }

    private static void test_move_next() {
        Buffer buff_teste = new Buffer("123456789abcdef");

        Cursor cursor1 = new Cursor(0,13);
        buff_teste.setCursor(cursor1);
        buff_teste.insertChar('\n');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));

        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        Cursor cursor2 = new Cursor(0,12);
        buff_teste.setCursor(cursor2);
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.moveNext();
        System.out.println("Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
    }

    private static void test_delete_char() {
        Buffer buff_teste = new Buffer("123456789abcdef");
        Cursor cursor1 = new Cursor(0,11);
        buff_teste.setCursor(cursor1);
        buff_teste.insertChar('\n');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("---------------------");

        Cursor cursor2 = new Cursor(1,4);
        buff_teste.setCursor(cursor2);

        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        //System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
        buff_teste.deleteChar();
        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        //System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------Position " + " l: " + buff_teste.getCursor().getL() + " c: " + buff_teste.getCursor().getC());
    }

}