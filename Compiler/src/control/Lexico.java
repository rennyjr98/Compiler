package control;

import design.MonitorView;

/**
 *   @author rennyjr
**/

public class Lexico extends Analyzer {
    private int[][] transitionTable;
    private final static String [] specialWord = {"def", "true", "false", "range", 
        "none", "findall", "replace", "len", "sample", "choice", "random", 
        "randrange", "mean", "median", "variance", "sum", "input", "end", 
        "elif", "else", "sort", "reverse", "count", "index", "append", "extend",
        "pop", "remove", "insert", "wend", "to", "print", "println", "if", 
        "for", "while", "break", "continue", "return", "in", "innot"};
    
    private int actualState, actualColumn;
    private int actualLine;
    private String actualLexema;
    
    /*  -------------------------------------------------------------
        Cabecera de la matriz y Descripcion de errores abajo del todo
        
        * Arregla el desmadre de memoria
        ------------------------------------------------------------- */
    
    public Lexico(int [][] transitionTable) {
        actualState = 0;
        actualColumn = 0;
        actualLine = 1;
        actualLexema = "";
        this.transitionTable = transitionTable;
    }
    
    public void analyze(String code) {
        char actualChar;
        
        for(int i = 0; i < code.length(); i++) {
            if(actualState == 0 && isNumerical(code.charAt(i)))
                i = complexNumberAnalyze(code, i);
            
            actualChar = code.charAt(i);
            
            actualColumn = calcColumn(actualChar); 
            actualState = transitionTable[actualState][actualColumn];
            actualLexema += actualChar;
            setVitals(actualChar);
            
            if(actualState == 400) 
                i -= responseToFailed400();
            else if(actualState >= 500 && actualChar != ' ') {
                i -= responseToError();
                actualState = 0;
            } else if(actualState < 0)
                i -= responseToToken(actualChar);
            
            if(actualChar == '\n')
                actualLine++;
        }
        
        MonitorView.vitals += "------------------------------------------------"
                + "-----------\n\n";
    }
    
    private boolean isNumerical(char actualChar) {
    	return actualChar >= '0' && actualChar <= '9';
    }
    
    private void setVitals(char actualChar) {
        if(actualChar == ' ' || actualChar == '\n')
            actualChar = '¨';
        MonitorView.vitals += "[Lexico] Caracter a procesar " + actualChar +
                "\n[Lexico] Estado actual " + actualState + "\n" +
                "[Lexico] Lexema actual '" + actualLexema.trim() + "'\n" +
                "[Lexico] Columna actual " + actualColumn + "\n" +
                "\n";
    }
    
    private int calcColumn(char character) {
        switch(character) {
            case 'c': case 'd': return 2;
            case 'g': case 'h': case 'i': return 5;
            case 'k': case 'l': case 'm': case 'n': case 'ñ': 
            case 'o': case 'p': case 'q': case 'r': case 's':
            case 't': case 'u': case 'v': case 'w': return 7;
            case ':': return 56;
            
            default:
                for(int i = 0; i < lexicoHeader.length; i++)
                    if((character+"").equals(lexicoHeader[i]))
                        return i;
        }
        
        return lexicoHeader.length-1;
    }
    
    private int responseToFailed400() {
        int amount = getIndexFromFaileComplexWithParentesis();
        actualState = -45;
        actualLexema = "(";
        addToken();
        backToInitialState();
        return amount;
    }
    
    private int getIndexFromFaileComplexWithParentesis() {
        actualLexema = actualLexema.trim();
        return actualLexema.length()-1;
    }
    
    private int responseToToken(char lastCharacter) {
        int amount = 0;
        
        if(endWithOC())
            amount = endWithOCResponse(lastCharacter);
        else if(actualState == -45)
            amount = parenthesisResponseToken();
        else if(actualState == -46) actualState = -10;
        
        addToken();
        Counter.setCounter(actualState);
        backToInitialState(); 
        return amount;
    }
    
    private boolean endWithOC() {
        switch(actualState) {
            case -1: case -2: case -6: case -7: case -8:
            case -9: case -10: case -11: case -12: case -15:
            case -18: case -19: case -22: case -23: case -26:
            case -28: case -30: case -33: case -36: case -38:
            case -40:
                return true;
            default:
                return false;
        }
    }
    
