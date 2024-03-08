package boardgame.entities;

public abstract class PieceEntity {

    protected PositionEntity position;

    private BoardEntity board;

    public PieceEntity(BoardEntity board) {
        this.board = board;
    }

    protected BoardEntity getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(PositionEntity position){ //Princípio do Padrão TemplateMethod.
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean aux [][] = possibleMoves();
        for (var i = 0; i < aux.length; i++){
            for (var j = 0; j < aux.length; j++){
                if (aux[i][j]){
                    return true;
                }
            }
        }
        return false;
    }
}
