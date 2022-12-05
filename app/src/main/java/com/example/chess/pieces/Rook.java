package com.example.chess.pieces;

import com.example.chess.abilities.StraightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Rook extends ChessPiece implements StraightSearch {

    private boolean moved;
    public Rook(Position position, PieceColor color) {
        super(position, color);
        moved = false;
    }

    @Override
    public ArrayList<Position> straightSearch(Board board) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        // vertical search
        for (int i = x + 1; i < board.getSIZE(); i++) {
            if (inBoard(i, y, board.getSIZE())) {
                if (board.isPiece(i, y) && board.getPiece(i, y).getColor() == this.color)
                    i = board.getSIZE() + 1;
                else if (board.isPiece(i, y) && board.getPiece(i, y).getColor() != this.color) {
                    positions.add(new Position(i, y));
                    i = board.getSIZE() + 1;
                } else if (!board.isPiece(i, y)) {
                    positions.add(new Position(i, y));
                }
            }
        }

        for (int i = x - 1; i >= 0; i--) {
            if (inBoard(i, y, board.getSIZE())) {
                if (board.isPiece(i, y) && board.getPiece(i, y).getColor() == this.color)
                    i = -1;
                else if (board.isPiece(i, y) && board.getPiece(i, y).getColor() != this.color) {
                    positions.add(new Position(i, y));
                    i = -1;
                } else if (!board.isPiece(i, y)) {
                    positions.add(new Position(i, y));
                }
            }
        }


        //horizontal search
        for (int i = y + 1; i < board.getSIZE(); i++) {
            if (inBoard(x, i, board.getSIZE())) {
                if (board.isPiece(x, i) && board.getPiece(x, i).getColor() == this.color)
                    i = board.getSIZE() +1;
                else if (board.isPiece(x, i) && board.getPiece(x, i).getColor() != this.color) {
                    positions.add(new Position(x, i));
                    i = board.getSIZE()+1;
                } else if (!board.isPiece(x, i)) {
                    positions.add(new Position(x, i));
                }
            }
        }

        for (int i = y - 1; i >= 0; i--) {
            if (inBoard(x, i, board.getSIZE())) {
                if (board.isPiece(x, i) && board.getPiece(x, i).getColor() == this.color)
                    i = -1;
                else if (board.isPiece(x, i) && board.getPiece(x, i).getColor() != this.color) {
                    positions.add(new Position(x, i));
                    i = -1;
                } else if (!board.isPiece(x, i)) {
                    positions.add(new Position(x, i));
                }
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

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
