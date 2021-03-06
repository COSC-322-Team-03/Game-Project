package AI;

import java.util.*;

import org.javatuples.Pair;

public class MoveList {
	static int MOVE_LIST_SIZE = 250; // limit number of moves being made
	
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
					// we generate a temporary board where the queen has moved to find the valid arrow moves given this queen move
					Board b = new Board(gameboard);
					ArrayList<ArrayList<Integer>> queen_move_board = b.get_game_board();
					Integer val = queen_move_board.get(currentQueenY).get(currentQueenX);
					b.update_value(currentQueenX, currentQueenY, 0); // move current queen off old space
					b.update_value(newQueenX, newQueenY, val); // move current queen to new space
					ArrayList<ArrayList<Integer>> updated_queen_move_board = b.get_game_board();
					// generate arrow moves with given queen move
					ArrayList<ArrayList<Integer>> all_arrow_moves = generate_all_possible_moves(newQueenX, newQueenY, updated_queen_move_board);
					for(ArrayList<Integer> arrow_move : all_arrow_moves) {
						Integer ArrowX = arrow_move.get(0);
						Integer ArrowY = arrow_move.get(1);
						// this is always a valid move, so just add it to our move list
						ArrayList<ArrayList<Integer>> validMove = new ArrayList<>(Arrays.asList(queen, move, arrow_move));
						moves.add(validMove);
					}
				}
			}
			// if we have more than 250 moves, select 250 moves at random
			if(moves.size() > MOVE_LIST_SIZE) {
				Collections.shuffle(moves);
				List<ArrayList<ArrayList<Integer>>> lst = moves.subList(0, MOVE_LIST_SIZE);
				ArrayList<ArrayList<ArrayList<Integer>>> moves_rtn = new ArrayList<ArrayList<ArrayList<Integer>>>(lst);
				return moves_rtn;
			} // else return all the moves
			return moves;
		}
	// generate only valid moves
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

	// generate all valid vertical moves
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
	
	// generate all valid horizontal moves
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
	
	// generate all valid diagonal moves
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