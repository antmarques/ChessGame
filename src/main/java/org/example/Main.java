package org.example;

import chess.entities.ChessMatchEntity;
import chess.entities.ChessPieceEntity;
import chess.entities.ChessPositionEntity;
import chess.exceptions.ChessException;
import view.UI;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatchEntity chessMatch = new ChessMatchEntity();
        List<ChessPieceEntity> listCaptureds = new ArrayList<>();
        var quit = false;

        while (!chessMatch.getCheckMate() || !quit){
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, listCaptureds, quit);

                System.out.println();
                System.out.print("Source: ");
                ChessPositionEntity source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("Target: ");
                ChessPositionEntity target = UI.readChessPosition(sc);

                System.out.println();
                ChessPieceEntity capturedPiece = chessMatch.performChessMove(source, target);

                if (capturedPiece != null){
                    listCaptureds.add(capturedPiece);
                }

                if (chessMatch.getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    var s = sc.nextLine().toUpperCase();
                    while(!s.equals("B") && !s.equals("N") && !s.equals("R") && !s.equals("Q")) {
                        System.out.print("Invalid value, Enter again with piece for promotion (B/N/R/Q): ");
                        s = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(s);
                }
            } catch (InputMismatchException | ChessException e){
                System.out.print(e.getMessage() + "\nDo you want to play again (y/n)? ");
                var choice = sc.nextLine().toLowerCase();
                while (!choice.equals("n") && !choice.equals("y")){
                    System.out.print("Invalid value, inform 'y' (for yes) or 'n' (for no) ");
                    choice = sc.nextLine().toLowerCase();
                }
                if (choice.equals("n")) {
                    quit = true;
                    break;
                } else {
                    System.out.print("Press enter! ");
                    sc.nextLine();
                }
            }
        }
        UI.printMatch(chessMatch, listCaptureds, quit);
        sc.close();
    }
}