package com.baljeet.api.Chess;

public class Move {

    //1-6:from; 7-12:to; 13-16:flags
    public int move;

    public Move(int from, int to, int flags) {
        move =  (from | (to<<6) | (flags << 12));
    }

    public int getFrom() {
        return move & 0x3f;
    }
    public int getTo() {
        return (move & 0xfc0) >>> 6;
    }

    public int getFlag() {
        return (move & 0xf000) >>>12;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move1 = (Move) o;
        return move == move1.move; // Compare the underlying int
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(move); // Hash based on the underlying int
    }
    @Override
    public String toString() {
        return "From: "+Board.indexToSquare(getFrom()) +" To: " + Board.indexToSquare(getTo())+  " Flags: " +getFlag();
    }
}
