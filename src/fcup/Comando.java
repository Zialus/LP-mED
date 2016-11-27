package fcup;

public class Comando {
	public Type tipo;
	public Cursor cursor;
	public char caracter;

	Comando(Type tipo,Cursor cursor, char caracter ){
		this.tipo = tipo;
		this.cursor = cursor;
		this.caracter = caracter;
	}

}