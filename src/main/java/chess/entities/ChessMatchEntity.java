package chess.entities;

import boardgame.entities.BoardEntity;
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

    protected void placeNewPiece(Character column, Integer row, ChessPieceEntity piece){
        board.placePiece(piece, new ChessPositionEntity(column, row).toPosition());
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new RookEntity(board, ColorEnum.WHITHE));
        placeNewPiece('e', 1, new KingEntity(board, ColorEnum.WHITHE));

        placeNewPiece('a', 8, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('e', 8, new KingEntity(board, ColorEnum.BLACK));
    }
}
