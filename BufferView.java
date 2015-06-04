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
	private int currentBuffer;                           					 // Indice do Buffer que está a ser editado neste momento
	private int width, height;               								 // Altura e largura da janela com o terminal
	private int startRow;                   								 // Primeira linha logica que aparece na janela
	private int lastRow;                                                     // Ultima linha logica que aparece na janela
	private ArrayList<FileBuffer> bufferList = new ArrayList<FileBuffer>();  // Lista com os varios Buffers
	private ArrayList<Integer> modifiedLines = new ArrayList<Integer>();     // Lista com as linhas alteradas
	private int cursorLine,cursorRow;                        				 // Linha e coluna visual do cursor
	

	// Constuir um BufferView só com um buffer
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
						startRow = Math.max(startRow-10, 0); 
						refreshAfterLine(startRow);
					}

					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case Enter:
					int linhaActual1 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de inserir nova linha
					fbuffer.insertLn(); // Inserir nova linha

					if(cursorLine==height-1){
						Math.min(startRow+=10,lastRow);
					}

					Comando commandE = new Comando(Type.InsertChar, fbuffer.getCursor(),' ');

					fbuffer.commandList.push(commandE);	


					fbuffer.setModified(true);
					refreshAfterLine(startRow);
					break;
				case Backspace:
					if(cursorLine==0 && startRow != 0){ 
						startRow = Math.max(startRow-10, 0); 
						refreshAfterLine(startRow);
					}


					int linhaActual2 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de apagar "caracter"

					int cursorLinha  = fbuffer.getCursor().getL();
					int cursorColuna = fbuffer.getCursor().getC();
					StringBuilder tmp = fbuffer.getNthLine(cursorLinha);

					if(cursorColuna>0){ // Se a apagar alguma coisa na linha, mas a linha continuar "viva"
						char c = tmp.charAt(cursorColuna-1);
						fbuffer.deleteChar(); // Apagar esse "caracter"
						Comando commandB = new Comando(Type.DeleteChar, fbuffer.getCursor(),c);
						fbuffer.commandList.push(commandB);	
					}

					else{ // Se estiver mesmo a apagar a linha em si
						char c = ' ';
						fbuffer.deleteChar(); // Apagar esse "caracter"
						Comando commandB = new Comando(Type.DeleteLine, fbuffer.getCursor(),c);
						fbuffer.commandList.push(commandB);	
					}


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


						// CONTROL-Z  (DESFAZER ULTIMA ACÇÃO)
						if(k.getCharacter() == 'z'){

							if (!fbuffer.commandList.empty()) {
								Comando command = fbuffer.commandList.pop(); // ir buscar ultimo comando guardado

								fbuffer.setCursor(command.cursor);
								int linhaActual99 = fbuffer.getCursor().getL();

								switch (command.tipo){
								case InsertChar:
									fbuffer.deleteChar();
									break;
								case DeleteChar:
									fbuffer.insertChar(command.caracter);
									break;
								case InsertLn:
									fbuffer.deleteChar(); 
									break;
								case DeleteLine:
									fbuffer.insertLn();
									break;
								default:
									break;
								}

								fbuffer.setModified(true);


								if(startRow > linhaActual99){
									startRow = Math.max(linhaActual99-10, 0);
									refreshAfterLine(startRow);
								}

								if(linhaActual99 > lastRow){
									startRow = Math.min(linhaActual99+10,fbuffer.getNumLines()-1);
									refreshAfterLine(startRow);
								}

								else {refreshAfterLine(linhaActual99-1);}



							}
							else {
								System.out.println("Nada a disfazer!");
							}


						}


					}

					else if(k.isAltPressed() ){
						System.out.println("ENTROU NO ALT");
						if(k.getCharacter() == 'b'){
							System.out.println("prev buffer");
							int sizeCircList = bufferList.size();
							currentBuffer = (currentBuffer-1);
							if (currentBuffer == -1) { currentBuffer = sizeCircList-1;}
							fbuffer = bufferList.get(currentBuffer);
							fbuffer.setModified(true);
							term.clearScreen();
							//							System.out.println("prev DID IT");
							refreshAfterLine(0);
						}
					}

					//Inserir caracter "normal"
					else{
						int linhaActual3 = fbuffer.getCursor().getL(); 
						fbuffer.insertChar( k.getCharacter() );
						fbuffer.setModified(true);
						modifiedLines.add(linhaActual3);

						Comando commandI = new Comando(Type.InsertChar, fbuffer.getCursor(),' ');

						fbuffer.commandList.push(commandI);	

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
			if (line>=0) {
				//System.out.println("linha: " + line + " starRow: " + startRow);
				drawN(line.intValue());}
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

		int linhaSize = linha.length();

		//Apagar lixo
		for(int j=0; j<nLines+1; j++){
			for (int i = 0; i < width; i++) {
				term.putCharacter(' ');
			}
		}

		//Apagar linhas que ja nao existem
		if ( fbuffer.getNumLines()-1 == line ){
			for(int j=initRow; j<lastRow; j++){
				for (int i = 0; i < width; i++) {
					term.putCharacter(' ');
				}
			}
		}

		term.moveCursor(0,initRow);

		for (int i = 0; i < linhaSize; i++) {
			System.out.print(linha.charAt(i));
			if ( i>0 && ((i%width) == 0)) { 
				System.out.println("!!mudei!!");
				initRow++; term.moveCursor(0,initRow); }
			term.putCharacter(linha.charAt(i));

		}





	}

	public int[] viewPos(int line,int col){
		int[] vector = new int[3] ;
		int row = startRow; // Linha logica inicial a considerar
		int vis = 0;        // Linha visual inicial a considerar	
		int r = 0;          // Quantidade de caracteres na ultima linha
		int q = 0;          // Quantidade de linhas visuais completas que uma linha logica ocupa

		if (line == row){	
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 
			q = tamanho/width ;
			r = tamanho%width ;
			//System.out.println("tamanho logico da linha 0 da janela: " + tamanho);
		}


		while( vis<height &&  row<line ) {
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 

			q = tamanho/width ;
			//System.out.println("tamanho logico da linha: " + row + " " + tamanho);
			r = tamanho%width; 
			if(r==0) {vis+= Math.max(q,1);}
			else {vis += q+1;}
			row++;
		}

		StringBuilder sb = fbuffer.getNthLine(line);
		int tamanho = sb.length(); 
		r = tamanho%width; 
		q = tamanho/width ;

		if(vis==height-1){
			lastRow = line;
		}

		if (vis==height){
			vector[0] = -20;
			return vector;
		}


		//System.out.println("line " + line + " v: " + vis + " r: " + r + " q " + q);			
		vector[0] = vis; // posicao que a linha logica vai ter na janela
		vector[1] = q; // quantas linhas essa linha logica vai ocupar na janela

		if(col==0){
			vector[2] = col;
		}

		else if(col==width){
			vector[2] = col%width; // quantos caracteres sobram (not really)
			vector[0] = vis+col/width;
		}

		else if(col%width==0){
			vector[2] = 0;
			vector[0] = vis+(col/width);
		}

		else if(col>width){
			vector[2] = col%width ; // quantos caracteres sobram (not really)
			vector[0] = vis+col/width;
		}

		else if(col<width){
			vector[2] = col%width; // quantos caracteres sobram
			vector[0] = vis;
		}

		return vector;
	}
}