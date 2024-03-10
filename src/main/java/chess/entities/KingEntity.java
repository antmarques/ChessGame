package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public class KingEntity extends ChessPieceEntity{
    public KingEntity(BoardEntity board, ColorEnum color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean mat[][] = new boolean[getBoard().getColumns()][getBoard().getColumns()];

        PositionEntity pe = new PositionEntity(0, 0);

        //above
        pe.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //below
        pe.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //left
        pe.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //right
        pe.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //nw
        pe.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //ne
        pe.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //sw
        pe.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //se
        pe.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        return mat;
    }

    private boolean canMove(PositionEntity position){
        ChessPieceEntity cpe = (ChessPieceEntity) getBoard().piece(position);
        return cpe == null || cpe.getColor() != getColor();
    }
}
