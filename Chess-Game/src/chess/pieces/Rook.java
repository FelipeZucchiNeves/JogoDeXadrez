package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	

	@Override
	public boolean[][] possibleMove() {
		boolean [][]matriz = new boolean [getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0,0);
		
		// above, para cima.
		p.setValues(position.getRow()-1,position.getColumn());
		while(getBoard().positionExists(p)&&(!getBoard().thereIsPiece(p))) {
			matriz[p.getRow()][p.getColumn()]= true;
			p.setRow(p.getRow()-1);
		}
		if((getBoard().positionExists(p)) && (isThereOpponentPiece(p))){
			matriz[p.getRow()][p.getColumn()]= true;
		}
		

		
		//Left, esquerda
		p.setValues(position.getRow(),position.getColumn()-1);
		while(getBoard().positionExists(p)&&(!getBoard().thereIsPiece(p))) {
			matriz[p.getRow()][p.getColumn()]= true;
			p.setColumn(p.getColumn()-1);
		}
		if((getBoard().positionExists(p)) && (isThereOpponentPiece(p))){
			matriz[p.getRow()][p.getColumn()]= true;
		}
		
		
		//Right, Direita
		
		p.setValues(position.getRow(),position.getColumn()+1);
		while(getBoard().positionExists(p)&&(!getBoard().thereIsPiece(p))) {
			matriz[p.getRow()][p.getColumn()]= true;
			p.setColumn(p.getColumn()+1);
		}
		if((getBoard().positionExists(p)) && (isThereOpponentPiece(p))){
			matriz[p.getRow()][p.getColumn()]= true;
		}
		
		//Belor, para baixo
		
		p.setValues(position.getRow()+1,position.getColumn());
		while(getBoard().positionExists(p)&&(!getBoard().thereIsPiece(p))) {
			matriz[p.getRow()][p.getColumn()]= true;
			p.setRow(p.getRow()+1);
		}
		if((getBoard().positionExists(p)) && (isThereOpponentPiece(p))){
			matriz[p.getRow()][p.getColumn()]= true;
		}
		
			
		
		
		return matriz;
	}

}
