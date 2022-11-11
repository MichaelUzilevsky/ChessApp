package com.example.chess.pieces;

import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Pawn extends ChessPiece implements StraightSearch {
    protected Boolean moved;

    public Pawn(Position position, Color color) {
        super(position, color);
        moved = false;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        if (getColor() == Color.WHITE) {
            // 1 forward
            if (inBoard(getPos_x(), getPos_y() - 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x()][getPos_y() - 1] == null) {
                positions.add(new Position(getPos_x(), getPos_y() - 1));
            }
            // 2 forward
            if (!moved && inBoard(getPos_x(), getPos_y() - 2, board.getSIZE()) &&
                    board.getBoard()[getPos_x()][getPos_y() - 2] == null) {
                positions.add(new Position(getPos_x(), getPos_y() - 2));
            }
            //diagonal left
            if (inBoard(getPos_x() - 1, getPos_y() - 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x() - 1][getPos_y() - 1].getColor() != getColor()) {
                positions.add(new Position(getPos_x() - 1, getPos_y() - 1));
            }
            //diagonal right
            if (inBoard(getPos_x() + 1, getPos_y() - 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x() + 1][getPos_y() - 1].getColor() != getColor()) {
                positions.add(new Position(getPos_x() + 1, getPos_y() - 1));
            }

        }
        else {
            // 1 forward
            if (inBoard(getPos_x(), getPos_y() + 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x()][getPos_y() + 1] == null) {
                positions.add(new Position(getPos_x(), getPos_y() + 1));
            }
            // 2 forward
            if (!moved && inBoard(getPos_x(), getPos_y() + 2, board.getSIZE()) &&
                    board.getBoard()[getPos_x()][getPos_y() + 2] == null) {
                positions.add(new Position(getPos_x(), getPos_y() + 2));
            }
            //diagonal left
            if (inBoard(getPos_x() + 1, getPos_y() + 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x() + 1][getPos_y() + 1].getColor() != getColor()) {
                positions.add(new Position(getPos_x() + 1, getPos_y() + 1));
            }
            //diagonal right
            if (inBoard(getPos_x() + 1, getPos_y() + 1, board.getSIZE()) &&
                    board.getBoard()[getPos_x() + 1][getPos_y() + 1].getColor() != getColor()) {
                positions.add(new Position(getPos_x() + 1, getPos_y() + 1));
            }
        }
        return positions;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        return straightSearch(board);
    }

    @Override
    public void move() {
        moved = true;
    }

    public Boolean getMoved() {
        return moved;
    }

    public void setMoved(Boolean moved) {
        this.moved = moved;
    }
}
