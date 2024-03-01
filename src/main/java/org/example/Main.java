package org.example;

import chess.entities.ChessMatchEntity;
import view.UI;

public class Main {
    public static void main(String[] args) {
        ChessMatchEntity chessMatch = new ChessMatchEntity();

        UI.printBoard(chessMatch.getPieces());
    }
}