import java.util.LinkedList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class BufferView {
	private Terminal term = TerminalFacade.createTerminal();
	private FileBuffer fbuffer; // ou FileBuffer
	private int width, height;
	private int startRow; // linha início da janela

	private LinkedList<Integer> modifiedLines;  // linhas alteradas

	public BufferView(FileBuffer fbuffer) {
		this.fbuffer = fbuffer;
	}


	public void TestTerm()
	{
		term.enterPrivateMode();

		while (true){
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
				Thread.sleep(200);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}

	public void predraw(){
		term.enterPrivateMode();
		TerminalSize tamanhoterminal = term.getTerminalSize();
		width = tamanhoterminal.getColumns();
		height = tamanhoterminal.getRows();

		int totalLines = fbuffer.getNumLines();
		int remLines = totalLines - startRow; // 

		int i = startRow;
		while( i<remLines &&  i<(startRow + height) ) {
			StringBuilder sb = fbuffer.getNthLine(i);
			int tamanho = sb.length();

			int divlinhas = tamanho/width; //quantas linhas visuais sao necessarias para cada linha lógica

		}


	}

	//	public divideLinhas


	public int[] viewPos(int row, int col) {



		System.out.println(width + " " + height);

		return null;
	}





	public void redraw() {
		for (Integer line : modifiedLines) {
			// desenhar a linha
		}
		modifiedLines.clear();  // feito
	}



}