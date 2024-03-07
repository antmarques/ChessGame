package org.example;

import chess.entities.ChessMatchEntity;
import chess.entities.ChessPieceEntity;
import chess.entities.ChessPositionEntity;
import chess.exceptions.ChessException;
import view.UI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatchEntity chessMatch = new ChessMatchEntity();

        while (true){
            try {
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPositionEntity source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPositionEntity target = UI.readChessPosition(sc);

                sc.close();
                System.out.println();
                ChessPieceEntity capturedPiece = chessMatch.performChessMove(source, target);
            }catch (InputMismatchException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }catch (ChessException e){
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}