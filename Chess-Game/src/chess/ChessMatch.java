package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	private Color currentPlayer;
	private int turn;
	
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] matriz = new ChessPiece [board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				matriz[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matriz;
	}
	
	private void placeNewPiece(char  column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, Color.BLACK));
		placeNewPiece('h', 1, new King(board, Color.WHITE));
	}
	
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturePiece= makeMove(source, target);
		nextTurn();
		return (ChessPiece) capturePiece;
	}


	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMoves(target));
		throw new ChessException("Movimento Inválido para essa peça");
	}


	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		return capturedPiece;
	}


	private void validateSourcePosition(Position position) {
		if(!board.thereIsPiece(position)) {
			throw new ChessException("Não existe peça na posição de origem");
			
		}if(board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("Esse não é um movimento possível, escolha outra peça ou faça um novo movimento");
		}if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("Peça inválida");
		}
		
		
	}
	
	
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMove();
	}
	
	
	
	private void nextTurn () {
		turn ++;
		currentPlayer = (currentPlayer == Color.WHITE)? Color.BLACK : Color.WHITE;
	}
	
	

}
