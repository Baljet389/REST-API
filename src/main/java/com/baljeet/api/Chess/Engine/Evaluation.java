package com.baljeet.api.Chess.Engine;

import com.baljeet.api.Chess.Core.Board;
import com.baljeet.api.Chess.Core.Piece;

import java.util.Arrays;

public class Evaluation {

    private final Board board;


    public Evaluation(Board board){
        this.board = board;
        new EvaluationData();
    }


    public int evaluate(){
        long[] white = board.whiteBitboards;
        long[] black = board.blackBitboards;

        //Material score
        int whiteMatScore = materialScore(white);
        int blackMatScore = materialScore(black);

        int score = whiteMatScore - blackMatScore;

        //Apply PSTs
        score += applyPST(white[Piece.PAWN],EvaluationData.W_PAWN_PST);
        score -= applyPST(black[Piece.PAWN],EvaluationData.B_PAWN_PST);

        score += applyPST(white[Piece.KNIGHT],EvaluationData.KNIGHT_PST);
        score -= applyPST(black[Piece.KNIGHT],EvaluationData.KNIGHT_PST);

        score += applyPST(white[Piece.BISHOP],EvaluationData.W_BISHOP);
        score -= applyPST(black[Piece.BISHOP],EvaluationData.B_BISHOP);

        //Check if Queens are on board
        if(isEndgame()){
            score += applyPST(white[Piece.KING],EvaluationData.W_KING_PST_END);
            score -= applyPST(black[Piece.KING],EvaluationData.B_KING_PST_END);
        }
        else{
            score += applyPST(white[Piece.KING],EvaluationData.W_KING_PST_MIDDLE);
            score -= applyPST(black[Piece.KING],EvaluationData.B_KING_PST_MIDDLE);
        }


        return board.whiteToMove ? score : -score;
    }
    private int applyPST(long bitboard,int[] PST){
        int score = 0;
        while(bitboard != 0){
            int position = Long.numberOfTrailingZeros(bitboard);
            score += PST[position];
            bitboard &= (bitboard-1);
        }
        return score;
    }

    private static void printPST(int[] PST){
        for(int i = 0; i < PST.length;i++){
            if((i+1) % 8 == 0){
                System.out.println(PST[i]);
            }
            else{
                System.out.print(PST[i]+ ", ");
            }
        }
    }
    public boolean isEndgame(){
        return (Long.bitCount(board.whiteBitboards[Piece.QUEEN]) == 0
                && Long.bitCount(board.blackBitboards[Piece.QUEEN]) == 0)
                || (Long.bitCount(board.whiteBitboards[Piece.ROOK]
                | board.whiteBitboards[Piece.BISHOP] | board.whiteBitboards[Piece.KNIGHT]) ==0
               && Long.bitCount(board.blackBitboards[Piece.ROOK]
                | board.blackBitboards[Piece.BISHOP] | board.blackBitboards[Piece.KNIGHT]) ==0);
    }
    public int manhattanDistance(int square1, int square2){
        return Math.abs(square1 % 8 -square2 % 8) + Math.abs(square1 / 8 - square2 / 8);
    }
    public int materialScore(long[] bitboards){
        return    EvaluationData.QUEEN_WEIGHT * Long.bitCount(bitboards[Piece.QUEEN])
                + EvaluationData.ROOK_WEIGHT * Long.bitCount(bitboards[Piece.ROOK])
                + EvaluationData.KNIGHT_WEIGHT * Long.bitCount(bitboards[Piece.KNIGHT])
                + EvaluationData.BISHOP_WEIGHT * Long.bitCount(bitboards[Piece.BISHOP])
                + EvaluationData.PAWN_WEIGHT * Long.bitCount(bitboards[Piece.PAWN]);
    }
}
