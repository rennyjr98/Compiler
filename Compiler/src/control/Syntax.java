package control;

import design.MonitorView;
import java.util.LinkedList;
import java.util.Stack;

/**
 *   @author rennyjr
**/

public class Syntax extends Analyzer {
    private Integer actualState;
    private int [][] transitionTable;
    private int [] entriesProductions;
    private Stack<Integer> syntaxStack;
    private LinkedList<Token> copyTokenList = new LinkedList<Token>();
    
    private final int token$ = -100;
    private final int COMMENT = -2;
    private final int EPSILON = 125;
    private final int ERROR_INITIAL = 600;
    private final int EST_EPSILON = 736;
    private final int INITIAL_PRODUCTION = 701;
    
    public Syntax(int [][] transitionTable) {
        this.transitionTable = transitionTable;
        actualState = 0;
        syntaxStack = new Stack<>();
    }
    
    public void prepareTokensForSyntax() {
        entriesProductions = new int[21];
        copyTokenList = (LinkedList<Token>) listToken.clone();
        
        int listTokenSize = listToken.size() - 1;
        int last_line = listToken.get(listTokenSize).getLine();
        Token token$ = new Token(last_line, this.token$, "$");
        copyTokenList.add(token$);
        
        syntaxStack.push(this.token$);
        syntaxStack.push(INITIAL_PRODUCTION);
    }
    
    private void setVitals(String extraAction) {
        MonitorView.vitals += "[Syntax] Token actual " + 
                (copyTokenList.get(0).getToken()) + "\n" +
                "[Syntax] Lexema actual " + copyTokenList.get(0).getLexema() +
                "\n" + extraAction + "\n";
    }
    
    public void analyze() {
        while(!syntaxStack.isEmpty() && !copyTokenList.isEmpty()) {
            if(isComment())
                removeToken();
            else
                checkSyntax();
        }
        
        MonitorView.vitals += "------------------------------------------------"
                + "-----------\n\n";
    }
    
    private void checkSyntax() {
        if(syntaxStack.peek() >= INITIAL_PRODUCTION)
            analyzeTransition();
        else
            analyzeMatches();
    }
    
    private void analyzeMatches() {
        if(syntaxStack.peek() == copyTokenList.get(0).getToken()) {
            setVitals("[Syntax] Match con " + syntaxStack.peek());
            copyTokenList.remove(0);
            syntaxStack.pop();
        } else {
            actualState = 631;
            addError();
            syntaxStack.clear();
        }
    }
    
    private void analyzeTransition() {
        actualState = getFromMatrix();
        setVitals("");
        
        if(actualState != EPSILON && actualState < ERROR_INITIAL) 
            addProductionToStack();
        else if(actualState == EPSILON) {
            setVitals("[Syntax] Epsilon con " + actualState + "\n");
            if(syntaxStack.peek() == EST_EPSILON)
                entriesProductions[Productions.getIndexOfEST()]++;
            syntaxStack.pop();
        } else if(actualState >= ERROR_INITIAL) {
            addError();
            copyTokenList.remove(0);
        }
    }
    
    private void addError() {
        int line = copyTokenList.get(0).getLine();
        String lexema = copyTokenList.get(0).getLexema();
        setVitals("[Syntax] Error con " + actualState + " - " + lexema);
        Error error = new Error(line, actualState, "Sintaxis", 
                descriptionSyntaxError[actualState - ERROR_INITIAL], lexema);
        listError.add(error);
    }
    
    private void addProductionToStack() {
        int [] production = Productions.getProduction(actualState);
        countEntryToDiagrams(syntaxStack.peek());
        syntaxStack.pop();
        
        String vitalProduction = "[Syntax] Produccion ";
        for(int i = 0; i < production.length; i++) {
            vitalProduction += " [" + production[i] + "] ";
            syntaxStack.push(production[i]);
        } 
        
        setVitals(vitalProduction);
    }
    
    private void countEntryToDiagrams(int production) {
        for(int i = 0; i < Productions.idProductions.length; i++) {
            if(production == Productions.idProductions[i])
                entriesProductions[i]++;
        }
    }
    
    private Integer getFromMatrix() {
        int row = syntaxStack.peek()-INITIAL_PRODUCTION;
        int column = getColMatrix(copyTokenList.get(0).getToken());
        setVitals("[Syntax] Desde matriz " + transitionTable[row][column] + 
                " con " + row + ", " + column);
        return transitionTable[row][column];
    }
    
