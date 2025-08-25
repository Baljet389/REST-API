package com.baljeet.api.Chess.Engine;

public class EvaluationData {
    public static final int[] W_PAWN_PST = {
            0,  0,  0,  0,  0,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            10, 10, 20, 30, 30, 20, 10, 10,
            5,  5, 10, 25, 25, 10,  5,  5,
            0,  0,  0, 20, 20,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0};

    public static final int[] W_KING_PST_MIDDLE = {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 40, 10,  0,  0, 10, 40, 20};

    public static final int[] W_KING_PST_END = {
            -50,-40,-30,-20,-20,-30,-40,-50,
            -30,-20,-10,  0,  0,-10,-20,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 30, 40, 40, 30,-10,-30,
            -30,-10, 20, 30, 30, 20,-10,-30,
            -30,-30,  0,  0,  0,  0,-30,-30,
            -50,-30,-30,-30,-30,-30,-30,-50};

    public static final int[] KNIGHT_PST = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50,};

    public static final int[] W_BISHOP =  {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
    };
    public static int[] B_BISHOP ;
    public static int[] B_PAWN_PST;
    public static int[] B_KING_PST_MIDDLE;
    public static int[] B_KING_PST_END;

    public static final int QUEEN_WEIGHT = 900;
    public static final int ROOK_WEIGHT = 500;
    public static final int BISHOP_WEIGHT = 320;
    public static final int KNIGHT_WEIGHT = 300;
    public static final int PAWN_WEIGHT = 100;

    public static final int[] PieceWeights = {
            0,PAWN_WEIGHT,KNIGHT_WEIGHT,
            BISHOP_WEIGHT,ROOK_WEIGHT,
            QUEEN_WEIGHT,0};

    public EvaluationData(){
        B_PAWN_PST = mirrorForBlack(W_PAWN_PST);
        B_KING_PST_MIDDLE = mirrorForBlack(W_KING_PST_MIDDLE);
        B_KING_PST_END = mirrorForBlack(W_KING_PST_END);
        B_BISHOP = mirrorForBlack(W_BISHOP);
    }
    private int[] mirrorForBlack(int[] whitePST) {
        int[] blackPST = new int[64];
        for (int square = 0; square < 64; square++) {
            int rank = square / 8;
            int file = square % 8;

            int mirroredRank = 7 - rank;
            int mirroredSquare = mirroredRank * 8 + file;

            blackPST[square] = whitePST[mirroredSquare];
        }
        return blackPST;
    }
}
