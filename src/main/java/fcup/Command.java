package fcup;

public class Command {
    public Type tipo;
    public Cursor cursor;
    public char caracter;

    Command(Type tipo, Cursor cursor, char caracter ){
        this.tipo = tipo;
        this.cursor = cursor;
        this.caracter = caracter;
    }

}