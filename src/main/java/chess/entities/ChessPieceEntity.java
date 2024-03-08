package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public abstract class ChessPieceEntity extends PieceEntity {

    private ColorEnum color;

    public ChessPieceEntity(BoardEntity board, ColorEnum color) {
        super(board);
        this.color = color;
    }

    public ColorEnum getColor() {
        return color;
    }

    protected Boolean isThereOpponentPiece(PositionEntity position) {
        ChessPieceEntity p = (ChessPieceEntity) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
