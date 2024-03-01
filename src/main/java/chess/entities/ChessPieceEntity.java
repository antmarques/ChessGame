package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import chess.enums.ColorEnum;

public class ChessPieceEntity extends PieceEntity {

    private ColorEnum color;

    public ChessPieceEntity(BoardEntity board, ColorEnum color) {
        super(board);
        this.color = color;
    }

    public ColorEnum getColor() {
        return color;
    }

}
