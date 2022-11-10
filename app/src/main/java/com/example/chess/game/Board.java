package com.example.chess.game;

import com.example.chess.pieces.Bishop;
import com.example.chess.pieces.ChessPiece;
import com.example.chess.pieces.Color;
import com.example.chess.pieces.King;
import com.example.chess.pieces.Knight;
import com.example.chess.pieces.Pawn;
import com.example.chess.pieces.Queen;
import com.example.chess.pieces.Rook;
import com.example.chess.utils.Position;

public class Board {
    private final int SIZE = 8;
    private ChessPiece[][] pieces;

    public Board(ChessPiece[][] pieces) {
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
        pieces[0][0] = new Rook(new Position(0, 0), Color.BLACK);
        pieces[0][1] = new Knight(new Position(0, 1), Color.BLACK);
        pieces[0][2] = new Bishop(new Position(0, 2), Color.BLACK);
        pieces[0][3] = new Queen(new Position(0, 3), Color.BLACK);
        pieces[0][4] = new King(new Position(0, 4), Color.BLACK);
        pieces[0][5] = new Bishop(new Position(0, 5), Color.BLACK);
        pieces[0][6] = new Knight(new Position(0, 6), Color.BLACK);
        pieces[0][7] = new Rook(new Position(0, 7), Color.BLACK);

        //Black Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[1][i] = new Pawn(new Position(1, i), Color.BLACK);
        }

        //White Row
        pieces[7][0] = new Rook(new Position(7, 0), Color.BLACK);
        pieces[7][1] = new Knight(new Position(7, 1), Color.BLACK);
        pieces[7][2] = new Bishop(new Position(7, 2), Color.BLACK);
        pieces[7][3] = new Queen(new Position(7, 3), Color.BLACK);
        pieces[7][4] = new King(new Position(7, 4), Color.BLACK);
        pieces[7][5] = new Bishop(new Position(7, 5), Color.BLACK);
        pieces[7][6] = new Knight(new Position(7, 6), Color.BLACK);
        pieces[7][7] = new Rook(new Position(7, 7), Color.BLACK);

        //White Pawns
        for (int i = 0; i < SIZE; i++) {
            pieces[6][i] = new Pawn(new Position(1, i), Color.WHITE);
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
}
