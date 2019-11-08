package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	private Color currentPlayer;
	private int turn;
	private boolean check;
	private boolean checkMate;
	
	private List <Piece> piecesOnTheBoard;
	private List <Piece> capturedPieces;
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false;
		piecesOnTheBoard = new ArrayList<Piece>();
		capturedPieces = new ArrayList<Piece>();
		initialSetup();
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
		piecesOnTheBoard.add(piece);
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
		Piece capturedPiece= makeMove(source, target);
		if(testCheck(currentPlayer)) {
			undoMovie(source, target, capturedPiece);
			throw new ChessException("Voc� n�o pode fazer este movimento");
		}
		
		check = (testCheck(opponent(currentPlayer)))?true : false;
		
		if(testCheckMate(opponent(currentPlayer))) checkMate = true;
		else nextTurn();
		
		return (ChessPiece) capturedPiece;
	}


	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMoves(target));
		throw new ChessException("Movimento Inválido para essa peça");
	}


	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		return capturedPiece;
	}
	
	
	private void undoMovie (Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
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
	
	
	private Color opponent(Color color) {
		return (color == Color.WHITE)? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king (Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece piece : list) {
			if(piece instanceof King) {
				return (ChessPiece)piece;
			}
		}
		throw new IllegalStateException("N�o existe o rei na cor " + color);
	}
	
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece>opponentPiece = piecesOnTheBoard.stream().filter(x-> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList()); 
		for (Piece piece : opponentPiece) {
			boolean[][] matriz = piece.possibleMove();
			if(matriz[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) return false;
		List<Piece> list = piecesOnTheBoard.stream().filter(x->((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece piece : list) {
			boolean [][] matriz = piece.possibleMove();
			for(int i = 0; i< board.getRows(); i++) {
				for(int j = 0; j<board.getColumns(); j++) {
					if(matriz[i][j]) {
						// Devido arquitetura temos que fazer um downCast para acesso ao m�todo protected;
						Position source = ((ChessPiece)piece).getChessPosition().toPosition(); 
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source, target);
						//Faz o movimento e depois testa se ele ainda esta em CHECK
						boolean testCheck = testCheck(color);
						undoMovie(source, target, capturedPiece);
						//Desfaz o movimento e agora testa se ele continua em check
						if(!testCheck) return false;
					}
				}
				
			}
		}
		return true;
	}
	
	
	

}
