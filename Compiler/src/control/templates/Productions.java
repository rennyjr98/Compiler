package control.templates;

/**
 *    @author rennyjr
**/

public class Productions {
    public static int[] getProduction(int fila) {
        return productions[fila-1];
    }
    
    public static int getIndexOfEST() {
        return 15;
    }
    
    private static final int
        PROGRAM = 701, CONST = 705, CONSTENTIRE = 706, LISTUPRANGO = 707,
        TERMPASCAL = 711, ELEVATION = 713, SIMPLEEXPPAS = 715, FACTOR = 717,
        NOT = 722, OR = 724, OPBIT = 726, AND = 728,
        ANDLOG = 730, ORLOG = 732, XORLOG = 734, EST = 736,
        ASIGN = 741, FUNLIST = 742, ARR = 743, EXPPAS = 747,
        FUNCIONES = 749;
    
    public static int [] idProductions = {
        PROGRAM, CONST, CONSTENTIRE, LISTUPRANGO,
        TERMPASCAL, ELEVATION, SIMPLEEXPPAS, FACTOR,
        NOT, OR, OPBIT, AND,
        ANDLOG, ORLOG, XORLOG, EST,
        ASIGN, FUNLIST, ARR,FUNCIONES, EXPPAS
    };
    
    /* ---------- Producciones ---------- */
    private static int [][] productions = {
        /* 1 */ {802, -50, 703, 736, 801, -49, 702}, // A1 { @801 Estatutos A2 } @802
        /* 2 */ {702, -43, 701, -97, 750, 800, -45, 803,-1, -52}, // def id @803 ( @800 A4 ) Program ; A1 [Funciones]
        /* 3 */ {702, 804, -43, 705, -28, -1}, // id = Constante ; @804 A1 [Enviar Variable]
        /* 4 */ {703, 736, -43}, // ; Estatutos A2 
        /* 5 */ {704, 805, -1, -44}, // , id @805 A3 [Parametros]
        /* 6 */ {704, 806, -1}, // id @806 A3 [Parametros]
        /* 7 */ {-9, 807}, // Const-Flotante @807 [Flotante]
        /* 8 */ {-5, 808}, // Const-Cadena @808 [Cadena]
        /* 9 */ {-4, 809}, // Const-Caracter @809 [Caracter]
        /* 10 */ {706}, // CONST-ENTERO
        /* 11 */ {-10, 810}, // Const-Complejo @810 [Complejo]
        /* 12 */ {-53, 811}, // true @811 [True]
        /* 13 */ {-54, 812}, // false @812 [False]
        /* 14 */ {707}, // LIST-TUP-RANGOS
        /* 15 */ {-56, 813}, // none @813 [None]
        /* 16 */ {-11, 814}, // Decimal @814 [Decimal]
        /* 17 */ {-6, 815}, // Binario @815 [Binario]
        /* 18 */ {-7, 816}, // Hexadecimal @816 [Hexadecimal]
        /* 19 */ {-8, 817}, // Octal @817 [Octal]
        /* 20 */ {819, -97, 708, 818, 724, -45}, // ( OR @818 B1 ) @819
        /* 21 */ {708, 818, 724, -44}, // , OR @818 B1
        /* 22 */ {743}, // ARR
        /* 23 */ {820, -97, 706, -44, 706, -44, 706, -45, -55}, // range ( CONST-ENTERO , CONST-ENTERO , CONST-ENTERO ) @820
        /* 24 */ {-50, 709, 705, -49}, // { CONSTANTE B2 }
        /* 25 */ {821, 710, 705, -95}, //: CONSTANTE B3 @821
        /* 26 */ {822, 709, 705, -44}, // , CONSTANTE B2 @822
        /* 27 */ {712, 713}, // ELEVACION C1
        /* 28 */ {712, 713, -18}, // * ELEVACION C1
        /* 29 */ {712, 713, -22}, // / ELEVACION C1
        /* 30 */ {712, 713,-23}, // // ELEVACION C1
        /* 31 */ {712, 713, -26}, // % ELEVACION C1
        /* 32 */ {714, 717}, // FACTOR D1
        /* 33 */ {714, 717, -19}, // ** FACTOR D1
        /* 34 */ {716, 711}, // Termino-Pascal E1
        /* 35 */ {716, 711, -12}, // + Termino-Pascal E1
        /* 36 */ {716, 711, -15}, // - Termino-Pascal E1
        /* 37 */ {705}, // CONSTANTE
        /* 38 */ {718, -1}, // id F1
        /* 39 */ {718, -1, -13}, // ++ id F1
        /* 40 */ {718, -1, -16}, // -- id F1
        /* 41 */ {749}, // FUNCIONES
        /* 42 */ {719, 743}, // ARR F2
        /* 43 */ {720, 741}, // ASIGN F3
        /* 44 */ {-13}, // ++
        /* 45 */ {-16}, // --
        /* 46 */ {742, -94}, // . FunList
        /* 47 */ {720, 741}, // ASIGN F3
        /* 48 */ {724}, // OR
        /* 49 */ {-97, 721, -45, -68}, // input ( F4 )
        /* 50 */ {-5}, // Const-cadena
        /* 51 */ {723, 747}, // EXP-PAS G1
        /* 52 */ {723, 747, -36}, // ! EXP-PAS G1
        /* 53 */ {725, 728}, // AND H1
        /* 54 */ {725, 728, -41}, // || AND H1
        /* 55 */ {727, 715}, // SIMPLE-EXP-PAS I1
        /* 56 */ {727, 715, -31}, // << SIMPLE-EXP-PAS I1
        /* 57 */ {727, 715, -34}, // >> SIMPLE-EXP-PAS I1
        /* 58 */ {729, 722}, // NOT J1
        /* 59 */ {729, 722, -39}, // && NOT J1
        /* 60 */ {729, 722, -96}, // ## NOT J1
        /* 61 */ {731, 726}, // OP-BIT K1
        /* 62 */ {731, 726, -38}, // & OP-BIT K1
        /* 63 */ {733, 734}, // XORLOG L1
        /* 64 */ {733, 734, -40}, // | XORLOG L1
        /* 65 */ {735, 730}, // ANDLOG M1
        /* 66 */ {735, 730, -42}, // ^ ANDLOG M1
        /* 67 */ {-97, 737, 724, -45, -83}, // print ( OR N1 )
        /* 68 */ {737, 724, -44}, // , OR N1
        /* 69 */ {-97, 738, -45, -84}, // println ( N2 )
        /* 70 */ {737, 724}, // OR N1
        /* 71 */ {740, 739, 736, -95, 724, -85}, // if OR : EST N3 N4
        /* 72 */ {739, 736, -43}, // ; EST N3
        /* 73 */ {-69}, // end
        /* 74 */ {740, 739, 736, -70}, // elif EST N3 N4
        /* 75 */ {-69, 739, 736, -71}, // else EST N3 end
        /* 76 */ {-69, 739, 736, -95, 724, -82, 724, -86}, // for OR to OR : EST N3 end
        /* 77 */ {-81, 739, 736, -95, 724, -87}, // while OR : EST N3 wend
        /* 78 */ {-88}, // break
        /* 79 */ {-89}, // continue
        /* 80 */ {724, -90}, // return OR
        /* 81 */ {724}, // OR
        /* 82 */ {-28}, // =
        /* 83 */ {-14}, // +=
        /* 84 */ {-25}, // /=
        /* 85 */ {-21}, // *=
        /* 86 */ {-17}, // -=
        /* 87 */ {-24}, // //=
        /* 88 */ {-20}, // **=
        /* 89 */ {-27}, // %=
        /* 90 */ {-72}, // sort
        /* 91 */ {-73}, // reverse
        /* 92 */ {-74}, // count
        /* 93 */ {-75}, // index
        /* 94 */ {-76}, // append
        /* 95 */ {-77}, // extend
        /* 96 */ {-78}, // pop
        /* 97 */ {-79}, // remove
        /* 98 */ {-80}, // insert
        /* 99 */ {823, -48, 745, 724, 744, -47}, // [ O1 OR O2 ] @823
        /* 100 */ {-15}, // -
        /* 101 */ {746, 724, 744, -95}, // : O1 OR O3
        /* 102 */ {724, 744, -95}, // : O1 OR
        /* 103 */ {748, 732}, // ORLOG P1
        /* 104 */ {748, 732, -30}, // < ORLOG P1
        /* 105 */ {748, 732, -32}, // <= ORLOG P1
        /* 106 */ {748, 732, -29}, // == ORLOG P1
        /* 107 */ {748, 732, -37}, // != ORLOG P1
        /* 108 */ {748, 732, -35}, // >= ORLOG P1
        /* 109 */ {748, 732, -33}, // > ORLOG P1
        /* 110 */ {748, 732, -98}, // IS ORLOG P1
        /* 111 */ {748, 732, -93}, // ISNOT ORLOG P1
        /* 112 */ {748, 732, -91}, // IN ORLOG P1
        /* 113 */ {748, 732, -92}, // INNOT ORLOG P1
        /* 114 */ {-97, 724, -44, 724, -45, -57}, // findall ( OR , OR )
        /* 115 */ {-97, 724, -44, 724, -45, -58}, // replace ( OR , OR )
        /* 116 */ {-97, 724, -45, -59}, // len ( OR )
        /* 117 */ {-97, 724, -44, 724, -45, -60}, // Sample ( OR , OR )
        /* 118 */ {-97, 724, -45, -61}, // choice ( OR )
        /* 119 */ {-97, -45, -62}, // random ( )
        /* 120 */ {-97, 724, -45, -63}, // randrange ( OR )
        /* 121 */ {-97, 724, -45, -64}, // mean ( OR )
        /* 122 */ {-97, 724, -45, -65}, // median ( OR )
        /* 123 */ {-97, 724, -45, 66}, // variance ( OR )
        /* 124 */ {-97, 724, -45, -67} // sum ( OR )
    };
}