    private int getColMatrix(int token) {
        switch(token) {
            case -52: return 0; // def
            case -44: return 1; // ,
            case -43: return 2; // ;
            case -9: return 3; // const-flotante
            case -5: return 4; // const-cadena
            case -4: return 5; // const-caracter
            case -10: return 6; // const-complejo
            case -11: return 7; // decimal
            case -6: return 8; // binario
            case -7: return 9; // hexadecimal
            case -8: return 10; // octal
            case -53: return 11; // true
            case -54: return 12; // false
            case -45: return 13; // )
            case -47: return 14; // [
            case -55: return 15; // range
            case -49: return 16; // {
            case -56: return 17; // none
            case -95: return 18; // :
            case -1: return 19; // id
            case -13: return 20; // ++
            case -16: return 21; // --
            case -57: return 22; // findall
            case -58: return 23; // replace
            case -59: return 24; // len
            case -60: return 25; // sample
            case -61: return 26; // choice
            case -62: return 27; // random
            case -63: return 28; // randrange
            case -64: return 29; // mean
            case -65: return 30; // median
            case -66: return 31; // variance
            case -67: return 32; // sum
            case -18: return 33; // *
            case -22: return 34; // /
            case -23: return 35; // //
            case -26: return 36; // %
            case -19: return 37; // **
            case -12: return 38; // +
            case -15: return 39; // -
            case -94: return 40; // .
            case -28: return 41; // =
            case -14: return 42; // +=
            case -25: return 43; // /=
            case -21: return 44; // *=
            case -17: return 45; // -=
            case -24: return 46; // //=
            case -20: return 47; // **=
            case -27: return 48; // %=
            case -68: return 49; // input
            case -36: return 50; // !
            case -41: return 51; // ||
            case -31: return 52; // <<
            case -34: return 53; // >>
            case -39: return 54; // &&
            case -96: return 55; // ##
            case -38: return 56; // &
            case -40: return 57; // |
            case -42: return 58; // ^
            case -69: return 59; // end
            case -70: return 60; // elif
            case -71: return 61; // else
            case -72: return 62; // sort
            case -73: return 63; // reverse
            case -74: return 64; // count
            case -75: return 65; // index
            case -76: return 66; // append
            case -77: return 67; // extend 
            case -78: return 68; // pop
            case -79: return 69; // remove
            case -80: return 70; // insert
            case -30: return 71; // <
            case -32: return 72; // <=
            case -29: return 73; // ==
            case -37: return 74; // !=
            case -35: return 75; // >=
            case -33: return 76; // >
            case -98: return 77; // is
            case -93: return 78; // isnot
            case -91: return 79; // in
            case -92: return 80; // innot
            case -50: return 81; // }
            case -97: return 82; // )
            case -81: return 83; // wend
            case -48: return 84; // ]
            case -82: return 85; // to
            case -83: return 86; // print
            case -84: return 87; // println
            case -85: return 88; // if
            case -86: return 89; // for
            case -87: return 90; // while
            case -88: return 91; // break
            case -89: return 92; // continue
            case -90: return 93; // return
            case -100: return 94; // $
            default: return 631;
        }
    }
    
    public void reset() {
        actualState = 0;
        syntaxStack.clear();
        copyTokenList.clear();
    }
    
    private boolean isComment() {
        return copyTokenList.get(0).getToken() == COMMENT;
    }
    
    private void removeToken() {
        copyTokenList.remove(0);
    }
    
    protected String [] descriptionSyntaxError = {
        "Esta de más ",
        "Se esperaba un def, id o {",
        "Se esperaba un id",
        "Se esperaba un ,",
        "Se esperaba un ;",
        "Se esperaba un const-flotante, const-cadena, const-caracter, " +
        "decimal, binario, hexadecimal, octal, const-complejo, true, " +
        "false, (, [, range, {, none",
        
        "Se esperaba un decimal, binario, hexadecimal, octal",
        "Se esperaba un (, [, range, {",
        "Se esperaba un :",
        "Se esperaba un const-flotante, const-cadena, const-car, decimal," +
        " binario, hexadecimal, octal, const-complejo, true, false, (, [," +
        "range, {, None, id, ++, --, findall, replace, len, sample, choice, " +
        "random, randrange, mean, median, variance, sum",
        
        "Se esperaba un *, /, //, %",
        "Se esperaba un **",
        "Se esperaba un +, -",
        "Se esperaba un [, ++, --, ., =, +=, /=, *=, -=, //=, **=, %=",
        "Se esperaba un =, +=, /=, *=, -=, //=, **=, %=",
        "Se esperaba un const-f, const-cad, const-car, decimal, binario," +
        " hexadecimal, octal, const-com, true, false, (, [, range, {, None," +
        " id, ++, --, findall, replace, len, sample, choice, random, " + 
        "randrange, mean, median, variance, sum, input",
        
        "Se esperaba un const-cad",
        "Se esperaba un !",
        "Se esperaba un ||",
        "Se esperaba un <<, >>",
        "Se esperaba un <<, >>",
        "Se esperaba un &",
        "Se esperaba un |",
        "Se esperaba un ^",
        "Se esperaba un findall, replace, len, sample, choice, random, " +
        "randrange, mean, median, variance, sum, print, println, if, for," +
        " while, break, continue, return, const-f, const-cad, const-car," +
        " decimal, binario, hexa, octal, const-com, TRUE, FALSE, (, [, " +
        "range, {, none, id, ++, --",
        
        "Se esperaba un end, elif, else",
        "Se esperaba un sort, reverse, count, index, append, extend, pop," +
        " remove, insert",
        
        "Se esperaba un [",
        "Se esperaba un -",
        "Se esperaba un <, <=, ==, !=, >=, >, is, isnot, in, innot",
        "Se esperaba un findall, replace, len, sample, choice, random, " +
        "randrange, mean, median, variance, sum",
        
        "Error de fuerza bruta"
    };
}
