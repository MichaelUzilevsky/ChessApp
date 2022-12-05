package com.example.chess.pieces;

import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Pawn extends ChessPiece implements StraightSearch {
    protected Boolean moved;

    public Pawn(Position position, PieceColor color) {
        super(position, color);
        moved = false;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        if (getColor() == PieceColor.WHITE) {
            // 1 forward
            if (inBoard(getPos_x() - 1, getPos_y(), board.getSIZE()) &&
                    !board.isPiece(getPos_x() - 1, getPos_y())) {
                positions.add(new Position(getPos_x() - 1, getPos_y()));
            }
            // 2 forward
            if (!moved && inBoard(getPos_x() - 2, getPos_y(), board.getSIZE()) &&
                    !board.isPiece(getPos_x() - 2, getPos_y())) {
                positions.add(new Position(getPos_x() - 2, getPos_y()));
            }
            //diagonal left
            if (inBoard(getPos_x() - 1, getPos_y() - 1, board.getSIZE()) &&
                    board.isPiece(getPos_x() - 1, getPos_y() - 1) &&
                    board.getPiece(getPos_x(), getPos_y()).getColor() != this.getColor()) {
                positions.add(new Position(getPos_x() - 1, getPos_y() - 1));
            }
            //diagonal right
            if (inBoard(getPos_x() - 1, getPos_y() + 1, board.getSIZE()) &&
                    board.isPiece(getPos_x() - 1, getPos_y() + 1) &&
                    board.getPiece(getPos_x() - 1, getPos_y() + 1).getColor() != this.getColor()) {
                positions.add(new Position(getPos_x() - 1, getPos_y() + 1));
            }

        }
        else {
            // 1 forward
            if (inBoard(getPos_x() + 1, getPos_y(), board.getSIZE()) &&
                    !board.isPiece(getPos_x() + 1, getPos_y())) {
                positions.add(new Position(getPos_x() + 1, getPos_y()));
            }
            // 2 forward
            if (!moved && inBoard(getPos_x() + 2, getPos_y(), board.getSIZE()) &&
                    !board.isPiece(getPos_x() + 2, getPos_y())) {
                positions.add(new Position(getPos_x() + 2, getPos_y()));
            }
            //diagonal right
            if (inBoard(getPos_x() + 1, getPos_y() + 1, board.getSIZE()) &&
                    board.isPiece(getPos_x() + 1, getPos_y() + 1) &&
                    board.getPiece(getPos_x() + 1, getPos_y() + 1).getColor() != getColor()) {
                positions.add(new Position(getPos_x() + 1, getPos_y() + 1));
            }
            //diagonal left
            if (inBoard(getPos_x() + 1, getPos_y() - 1, board.getSIZE()) &&
                    board.isPiece(getPos_x() + 1, getPos_y() - 1) &&
                    board.getPiece(getPos_x() + 1, getPos_y() - 1).getColor() != getColor()) {
                positions.add(new Position(getPos_x() + 1, getPos_y() - 1));
            }
        }

        return positions;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        return straightSearch(board);
    }

    @Override
    public boolean movePiece(int x, int y, Board board) {
        boolean b = super.movePiece(x, y, board);
        if(b)
            moved = true;
        return b;
    }

    public Boolean getMoved() {
        return moved;
    }

    public void setMoved(Boolean moved) {
        this.moved = moved;
    }
}
