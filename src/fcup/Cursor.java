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

}