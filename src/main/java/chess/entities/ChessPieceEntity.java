package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public abstract class ChessPieceEntity extends PieceEntity {

    private ColorEnum color;

    private Integer moveCount;

    public ChessPieceEntity(BoardEntity board, ColorEnum color) {
        super(board);
        this.color = color;
        this.moveCount = 0;
    }

    public ColorEnum getColor() {
        return color;
    }

    public Integer getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    protected Boolean isThereOpponentPiece(PositionEntity position) {
        ChessPieceEntity p = (ChessPieceEntity) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }

    public ChessPositionEntity getChessPosition() {
        return ChessPositionEntity.fromPosition(position);
    }
}
