package entities.boardgame;

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
}
