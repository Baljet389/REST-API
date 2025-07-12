package com.baljeet.api;

import com.baljeet.api.Chess.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.stream.Stream;

@SpringBootTest
public class ChessTest {
    MoveGeneration moveGeneration;
    Board board;

    @ParameterizedTest
    @MethodSource("testPositions")
    void testMoveGeneration(String fen, int depth, long expected) {
        new PrecomputedData();
        board = new Board(fen);
        moveGeneration = new MoveGeneration(board);
        long result = numberOfPositionsReached(depth);
        assertEquals(expected, result, "Mismatch for FEN: " + fen);
    }

    private static Stream<Arguments> testPositions() {
        return Stream.of(
                Arguments.of("rnb1kb2/pp1pqp2/4p1pn/KPp4r/3P3p/4P3/P1P2PPP/RNBQ1BNR w q - 0 12", 5, 21045961L),
                Arguments.of("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8", 4, 2103487L),
                Arguments.of("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", 4, 3894594L),
                Arguments.of("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 4, 422333L),
                Arguments.of("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5, 4865609L)
        );
    }
    public long numberOfPositionsReached(int depth){
        if (depth == 0){
            return 1;
        }
        long numberOfPositions = 0;
        ArrayList<Move> moves = moveGeneration.getAllMoves();
        for (Move move: moves){
            board.makeMove(move);
            long abc= numberOfPositionsReached(depth-1);
            numberOfPositions+=abc;
            board.undoMove(move);
        }
        return numberOfPositions;
    }


}
