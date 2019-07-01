package control;

/**
 *   @author rennyjr
**/

public class Error {
    private int line;
    private int error;
    private String type;
    private String lexema;
    private String description;
    
    public Error(int line, int error, String type, String description,
            String lexema) {
        this.line = line;
        this.error = error;
        this.type = type;
        this.description = description;
        this.lexema = lexema;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getError() {
        return error;
    }
    
    public String getType() {
        return type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLexema() {
        return lexema;
    }
}
