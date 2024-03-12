package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public class KnightEntity extends ChessPieceEntity{

    public KnightEntity(BoardEntity board, ColorEnum color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        PositionEntity pe = new PositionEntity(0, 0);

        //
        pe.setValues(position.getRow() - 1, position.getColumn() - 2);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() - 2, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() - 1, position.getColumn() + 2);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //
        pe.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        return mat;
    }

    private boolean canMove(PositionEntity position){
        ChessPieceEntity cpe = (ChessPieceEntity) getBoard().piece(position);
        return cpe == null || cpe.getColor() != getColor();
    }

    @Override
    public String toString() {
        return "N";
    }
}
