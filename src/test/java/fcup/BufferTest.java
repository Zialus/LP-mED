package fcup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BufferTest {

    @Test
    public void testNewEmptyBuffer() {

        Buffer b = new Buffer();
        assertEquals( 1, b.getNumLines());
        assertEquals( new Cursor(0,0) , b.getCursor());

    }

    @Test
    public void testSetCursorToValidPosition(){

        Buffer b = new Buffer("xalala");
        b.setCursor(new Cursor(0,3));
        assertEquals(new Cursor(0,3),b.getCursor());

    }

    @Test
    public void testInsertChar() {

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
    public void testEmptyBuffer() {
        Buffer b = new Buffer();
        assertEquals("", b.getNthLine(0).toString());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            StringBuilder nthLine = b.getNthLine(1);
        });

    }

    @Test
    public void testComplexBuffer() {
        Buffer b = new Buffer("123456789abcdef");
        assertEquals("123456789abcdef", b.getNthLine(0).toString());
        assertEquals("", b.getNthLine(1).toString());
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            StringBuilder nthLine = b.getNthLine(2);
        });
    }

}