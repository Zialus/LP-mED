package fcup;

public class Cursor {

    private int l;
    private int c;

    public Cursor(int l, int c){
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

        if (l != cursor.l) return false;
        return c == cursor.c;
    }

    @Override
    public int hashCode() {
        int result = l;
        result = 31 * result + c;
        return result;
    }
}