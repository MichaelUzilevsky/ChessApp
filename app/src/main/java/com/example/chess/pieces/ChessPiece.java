package com.example.chess.pieces;

import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public abstract class ChessPiece {
    protected Position position;
    protected PieceColor color;

    public ChessPiece(Position position, PieceColor color) {
        this.position = position;
        this.color = color;
    }

    public int getPos_x() {
        return position.getPos_x();
    }

    public void setPos_x(int pos_x) {
        this.position.setPos_x(pos_x);
    }

    public int getPos_y() {
        return position.getPos_y();
    }

    public void setPos_y(int pos_y) {
        this.position.setPos_y(pos_y);
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public ArrayList<Position> legalMoves(Board board) {
        return null;
    }

    public boolean movePiece(int x, int y, Board board) {
        if (canMove(x, y, board)) {
            this.setPos_x(x);
            this.setPos_y(y);
            return true;
        }
        return false;
    }

    public boolean canMove(int x, int y, Board board) {
        return inLegalMove(x, y, board);
    }

    protected boolean inLegalMove(int x, int y, Board board) {
        for (Position position : legalMoves(board)) {
            if (position.equals(new Position(x, y)))
                return true;
        }
        return false;
    }

    protected Boolean inBoard(int x, int y, int size) {
        if ((x < 0 || x > size - 1) || (y < 0 || y > size - 1)) {
            return false;
        }
        return true;
    }

}
