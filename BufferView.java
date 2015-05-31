import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class BufferView {
	private Terminal term;
	private FileBuffer fbuffer;
	private int currentBuffer;
	private int width, height;
	private int startRow; // primeira linha logica que aparece na janela
	private ArrayList<FileBuffer> bufferList = new ArrayList<FileBuffer>();  // Lista de Buffers
	private ArrayList<Integer> modifiedLines = new ArrayList<Integer>();  // linhas alteradas
	private int linha,coluna; // linha e coluna visual do cursor
	
	
	public BufferView(FileBuffer fbuffer) {
		term = TerminalFacade.createTerminal();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();
		//term = new SwingTerminal(30,40);

		this.fbuffer = fbuffer;
	}

	public BufferView(ArrayList<FileBuffer> bufferList) {
		term = TerminalFacade.createTerminal();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();
		//term = new SwingTerminal(30,40);

		this.bufferList = bufferList;
		this.fbuffer = bufferList.get(currentBuffer);

	}

	public void refreshAfterLine(int line){
		for(int i = line; (i<line+height) && (i < fbuffer.getNumLines() ); i++){
			if(i!=-1){modifiedLines.add(i);}
		}

	}

	public void StartTerm() throws IOException
	{
		term.enterPrivateMode();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();

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
					
					if(linha==height-1){startRow += 10;}
					refreshAfterLine(startRow);
					
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case ArrowUp:
					fbuffer.movePrevLine();
					fbuffer.setModified(true);
					
					if(linha==0 && startRow != 0){startRow -= 10;}
					refreshAfterLine(startRow);
					
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case Enter:
					int linhaActual1 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de inserir nova linha
					fbuffer.insertLn(); // Inserir nova linha
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					fbuffer.setModified(true);
					refreshAfterLine(linhaActual1);
					break;
				case Backspace:
					int linhaActual2 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de apagar "caracter"
					fbuffer.deleteChar(); // Apagar esse "caracter"
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					fbuffer.setModified(true);
					refreshAfterLine(linhaActual2-1);
					break;
				case CursorLocation:
					break;
				case Delete:
					break;
				case EOF:
					break;
				case End:
					break;
				case Home:
					break;
				case Insert:
					break;
				case NormalKey:
					if(k.isCtrlPressed()){
						System.out.println("ENTROU NO CONTROL");
						if(k.getCharacter() == 's'){
							fbuffer.setModified(true);
							System.out.println("ENTROU NO SAVE------------");
							fbuffer.save();
							System.out.println("ENTROU NO SAVE");
						}

						if(k.getCharacter() == 'b'){
							System.out.println("next buffer");
							
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
						if(k.getCharacter() == 'b'){
							fbuffer.setModified(true);
							System.out.println("prev buffer");
							fbuffer = bufferList.get(currentBuffer);
							System.out.println("prev DID IT");
						}
					}


					else{
						int linhaActual3 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de apagar "caracter"
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
		
		modifiedLines.clear();  // limpar lista de linhas modificadas uma vez que estas ja foram imprimidas

		int cursorL = fbuffer.getCursor().getL();
		int cursorC = fbuffer.getCursor().getC();
		
		System.out.println("posicoes logicas: " + cursorL + "," + cursorC);
		
		int[] tmp = viewPos(cursorL,cursorC);
		coluna = Math.min(tmp[2],cursorC);
		linha = tmp[0];
		
		System.out.println("posicoes visuais: " + coluna + "," + linha);
		term.moveCursor(coluna,linha);
		
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

		term.moveCursor(0,initRow);


		for (int i = 0; i < size; i++) {
			//System.out.print(linha.charAt(i));
			term.putCharacter(linha.charAt(i));
			if ( i>0 && ((i%width) == 0)) { initRow=initRow+1; term.moveCursor(0,initRow); }
		}




	}

	public int[] viewPos(int line,int col){

		int row = startRow; // linha logica inicial a considerar
		int vis = 0;        // linha visual inicial a considerar
		int r = 0;          // quantidade de caracteres na ultima linha
		int q = 0;          // quantidade de linhas visuais completas que uma linha logica ocupa

		if (line == 0){	
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 
			q = tamanho/width ;
			r = tamanho%width ;
			System.out.println("tamanho linha 0: " + tamanho);

		}

		while( vis<height &&  row<line ) {
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 

			q = tamanho/width ;
			System.out.println("tamanho normal: " + tamanho);
			r = tamanho%width; 
			if(r==0) {vis+= Math.max(q,1);}
			else {vis += q+1;}
			row++;
		}
		
		StringBuilder sb = fbuffer.getNthLine(line);
		int tamanho = sb.length(); 
		r = tamanho%width; 

		if (vis==30){
			int[] vector = new int[3] ;
			vector[0] = -20;
			return vector;
		}

		System.out.println("line " + line + " v: " + vis + " r: " + r);			
		int[] vector = new int[3] ;
		vector[0] = vis; // posicao que a linha logica vai ter na janela
//		vector[1] = q; // quantas linhas essa linha logica vai ocupar na janela
		vector[2] = r; // quantos caracteres sobram
		return vector;
	}

}