package com.example.chess.game;

import com.example.chess.pieces.Bishop;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.King;
import com.example.chess.pieces.Knight;
import com.example.chess.pieces.Pawn;
import com.example.chess.pieces.PieceColor;
import com.example.chess.pieces.Queen;
import com.example.chess.pieces.Rook;
import com.example.chess.utils.Position;

public class Board {
    private final int SIZE = 8;
    private ChessPiece[][] pieces;

    public Board() {
        this.pieces = new ChessPiece[SIZE][SIZE];
        setAllNull();
        setStartingPos();
    }

    public void setAllNull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                pieces[i][j] = null;
            }
        }
    }

    public void setStartingPos() {

        //Black Row
        pieces[0][0] = new Rook(new Position(0, 0), PieceColor.BLACK);
        pieces[0][1] = new Knight(new Position(0, 1), PieceColor.BLACK);
        pieces[0][2] = new Bishop(new Position(0, 2), PieceColor.BLACK);
        pieces[0][3] = new Queen(new Position(0, 3), PieceColor.BLACK);
        pieces[0][4] = new King(new Position(0, 4), PieceColor.BLACK);
        pieces[0][5] = new Bishop(new Position(0, 5), PieceColor.BLACK);
        pieces[0][6] = new Knight(new Position(0, 6), PieceColor.BLACK);
        pieces[0][7] = new Rook(new Position(0, 7), PieceColor.BLACK);

        //Black Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[1][i] = new Pawn(new Position(1, i), PieceColor.BLACK);
        }

        //White Row
        pieces[7][0] = new Rook(new Position(7, 0), PieceColor.WHITE);
        pieces[7][1] = new Knight(new Position(7, 1), PieceColor.WHITE);
        pieces[7][2] = new Bishop(new Position(7, 2), PieceColor.WHITE);
        pieces[7][3] = new Queen(new Position(7, 3), PieceColor.WHITE);
        pieces[7][4] = new King(new Position(7, 4), PieceColor.WHITE);
        pieces[7][5] = new Bishop(new Position(7, 5), PieceColor.WHITE);
        pieces[7][6] = new Knight(new Position(7, 6), PieceColor.WHITE);
        pieces[7][7] = new Rook(new Position(7, 7), PieceColor.WHITE);

        //White Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[6][i] = new Pawn(new Position(6, i), PieceColor.WHITE);
        }

    }

    public int getSIZE() {
        return SIZE;
    }

    public ChessPiece[][] getBoard() {
        return pieces;
    }

    public void setBoard(ChessPiece[][] boa) {
        this.pieces = boa;
    }

    public void Reverse() {
        ChessPiece[][] reverse = new ChessPiece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                reverse[i][j] = pieces[SIZE - 1 - i][SIZE - 1 - j];
            }
        }
    }

    public boolean isPiece(int x, int y) {
        return pieces[x][y] != null;
    }

    public ChessPiece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public boolean movePiece(int current_x, int current_y, int move_x, int move_y){
        if(canMove(current_x, current_y, move_x, move_y)) {
            ChessPiece piece = getPiece(current_x, current_y);
            if(piece.movePiece(move_x, move_y, this)) {
                pieces[move_x][move_y] = pieces[current_x][current_y];
                pieces[current_x][current_y] = null;
                return true;
            }
            else return false;
        }
        return false;
    }

    public boolean canMove(int current_x, int current_y, int move_x, int move_y){
        ChessPiece piece = getPiece(current_x, current_y);
        return piece.canMove(move_x, move_y, this);
    }
}
