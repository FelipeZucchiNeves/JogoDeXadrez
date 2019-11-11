package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece{

	private ChessMatch chessMatch;
	
	public Pawn(Board board, Color color) {
		super(board, color);
		this.chessMatch = chessMatch;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean[][] possibleMove() {
		boolean matriz [][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);
		
		if(getColor() == Color.WHITE) {
			p.setValues(position.getRow()-1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()-2, position.getColumn());
			Position p2 = new Position(position.getRow()-1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsPiece(p2) && getMoveCount()==0) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()-1, position.getColumn()-1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()-1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			
			//Movimento especial EnPassant
			if(position.getRow()==3) {
				Position left = new Position(position.getRow(), position.getColumn()-1);
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					matriz[left.getRow()-1][left.getColumn()]=true;
				}
				Position right = new Position(position.getRow(), position.getColumn()+1);
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					matriz[right.getRow()-1][right.getColumn()]=true;
				}
			}
			
			
		}else {
			
			p.setValues(position.getRow()+1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()+2, position.getColumn());
			Position p2 = new Position(position.getRow()+1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsPiece(p2) && getMoveCount()==0) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()+1, position.getColumn()-1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			p.setValues(position.getRow()+1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				matriz [p.getRow()][p.getColumn()]=true;
			}
			
			if(position.getRow()==4) {
				Position left = new Position(position.getRow(), position.getColumn()-1);
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
					matriz[left.getRow()+1][left.getColumn()]=true;
				}
				Position right = new Position(position.getRow(), position.getColumn()+1);
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
					matriz[right.getRow()+1][right.getColumn()]=true;
				}
			}
			
		}
		
		
		return matriz;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
