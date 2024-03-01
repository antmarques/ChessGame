package view;

import chess.entities.ChessPieceEntity;

public class UI {

    public static void printBoard(ChessPieceEntity[][] pieces){
        for (var i = 0; i < pieces.length; i++){
            System.out.print(8 - i + " ");
            for (var j = 0; j < pieces.length; j++){
                printPiece(pieces[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPieceEntity piece){
        if (piece == null){
            System.out.print("-");
        } else {
            System.out.print(piece);
        }
        System.out.print(" ");
    }
}
