package me.checkers;

public class Tile {
    private Piece piece;

    Tile(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }
}
