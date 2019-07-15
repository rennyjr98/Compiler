package control;

import java.util.LinkedList;

/**
 *   @author rennyjr
**/

public class Analyzer {
    protected static LinkedList<Token> listToken;
    protected static LinkedList<Error> listError;
    
    // Mapeo para contadores
    
    public Analyzer() {
        listToken = new LinkedList<Token>();
        listError = new LinkedList<Error>();
    }
    
    public void setToken(Token token) {
        listToken.add(token);
    }
    
    public static Token getToken(int index) {
        return listToken.get(index);
    }
    
    public void setError(Error error) {
        listError.add(error);
    }
    
    public static Error getError(int index) {
        return listError.get(index);
    }
    
    public static int getSizeTokens() {
        return listToken.size();
    }
    
    public static int getSizeError() {
        return listError.size();
    }
}
