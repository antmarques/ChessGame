package chess.entities;

import boardgame.entities.BoardEntity;
import boardgame.entities.PositionEntity;
import chess.enums.ColorEnum;

public class PawnEntity extends ChessPieceEntity{

    private ChessMatchEntity chessMatch;

    public PawnEntity(BoardEntity board, ColorEnum color, ChessMatchEntity chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean mat[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        PositionEntity p = new PositionEntity(0, 0);

        if (getColor() == ColorEnum.BLUE) {
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 2, position.getColumn());
            PositionEntity p2 = new PositionEntity(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            // #specialmove en passant blue
            if (position.getRow() == 3) {
                PositionEntity pLeft = new PositionEntity(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(pLeft) && isThereOpponentPiece(pLeft) && getBoard().piece(pLeft) == chessMatch.getEnPassantVulnerable()) {
                    mat[pLeft.getRow() - 1][pLeft.getColumn()] = true;
                }
                PositionEntity pRight = new PositionEntity(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(pRight) && isThereOpponentPiece(pRight) && getBoard().piece(pRight) == chessMatch.getEnPassantVulnerable()) {
                    mat[pRight.getRow() - 1][pRight.getColumn()] = true;
                }
            }
        } else {
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 2, position.getColumn());
            PositionEntity p2 = new PositionEntity(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                mat[p.getRow()][p.getColumn()] = true;
            }

            // #specialmove en passant yellow
            if (position.getRow() == 4) {
                PositionEntity pLeft = new PositionEntity(position.getRow(), position.getColumn() - 1);
                if (getBoard().positionExists(pLeft) && isThereOpponentPiece(pLeft) && getBoard().piece(pLeft) == chessMatch.getEnPassantVulnerable()) {
                    mat[pLeft.getRow() + 1][pLeft.getColumn()] = true;
                }
                PositionEntity pRight = new PositionEntity(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(pRight) && isThereOpponentPiece(pRight) && getBoard().piece(pRight) == chessMatch.getEnPassantVulnerable()) {
                    mat[pRight.getRow() + 1][pRight.getColumn()] = true;
                }
            }
        }

        return mat;
    }

    @Override
    public String toString(){
        return "P";
    }
}
