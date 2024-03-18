package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PieceEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;
import chess.exceptions.ChessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChessMatchEntity {

    private BoardEntity board;

    private Integer turn;

    private ColorEnum player;

    private Boolean check;

    private Boolean checkMate;

    private ChessPieceEntity enPassantVulnerable;

    private ChessPieceEntity promoted;

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

    public ChessPieceEntity getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPieceEntity getPromoted() {
        return promoted;
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

        ChessPieceEntity movedPiece = (ChessPieceEntity)board.piece(target);

        // #specialmove promotion

        promoted = null;
        if (movedPiece instanceof PawnEntity) {
            if ((movedPiece.getColor() == ColorEnum.BLUE && target.getRow() == 0) || (movedPiece.getColor() == ColorEnum.YELLOW && target.getRow() == 7)) {
                promoted = (ChessPieceEntity)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }
        check = (testCheck(opponet(player))) ? true: false;

        if (testCheckMate(opponet(player))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // #specialmove en passant
        if (movedPiece instanceof PawnEntity && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPieceEntity) capturedPiece;
    }

    public ChessPieceEntity replacePromotedPiece(String s) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!s.equals("B") && !s.equals("N") && !s.equals("R") && !s.equals("Q")) {
            return promoted;
        }

        PositionEntity pe = promoted.getChessPosition().toPosition();
        PieceEntity p = board.removePiece(pe);
        piecesOnTheBoard.remove(p);

        ChessPieceEntity newPiece = newPiece(s, promoted.getColor());
        board.placePiece(newPiece, pe);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPieceEntity newPiece(String s, ColorEnum color) {
        if (s.equals("B")) {
            return new BishopEntity(board, color);
        }
        if (s.equals("N")) {
            return new KnightEntity(board, color);
        }
        if (s.equals("R")) {
            return new RookEntity(board, color);
        }

        return new QueenEntity(board, color);
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

        //#specialmove castiling kingside rook
        if (p instanceof KingEntity && target.getColumn() == source.getColumn() + 2){
            PositionEntity sourceT = new PositionEntity(source.getRow(), source.getColumn() + 3);
            PositionEntity targetT = new PositionEntity(source.getRow(), source.getColumn() + 1);
            ChessPieceEntity rook = (ChessPieceEntity)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        //#specialmove castiling queenside rook
        if (p instanceof KingEntity && target.getColumn() == source.getColumn() - 2){
            PositionEntity sourceT = new PositionEntity(source.getRow(), source.getColumn() - 4);
            PositionEntity targetT = new PositionEntity(source.getRow(), source.getColumn() - 1);
            ChessPieceEntity rook = (ChessPieceEntity)board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove en passant
        if (p instanceof PawnEntity) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null){
                PositionEntity pawnPosition;
                if (p.getColor() == ColorEnum.BLUE) {
                    pawnPosition = new PositionEntity(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new PositionEntity(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                piecesOnTheBoard.remove(capturedPiece);
                capturedPieces.add(capturedPiece);
            }
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

        //#specialmove castiling kingside rook
        if (pe instanceof KingEntity && target.getColumn() == source.getColumn() + 2){
            PositionEntity sourceT = new PositionEntity(source.getRow(), source.getColumn() + 3);
            PositionEntity targetT = new PositionEntity(source.getRow(), source.getColumn() + 1);
            ChessPieceEntity rook = (ChessPieceEntity)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        //#specialmove castiling queenside rook
        if (pe instanceof KingEntity && target.getColumn() == source.getColumn() - 2){
            PositionEntity sourceT = new PositionEntity(source.getRow(), source.getColumn() - 4);
            PositionEntity targetT = new PositionEntity(source.getRow(), source.getColumn() - 1);
            ChessPieceEntity rook = (ChessPieceEntity)board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #specialmove en passant
        if (pe instanceof PawnEntity) {
            if (!Objects.equals(source.getColumn(), target.getColumn()) && capturedPiece == enPassantVulnerable){
                ChessPieceEntity pawn = (ChessPieceEntity)board.removePiece(target);
                PositionEntity pawnPosition;
                if (pe.getColor() == ColorEnum.BLUE) {
                    pawnPosition = new PositionEntity(3, target.getColumn());
                } else {
                    pawnPosition = new PositionEntity(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    private ColorEnum opponet(ColorEnum color) {
        return (color == ColorEnum.BLUE) ? ColorEnum.YELLOW: ColorEnum.BLUE;
    }

    private ChessPieceEntity king(ColorEnum color) {
        List<PieceEntity> list = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == color).toList();
        for (PieceEntity pe: list){
            if (pe instanceof KingEntity){
                return (ChessPieceEntity) pe;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(ColorEnum color) {
        PositionEntity kingPosition = king(color).getChessPosition().toPosition();
        List<PieceEntity> opponetPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == opponet(color)).toList();
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
        List<PieceEntity> allPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPieceEntity)x).getColor() == color).toList();
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
        placeNewPiece('e', 1, new KingEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('d', 1, new QueenEntity(board, ColorEnum.BLUE));
        placeNewPiece('c', 1, new BishopEntity(board, ColorEnum.BLUE));
        placeNewPiece('f', 1, new BishopEntity(board, ColorEnum.BLUE));
        placeNewPiece('b', 1, new KnightEntity(board, ColorEnum.BLUE));
        placeNewPiece('g', 1, new KnightEntity(board, ColorEnum.BLUE));
        placeNewPiece('a', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('h', 1, new RookEntity(board, ColorEnum.BLUE));
        placeNewPiece('a', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('b', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('c', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('d', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('e', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('f', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('g', 2, new PawnEntity(board, ColorEnum.BLUE, this));
        placeNewPiece('h', 2, new PawnEntity(board, ColorEnum.BLUE, this));

        //Team Yellow
        placeNewPiece('e', 8, new KingEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('d', 8, new QueenEntity(board, ColorEnum.YELLOW));
        placeNewPiece('c', 8, new BishopEntity(board, ColorEnum.YELLOW));
        placeNewPiece('f', 8, new BishopEntity(board, ColorEnum.YELLOW));
        placeNewPiece('b', 8, new KnightEntity(board, ColorEnum.YELLOW));
        placeNewPiece('g', 8, new KnightEntity(board, ColorEnum.YELLOW));
        placeNewPiece('a', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('h', 8, new RookEntity(board, ColorEnum.YELLOW));
        placeNewPiece('a', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('b', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('c', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('d', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('e', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('f', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('g', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
        placeNewPiece('h', 7, new PawnEntity(board, ColorEnum.YELLOW, this));
    }
}
