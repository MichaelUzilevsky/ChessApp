package com.example.chess.pieces;

import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Rook extends ChessPiece implements StraightSearch {

    private boolean moved;
    public Rook(Position position, Color color) {
        super(position, color);
        moved = false;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        // horizontal search
        for (int i = x + 1; i < board.getSIZE(); i++) {
            if (inBoard(i, y, board.getSIZE()) && board.getBoard()[i][y].getColor() != this.getColor()) {
                positions.add(new Position(i, y));
            }
        }

        for (int i = x - 1; i >= 0; i--) {
            if (inBoard(i, y, board.getSIZE()) && board.getBoard()[i][y].getColor() != this.getColor()) {
                positions.add(new Position(i, y));
            }
        }


        //vertical search
        for (int i = y + 1; i < board.getSIZE(); i++) {
            if (inBoard(x, i, board.getSIZE()) && board.getBoard()[x][i].getColor() != this.getColor()) {
                positions.add(new Position(x, i));
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (inBoard(x, i, board.getSIZE()) && board.getBoard()[x][i].getColor() != this.getColor()) {
                positions.add(new Position(x, i));
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

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
