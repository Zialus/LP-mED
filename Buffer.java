import java.util.LinkedList;


public class Buffer {

	LinkedList<StringBuilder> LineList = new LinkedList<StringBuilder>();
	Position currentCursor;
	
	//Construir um buffer vazio
	public Buffer() {
		StringBuilder vazia = new StringBuilder();
		LineList.add(vazia);
		currentCursor = new Position(0,0);
	}
	
	//Contruir um buffer já com uma linha
	public Buffer(StringBuilder linha) {
		LineList.add(linha);
		StringBuilder vazia = new StringBuilder();
		LineList.add(vazia);
		currentCursor = new Position(0,0);
	}
	
	boolean validPosition(Position newCursor){
		
		int x = newCursor.getX();
		int y = newCursor.getY();
		
		return( x>=0 && x<LineList.size() && y >= 0 && y<= LineList.get(x).length() );
	}
	

	void setCursor(Position newCursor){
		
		int x = newCursor.getX();
		int y = newCursor.getY();
		
		if ( validPosition(newCursor)){
			currentCursor.setX(x);
			currentCursor.setY(y);
		}
		else{
			System.out.println("Buffer.setCursor : Invalid Position" + "x :" + x + " y: " + y);
		}
	}
	
	void movePrev() {
		
		int x = currentCursor.getX();
		int y = currentCursor.getY();
		
		if ( currentCursor.getY() > 0){ currentCursor.setY(y-1); }
		else if( currentCursor.getX() > 0){ currentCursor = new Position(x-1, LineList.get(x-1).length()); }
		else {
			System.out.println("Already at the begining");
		}
		
		
		
		//LineList.get(x).length()	
	
	}
	
	void moveNext(){
		
	}
	
}
