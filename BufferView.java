import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class BufferView {
	private Terminal term;
	private FileBuffer fbuffer;												 // FileBuffer associado ao terminal neste momento
	private int currentBuffer;                           					 // Indice do Buffer que esta a ser editado neste momento
	private int width, height;               								 // Altura e largura da janela com o terminal
	private int startRow;                   								 // Primeira linha logica que aparece na janela
	private ArrayList<FileBuffer> bufferList = new ArrayList<FileBuffer>();  // Lista com os varios Buffers
	private ArrayList<Integer> modifiedLines = new ArrayList<Integer>();     // Lista com as linhas alteradas
	private int cursorLine,cursorRow;                        				 // Linha e coluna visual do cursor
	private Stack<Comando> actionsList = new Stack<Comando>();
	
    // Constuir um BufferView so com um buffer
	public BufferView(FileBuffer fbuffer) {
		term = TerminalFacade.createTerminal();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();
		this.fbuffer = fbuffer;
	}

	// Constuir um BufferView com multiplos buffers
	public BufferView(ArrayList<FileBuffer> bufferList) {
		term = TerminalFacade.createTerminal();
		TerminalSize sizeTerm = term.getTerminalSize();
		width = sizeTerm.getColumns();
		height = sizeTerm.getRows();
		this.bufferList = bufferList;
		this.fbuffer = bufferList.get(0); 			// Usar o primeiro buffer da lista de buffers como "default"
	}

	public void refreshAfterLine(int line) {
		for(int i = line; (i<line+height) && (i < fbuffer.getNumLines() ); i++){
			if(i!=-1){modifiedLines.add(i);}
		}
	}

	public void StartTerm() throws IOException {
		term.enterPrivateMode();

		refreshAfterLine(0);

		while (true){

			if (fbuffer.getModified() == true){ redraw();}

			Key k = term.readInput();
			if (k != null) {
				switch (k.getKind()) {
				case Escape:
					term.exitPrivateMode();
					return;
				case ArrowLeft: 
					fbuffer.movePrev();
					fbuffer.setModified(true);
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case ArrowRight:
					fbuffer.moveNext();
					fbuffer.setModified(true);
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case ArrowDown:
					fbuffer.moveNextLine();
					fbuffer.setModified(true);

					if(cursorLine==height-1){
						startRow += 10; 
						term.clearScreen(); 
						refreshAfterLine(startRow);
					}

					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case ArrowUp:
					fbuffer.movePrevLine();
					fbuffer.setModified(true);

					if(cursorLine==0 && startRow != 0){ 
						startRow -= 10; 
						refreshAfterLine(startRow);
					}

					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case Enter:
					int linhaActual1 = fbuffer.getCursor().getL(); // Linha onde esta o cursor antes de inserir nova linha
					fbuffer.insertLn(); // Inserir nova linha
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					fbuffer.setModified(true);
					refreshAfterLine(linhaActual1);
					break;
				case Backspace:
					int linhaActual2 = fbuffer.getCursor().getL(); // Linha onde esta o cursor antes de apagar "caracter"
					fbuffer.deleteChar(); // Apagar esse "caracter"
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					fbuffer.setModified(true);
					refreshAfterLine(linhaActual2-1);
					break;
				case CursorLocation:
					break;
				case Delete:
					break;
				case End:
					fbuffer.moveEndLine();
					fbuffer.setModified(true);
					break;
				case Home:
					fbuffer.moveStartLine();
					fbuffer.setModified(true);
					break;
				case NormalKey:
					if(k.isCtrlPressed()){
						System.out.println("ENTROU NO CONTROL");

						if(k.getCharacter() == 's'){
							System.out.println("ENTROU NO SAVE");
							fbuffer.setModified(true);
							fbuffer.save();
							System.out.println("FEZ SAVE");
						}

						if(k.getCharacter() == 'b'){
							System.out.println("ENTROU NO next buffer");

							int sizeCircList = bufferList.size();
							currentBuffer = (currentBuffer+1)%sizeCircList;
							fbuffer = bufferList.get(currentBuffer);
							fbuffer.setModified(true);
							term.clearScreen();
							System.out.println("next DID IT");
							refreshAfterLine(0);
						}

					}

					else if(k.isAltPressed() ){
						System.out.println("ENTROU NO ALT");
						if(k.getCharacter() == 'b'){
							System.out.println("prev buffer");
							fbuffer.setModified(true);

							int sizeCircList = bufferList.size();
							currentBuffer = (currentBuffer-1);
							if (currentBuffer == 0) { currentBuffer = sizeCircList-1;}


							fbuffer = bufferList.get(currentBuffer);
							System.out.println("prev DID IT");
						}
					}


					else{
						int linhaActual3 = fbuffer.getCursor().getL(); // Linha onde esta o cursor antes de apagar "caracter"
						fbuffer.insertChar( k.getCharacter() );
						fbuffer.setModified(true);
						modifiedLines.add(linhaActual3);
					}
					break;
				case PageDown:
					break;
				case PageUp:
					break;
				case ReverseTab:
					break;
				case Tab:
					break;
				case Unknown:
					break;
				default:
					break;

				}


			}


			term.flush();

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}


	public void redraw() {

		System.out.println(Arrays.toString(modifiedLines.toArray()));

		for (Integer line : modifiedLines) {
			drawN(line.intValue());
		}

		modifiedLines.clear();  // Limpar lista de linhas modificadas uma vez que estas ja foram imprimidas

		int cursorL = fbuffer.getCursor().getL();
		int cursorC = fbuffer.getCursor().getC();

		System.out.println("posicoes logicas do cursor: " + cursorL + "," + cursorC);

		int[] pos = viewPos(cursorL,cursorC);
		int line = pos[0]; int row = pos[2];
		cursorRow = row;
		cursorLine = line;

		System.out.println("posicoes visuais do cursor: " + cursorLine + "," + cursorRow);
		term.moveCursor(cursorRow,cursorLine);

		fbuffer.setModified(false);
	}

	public void drawN(int line){
		int[] tmp = viewPos(line,0);
		int initRow = tmp[0];
		int nLines = tmp[1];

		if (initRow == -20){
			return;
		}

		StringBuilder linha = fbuffer.getNthLine(line);

		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();

		term.moveCursor(0,initRow);

		int size = linha.length();

		for(int j=0; j<nLines+1; j++){
			for (int i = 0; i < width; i++) {
				term.putCharacter(' ');
			}
		}

		if ( fbuffer.getNumLines()-1 == line ) ;
		{
			for(int j=initRow; j<height; j++){
				for (int i = 0; i < width; i++) {
					term.putCharacter(' ');
				}
			}
		}

		term.moveCursor(0,initRow);


		for (int i = 0; i < size; i++) {
			//System.out.print(linha.charAt(i));
			term.putCharacter(linha.charAt(i));
			if ( i>0 && ((i%width) == 0)) { initRow=initRow+1; term.moveCursor(0,initRow); }
		}





	}

	public int[] viewPos(int line,int col){

		int row = startRow; // Linha logica inicial a considerar
		int vis = 0;        // Linha visual inicial a considerar	
		int r = 0;          // Quantidade de caracteres na ultima linha
		int q = 0;          // Quantidade de linhas visuais completas que uma linha logica ocupa

		if (line == row){	
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 
			q = tamanho/width ;
			r = tamanho%width ;
			System.out.println("tamanho logico da linha 0 da janela: " + tamanho);
		}


		while( vis<height &&  row<line ) {
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 

			q = tamanho/width ;
			System.out.println("tamanho logico da linha: " + row + " " + tamanho);
			r = tamanho%width; 
			if(r==0) {vis+= Math.max(q,1);}
			else {vis += q+1;}
			row++;
		}

		StringBuilder sb = fbuffer.getNthLine(line);
		int tamanho = sb.length(); 
		r = tamanho%width; 
		q = tamanho/width ;

		if (vis==height){
			int[] vector = new int[3] ;
			vector[0] = -20;
			return vector;
		}

		
		System.out.println("line " + line + " v: " + vis + " r: " + r + " q " + q);			
		int[] vector = new int[3] ;
		vector[0] = vis; // posicao que a linha logica vai ter na janela
		vector[1] = q; // quantas linhas essa linha logica vai ocupar na janela

		if(col==0){
			vector[2] = col;
		}

		else if(col>0 && col%width==0){
			System.out.println(width);
			vector[2] = width-1 ;
			//System.out.println("q: " + q);			
			vector[0] = vis+(col/width)-1;
		}

		else if(col>0 && col>width){
			vector[2] = col%width -1 ; // quantos caracteres sobram (not really) ( se houver um caracter fico na posicao 0)
			//System.out.println("q: " + q);			
			vector[0] = vis+col/width;
		}

		else if(col>0 && col<width){
			vector[2] = col%width; // quantos caracteres sobram
			vector[0] = vis;
		}
		
		return vector;
	}
}
