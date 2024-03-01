package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public class ChessMatchEntity {

    private BoardEntity board;

    public ChessMatchEntity() {
        board = new BoardEntity(8, 8);
        initialSetup();
    }

    public ChessPieceEntity[][] getPieces() {
        ChessPieceEntity[][] mat = new ChessPieceEntity[board.getRows()][board.getColumns()];
        for (var i = 0; i < board.getRows(); i++){
            for (var j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPieceEntity) board.piece(i, j);
            }
        }
        return mat;
    }

    private void initialSetup() {
        board.placePiece(new RookEntity(board, ColorEnum.WHITHE), new PositionEntity(0, 0));
        board.placePiece(new KingEntity(board, ColorEnum.WHITHE), new PositionEntity(0, 4));
    }
}
