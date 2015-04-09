import java.util.LinkedList;


public class Buffer {

	LinkedList<StringBuilder> LineList = new LinkedList<StringBuilder>();
	Position cursor;
	
	//Construir um buffer vazio
	public Buffer() {
		StringBuilder vazia = new StringBuilder();
		vazia.append("");
		LineList.add(vazia);
		cursor = new Position(0,0);
	}
	
	//Contruir um buffer já com uma linha
	public Buffer(StringBuilder linha) {
		LineList.add(linha);
		StringBuilder vazia = new StringBuilder();
		vazia.append("");
		LineList.add(vazia);
		cursor = new Position(0,0);
	}
	
	
	
	
	
	void movePrev() {
	}
	
	
}
