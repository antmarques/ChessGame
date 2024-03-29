package view;

import chess.entities.ChessMatchEntity;
import chess.entities.ChessPieceEntity;
import chess.entities.ChessPositionEntity;
import chess.enums.ColorEnum;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    //Manipulação de cores nos consoles.

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPositionEntity readChessPosition(Scanner sc){
        try {
            var s = sc.nextLine();
            var column = s.charAt(0);
            var row = Integer.parseInt(s.substring(1));
            //Estou dividindo minha string e convertendo o último caracter em inteiro.

            return new ChessPositionEntity(column, row);
        } catch (RuntimeException e){
            throw new InputMismatchException("Error reading ChessPosition: Valid values are from a1 to h8.");
        }
    }

    public static void printMatch(ChessMatchEntity chessMatch, List<ChessPieceEntity> listCaptureds, boolean quit){
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(listCaptureds);
        System.out.println();
        System.out.println("Turn : " + chessMatch.getTurn());
        if (!chessMatch.getCheckMate()){
            if (quit) {
                System.out.println("Match terminated by player: " + chessMatch.getPlayer().getDesc());
                if (chessMatch.getPlayer() == ColorEnum.BLUE) {
                    System.out.println("Winner by WO: " + ColorEnum.YELLOW.getDesc());
                }
                 else {
                    System.out.println("Winner by WO: " + ColorEnum.BLUE.getDesc());
                }
                return;
            }
            System.out.println("Waiting player: " + chessMatch.getPlayer().getDesc());
            if (chessMatch.getCheck()){
                System.out.println("CHECK!");
            }
        } else {
            System.out.println("CHECKMATE!");
            System.out.println("Winner: " + chessMatch.getPlayer());
        }
    }

    public static void printBoard(ChessPieceEntity[][] pieces){
        for (var i = 0; i < pieces.length; i++){
            System.out.print(8 - i + " ");
            for (var j = 0; j < pieces.length; j++){
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPieceEntity[][] pieces, boolean[][] possibleMoves){
        for (var i = 0; i < pieces.length; i++){
            System.out.print(8 - i + " ");
            for (var j = 0; j < pieces.length; j++){
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPieceEntity piece, boolean backGround){
        if (backGround) {
            System.out.print(ANSI_RED_BACKGROUND);
        }
        if (piece == null){
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == ColorEnum.YELLOW){
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_BLUE + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    private static void printCapturedPieces(List<ChessPieceEntity> list) {
        List<ChessPieceEntity> listYellow =list.stream().filter(x -> x.getColor() == ColorEnum.YELLOW).toList();
        List<ChessPieceEntity> listBlue =
                list.stream().filter(x -> x.getColor() == ColorEnum.BLUE).toList();
        System.out.println("Captured pieces: ");
        System.out.print(ColorEnum.YELLOW.getDesc() + ": ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(listYellow.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print(ColorEnum.BLUE.getDesc() + ": ");
        System.out.print(ANSI_BLUE);
        System.out.println(Arrays.toString(listBlue.toArray()));
        System.out.print(ANSI_RESET);
    }
}
