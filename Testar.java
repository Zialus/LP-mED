
public class Testar {

	public static void main(String[] args) {

		Position cursor1 = new Position(4,5);
		Position cursor2 = new Position(1,5);
		Position cursor3 = new Position(4,1);
		Position cursor4 = new Position(5,1);
		
		Buffer buff_teste = new Buffer();
		
		buff_teste.setCursor(cursor1);
		buff_teste.setCursor(cursor2);
		buff_teste.setCursor(cursor3);
		buff_teste.setCursor(cursor4);

	}

}
