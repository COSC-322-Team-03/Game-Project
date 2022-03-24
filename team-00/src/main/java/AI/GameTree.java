package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class GameTree {
	Board board;
	int heuristic;
	HashMap<Integer, Integer> children;
	HashMap<Integer, ArrayList<ArrayList<Integer>>> valid_moves;
	// set up a game tree with root (current game board) and children-heurisitics + move to get to that child
	public GameTree(Board board, Boolean is_white) {
		this.board = board;
		this.valid_moves = generate_valid_moves(is_white);
		this.children = generate_children_heuristic(this.valid_moves, is_white);
		this.heuristic = board.get_heuristic(is_white);
	}
	// calculate next move based on highest heurisitc
	// TODO implement some alpha-beta pruning and depth limited search here
	public ArrayList<ArrayList<Integer>> next_move() {
		int max = Collections.max(children.values());
		Integer key;
		for (Entry<Integer, Integer> entry : children.entrySet()) {
		    if (entry.getValue()==max) {
		        key = entry.getKey();
		        return valid_moves.get(key);
		    }
		}
		return null;
	}
	
	public void game_over() {
		int white_heuristic = board.get_heuristic(true);
		int black_heurisitc = board.get_heuristic(false);
		System.out.println("The white team as " + white_heuristic + " points");
		System.out.println("The black team as " + black_heurisitc + " points");
	}
	
	// generate all the valid moves and save them in an array
	public HashMap<Integer, ArrayList<ArrayList<Integer>>> generate_valid_moves(Boolean is_white) {
		ArrayList<ArrayList<Integer>> queenLocations = this.board.get_queen_locations(is_white);
		MoveList move_list = new MoveList();
		ArrayList<ArrayList<ArrayList<Integer>>> possible_moves = move_list.get_moves(this.board, is_white, queenLocations);
		HashMap<Integer,ArrayList<ArrayList<Integer>>> map = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
		int count = 0;
		for(ArrayList<ArrayList<Integer>> move : possible_moves) {
			map.put(count, move);
			count = count + 1;
		}
		return map;
	}
	// generate the child created from each move and save it in an map with the matching key
	public HashMap<Integer, Integer> generate_children_heuristic(HashMap<Integer, ArrayList<ArrayList<Integer>>> moves, Boolean is_white) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ArrayList<Integer> original_game_board = board.board_array_list();
		for(Map.Entry m : moves.entrySet()) {
			Integer key = (Integer) m.getKey();
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			Board child = new Board(original_game_board);
			child.update_game_board(move.get(0), move.get(1), move.get(2));
			Integer heuristic = child.get_heuristic(is_white);
			map.put(key, heuristic);
		}
		return map;
	}
}