    private int endWithOCResponse(char character) {
        if(character != '\n')
            actualLexema = actualLexema.substring(0, actualLexema.length()-1);
        if(actualState == -1) 
            actualState = getStateReservedWord();
        return 1;
    }
    
    private int getStateReservedWord() {
        for(int i = 0; i < specialWord.length; i++) {
            if((actualLexema.trim()).equals(specialWord[i])) 
                return -51 + ((i+1)*-1);
        } 
        
        if(actualLexema.equals("is"))
            return -98;
        else if(actualLexema.equals("isnot"))
            return -93;
        return -1;
    }
    
    private int parenthesisResponseToken() {
        int lastIndexOnLexema = actualLexema.length()-1;
        
        switch(actualLexema.charAt(lastIndexOnLexema)) {
            case '0': case '1': case '2': case '3': case '4': 
            case '5': case '6': case '7': case '8': case '9': 
                return 0;
            default:
                if(actualLexema.length()>1)
                    actualLexema = actualLexema.substring(0, lastIndexOnLexema);
                return 1;
        }
    }
    
    private void addToken() {
        Token token = new Token(actualLine, actualState, actualLexema.trim());
        Analyzer.listToken.add(token);
    }
    
    private int responseToError() {
        addError();
        actualLexema = "";
        if(actualState == 500)
            return 0;
        else return 1;
    }
    
    private void addError() {
        int indexError = actualState - 500;
        Error error = new Error(actualLine, actualState, "Lexico", 
                descriptionLexicoError[indexError], actualLexema.trim());
        Analyzer.listError.add(error);
    }
    
    public int complexNumberAnalyze(String code, int actualIndexInCode) {
        for(int i = actualIndexInCode; i < code.length(); i++) {

            actualColumn = getColumnComplex(code.charAt(i));
            
            actualState = transitionTable[actualState][actualColumn];
            actualLexema += code.charAt(i);
            setVitals(code.charAt(i));
            
            if(actualState < 0) {
                addToken();
                backToInitialState();
                return i+1;
            } else if (actualState == 300) {
                backToInitialState();
                break;
            }
        }
        
        return actualIndexInCode;
    }
    
    public int getColumnComplex(char digit) {
        if(digit >= '0' && digit <= '9')
            return 50;
        else {
            switch(digit) {
                case '+': return 51;
                case '-': return 52;
                case 'e': case 'E': return 53;
                case '.': return 54;
                case 'j': return 55;
                default: return lexicoHeader.length-1;
            }
        }
    }
    
    public void backToInitialState() {
        actualState = 0;
        actualColumn = 0;
        actualLexema = "";
    }
    
    public void resetLexico() {
        actualState = 0;
        actualColumn = 0;
        actualLexema = "";
        actualLine = 1;
        cleanLists();
    }
    
    public void cleanLists() {
        Analyzer.listToken.clear();
        Analyzer.listError.clear();
    }
    
    public static boolean isSpecialWord(String word) {
        for(String reservedWord : specialWord)
            if(word.equalsIgnoreCase(reservedWord))
                return true;
        return false;
    }
    
    private final String [] lexicoHeader = {
        "a", "b", "", "e", "f", "", "j", "", "x", "y", "z", "0", "1", "2", "3",
        "4", "5", "6", "7", "8", "9", "+", "-", "*", "/", "!", "=", "_", "%",
        "&", "|", "^", "<", ">", ".", "{", "}", "[", "]", "(", ")", ";", ",",
        "\"", "'", "#", "\n", " ", "\t", ""
    };
    
    private final String [] descriptionLexicoError = {
        "Se espera un caracter del abecedario miniscula o mayuscula, el " +
        "simbolor de #, ', \", o un numero del 0 al 9, +, -, *, /, %, &," +
        " |, !, (, ), {, }, [, ], = , coma, , ;, < o >",
        
        "Se espera una cualquier cosa menos un salto de linea",
        "Se espera una '",
        "Se espera un 1 o 0",
        "Se espera un numero del 1 al 9 o una letra de la a a la f",
        "Se espera un numero del 0 al 9",
        "Se espera un numero del 0 al 9 o un +",
        "Se espera un + o un numero del 0 al 9 o un punto",
        "Se espera un + o un numero del 0 al 9",
        "Se espera un numero del 0 al 9 o una j o un punto",
        "Se espera un )",
        "Se espera un numero del 0 al 9 o una j",
        "Se espera una b, x, numero del 0 al 7, un punto, una e o E o una j"
    };
}
