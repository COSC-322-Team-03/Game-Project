package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

import org.javatuples.Pair;

public class GameTree {
	Board board;
	int heuristic;
	int DEPTH_LIMIT = 1;
	HashMap<Integer, Double> children;
	HashMap<Integer, ArrayList<ArrayList<Integer>>> valid_moves;
	// set up a game tree with root (current game board) and children-heurisitics + move to get to that child
	public GameTree(Board board, Boolean is_white) {
		this.board = board;
		this.valid_moves = generate_valid_moves(is_white);
		this.children = generate_children_heuristic(this.valid_moves, is_white);
		this.heuristic = board.get_heuristic(is_white);
	}
	
	public ArrayList<ArrayList<Integer>> next_move(boolean is_white) {
		Pair<ArrayList<ArrayList<Integer>>, Double> move_heurisitc = depth_limited_next_move(is_white, 0);
		return move_heurisitc.getValue0();
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Double> depth_limited_next_move(boolean is_white, Integer depth) {
		System.out.println("Depth: " + depth);
		HashMap<Integer, Double> best_heuristic_child = new HashMap<Integer, Double>();
		if(depth == DEPTH_LIMIT) {
			return next_move_pair(this.children, this.valid_moves, is_white);
		}
		ArrayList<Integer> current_game_board = this.board.board_array_list();
		for(Map.Entry m : valid_moves.entrySet()) {
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			Integer key = (Integer) m.getKey();
			Board gameboard = new Board(current_game_board);
			gameboard.update_game_board(move.get(0), move.get(1), move.get(2));
			GameTree gametree = new GameTree(gameboard, is_white);
			Pair<ArrayList<ArrayList<Integer>>, Double> gametree_bestmove = gametree.depth_limited_next_move(is_white, depth + 1);
			
			Double heuristic = gametree_bestmove.getValue1();
			best_heuristic_child.put(key, heuristic);
		}
		return next_move_pair(best_heuristic_child, this.valid_moves, is_white);
	}
	
	public ArrayList<ArrayList<Integer>> alpha_beta_search(boolean is_white) {
		Pair<ArrayList<ArrayList<Integer>>, Double> move_value = Max_Value(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, is_white, 0);
		return move_value.getValue0();
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Double> Max_Value(Double alpha, Double beta, boolean is_white, Integer depth) {
		if(depth == DEPTH_LIMIT) {
			return next_move_pair(this.children, this.valid_moves, is_white);
		}
		ArrayList<Integer> current_game_board = this.board.board_array_list();
		Double v = Double.NEGATIVE_INFINITY;
		ArrayList<ArrayList<Integer>> action_move = null;
		for(Map.Entry m : valid_moves.entrySet()) {
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			Board gameboard = new Board(current_game_board);
			gameboard.update_game_board(move.get(0), move.get(1), move.get(2));
			GameTree gametree = new GameTree(gameboard, is_white);
			Pair<ArrayList<ArrayList<Integer>>, Double> move_value_2 = gametree.Min_Value(alpha, beta, !is_white, depth+1);
			if(move_value_2.getValue1() > v) {
				v = move_value_2.getValue1();
				action_move = move;
				alpha = Math.max(alpha, v);
			}
			if(v >= beta) {
				return Pair.with(action_move, v);
			}
		}
		return Pair.with(action_move, v);
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Double> Min_Value(Double alpha, Double beta, boolean is_white, Integer depth) {
		if(depth == DEPTH_LIMIT) {
			return next_move_pair(this.children, this.valid_moves, is_white);
		}
		ArrayList<Integer> current_game_board = this.board.board_array_list();
		Double v = Double.POSITIVE_INFINITY;
		ArrayList<ArrayList<Integer>> action_move = null;
		for(Map.Entry m : valid_moves.entrySet()) {
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			Board gameboard = new Board(current_game_board);
			gameboard.update_game_board(move.get(0), move.get(1), move.get(2));
			GameTree gametree = new GameTree(gameboard, is_white);
			Pair<ArrayList<ArrayList<Integer>>, Double> move_value_2 = gametree.Max_Value(alpha, beta, !is_white, depth + 1);
			if(move_value_2.getValue1() > v) {
				v = move_value_2.getValue1();
				action_move = move_value_2.getValue0();
				beta = Math.min(beta, v);
			}
			if(v <= alpha) {
				return Pair.with(action_move, v);
			}
		}
		return Pair.with(action_move, v);
	}
	
	public Integer next_move_key(HashMap<Integer, Integer> children, HashMap<Integer, ArrayList<ArrayList<Integer>>> valid_moves) {
		if(children.size() < 1) {
			return null;
		}
		int max = Collections.max(children.values());
		Integer key;
		for (Entry<Integer, Integer> entry : children.entrySet()) {
		    if (entry.getValue()==max) {
		        key = entry.getKey();
		        return key;
		    }
		}
		return null;
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Double> next_move_pair(HashMap<Integer, Double> children, HashMap<Integer, ArrayList<ArrayList<Integer>>> valid_moves, boolean is_white) {
		if(children.size() < 1) {
			return null;
		}
		Double max = Collections.max(children.values());
		Integer key;
		for (Entry<Integer, Double> entry : children.entrySet()) {
		    if (entry.getValue()==max) {
		        key = entry.getKey();
		        return Pair.with(valid_moves.get(key), max);
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
	public HashMap<Integer, Double> generate_children_heuristic(HashMap<Integer, ArrayList<ArrayList<Integer>>> moves, Boolean is_white) {
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		ArrayList<Integer> original_game_board = board.board_array_list();
		for(Map.Entry m : moves.entrySet()) {
			Integer key = (Integer) m.getKey();
			ArrayList<ArrayList<Integer>> move = (ArrayList<ArrayList<Integer>>) m.getValue();
			Board child = new Board(original_game_board);
			child.update_game_board(move.get(0), move.get(1), move.get(2));
			Double heuristic = (double) child.get_heuristic(is_white);
			map.put(key, heuristic);
		}
		return map;
	}
}
