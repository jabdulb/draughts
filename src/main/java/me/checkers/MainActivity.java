package me.checkers;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public boolean firstPlayerTurn;
    public ArrayList<Coordinates> coords = new ArrayList<>();
    public Tile[][] Board = new Tile[8][8];
    public boolean selected = false;
    public Coordinates lastPos = null;
    public Coordinates clickedPosition = new Coordinates(0,0);
    public TextView game_over;
    public TextView[][] DisplayBoard = new TextView[8][8];
    public TextView[][] DisplayBoardBackground = new TextView[8][8];
    public int numberOfMoves;

    ArrayList<Piece> redPieces;
    ArrayList<Piece> whitePieces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        initialiseBoard();
    }

    private void initialiseBoard(){
        for (int i = 0; i < 12; i++) {
            redPieces.add(new Piece(true));
            whitePieces.add(new Piece(false));
        }

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Board[x][y] = new Tile(null);
            }
        }

        //ADD PIECE TO BOARD

        for (Piece piece : whitePieces) {
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 8; x += 2) {
                    if (y % 2 == 0) {
                        try {
                            Board[x + 1][y].setPiece(piece);
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    } else {
                        Board[x][y].setPiece(piece);
                    }
                }
            }
        }

        for (Piece piece : redPieces) {
            for (int y = 5; y < 8; y++) {
                for (int x = 0; x < 8; x += 2) {
                    if (y % 2 == 0) {
                        try {
                            Board[x + 1][y].setPiece(piece);
                        } catch (ArrayIndexOutOfBoundsException ignored) { }
                    } else {
                        Board[x][y].setPiece(piece);
                    }
                }
            }
        }

        Resources r = MainActivity.this.getResources();
        String name = getPackageName();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int idName = r.getIdentifier("R" + x + "" + y, "id", name);
                int idBackName = r.getIdentifier("R0" + x + "" + y, "id", name);
                DisplayBoard[x][y] = findViewById(idName);
                DisplayBoardBackground[x][y] = findViewById(idBackName);
            }
        }

        numberOfMoves = 0;
        selected = false;
        firstPlayerTurn = true;
        setBoard();
    }

    // Draws pieces for the foreground board
    private void setBoard(){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = Board[x][y].getPiece();

                if (Board[x][y].getPiece() != null) {
                    if (piece.isRed()) {
                        DisplayBoard[x][y].setBackgroundResource(R.drawable.rpawn);
                        Log.d("whitepawn", "Check");
                    } else {
                        DisplayBoard[x][y].setBackgroundResource(R.drawable.wpawn);
                        Log.d("blackpawn", "Check");
                    }
                } else{
                    DisplayBoard[x][y].setBackgroundResource(0);
                }
            }
        }
    }

    // Checks location of click and allows for
    // movement of pieces if permitted
    @Override
    public void onClick(View view){
        Resources r = getResources();
        String name = getPackageName();
        boolean found = false;

        int tileID = view.getId();
        for (int x = 0; x < 8 && !found; x++) {
            for (int y = 0; y < 8; y++) {
                int idName = r.getIdentifier("R" + x + "" + y, "id", name);
                if(tileID == idName){
                    clickedPosition.setX(x);
                    clickedPosition.setY(y);
                    found = true;
                    break;
                }
            }
        }

        if(!selected){
            if(Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() != null){

            }
        }

        //setBoard();
    }

    public void allowedMoves(Piece piece, int px, int py){
        if(!piece.isKing()){
            try{
                DisplayBoardBackground[px+1][py+1].setBackgroundResource(R.color.colorBoardSelected);
            } catch(IndexOutOfBoundsException ignored){}
            try{
                DisplayBoardBackground[px-1][py-1].setBackgroundResource(R.color.colorBoardSelected);
            } catch(IndexOutOfBoundsException ignored){}
        }
    }
}
