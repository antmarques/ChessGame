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
        var x = true;

        while (x){
            try {
                UI.printBoard(chessMatch.getPieces());

                System.out.println();
                System.out.print("Source: ");
                ChessPositionEntity source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: ");
                ChessPositionEntity target = UI.readChessPosition(sc);

                System.out.println();
                ChessPieceEntity capturedPiece = chessMatch.performChessMove(source, target);
            }catch (InputMismatchException e){
                System.out.println(e.getMessage() + "\nDo you want to position again? (y/n)");
                var choice = sc.next().charAt(0);
                if (choice == 'n'){
                    x = false;
                    sc.close();
                } else if (choice == 'y'){
                    x = true;
                    sc.nextLine();
                }
            }catch (ChessException e){
                System.out.println(e.getMessage() + "\nDo you want to position again? (y/n)");
                var choice = sc.next().charAt(0);
                if (choice == 'n'){
                    x = false;
                    sc.close();
                } else if (choice == 'y'){
                    x = true;
                    sc.nextLine();
                }
            }
        }
    }
}