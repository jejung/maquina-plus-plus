package model;

public interface ParserConstants
{
    int START_SYMBOL = 32;

    int FIRST_NON_TERMINAL    = 32;
    int FIRST_SEMANTIC_ACTION = 41;

    int[][] PARSER_TABLE =
    {
        {  0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  4, -1, -1 },
        {  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 26, 27, 27, 27, 27, 30, 28, -1, 31, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 32, 33, 33, 33, 33, -1, 34, -1, 36, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 37, 38, 38, 38, 38, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }
    };

    int[][] PRODUCTIONS = 
    {
        { 91, 34, 93 },
        {  0 },
        { 33, 92, 35, 30, 34 },
        {  0 },
        { 29, 84 },
        { 14, 42, 38 },
        { 15, 43, 38 },
        { 16, 44, 38 },
        { 17, 45, 38 },
        { 18, 46, 38 },
        { 19, 47, 38 },
        { 20, 48, 38 },
        { 21, 49, 38 },
        { 22, 51, 55, 81, 36 },
        { 23, 51, 56, 81, 36 },
        { 24, 51, 57, 81, 36 },
        { 25, 51, 58, 81, 36 },
        { 26, 51, 51, 52, 81 },
        { 27, 51, 51, 55, 37, 81 },
        { 28, 51, 51, 56, 37, 81 },
        {  9, 82 },
        { 13, 83 },
        {  3, 72 },
        {  4, 73 },
        {  5, 74 },
        {  6, 75 },
        {  2, 31, 39 },
        { 37, 56, 81, 31,  2 },
        { 57, 81,  8, 77, 31,  2 },
        { 11, 76, 58, 81, 31,  2 },
        {  7, 78, 51, 31, 40 },
        { 10, 37, 53, 51, 51, 81, 31,  2 },
        {  2, 52, 81 },
        { 37, 53, 81 },
        { 54, 81,  8, 77 },
        { 12, 76, 55, 81 },
        { 10, 37, 54, 51, 51, 81 },
        {  2, 52, 81, 79 },
        { 37, 53, 81, 79 },
        { 54, 81, 79,  8, 77 }
    };

    String[] PARSER_ERROR =
    {
        "",
        "Era esperado fim de programa",
        "Era esperado \"A\"",
        "Era esperado \"B\"",
        "Era esperado \"C\"",
        "Era esperado \"D\"",
        "Era esperado \"E\"",
        "Era esperado ROM",
        "Era esperado RAM",
        "Era esperado REND",
        "Era esperado DRAM",
        "Era esperado IN",
        "Era esperado OUT",
        "Era esperado ID",
        "Era esperado ADD",
        "Era esperado SUB",
        "Era esperado AND",
        "Era esperado OR",
        "Era esperado XOR",
        "Era esperado NOT",
        "Era esperado MOV",
        "Era esperado INC",
        "Era esperado JMP",
        "Era esperado JMPC",
        "Era esperado JMPZ",
        "Era esperado CALL",
        "Era esperado RET",
        "Era esperado PUSH",
        "Era esperado POP",
        "Era esperado LABEL",
        "Era esperado \";\"",
        "Era esperado \",\"",
        "<programa> inválido",
        "<label> inválido",
        "<instrucoes> inválido",
        "<instrucao> inválido",
        "<rend> inválido",
        "<reg> inválido",
        "<tipo> inválido",
        "<tipo2> inválido",
        "<tipo3> inválido"
    };
}
