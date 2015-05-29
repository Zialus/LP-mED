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
	private int width, height;
	private int startRow; // primeira linha logica que aparece na janela
	private ArrayList<FileBuffer> bufferList = new ArrayList<FileBuffer>();  // Lista de Buffers
	private ArrayList<Integer> modifiedLines = new ArrayList<Integer>();  // linhas alteradas

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

		this.fbuffer = bufferList.get(0);
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
//					if(term.)
					//if(fbuffer.getCursor().getL() == fbuffer.getNumLines()-1){break;}
					
					fbuffer.moveNextLine();
					fbuffer.setModified(true);
					//System.out.println( fbuffer.getCursor().getL() + " " + fbuffer.getCursor().getC() );
					break;
				case ArrowUp:
					
					fbuffer.movePrevLine();
					fbuffer.setModified(true);
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

			//term.clearScreen();

			//term.applySGR(Terminal.SGR.ENTER_BOLD);
			//term.applyForegroundColor(Terminal.Color.RED);

			term.flush();

			try
			{
				Thread.sleep(10);
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
		modifiedLines.clear();  // feito

		term.moveCursor(fbuffer.getCursor().getC(), fbuffer.getCursor().getL());

		fbuffer.setModified(false);
	}

	public void drawN(int line){
		int[] tmp = viewPos(line);
		int initRow = tmp[0];
		int nLines = tmp[1];

		StringBuilder linha = fbuffer.getNthLine(line);

		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();

		term.moveCursor(0,initRow);

		int size = linha.length();

		//for(int j=0; j<nLines+1; j++){
		for (int i = 0; i < width; i++) {
			term.putCharacter(' ');
		}
		//}

		term.moveCursor(0,initRow);


		for (int i = 0; i < size; i++) {
			//System.out.print(linha.charAt(i));
			term.putCharacter(linha.charAt(i));
			if ( i>0 && ((i%width) == 0)) { initRow=initRow+1; term.moveCursor(0,initRow); }
		}




	}

	//	public int[] viewPos(int row, int col) {
	//
	//		System.out.println(width + " " + height);
	//
	//		return null;
	//	}

	public int[] viewPos(int line){

		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();

		int row = startRow; // linha logica inicial
		int vis = 0; //  linha visual 
		int resto = 0;
		int divlinhas = 0;
		while( vis<height &&  row<line ) {
			StringBuilder sb = fbuffer.getNthLine(row);
			int tamanho = sb.length(); 

			divlinhas = tamanho/width ;
			resto = tamanho%width; 
			//System.out.println("TAMANHO: " + tamanho + "width: " + width );
			if (divlinhas > 1) {vis+=divlinhas;} //quantas linhas visuais sao necessarias para cada linha lógica 
			else{vis++;}
			row++;
		}

		int[] vector = new int[3] ;
		vector[0] = vis; // posicao que a linha logica vai ter na janela
		vector[1] = divlinhas; // quantas linhas essa linha logica vai ocupar na janela
		vector[2] = resto; // quantos caracteres sobram
		return vector;
	}

}