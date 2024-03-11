package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;
import chess.exceptions.ChessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatchEntity {

    private BoardEntity board;

    private Integer turn;

    private ColorEnum player;

    private Boolean check;

    private List<PieceEntity> piecesOnTheBoard = new ArrayList<>();

    private List<PieceEntity> capturedPieces = new ArrayList<>();

    public ChessMatchEntity() {
        board = new BoardEntity(8, 8);
        turn = 1;
        player = ColorEnum.BLUE;
        check = false;
        initialSetup();
    }

    public Boolean getCheck() {
        return check;
    }

    public Integer getTurn() {
        return turn;
    }

    public ColorEnum getPlayer() {
        return player;
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

    public boolean[][] possibleMoves(ChessPositionEntity sourcePosition) {
        PositionEntity pe = sourcePosition.toPosition();
        validateSourcePosition(pe);
        return board.piece(pe).possibleMoves();
    }

    public ChessPieceEntity performChessMove(ChessPositionEntity sourcePosition, ChessPositionEntity targetPosition){
        PositionEntity source = sourcePosition.toPosition();
        PositionEntity target = targetPosition.toPosition();
        validateSourcePosition(source);
        validadteTargetPosition(source, target);
        PieceEntity capturedPiece = makeMove(source, target);

        if (testCheck(player)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = (testCheck(opponet(player))) ? true: false;

        nextTurn();
        return (ChessPieceEntity) capturedPiece;
    }

    private void validateSourcePosition(PositionEntity position){
        if (!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if (player != ((ChessPieceEntity)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validadteTargetPosition(PositionEntity source, PositionEntity target) {
        if (!board.piece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        player = (player == ColorEnum.BLUE) ? ColorEnum.YELLOW: ColorEnum.BLUE;
    }

    private PieceEntity makeMove(PositionEntity source, PositionEntity target) {
        PieceEntity p = board.removePiece(source);
        PieceEntity capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }

    private void undoMove(PositionEntity source, PositionEntity target, PieceEntity capturedPiece) {
        PieceEntity pe = board.removePiece(target);
        board.placePiece(pe, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private ColorEnum opponet(ColorEnum color) {
        return (color == ColorEnum.BLUE) ? ColorEnum.YELLOW: ColorEnum.BLUE;
    }

    private ChessPieceEntity king(ColorEnum color) {
        List<PieceEntity> list = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == color).collect(Collectors.toList());
        for (PieceEntity pe: list){
            if (pe instanceof KingEntity){
                return (ChessPieceEntity) pe;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(ColorEnum color) {
        PositionEntity kingPosition = king(color).getChessPosition().toPosition();
        List<PieceEntity> opponetPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == opponet(color)).collect(Collectors.toList());
        for (PieceEntity pe: opponetPieces) {
            boolean[][] mat = pe.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    protected void placeNewPiece(Character column, Integer row, ChessPieceEntity piece){
        board.placePiece(piece, new ChessPositionEntity(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        placeNewPiece('c', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('c', 2, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('d', 2, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('e', 2, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('e', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('d', 1, new KingEntity(board, ColorEnum.BLUE));

        placeNewPiece('c', 7, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('c', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('d', 7, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('e', 7, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('e', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('d', 8, new KingEntity(board, ColorEnum.YELLOW));
    }
}
