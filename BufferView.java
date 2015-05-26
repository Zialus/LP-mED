import java.util.ArrayList;
import java.util.LinkedList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class BufferView {
	private Terminal term = TerminalFacade.createTerminal();
	private FileBuffer fbuffer;
	private int width, height;
	private int startRow; // primeira linha logica que aparece na janela

	private ArrayList<Integer> modifiedLines = new ArrayList<Integer>();  // linhas alteradas
	


	public BufferView(FileBuffer fbuffer) {

		this.fbuffer = fbuffer;
	}


	public void TestTerm()
	{

		term.enterPrivateMode();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();

		for(int i = 0; i<height; i++){
			//System.out.println(i + " " + height);
			modifiedLines.add(i);
		}

		while (true){

			System.out.println("--------lalalalalalalal----------");
			
			for(int i = 0; i<height; i++){
				System.out.println(fbuffer.getNthLine(i));
			}
			
			
			redraw();

			Key k = term.readInput();
			if (k != null) {
				switch (k.getKind()) {
				case Escape:
					term.exitPrivateMode();
					return;
				case ArrowLeft: 
					fbuffer.movePrev();
					break;
				case ArrowRight:
					fbuffer.moveNext();
					break;
				case ArrowDown: 
					fbuffer.moveNextLine();
					break;
				case ArrowUp:
					fbuffer.movePrevLine();
					break;
				case Enter:
					//TODO inserir no modifiedLines todas as linhas para baixo.
					int temp = fbuffer.getCursor().getL();
					modifiedLines.add(temp);
					fbuffer.insertLn();
				case Backspace:
					break;
				case CursorLocation:
					break;
				case Delete:
					break;
				case EOF:
					break;
				case End:
					break;
				case F1:
					break;
				case F10:
					break;
				case F11:
					break;
				case F12:
					break;
				case F2:
					break;
				case F3:
					break;
				case F4:
					break;
				case F5:
					break;
				case F6:
					break;
				case F7:
					break;
				case F8:
					break;
				case F9:
					break;
				case Home:
					break;
				case Insert:
					break;
				case NormalKey:
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

			term.clearScreen();


			term.applySGR(Terminal.SGR.ENTER_BOLD);
			term.applyForegroundColor(Terminal.Color.RED);

			term.flush();

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}


	public void redraw() {
		for (Integer line : modifiedLines) {
			drawN(line);
		}
		modifiedLines.clear();  // feito
	}

	public void drawN(Integer line){
		int[] tmp = viewPos(line);
		int xInit = tmp[0];
		int nLines = tmp[1];
		int resto = tmp[2];
		
		
		System.out.println(xInit + " " + nLines + " " + resto);

		StringBuilder linha = fbuffer.getNthLine(line);

		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();

		term.moveCursor(xInit, 0);

		
		//imprimir ar varias linhas no ecrâ
		for (int i = 0; i < nLines; i++) {
			for (int j = 0; j < width; j++) {
				term.putCharacter(linha.charAt(j));
			}
			term.moveCursor(xInit++,0);
		}

		//imprimir ultima linha se necessario
		if (resto!=0){
			term.moveCursor(xInit++,0);
			for (int j = 0; j < resto; j++) {
				term.putCharacter(linha.charAt(j));
			}
		}

		//"limpar" cursor
		term.moveCursor(0,0);


	}

	public int[] viewPos(int row, int col) {

		System.out.println(width + " " + height);

		return null;
	}

	public int[] viewPos(Integer line){
		
		line++; // Indices começam em 0 mas linhas logicas começam em 1
		
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();

		int l = startRow; // linha logica que estamos a analisar
		int v = 0; // posição que a linha logica vai ter na janela
		int resto = 0;
		int divlinhas = 0;
		while( v<height &&  l<line ) {
			StringBuilder sb = fbuffer.getNthLine(l);
			int tamanho = sb.length();
			divlinhas = tamanho/width;
			resto = tamanho%width;
			if (divlinhas > 1) {v+=divlinhas;} //quantas linhas visuais sao necessarias para cada linha lógica 
			else{v++;}
			l++;
		}

		int[] vector = new int[3] ;
		vector[0] = v; // posicao que a linha logica vai ter na janela
		vector[1] = divlinhas; // quantas linhas essa linha logica vai ocupar na janela
		vector[2] = resto; // quantos caracteres sobram
		return vector;
	}

}