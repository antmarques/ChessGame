package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;
import chess.exceptions.ChessException;

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

    public ChessPieceEntity performChessMove(ChessPositionEntity sourcePosition, ChessPositionEntity targetPosition){
        PositionEntity source = sourcePosition.toPosition();
        PositionEntity target = targetPosition.toPosition();
        validateSourcePosition(source);
        PieceEntity capturedPiece = makeMove(source, target);
        return (ChessPieceEntity) capturedPiece;
    }

    private void validateSourcePosition(PositionEntity position){
        if (!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if (!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private PieceEntity makeMove(PositionEntity source, PositionEntity target) {
        PieceEntity p = board.removePiece(source);
        PieceEntity capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        return capturedPiece;
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
