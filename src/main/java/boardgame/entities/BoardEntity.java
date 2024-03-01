package boardgame.entities;

public class BoardEntity {

    private Integer rows;

    private Integer columns;

    private PieceEntity[][] pieces;

    public BoardEntity(Integer rows, Integer columns) {
        this.rows = rows;
        this.columns = columns;
        pieces = new PieceEntity[rows][columns];
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public PieceEntity piece(Integer row, Integer clumn){
        return pieces[row][clumn];
    }

    public PieceEntity piece(PositionEntity position){
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(PieceEntity piece, PositionEntity position) {
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }
}
