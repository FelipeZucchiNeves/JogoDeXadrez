package application;

import chess.ChessMatch;

public class Program {
	
	public static void main(String[] args) {
		
		ChessMatch cm = new ChessMatch();
		
		// O console do eclipse não reconhece as cores rode direto pelo terminal caso ele tenha reconhecimento de cores.
		
		UI.printBoard(cm.getPieces());
		
		
	}

}
