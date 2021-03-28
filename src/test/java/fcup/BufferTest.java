package fcup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BufferTest {

    @BeforeEach
    void setUp() {
        LogManager.getLogManager().reset();
    }

    @Test
    void testNewEmptyBuffer() {

        Buffer b = new Buffer();
        assertEquals( 1, b.getNumLines());
        assertEquals( new Cursor(0,0) , b.getCursor());

    }

    @Test
    void testSetCursorToValidPosition(){

        Buffer b = new Buffer("xalala");
        b.setCursor(new Cursor(0,3));
        assertEquals(new Cursor(0,3),b.getCursor());

    }

    @Test
    void testInsertChar() {

        Buffer b = new Buffer("123456789abcdef");
        Cursor c1 = new Cursor(0,5);
        b.setCursor(c1);
        b.insertChar('\n');


        assertEquals("12345", new String(b.getNthLine(0)));
        assertEquals("6789abcdef", new String(b.getNthLine(1)));
        assertEquals("", new String(b.getNthLine(2)));


        Cursor c2 = new Cursor(1,1);
        b.setCursor(c2);
        b.insertChar('#');
        b.insertChar('#');
        b.insertChar('#');


        assertEquals("12345",new String(b.getNthLine(0)));
        assertEquals("6###789abcdef", new String(b.getNthLine(1)));
        assertEquals("", new String(b.getNthLine(2)));


        Cursor c3 = new Cursor(1,2);
        b.setCursor(c3);
        b.insertChar('\n');


        assertEquals("12345", new String(b.getNthLine(0)));
        assertEquals("6#", new String(b.getNthLine(1)));
        assertEquals("##789abcdef", new String(b.getNthLine(2)));
        assertEquals("", new String(b.getNthLine(3)));

    }

    @Test
    void testEmptyBuffer() {
        Buffer b = new Buffer();
        assertEquals("", b.getNthLine(0).toString());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> b.getNthLine(1));

    }

    @Test
    void testComplexBuffer() {
        Buffer b = new Buffer("123456789abcdef");
        assertEquals("123456789abcdef", b.getNthLine(0).toString());
        assertEquals("", b.getNthLine(1).toString());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> b.getNthLine(2));
    }

    @Test
    void testMovePrev() {
        Buffer b = new Buffer("123456789abcdef");

        Cursor c1 = new Cursor(0, 2);
        b.setCursor(c1);
        b.insertChar('\n');

        assertEquals("12", b.getNthLine(0).toString());
        assertEquals("3456789abcdef", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());


        Cursor c2 = new Cursor(1, 1);
        b.setCursor(c2);
        assertEquals(1, b.getCursor().getL());
        assertEquals(1, b.getCursor().getC());

        b.movePrev();
        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.movePrev();
        assertEquals(0, b.getCursor().getL());
        assertEquals(2, b.getCursor().getC());

        b.movePrev();
        assertEquals(0, b.getCursor().getL());
        assertEquals(1, b.getCursor().getC());

        b.movePrev();
        assertEquals(0, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.movePrev();
        assertEquals(0, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.movePrev();
        assertEquals(0, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());
    }

    @Test
    void testMoveCursorToInvalidPosition() {
        Buffer b = new Buffer("123456789abcdef");

        assertEquals(b.getCursor(), new Cursor(0,0));

        Cursor c1 = new Cursor(10, 99);
        b.setCursor(c1);

        assertEquals(b.getCursor(), new Cursor(0,0));
    }

    @Test
    void testMoveNext() {
        Buffer b = new Buffer("123456789abcdef");

        Cursor c1 = new Cursor(0, 13);
        b.setCursor(c1);
        b.insertChar('\n');

        assertEquals("123456789abcd", b.getNthLine(0).toString());
        assertEquals("ef", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        Cursor cursor2 = new Cursor(0, 12);
        b.setCursor(cursor2);

        assertEquals(0, b.getCursor().getL());
        assertEquals(12, b.getCursor().getC());

        b.moveNext();
        assertEquals(0, b.getCursor().getL());
        assertEquals(13, b.getCursor().getC());

        b.moveNext();
        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.moveNext();
        assertEquals(1, b.getCursor().getL());
        assertEquals(1, b.getCursor().getC());

        b.moveNext();
        assertEquals(1, b.getCursor().getL());
        assertEquals(2, b.getCursor().getC());

        b.moveNext();
        assertEquals(2, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.moveNext();
        assertEquals(2, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.moveNext();
        assertEquals(2, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());
    }

    @Test
    void test_delete_char() {
        Buffer b = new Buffer("123456789abcdef");
        Cursor cursor1 = new Cursor(0, 11);
        b.setCursor(cursor1);
        b.insertChar('\n');

        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("cdef", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        Cursor cursor2 = new Cursor(1, 4);
        b.setCursor(cursor2);

        assertEquals(1, b.getCursor().getL());
        assertEquals(4, b.getCursor().getC());


        b.deleteChar();
        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("cde", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(3, b.getCursor().getC());

        b.deleteChar();
        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("cd", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(2, b.getCursor().getC());

        b.deleteChar();
        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("c", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(1, b.getCursor().getC());

        b.deleteChar();
        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("", b.getNthLine(1).toString());
        assertEquals("", b.getNthLine(2).toString());

        assertEquals(1, b.getCursor().getL());
        assertEquals(0, b.getCursor().getC());

        b.deleteChar();
        assertEquals("123456789ab", b.getNthLine(0).toString());
        assertEquals("", b.getNthLine(1).toString());

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> b.getNthLine(2));

        assertEquals(0, b.getCursor().getL());
        assertEquals(11, b.getCursor().getC());

        b.deleteChar();
        assertEquals("123456789a", b.getNthLine(0).toString());
        assertEquals("", b.getNthLine(1).toString());

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> b.getNthLine(2));

        assertEquals(0, b.getCursor().getL());
        assertEquals(10, b.getCursor().getC());
    }

}