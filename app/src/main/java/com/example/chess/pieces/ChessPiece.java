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

    public ArrayList<Position> legalMoves(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> moves = possibleMoves(board, pieces);
        Position[] positions = new Position[moves.size()];
        positions = moves.toArray(positions);
        for (int i = 0; i < positions.length; i++) {
            if (!isMoveLegal(positions[i], board))
                positions[i] = null;
        }
        return toList(positions);
    }

    public ArrayList<Position> possibleMoves(Board board, ChessPiece[][] pieces) {
        return null;
    }

    public boolean movePiece(int x, int y) {
        this.setPos_x(x);
        this.setPos_y(y);
        return true;
    }

    public boolean canMove(int x, int y, Board board, ChessPiece[][] pieces) {
        return inLegalMove(x, y, board, pieces);
    }

    public boolean inLegalMove(int x, int y, Board board, ChessPiece[][] pieces) {
        for (Position position : legalMoves(board, pieces)) {
            if (position.equals(new Position(x, y)))
                return true;
        }
        return false;
    }

    public boolean inPossibleMoves(int x, int y, Board board, ChessPiece[][] pieces) {
        for (Position position : possibleMoves(board, pieces)) {
            if (position.equals(new Position(x, y)))
                return true;
        }
        return false;
    }

    protected Boolean inBoard(int x, int y, int size) {
        return (x >= 0 && x <= size - 1) && (y >= 0 && y <= size - 1);
    }

    public boolean isMoveLegal(Position pos, Board board) {
        int move_x = pos.getPos_x(), move_y = pos.getPos_y();
        if(!inBoard(move_x, move_y, board.getSIZE()))
            return false;
        return !board.checkCheckToColor(getPos_x(), getPos_y(), move_x, move_y, this.getColor());
    }

    public ArrayList<Position> toList(Position[] positions){
        ArrayList<Position> mov = new ArrayList<Position>();
        for (Position value : positions) {
            if (value != null)
                mov.add(value);
        }
        return mov;
    }
}
