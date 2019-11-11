package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
	
	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}
	
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor();
	}
	
	//Testa a jogada especial do castling
	private boolean testCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p!= null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount()==0;
	}


	@Override
	public boolean[][] possibleMove() {
		boolean [][]matriz = new boolean [getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position (0,0);
		
		//ABOVE
		
		p.setValues(position.getRow()-1, position.getColumn());
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		
		//BELOW
		
		p.setValues(position.getRow()+1, position.getColumn());
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//LEFT
		
		
		p.setValues(position.getRow(), position.getColumn()-1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//RIGHT
		
		p.setValues(position.getRow(), position.getColumn()+1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//NW
		
		p.setValues(position.getRow()-1, position.getColumn()-1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		
		//NE
		
		p.setValues(position.getRow()-1, position.getColumn()+1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//SO
		
		p.setValues(position.getRow()+1, position.getColumn()-1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//SE
		p.setValues(position.getRow()+1, position.getColumn()+1);
		if( getBoard().positionExists(p)&& canMove(p)) {
			matriz[p.getRow()][p.getColumn()]=true;
		}
		
		//Movemento especial do Castling
		
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			Position posicaoTorre1 = new Position(position.getRow(), position.getColumn()+3);
			if(testCastling(posicaoTorre1)) {
				Position p1 = new Position(position.getRow(), position.getColumn()+1);
				Position p2 = new Position(position.getRow(), position.getColumn()+2);
				if(getBoard().piece(p1)==null && getBoard().piece(p2)==null) {
					matriz[position.getRow()][position.getColumn()+2]=true;
				}
			}
			
			Position posicaoTorre2 = new Position(position.getRow(), position.getColumn()-4);
			if(testCastling(posicaoTorre2)) {
				Position p1 = new Position(position.getRow(), position.getColumn()-1);
				Position p2 = new Position(position.getRow(), position.getColumn()-2);
				Position p3 = new Position(position.getRow(), position.getColumn()-3);
				if(getBoard().piece(p1)==null && getBoard().piece(p2)==null && getBoard().piece(p3)==null) {
					matriz[position.getRow()][position.getColumn()+2]=true;
				}
			}
		}
		
		
		
		return matriz;
	}

}
