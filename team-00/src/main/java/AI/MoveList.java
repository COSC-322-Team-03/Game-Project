package AI;

import java.util.*;

public class MoveList {
	// get all the moves (mostly valid)
	public ArrayList<ArrayList<ArrayList<Integer>>> get_moves(Board board, Boolean is_white, ArrayList<ArrayList<Integer>> queenLocations) {
		ArrayList<ArrayList<ArrayList<Integer>>> moves = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> gameboard = board.get_game_board();
		for(ArrayList<Integer> queen : queenLocations) {
			Integer currentQueenX = queen.get(0);
			Integer currentQueenY = queen.get(1);
			ArrayList<ArrayList<Integer>> all_moves = generate_all_possible_moves(currentQueenX, currentQueenY);
			for(ArrayList<Integer> move : all_moves) {
				Integer newQueenX = move.get(0);
				Integer newQueenY = move.get(1);
				if(gameboard.get(newQueenY).get(newQueenX) == 0) {
					ArrayList<ArrayList<Integer>> all_arrow_moves = generate_all_possible_moves(newQueenX, newQueenY);
					for(ArrayList<Integer> arrow_move : all_arrow_moves) {
						Integer ArrowX = arrow_move.get(0);
						Integer ArrowY = arrow_move.get(1);
						if(gameboard.get(ArrowX).get(ArrowY) == 0) {
							// this is now a valid move (doesn't check if a queen or arrow is in the way
							if(board.isValid(queen, move, arrow_move, is_white)) {
								ArrayList<ArrayList<Integer>> validMove = new ArrayList<>(Arrays.asList(queen, move, arrow_move));
								moves.add(validMove);
							}
						}
					}
				}
			}
		}
		return moves;
	}
	// generate all possible moves (doesn't check if there is anything in that spot)
	public ArrayList<ArrayList<Integer>> generate_all_possible_moves(Integer currentX, Integer currentY) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> vet_moves = vertical_moves(currentX, currentY);
		ArrayList<ArrayList<Integer>> horz_moves = horizontal_moves(currentX, currentY);
		ArrayList<ArrayList<Integer>> dig_moves = diagonal_moves(currentX, currentY);
		moves.addAll(vet_moves);
		moves.addAll(horz_moves);
		moves.addAll(dig_moves);
		return moves;
	}
	// generate all possible vertiacla moves
	public ArrayList<ArrayList<Integer>> vertical_moves(Integer currentX, Integer currentY) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		for(int y = 0; y < 10; y++) {
			moves.add(new ArrayList<>(Arrays.asList(currentX, y)));
		}
		return moves;
	}
	// generate all possible horizontal moves
	public ArrayList<ArrayList<Integer>> horizontal_moves(Integer currentX, Integer currentY) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		for(int x = 0; x < 10; x++) {
			moves.add(new ArrayList<>(Arrays.asList(x, currentY)));
		}
		return moves;
	} 
	// generate all possible diagonal moves
	public ArrayList<ArrayList<Integer>> diagonal_moves(Integer currentX, Integer currentY) {
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		for(int d = -10; d < 10; d++) {
			Integer new_x = currentX+d;
			Integer new_y = currentY+d;
			if((new_x >= 0 && new_x < 10) && (new_y >= 0 && new_y < 10)) {
				moves.add(new ArrayList<>(Arrays.asList(new_x, new_y)));
			}
		}
		for(int d = -10; d < 10; d++) {
			Integer new_x = currentX-d;
			Integer new_y = currentY+d;
			if((new_x >= 0 && new_x < 10) && (new_y >= 0 && new_y < 10)) {
				moves.add(new ArrayList<>(Arrays.asList(new_x, new_y)));
			}
		}
		return moves;
	} 
}