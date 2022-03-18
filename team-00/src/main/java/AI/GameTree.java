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
	
	public GameTree(Board board, Boolean is_white) {
		this.board = board;
		System.out.println("GameTree Board: " + this.board);
		this.valid_moves = generate_valid_moves(is_white);
		System.out.println("GameTree Valid Moves: "+ this.valid_moves.size());
		this.children = generate_children_heuristic(this.valid_moves, is_white);
		System.out.println("GameTree Valid Children: "+ this.children.size());
		this.heuristic = board.get_heuristic(is_white);
	}
	
	public ArrayList<ArrayList<Integer>> next_move() {
		System.out.println("NextMove Children: " + this.children.size());
		System.out.println("NextMove valid moves: " + this.valid_moves.size());
		System.out.println("NextMove Board: " + this.board.get_game_board());
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
	
	public HashMap<Integer, ArrayList<ArrayList<Integer>>> generate_valid_moves(Boolean is_white) {
		System.out.println("GameTree is white?" + is_white);
		ArrayList<ArrayList<Integer>> queenLocations = this.board.get_queen_locations(is_white);
		System.out.println("GameTree queens" + queenLocations);
		MoveList move_list = new MoveList();
		ArrayList<ArrayList<ArrayList<Integer>>> possible_moves = move_list.get_moves(this.board, is_white, queenLocations);
		System.out.println("GameTree moves ");
		System.out.println(possible_moves.size());
		HashMap<Integer,ArrayList<ArrayList<Integer>>> map = new HashMap<Integer, ArrayList<ArrayList<Integer>>>();
		int count = 0;
		for(ArrayList<ArrayList<Integer>> move : possible_moves) {
			map.put(count, move);
			count = count + 1;
		}
		return map;
	}
	
	public HashMap<Integer, Integer> generate_children_heuristic(HashMap<Integer, ArrayList<ArrayList<Integer>>> moves, Boolean is_white) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(Map.Entry m : moves.entrySet()) {
			Integer key = (Integer) m.getKey();
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			try {
				Board child = (Board)board.clone();
				child.update_game_board(move.get(0), move.get(1), move.get(2));
				Integer heuristic = child.get_heuristic(is_white);
				map.put(key, heuristic);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
