package control;

/**
 *   @author rennyjr
**/

public class Token {
    private int line;
    private int token;
    private String lexema;
    
    public Token(int line, int token, String lexema) {
        this.line = line;
        this.token = token;
        this.lexema = lexema;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getToken() {
        return token;
    }
    
    public String getLexema() {
        return lexema;
    }
}
