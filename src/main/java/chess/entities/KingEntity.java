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
    public Boolean[][] possibleMoves() {
        return new Boolean[getBoard().getColumns()][getBoard().getColumns()];
    }
}
