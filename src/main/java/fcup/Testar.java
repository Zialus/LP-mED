package fcup;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class Testar {

    public static void main(String[] args) throws IOException {

        //Path path = FileSystems.getDefault().getPath(args[0]);

        ArrayList<FileBuffer> lista = new ArrayList<>();  // Lista de Buffers

        for (String arg : args) {
            Path path = FileSystems.getDefault().getPath(arg);
            FileBuffer fb = new FileBuffer(path);

            try {
                fb.open(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            lista.add(fb);
        }

        System.out.println(lista.toString());
        BufferView buff = new BufferView(lista);

        buff.StartTerm();

        System.out.println("-----------Testar buffer vazio----------");
        test_empty_buffer();

        System.out.println("-----------Testar buffer nao vazio------");
        test_complex_buffer();

        System.out.println("-----------Testar Move Prev-------------");
        test_move_prev();

        System.out.println("-----------Testar Move Next-------------");
        test_move_next();

        System.out.println("-----------Testar Delete Char-----------");
        test_delete_char();

        System.out.println("-----------Testar Insert Char-----------");
        test_insert_char();

    }

    private static void test_insert_char() {
        Buffer buff_teste = new Buffer("123456789abcdef");
        Cursor cursor1 = new Cursor(0,5);
        buff_teste.setCursor(cursor1);
        buff_teste.insertChar('\n');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------");

        Cursor cursor2 = new Cursor(1,1);
        buff_teste.setCursor(cursor2);

        buff_teste.insertChar('#');
        buff_teste.insertChar('#');
        buff_teste.insertChar('#');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println("------------");

        Cursor cursor3 = new Cursor(1,2);
        buff_teste.setCursor(cursor3);
        buff_teste.insertChar('\n');

        System.out.println(buff_teste.getNthLine(0));
        System.out.println(buff_teste.getNthLine(1));
        System.out.println(buff_teste.getNthLine(2));
        System.out.println(buff_teste.getNthLine(3));
        System.out.println("------------");

    }

    private static void test_empty_buffer() {
        //Buffer buff_teste = new Buffer();
        //StringBuilder sb1 = buff_teste.LineList.get(0);
        //System.out.println(buff_teste.LineList.get(1));
        //StringBuilder sb2 = new StringBuilder("");

        //assert sb1.toString().equals(sb2.toString());

    }

    private static void test_complex_buffer() {
        //Buffer buff_teste = new Buffer("123456789abcdef");
        //System.out.println(buff_teste.LineList.get(0));
        //System.out.println(buff_teste.LineList.get(1));
        //System.out.println(buff_teste.LineList.get(2));
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