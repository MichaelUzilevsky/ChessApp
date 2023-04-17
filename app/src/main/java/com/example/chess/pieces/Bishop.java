package com.example.chess.pieces;

import com.example.chess.abilities.DiagonalSearch;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public class Bishop extends ChessPiece implements DiagonalSearch {

    public Bishop(Position position, PieceColor color) {
        super(position, color);
    }

    @Override
    public ArrayList<Position> diagonalSearch(Board board, ChessPiece[][] chessPieces) {
        ArrayList<Position> positions = new ArrayList<>();
        int x = getPos_x(), y = getPos_y();

        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x + i, y + i, board.getSIZE())) {
                if (board.isPiece(x + i, y + i, chessPieces) && board.getPiece(x + i, y + i, chessPieces).getColor() != this.color) {
                    positions.add(new Position(x + i, y + i));
                    break;
                } else if (!board.isPiece(x + i, y + i, chessPieces))
                    positions.add(new Position(x + i, y + i));
                else if (board.isPiece(x + i, y + i, chessPieces) && board.getPiece(x + i, y + i, chessPieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x + i, y - i, board.getSIZE())) {
                if (board.isPiece(x + i, y - i, chessPieces) && board.getPiece(x + i, y - i, chessPieces).getColor() != this.color) {
                    positions.add(new Position(x + i, y - i));
                    break;
                } else if (!board.isPiece(x + i, y - i, chessPieces))
                    positions.add(new Position(x + i, y - i));
                else if (board.isPiece(x + i, y - i, chessPieces) && board.getPiece(x + i, y - i, chessPieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x - i, y + i, board.getSIZE())) {
                if (board.isPiece(x - i, y + i, chessPieces) && board.getPiece(x - i, y + i, chessPieces).getColor() != this.color) {
                    positions.add(new Position(x - i, y + i));
                    break;
                } else if (!board.isPiece(x - i, y + i, chessPieces))
                    positions.add(new Position(x - i, y + i));
                else if (board.isPiece(x - i, y + i, chessPieces) && board.getPiece(x - i, y + i, chessPieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        for (int i = 1; i < board.getSIZE(); i++) {
            if (inBoard(x - i, y - i, board.getSIZE())) {
                if (board.isPiece(x - i, y - i, chessPieces) && board.getPiece(x - i, y - i, chessPieces).getColor() != this.color) {
                    positions.add(new Position(x - i, y - i));
                    break;
                } else if (!board.isPiece(x - i, y - i, chessPieces))
                    positions.add(new Position(x - i, y - i));
                else if (board.isPiece(x - i, y - i, chessPieces) && board.getPiece(x - i, y - i, chessPieces).getColor() == this.color)
                    break;
            } else
                break;
        }
        return positions;
    }

    @Override
    public ArrayList<Position> possibleMoves(Board board, ChessPiece[][] chessPieces) {
        return diagonalSearch(board, chessPieces);
    }

}
