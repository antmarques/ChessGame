package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public class KingEntity extends ChessPieceEntity{

    private ChessMatchEntity chessMatch;

    public KingEntity(BoardEntity board, ColorEnum color, ChessMatchEntity chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean mat[][] = new boolean[getBoard().getColumns()][getBoard().getColumns()];

        PositionEntity pe = new PositionEntity(0, 0);

        //above
        pe.setValues(position.getRow() - 1, position.getColumn());
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //below
        pe.setValues(position.getRow() + 1, position.getColumn());
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //left
        pe.setValues(position.getRow(), position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //right
        pe.setValues(position.getRow(), position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //nw
        pe.setValues(position.getRow() - 1, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //ne
        pe.setValues(position.getRow() - 1, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //sw
        pe.setValues(position.getRow() + 1, position.getColumn() - 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        //se
        pe.setValues(position.getRow() + 1, position.getColumn() + 1);
        if (getBoard().positionExists(pe) && canMove(pe)) {
            mat[pe.getRow()][pe.getColumn()] = true;
        }

        // #specialmove castiling kingside rook
        if (getMoveCount() == 0 && !chessMatch.getCheck()){
            // #specialmove castiling kingside rook
            PositionEntity pe2 = new PositionEntity(position.getRow(), position.getColumn() + 3);
            if (testRookCastiling(pe2)){
                PositionEntity pe3 = new PositionEntity(position.getRow(), position.getColumn() + 1);
                PositionEntity pe4 = new PositionEntity(position.getRow(), position.getColumn() + 2);
                if (getBoard().piece(pe3) == null && getBoard().piece(pe4) == null) {
                    mat[position.getRow()][position.getColumn()+2] = true;
                }
            }

            // #specialmove castiling queenside rook
            PositionEntity pe5 = new PositionEntity(position.getRow(), position.getColumn() - 4);
            if (testRookCastiling(pe5)){
                PositionEntity pe3 = new PositionEntity(position.getRow(), position.getColumn() - 1);
                PositionEntity pe4 = new PositionEntity(position.getRow(), position.getColumn() - 2);
                PositionEntity pe6 = new PositionEntity(position.getRow(), position.getColumn() - 3);
                if (getBoard().piece(pe3) == null && getBoard().piece(pe4) == null && getBoard().piece(pe6) == null) {
                    mat[position.getRow()][position.getColumn()-2] = true;
                }
            }
        }

        return mat;
    }

    private boolean canMove(PositionEntity position){
        ChessPieceEntity cpe = (ChessPieceEntity) getBoard().piece(position);
        return cpe == null || cpe.getColor() != getColor();
    }

    private boolean testRookCastiling(PositionEntity position) {
        ChessPieceEntity cpe = (ChessPieceEntity)getBoard().piece(position);
        return cpe != null && cpe instanceof RookEntity && cpe.getColor() == getColor() && cpe.getMoveCount() == 0;
    }
}
