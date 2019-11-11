package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private Color currentPlayer;
	private int turn;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	private List<Piece> piecesOnTheBoard;
	private List<Piece> capturedPieces;

	public ChessMatch() {
		board = new Board(8, 8);
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
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] matriz = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				matriz[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matriz;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}


	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		if (testCheck(currentPlayer)) {
			undoMovie(source, target, capturedPiece);
			throw new ChessException("Você não pode fazer este movimento");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		//Checando a promoção da peça
		promoted = null;
		if(movedPiece instanceof Pawn) {
			if((movedPiece.getColor()==Color.WHITE && target.getRow()==0)||(movedPiece.getColor()==Color.BLACK && target.getRow()==7)){
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		
		
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer)))
			checkMate = true;
		else
			nextTurn();
		
		//Verificando movimento especial en passsant
		if(movedPiece instanceof Pawn && (target.getRow()==source.getRow()-2 || target.getRow()==source.getRow()+2)) enPassantVulnerable = movedPiece;
		else enPassantVulnerable = null;

		return (ChessPiece) capturedPiece;
	}


	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMoves(target))
			;
		throw new ChessException("Movimento InvÃ¡lido para essa peÃ§a");
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		// Movimento especial Castling Direita
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceTorre = new Position(source.getRow(), source.getColumn() + 3);
			Position targetTorre = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceTorre);
			board.placePiece(rook, targetTorre);
			rook.increaseMoveCount();
		}

		// Movimento especial Castling Esquerda
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceTorre = new Position(source.getRow(), source.getColumn() - 4);
			Position targetTorre = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceTorre);
			board.placePiece(rook, targetTorre);
			rook.increaseMoveCount();
		}
		
		// Movimento especial EnPassant Esquerda
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position posPeao;
				if(p.getColor()==Color.WHITE) {
					posPeao = new Position(target.getRow()+1, target.getColumn());
				}else {
					posPeao = new Position(target.getRow()-1, target.getColumn());
				}
				capturedPiece = board.removePiece(posPeao);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}

		return capturedPiece;
	}

	private void undoMovie(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		//Desfazer Movimento especial Castling Direita
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceTorre = new Position(source.getRow(), source.getColumn() + 3);
			Position targetTorre = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetTorre);
			board.placePiece(rook, sourceTorre);
			rook.decreaseMoveCount();
		}

		//Desfazer Movimento especial Castling Esquerda
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceTorre = new Position(source.getRow(), source.getColumn() - 4);
			Position targetTorre = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetTorre);
			board.placePiece(rook, sourceTorre);
			rook.decreaseMoveCount();
		}
		
		//Desfazer EnPassant 
		
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece peao = (ChessPiece)board.removePiece(target);
				
				Position posPeao;
				if(p.getColor()==Color.WHITE) {
					posPeao = new Position(3, target.getColumn());
				}else {
					posPeao = new Position(4, target.getColumn());
				}
				board.placePiece(peao, posPeao);
			}
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsPiece(position)) {
			throw new ChessException("NÃ£o existe peÃ§a na posiÃ§Ã£o de origem");

		}
		if (board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException(
					"Esse nÃ£o Ã© um movimento possÃ­vel, escolha outra peÃ§a ou faÃ§a um novo movimento");
		}
		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
			throw new ChessException("PeÃ§a invÃ¡lida");
		}

	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMove();
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece piece : list) {
			if (piece instanceof King) {
				return (ChessPiece) piece;
			}
		}
		throw new IllegalStateException("Não existe o rei na cor " + color);
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPiece = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece piece : opponentPiece) {
			boolean[][] matriz = piece.possibleMove();
			if (matriz[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color))
			return false;
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece piece : list) {
			boolean[][] matriz = piece.possibleMove();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (matriz[i][j]) {
						// Devido arquitetura temos que fazer um downCast para acesso ao método
						// protected;
						Position source = ((ChessPiece) piece).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						// Faz o movimento e depois testa se ele ainda esta em CHECK
						boolean testCheck = testCheck(color);
						undoMovie(source, target, capturedPiece);
						// Desfaz o movimento e agora testa se ele continua em check
						if (!testCheck)
							return false;
					}
				}

			}
		}
		return true;
	}
	
	
	
	public ChessPiece replacePromotedPiece(String string) {
		if(promoted == null)throw new IllegalStateException("Não há peça para ser promovida");
		if(!string.equals("B")&& !string.equals("N")&& !string.equals("R")&& !string.equals("Q")) throw new InvalidParameterException("Tipo de peça Inválida");
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(string, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		return newPiece;
	}
	
	private ChessPiece newPiece (String string, Color color) {
		if(string.equals("B")) return new Bishop(board, color);
		if(string.equals("N")) return new Knight(board, color);
		if(string.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}
	
	
	
	
	
	
	private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}


}
