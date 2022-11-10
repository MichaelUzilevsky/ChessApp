package com.example.chess.pieces;

import com.example.chess.abilities.MainAbilities;
import com.example.chess.game.Board;
import com.example.chess.utils.Position;

import java.util.ArrayList;

public abstract class ChessPiece implements MainAbilities {
    protected Position position;
    protected Color color;

    public ChessPiece(Position position, Color color) {
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
        this.setPos_y(pos_y);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public ArrayList<Position> legalMoves(Board board) {
        return null;
    }

    @Override
    public void move() {

    }

    protected Boolean inBoard(int x, int y, int size) {
        if ((x < 0 || x > size - 1) || (y < 0 || y > size - 1)){
            return  false;
        }
        return true;
    }

}
