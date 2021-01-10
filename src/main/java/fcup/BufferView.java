package fcup;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log
public class BufferView {
    private Terminal term;
    private FileBuffer fbuffer;                                   // FileBuffer associado ao terminal neste momento
    private int currentBuffer;                                    // Indice do Buffer que está a ser editado neste momento
    // Altura e largura da janela com o terminal
    private int width;
    private int height;
    private List<FileBuffer> bufferList = new ArrayList<>(); // Lista com os varios Buffers
    private List<Integer> modifiedLines = new ArrayList<>(); // Lista com as linhas alteradas
    // Linha e coluna visual do cursor
    private int cursorLine;
    private int cursorRow;


    // Constuir um BufferView só com um buffer
    public BufferView(FileBuffer fbuffer) throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        term = defaultTerminalFactory.createTerminal();
        TerminalSize tamanhoterminal = term.getTerminalSize();
        width = tamanhoterminal.getColumns();
        height = tamanhoterminal.getRows();
        this.fbuffer = fbuffer;
    }

    // Constuir um BufferView com multiplos buffers
    public BufferView(List<FileBuffer> bufferList) throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        term = defaultTerminalFactory.createTerminal();
        TerminalSize sizeTerm = term.getTerminalSize();
        width = sizeTerm.getColumns();
        height = sizeTerm.getRows();
        this.bufferList = bufferList;
        this.fbuffer = bufferList.get(0); 			// Usar o primeiro buffer da lista de buffers como "default"
    }

    private void refreshAfterLine(int line) {
        for(int i = line; (i<line+height) && (i < fbuffer.getNumLines() ); i++){
            if(i!=-1){modifiedLines.add(i);}
        }
    }

    public void startTerm() throws IOException {
        term.enterPrivateMode();

        refreshAfterLine(0);

        while (true){

            if (fbuffer.isModified()){ redraw();}

            KeyStroke k = term.pollInput();
            if (k != null) {
                switch (k.getKeyType()) {
                    case Escape:
                        this.endTerm();
                        return;
                    case ArrowLeft:
                        fbuffer.movePrev();
                        fbuffer.setModified(true);
                        break;
                    case ArrowRight:
                        fbuffer.moveNext();
                        fbuffer.setModified(true);
                        break;
                    case ArrowDown:
                        fbuffer.moveNextLine();
                        fbuffer.setModified(true);

                        if(cursorLine==height-1){
                            fbuffer.setStartRow(fbuffer.getStartRow() + 10);
                            term.clearScreen();
                            refreshAfterLine(fbuffer.getStartRow());
                        }

                        break;
                    case ArrowUp:
                        fbuffer.movePrevLine();
                        fbuffer.setModified(true);

                        if(cursorLine==0 && fbuffer.getStartRow() != 0){
                            fbuffer.setStartRow(Math.max(fbuffer.getStartRow()-10, 0));
                            refreshAfterLine(fbuffer.getStartRow());
                        }

                        break;
                    case Enter:
                        fbuffer.insertLn(); // Inserir nova linha

                        if(cursorLine==height-1){
                            fbuffer.setStartRow(Math.min(fbuffer.getStartRow()+10, fbuffer.getLastRow()));
                        }

                        Command commandE = new Command(Type.INSERT_CHAR, fbuffer.getCursor(),' ');

                        fbuffer.getCommandList().addFirst(commandE);


                        fbuffer.setModified(true);
                        refreshAfterLine(fbuffer.getStartRow());
                        break;
                    case Backspace:
                        if(cursorLine==0 && fbuffer.getStartRow() != 0){
                            fbuffer.setStartRow(Math.max(fbuffer.getStartRow()-10, 0));
                        }


                        int linhaActual2 = fbuffer.getCursor().getL(); // Linha onde está o cursor antes de apagar "caracter"

                        int cursorLinha  = fbuffer.getCursor().getL();
                        int cursorColuna = fbuffer.getCursor().getC();
                        StringBuilder tmp = fbuffer.getNthLine(cursorLinha);

                        if(cursorColuna>0){ // Se a apagar alguma coisa na linha, mas a linha continuar "viva"
                            char c = tmp.charAt(cursorColuna-1);
                            fbuffer.deleteChar(); // Apagar esse "caracter"
                            Command commandB = new Command(Type.DELETE_CHAR, fbuffer.getCursor(),c);
                            fbuffer.getCommandList().addFirst(commandB);
                        }

                        else{ // Se estiver mesmo a apagar a linha em si
                            char c = ' ';
                            fbuffer.deleteChar(); // Apagar esse "caracter"
                            Command commandB = new Command(Type.DELETE_LINE, fbuffer.getCursor(),c);
                            fbuffer.getCommandList().addFirst(commandB);
                        }


                        fbuffer.setModified(true);
                        refreshAfterLine(Math.min(linhaActual2-1, fbuffer.getStartRow()));

                        break;
                    case End:
                        fbuffer.moveEndLine();
                        fbuffer.setModified(true);
                        break;
                    case Home:
                        fbuffer.moveStartLine();
                        fbuffer.setModified(true);
                        break;
                    case Character:
                        if(k.isCtrlDown()){
                            log.info("ENTROU NO CONTROL");

                            if(k.getCharacter() == 's'){
                                log.info("ENTROU NO SAVE");
                                fbuffer.setModified(true);
                                fbuffer.save();
                                log.info("FEZ SAVE");
                            }

                            if(k.getCharacter() == 'b'){
                                log.info("ENTROU NO NEXT BUFFER");
                                int sizeCircList = bufferList.size();
                                currentBuffer = (currentBuffer+1)%sizeCircList;
                                fbuffer = bufferList.get(currentBuffer);
                                fbuffer.setModified(true);
                                term.clearScreen();
                                log.info("Movi-me para o next Buffer");
                                refreshAfterLine(fbuffer.getStartRow());
                            }


                            // CONTROL-Z  (DESFAZER ULTIMA ACÇÃO)
                            if(k.getCharacter() == 'z'){
                                log.info("ENTROU NO UNDO");
                                if (!fbuffer.getCommandList().isEmpty()) {
                                    Command command = fbuffer.getCommandList().removeFirst(); // ir buscar ultimo comando guardado

                                    fbuffer.setCursor(command.cursor);
                                    int linhaActual99 = fbuffer.getCursor().getL();

                                    switch (command.tipo){
                                        case INSERT_CHAR:
                                            fbuffer.deleteChar();
                                            break;
                                        case DELETE_CHAR:
                                            fbuffer.insertChar(command.caracter);
                                            break;
                                        case INSERT_LN:
                                            fbuffer.deleteChar();
                                            break;
                                        case DELETE_LINE:
                                            fbuffer.insertLn();
                                            break;
                                    }

                                    fbuffer.setModified(true);


                                    if(fbuffer.getStartRow() > linhaActual99){
                                        fbuffer.setStartRow(Math.max(linhaActual99-10, 0));
                                        refreshAfterLine(fbuffer.getStartRow());
                                    }

                                    if(linhaActual99 > fbuffer.getLastRow()){
                                        fbuffer.setStartRow(Math.min(linhaActual99+10,fbuffer.getNumLines()-1));
                                        refreshAfterLine(fbuffer.getStartRow());
                                    }

                                    else {refreshAfterLine(linhaActual99-1);}



                                }
                                else {
                                    log.info("Nada a disfazer!");
                                }


                            }


                        }

                        else if(k.isAltDown() ){
                            log.info("ENTROU NO ALT");
                            if(k.getCharacter() == 'b'){
                                log.info("prev buffer");
                                int sizeCircList = bufferList.size();
                                currentBuffer = (currentBuffer-1);
                                if (currentBuffer == -1) { currentBuffer = sizeCircList-1;}
                                fbuffer = bufferList.get(currentBuffer);
                                fbuffer.setModified(true);
                                term.clearScreen();
                                log.info("Movi-me para o prev Buffer");
                                refreshAfterLine(fbuffer.getStartRow());
                            }
                        }

                        //Inserir caracter "normal"
                        else{
                            int linhaActual3 = fbuffer.getCursor().getL();
                            fbuffer.insertChar( k.getCharacter() );
                            fbuffer.setModified(true);
                            modifiedLines.add(linhaActual3);

                            Command commandI = new Command(Type.INSERT_CHAR, fbuffer.getCursor(),' ');

                            fbuffer.getCommandList().addFirst(commandI);

                        }
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
                log.severe(ie.toString());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void endTerm() throws IOException {
        term.exitPrivateMode();
    }


    private void redraw() throws IOException {

        log.finest(Arrays.toString(modifiedLines.toArray()));

        for (Integer line : modifiedLines) {
            if (line>=0) {
                log.finest("linha: " + line + " starRow: " + fbuffer.getStartRow());
                drawN(line);
            }
        }

        modifiedLines.clear();  // Limpar lista de linhas modificadas uma vez que estas ja foram imprimidas

        int cursorL = fbuffer.getCursor().getL();
        int cursorC = fbuffer.getCursor().getC();

        log.finest("posicoes logicas do cursor: " + cursorL + "," + cursorC);

        int[] pos = viewPos(cursorL,cursorC);
        int line = pos[0];
        int row = pos[2];
        cursorRow = row;
        cursorLine = line;

        log.finest("posicoes visuais do cursor: " + cursorLine + "," + cursorRow);
        term.setCursorPosition(cursorRow,cursorLine);

        fbuffer.setModified(false);
    }

    private void drawN(int line) throws IOException {
        int[] tmp = viewPos(line,0);
        int initRow = tmp[0];

        if (initRow == -20){
            return;
        }

        int nLines = tmp[1];

        StringBuilder linha = fbuffer.getNthLine(line);

        TerminalSize tamanhoterminal = term.getTerminalSize();
        width = tamanhoterminal.getColumns();

        term.setCursorPosition(0,initRow);

        int linhaSize = linha.length();

        //Apagar lixo
        for(int j=0; j<nLines+1; j++){
            for (int i = 0; i < width; i++) {
                term.putCharacter(' ');
            }
        }

        //Apagar linhas que ja nao existem
        if ( fbuffer.getNumLines()-1 == line ){
            for(int j=initRow; j<fbuffer.getLastRow(); j++){
                for (int i = 0; i < width; i++) {
                    term.putCharacter(' ');
                }
            }
        }

        term.setCursorPosition(0,initRow);

        for (int i = 0; i < linhaSize; i++) {
            if ( i>0 && ((i%width) == 0)) {
                initRow++; term.setCursorPosition(0,initRow); }
            term.putCharacter(linha.charAt(i));

        }





    }

    private int[] viewPos(int line, int col){
        int[] vector = new int[3] ;
        int row = fbuffer.getStartRow(); // Linha logica inicial a considerar
        int vis = 0;                // Linha visual inicial a considerar
        int r;                      // Quantidade de caracteres na ultima linha
        int q;                      // Quantidade de linhas visuais completas que uma linha logica ocupa


        while( vis<height &&  row<line ) {
            StringBuilder sb = fbuffer.getNthLine(row);
            int tamanho = sb.length();

            q = tamanho/width ;
            log.finest("tamanho logico da linha: " + row + " " + tamanho);
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
            fbuffer.setLastRow(line);
        }

        if (vis==height){
            vector[0] = -20;
            return vector;
        }


        log.finest("line " + line + " v: " + vis + " r: " + r + " q " + q);
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