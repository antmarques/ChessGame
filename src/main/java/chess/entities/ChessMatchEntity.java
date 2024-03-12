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

    private Boolean checkMate;

    private List<PieceEntity> piecesOnTheBoard = new ArrayList<>();

    private List<PieceEntity> capturedPieces = new ArrayList<>();

    public ChessMatchEntity() {
        board = new BoardEntity(8, 8);
        turn = 1;
        player = ColorEnum.BLUE;
        check = false;
        checkMate = false;
        initialSetup();
    }

    public Integer getTurn() {
        return turn;
    }

    public ColorEnum getPlayer() {
        return player;
    }

    public Boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
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

        if (testCheckMate(opponet(player))) {
            checkMate = true;
        } else {
            nextTurn();
        }

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
        ChessPieceEntity p = (ChessPieceEntity) board.removePiece(source);
        p.increaseMoveCount();
        PieceEntity capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(PositionEntity source, PositionEntity target, PieceEntity capturedPiece) {
        ChessPieceEntity pe = (ChessPieceEntity) board.removePiece(target);
        pe.decreaseMoveCount();
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

    private boolean testCheckMate(ColorEnum color) {
        if (!testCheck(color)){
            return false;
        }
        List<PieceEntity> allPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == color).collect(Collectors.toList());
        for (PieceEntity pe: allPieces){
            boolean [][] mat = pe.possibleMoves();
            for (int i = 0; i< board.getRows(); i++){
                for (int j = 0; j < board.getColumns(); j++){
                    if (mat[i][j]) {
                        PositionEntity source = ((ChessPieceEntity)pe).getChessPosition().toPosition();
                        PositionEntity target = new PositionEntity(i, j);
                        PieceEntity capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    protected void placeNewPiece(Character column, Integer row, ChessPieceEntity piece){
        board.placePiece(piece, new ChessPositionEntity(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void initialSetup() {
        //Team Blue
        placeNewPiece('h', 1, new KingEntity(board, ColorEnum.BLUE));
        //rainha
        placeNewPiece('c', 1, new Bishop(board, ColorEnum.BLUE));
        placeNewPiece('f', 1, new Bishop(board, ColorEnum.BLUE));
        //cavalo
        placeNewPiece('a', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('e', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('a', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('b', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('c', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('d', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('e', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('f', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('g', 2, new Pawn(board, ColorEnum.BLUE));
        placeNewPiece('h', 2, new Pawn(board, ColorEnum.BLUE));

        //Team Yellow
        placeNewPiece('e', 8, new KingEntity(board, ColorEnum.YELLOW));
        //rainha
        placeNewPiece('c', 8, new Bishop(board, ColorEnum.YELLOW));
        placeNewPiece('f', 8, new Bishop(board, ColorEnum.YELLOW));
        //cavalo
        placeNewPiece('a', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('h', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('a', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('b', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('c', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('d', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('e', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('f', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('g', 7, new Pawn(board, ColorEnum.YELLOW));
        placeNewPiece('h', 7, new Pawn(board, ColorEnum.YELLOW));
    }
}
