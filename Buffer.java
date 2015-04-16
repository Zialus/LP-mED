import java.util.LinkedList;

public class Buffer {

	LinkedList<StringBuilder> LineList = new LinkedList<StringBuilder>();
	Cursor currentCursor;

	//Construir um buffer vazio
	public Buffer() {
		StringBuilder vazia = new StringBuilder();
		LineList.add(vazia);
		currentCursor = new Cursor(0,0);
	}

	//Contruir um buffer já com uma linha
	public Buffer(String linhatemp) {
		StringBuilder linha = new StringBuilder(linhatemp);
		LineList.add(linha);
		StringBuilder vazia = new StringBuilder();
		LineList.add(vazia);
		currentCursor = new Cursor(0,0);
	}
	
	int getNumLines(){
		return LineList.size();
	}

	StringBuilder getNthLine(int i){
		return LineList.get(i);
	}

	LinkedList<StringBuilder> getAllLines(){
		return LineList;
	}

	//--------------------------------------------------------------//
	//-------------funções para obter e mover o cursor--------------//
	//--------------------------------------------------------------//
	
	//Verificar se a posição do buffer é valida
	boolean validPosition(Cursor newCursor){

		int l = newCursor.getL();
		int c = newCursor.getC();

		return( l>=0 && l<LineList.size() && c >= 0 && c<= LineList.get(l).length() );
	}

	Cursor getCursor(){
		return currentCursor;
	}

	void setCursor(Cursor newCursor){

		int l = newCursor.getL();
		int c = newCursor.getC();

		if ( validPosition(newCursor)){
			currentCursor.setL(l);
			currentCursor.setC(c);
		}else{
			System.out.println("Buffer.setCursor - Invalid Position -" + " x : " + l + " y: " + c);
		}
	}

	void movePrev() {

		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if ( c > 0){
			currentCursor.setC(c-1);
		}
		else if( l > 0){
			currentCursor = new Cursor(l-1, LineList.get(l-1).length()); 
		}
		else{
			System.out.println("Already at the begining");
		}

	}

	void moveNext(){

		int l = currentCursor.getL();
		int c = currentCursor.getC();

		int len = LineList.get(l).length();

		if ( c < len ){
			currentCursor.setC(c+1);
		}
		else if( l < getNumLines()){
			currentCursor = new Cursor(l+1, 0); 
		}
		else{
			System.out.println("Already at the end");
		}


	}

	void movePrevLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(l>0){
			currentCursor = new Cursor(l-1, Math.min(c, LineList.get(l-1).length() ));			
		}
		else{
			System.out.println("Already at the first line of the buffer");
		}
	}

	void moveNextLine(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();

		if(l<LineList.size()){
			currentCursor = new Cursor(l+1, Math.min(c, LineList.get(l+1).length() ));			
		}
		else{
			System.out.println("Already at the last line of the buffer");
		}
	}

	void insertStr(String linha){
		int l = currentCursor.getL();
		int c = currentCursor.getC();
		
		StringBuilder sb = LineList.get(l);
		sb.insert(c, linha);
		currentCursor = new Cursor(l, c+linha.length());
	}
	
	void insertLn(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();
		
		StringBuilder sb = LineList.get(l);
		String st1 = sb.substring(0, c);
		String st2 = sb.substring(c+1, sb.length());
		StringBuilder sb1 = new StringBuilder(st1);
		StringBuilder sb2 = new StringBuilder(st2);
		LineList.set(l, sb1);
		LineList.add(l+1, sb2);
	}
	void insertChar(char c){
		if(c=='\n'){
			insertLn();
		}
		else{
			insertStr(Character.toString(c));
		}
	}
	
	
	
	void deleteChar(){
		int l = currentCursor.getL();
		int c = currentCursor.getC();
		
		if(c>0){
			StringBuilder sb = LineList.get(l);
			sb.deleteCharAt(c);
			currentCursor = new Cursor(l, c-1);
		}
		else if(l>0){
			deleteLn();
		}
		else{
			System.out.println("Already at the beginning of the buffer");
		}
		
	}

	private void deleteLn() {
		int l = currentCursor.getL();
		
		LineList.remove(l);
		currentCursor = new Cursor(l-1, LineList.get(l-1).length());
		
	}
}
