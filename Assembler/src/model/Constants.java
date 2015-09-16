package model;

public interface Constants extends ScannerConstants, ParserConstants
{
    int EPSILON  = 0;
    int DOLLAR   = 1;

    int t_TOKEN_2 = 2; //"A"
    int t_TOKEN_3 = 3; //"B"
    int t_TOKEN_4 = 4; //"C"
    int t_TOKEN_5 = 5; //"D"
    int t_TOKEN_6 = 6; //"E"
    int t_ROM = 7;
    int t_RAM = 8;
    int t_REND = 9;
    int t_DRAM = 10;
    int t_IN = 11;
    int t_OUT = 12;
    int t_ID = 13;
    int t_ADD = 14;
    int t_SUB = 15;
    int t_AND = 16;
    int t_OR = 17;
    int t_XOR = 18;
    int t_NOT = 19;
    int t_MOV = 20;
    int t_INC = 21;
    int t_JMP = 22;
    int t_JMPC = 23;
    int t_JMPZ = 24;
    int t_CALL = 25;
    int t_RET = 26;
    int t_PUSH = 27;
    int t_POP = 28;
    int t_LABEL = 29;
    int t_TOKEN_30 = 30; //";"
    int t_TOKEN_31 = 31; //","

}
