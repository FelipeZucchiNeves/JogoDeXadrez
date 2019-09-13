package boardgame;

public class Position {
	
	private int row;
	private int column;
	
	
	
	public Position(int row, int coll) {
		super();
		this.row = row;
		this.column = coll;
	}



	public int getRow() {
		return row;
	}



	public int getColumn() {
		return column;
	}



	public void setRow(int row) {
		this.row = row;
	}



	public void setColumn(int column) {
		this.column = column;
	}
	
	
	
	@Override
	public String toString() {
		return row + ", " + column;
	}

}
