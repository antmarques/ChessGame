package chess.entities;

import boardgame.entities.BoardEntity;
import chess.enums.ColorEnum;

public class RookEntity extends ChessPieceEntity{

    public RookEntity(BoardEntity board, ColorEnum color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public Boolean[][] possibleMoves() {
        return new Boolean[getBoard().getColumns()][getBoard().getColumns()];
    }
}
