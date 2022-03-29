package AI;

import java.util.*;

import org.javatuples.Pair;

public class MoveList {
	// get all the moves (mostly valid)
		public ArrayList<ArrayList<ArrayList<Integer>>> get_moves(Board board, Boolean is_white, ArrayList<ArrayList<Integer>> queenLocations) {
			ArrayList<ArrayList<ArrayList<Integer>>> moves = new ArrayList<ArrayList<ArrayList<Integer>>>();
			ArrayList<Integer> gameboard = board.board_array_list();
			for(ArrayList<Integer> queen : queenLocations) {
				Integer currentQueenX = queen.get(0);
				Integer currentQueenY = queen.get(1);
				ArrayList<ArrayList<Integer>> all_moves = generate_all_possible_moves(currentQueenX, currentQueenY, board.get_game_board());
				for(ArrayList<Integer> move : all_moves) {
					Integer newQueenX = move.get(0);
					Integer newQueenY = move.get(1);
					
					Board b = new Board(gameboard);
					ArrayList<ArrayList<Integer>> queen_move_board = b.get_game_board();
					Integer val = queen_move_board.get(currentQueenY).get(currentQueenX);
					b.update_value(currentQueenX, currentQueenY, 0); // move current queen off old space
					b.update_value(newQueenX, newQueenY, val); // move current queen to new space
					ArrayList<ArrayList<Integer>> updated_queen_move_board = b.get_game_board();
					
					ArrayList<ArrayList<Integer>> all_arrow_moves = generate_all_possible_moves(newQueenX, newQueenY, updated_queen_move_board);
					for(ArrayList<Integer> arrow_move : all_arrow_moves) {
						Integer ArrowX = arrow_move.get(0);
						Integer ArrowY = arrow_move.get(1);
						// this is now a valid move (doesn't check if a queen or arrow is in the way
//						ArrayList<ArrayList<Integer>> validMove = new ArrayList<>(Arrays.asList(queen, move, arrow_move));
//						moves.add(validMove);
						Pair<Boolean, String> is_valid = board.isValid(queen, move, arrow_move, is_white);
						if(is_valid.getValue0()) {
							ArrayList<ArrayList<Integer>> validMove = new ArrayList<>(Arrays.asList(queen, move, arrow_move));
							moves.add(validMove);
						} else {
							System.out.println(is_valid.getValue1());
							generate_all_possible_moves(newQueenX, newQueenY, updated_queen_move_board);
//							Pair<Boolean, String> is_valid_2 = board.isValid(queen, move, arrow_move, is_white);
						}
					}
				}
			}
			return moves;
		}
	
	public ArrayList<ArrayList<Integer>> generate_all_possible_moves(Integer currentX, Integer currentY, ArrayList<ArrayList<Integer>> gameboard) { 
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> vet_moves = vertical_moves(currentX, currentY, gameboard);
		ArrayList<ArrayList<Integer>> horz_moves = horizontal_moves(currentX, currentY, gameboard);
		ArrayList<ArrayList<Integer>> dig_moves = diagonal_moves(currentX, currentY, gameboard);
		moves.addAll(vet_moves);
		moves.addAll(dig_moves);
		moves.addAll(horz_moves);
		return moves;
	}

	// generate all possible vertical moves
	public ArrayList<ArrayList<Integer>> vertical_moves(Integer currentX, Integer currentY, ArrayList<ArrayList<Integer>> gameboard) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		int y = currentY + 1;
		while( (y < 10) && (gameboard.get(y).get(currentX) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(currentX, y)));
			y = y + 1;
		}
		y = currentY - 1;
		while( (y >= 0) && (gameboard.get(y).get(currentX) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(currentX, y)));
			y = y - 1;
		}
		return moves;
	}
	
	// generate all possible horizontal moves
	public ArrayList<ArrayList<Integer>> horizontal_moves(Integer currentX, Integer currentY, ArrayList<ArrayList<Integer>> gameboard) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		int x = currentX + 1;
		while( (x < 10) && (gameboard.get(currentY).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, currentY)));
			x = x + 1;
		}
		x = currentX - 1;
		while( (x >= 0) && (gameboard.get(currentY).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, currentY)));
			x = x - 1;
		}
		return moves;
	}
	
	// generate all possible diagonal moves
	public ArrayList<ArrayList<Integer>> diagonal_moves(Integer currentX, Integer currentY, ArrayList<ArrayList<Integer>> gameboard) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		int x = currentX + 1;
		int y = currentY + 1;
		while( ((x >= 0 && x < 10) && (y >= 0 && y < 10)) && (gameboard.get(y).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, y)));
			x = x + 1;
			y = y + 1;
		}
		x = currentX - 1;
		y = currentY - 1;
		while( ((x >= 0 && x < 10) && (y >= 0 && y < 10)) && (gameboard.get(y).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, y)));
			x = x - 1;
			y = y - 1;
		}
		
		x = currentX + 1;
		y = currentY - 1;
		while( ((x >= 0 && x < 10) && (y >= 0 && y < 10)) && (gameboard.get(y).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, y)));
			x = x + 1;
			y = y - 1;
		}
		x = currentX - 1;
		y = currentY + 1;
		while( ((x >= 0 && x < 10) && (y >= 0 && y < 10)) && (gameboard.get(y).get(x) == 0) ) {
			moves.add(new ArrayList<>(Arrays.asList(x, y)));
			x = x - 1;
			y = y + 1;
		}
		return moves;
	} 
}