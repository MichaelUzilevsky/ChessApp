package com.example.chess.pieces;

import com.example.chess.abilities.KnightSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Knight extends ChessPiece implements KnightSearch {

    public Knight(Position position, PieceColor color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> knightSearch(Board board, ChessPiece[][] pieces) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        int ONE = 1;
        int TWO = 2;

        if ((knightSearch(x, y, ONE, TWO, board,pieces) != null)) {
            positions.add(knightSearch(x, y, ONE, TWO, board,pieces));
        }
        if ((knightSearch(x, y, -ONE, -TWO, board,pieces) != null)) {
            positions.add(knightSearch(x, y, -ONE, -TWO, board,pieces));
        }

        if ((knightSearch(x, y, TWO, ONE, board,pieces) != null)) {
            positions.add(knightSearch(x, y, TWO, ONE, board,pieces));
        }
        if ((knightSearch(x, y, -TWO, -ONE, board,pieces) != null)) {
            positions.add(knightSearch(x, y, -TWO, -ONE, board,pieces));
        }

        if ((knightSearch(x, y, -ONE, TWO, board,pieces) != null)) {
            positions.add(knightSearch(x, y, -ONE, TWO, board,pieces));
        }
        if ((knightSearch(x, y, ONE, -TWO, board,pieces) != null)) {
            positions.add(knightSearch(x, y, ONE, -TWO, board,pieces));
        }

        if ((knightSearch(x, y, TWO, -ONE, board,pieces) != null)) {
            positions.add(knightSearch(x, y, TWO, -ONE, board,pieces));
        }
        if ((knightSearch(x, y, -TWO, ONE, board,pieces) != null)) {
            positions.add(knightSearch(x, y, -TWO, ONE, board,pieces));
        }

        return positions;
    }

    private Position knightSearch(int pos_x, int pos_y, int x_move, int y_move, Board board, ChessPiece[][] pieces) {
        int total_x = pos_x + x_move;
        int total_y = pos_y + y_move;
        if (inBoard(total_x, total_y, board.getSIZE())) {
            if(board.isPiece(total_x,total_y,pieces) && board.getPiece(total_x, total_y,pieces).getColor() != this.getColor())
                return new Position(total_x, total_y);
            else if(!board.isPiece(total_x,total_y,pieces)){
                return new Position(total_x, total_y);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Position> possibleMoves(Board board, ChessPiece[][] pieces) {
        return  knightSearch(board, pieces);
    }
}
