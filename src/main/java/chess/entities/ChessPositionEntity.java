package chess.entities;

import boardgame.entities.PositionEntity;
import chess.exceptions.ChessException;

public class ChessPositionEntity {

    private Character column;

    private Integer row;

    public ChessPositionEntity(Character column, Integer row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8){
            throw new ChessException("Error instanting ChessPosition. Values are from a1 to h8");
        }
        this.column = column;
        this.row = row;
    }

    public Character getColumn() {
        return column;
    }

    public Integer getRow() {
        return row;
    }

    protected PositionEntity toPosition(){
        return new PositionEntity(8 - row, column - 'a');
    }

    protected static ChessPositionEntity fromPosition(PositionEntity position){
        return new ChessPositionEntity((char)('a' - position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString(){
        return "" + column + row;
    }
}
