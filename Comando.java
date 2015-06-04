public class Comando {
	private Type tipo;
	private Cursor cursor;
	private char caracter;
	
	Comando(Type tipo,Cursor cursor, char caracter ){
		this.tipo = tipo;
		this.cursor = cursor;
		this.caracter = caracter;
	}
}