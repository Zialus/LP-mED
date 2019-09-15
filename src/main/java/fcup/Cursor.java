package fcup;

import java.util.Objects;

public class Cursor {

    private int l;
    private int c;

    public Cursor(int l, int c) {
        this.l = l;
        this.c = c;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cursor cursor = (Cursor) o;
        return l == cursor.l &&
                c == cursor.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(l, c);
    }

    @Override
    public String toString() {
        return "Cursor{" +
                "l=" + l +
                ", c=" + c +
                '}';
    }

}
