package boardgame;

public abstract class Piece {
	
	protected Position position;
	private Board board;
	
	
	
	public Piece( Board board) {
		this.board = board;
		this.position = null;
	}

	//ONLY LAYER OF BOARD CAN SEE THIS METHOD
	//SOMENTE A CAMADA DO TABULEITO PODE ENXERGAR ESTE M�TODO
	protected Board getBoard() {
		return board;
	}
	
	public abstract boolean [][]possibleMove();
	
	public boolean possibleMoves(Position position) {
		return possibleMove()[position.getRow()][position.getColumn()];
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean [][]matriz = possibleMove();
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(matriz[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	

	

}
