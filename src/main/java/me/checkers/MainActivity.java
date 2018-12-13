package me.checkers;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    public Piece lastSelectedPiece;


    ArrayList<Piece> redPieces;
    ArrayList<Piece> whitePieces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();
        initialiseBoard();
        lastSelectedPiece = null;
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
                    } else {
                        DisplayBoard[x][y].setBackgroundResource(R.drawable.wpawn);
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

        Piece selectedPiece = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece();

        if(!selected){
            if(selectedPiece != null){
                allowedMoves(selectedPiece, clickedPosition.getX(), clickedPosition.getY());
                lastSelectedPiece = selectedPiece;
                lastPos = new Coordinates(clickedPosition.getX(), clickedPosition.getY());
                selected = true;
            }
        }
        else {
            Log.d("selection", "true");
            if(selectedPiece != null){
                clearAllowedMoves();
                selected = false;
            }
            else{
                int x = clickedPosition.getX();
                int y = clickedPosition.getY();
                int color = Color.RED;
                ColorDrawable background = (ColorDrawable) DisplayBoardBackground[x][y].getBackground();
                Log.d("colorCheck", Boolean.toString(background.getColor() == color));
                if(background.getColor() == color) {
                    int lx = lastPos.getX();
                    int ly = lastPos.getY();
                    Board[x][y].setPiece(lastSelectedPiece);
                    Board[lx][ly].setPiece(null);
                    redPieces.add(lastSelectedPiece);
                    clearAllowedMoves();
                    setBoard();
                    selected = false;
                }
            }

        }

        //setBoard();
    }

    public void allowedMoves(Piece piece, int px, int py){
        if(!piece.isKing() && piece.isRed()){
            try {
                if (Board[px + 1][py - 1].getPiece() == null) {
                    DisplayBoardBackground[px + 1][py - 1].setBackgroundResource(R.color.colorBoardSelected);
                }
            }
            catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (Board[px - 1][py - 1].getPiece() == null) {
                    DisplayBoardBackground[px - 1][py - 1].setBackgroundResource(R.color.colorBoardSelected);
                }
            }
            catch (IndexOutOfBoundsException ignored) {}
        }
        if(!piece.isKing() && !piece.isRed()){
            try {
                if (Board[px + 1][py + 1].getPiece() == null) {
                    DisplayBoardBackground[px + 1][py + 1].setBackgroundResource(R.color.colorBoardSelected);
                }
            }
            catch (IndexOutOfBoundsException ignored) {
            }
            try {
                if (Board[px - 1][py + 1].getPiece() == null) {
                    DisplayBoardBackground[px - 1][py + 1].setBackgroundResource(R.color.colorBoardSelected);
                }
            }
            catch (IndexOutOfBoundsException ignored) {}
        }
    }

    public List<Coordinates> canCapture(Piece piece, int px, int py){
        if(piece.isRed()) {
            if(Board[px-1][py-1].getPiece() != null && !Board[px+1][py-1].getPiece().isRed()){

            }
        }

        return new ArrayList<Coordinates>() {};
    }

    public void clearAllowedMoves(){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if((x+y)%2==0)
                    DisplayBoardBackground[x][y].setBackgroundResource(R.color.colorBoardBuff);
                else DisplayBoardBackground[x][y].setBackgroundResource(R.color.colorBoardGreen);
            }
        }
    }
}
