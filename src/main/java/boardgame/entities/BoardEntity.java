package boardgame.entities;

import boardgame.exceptions.BoardException;

public class BoardEntity {

    private Integer rows;

    private Integer columns;

    private PieceEntity[][] pieces;

    public BoardEntity(Integer rows, Integer columns){
        if (rows < 1 || columns < 1){
            throw new BoardException("Error creating boad: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new PieceEntity[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public PieceEntity piece(Integer row, Integer clumn){
        if (!positionExists(row, clumn)){
            throw new BoardException("Position not on the board");
        }
        return pieces[row][clumn];
    }

    public PieceEntity piece(PositionEntity position){
        if (!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(PieceEntity piece, PositionEntity position) {
        if (thereIsAPiece(position)){
            throw new BoardException("There is alredy a piece on position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public PieceEntity removePiece(PositionEntity position){
        if (!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        if (piece(position) != null){
            PieceEntity pieceAux = piece(position);
            pieceAux.position = null;
            pieces[position.getRow()][position.getColumn()] = null;
            return pieceAux;
        } else {
            return null;
        }
    }

    public Boolean positionExists(Integer row, Integer column){
       return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public Boolean positionExists(PositionEntity position){
        return positionExists(position.getRow(), position.getColumn());
    }

    public Boolean thereIsAPiece(PositionEntity position){
        if (!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        return piece(position) != null;
    }

}
