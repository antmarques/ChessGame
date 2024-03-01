package chess.entities;

import boardgame.entities.BoardEntity;

public class ChessMatchEntity {

    private BoardEntity board;

    public ChessMatchEntity() {
        board = new BoardEntity(8, 8);
    }

    public ChessPieceEntity[][] getPieces() {
        ChessPieceEntity[][] mat = new ChessPieceEntity[board.getRows()][board.getColumns()];
        for (var i = 0; i < board.getRows(); i++){
            for (var j = 0; i < board.getColumns(); i++){
                mat[i][j] = (ChessPieceEntity) board.piece(i, j);
            }
        }
        return mat;
    }
}
