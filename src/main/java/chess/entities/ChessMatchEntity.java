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
        placeNewPiece('c', 1, new RookEntity(board, ColorEnum.WHITE));
        placeNewPiece('c', 2, new RookEntity(board, ColorEnum.WHITE));
        placeNewPiece('d', 2, new RookEntity(board, ColorEnum.WHITE));
        placeNewPiece('e', 2, new RookEntity(board, ColorEnum.WHITE));
        placeNewPiece('e', 1, new RookEntity(board, ColorEnum.WHITE));
        placeNewPiece('d', 1, new KingEntity(board, ColorEnum.WHITE));

        placeNewPiece('c', 7, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('c', 8, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('d', 7, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('e', 7, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('e', 8, new RookEntity(board, ColorEnum.BLACK));
        placeNewPiece('d', 8, new KingEntity(board, ColorEnum.BLACK));
    }
}
