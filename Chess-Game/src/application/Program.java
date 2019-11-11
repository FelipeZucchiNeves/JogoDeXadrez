package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {
	
	public static void main(String[] args) {
		
		
		Scanner scn = new Scanner(System.in);
		ChessMatch cm = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<ChessPiece>();
		
		// O console do eclipse nï¿½o reconhece as cores rode direto pelo terminal caso ele tenha reconhecimento de cores.
		
		
		while (!cm.getCheckMate()) {
			try {
				UI.clearSreen();
				UI.printMatch(cm, captured);
				System.out.println();
				System.out.println("Source: ");
				ChessPosition source = UI.readChessPosition(scn);
				
				boolean [][]possibleMoves = cm.possibleMoves(source);
				UI.clearSreen();
				UI.printBoard(cm.getPieces(), possibleMoves);
				
				
				System.out.println();
				System.out.println("Target :");
				ChessPosition target = UI.readChessPosition(scn);
				ChessPiece capturedPiece = cm.performChessMove(source, target);
				
				if(capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
				if(cm.getPromoted() != null) {
					System.out.println("Digite a Letra da peça para qual deseja promover o Peão (B/N/R/Q)");
					String string = scn.nextLine();
					cm.replacePromotedPiece(string);
				}
				
				
			}catch(ChessException e ) {
				System.out.println(e.getMessage());
				scn.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				scn.nextLine();
			}
			
			
		}
		
		UI.clearSreen();
		UI.printMatch(cm, captured);
		
	}

}
