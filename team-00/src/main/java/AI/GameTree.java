package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.javatuples.Pair;

public class GameTree {
	static int DEPTH_LIMIT = 1;
	
	boolean next_move_white;
	Board board;
	Integer heuristic; // heuristic value for team to play
	ArrayList<ArrayList<Integer>> action_from_parent;
	GameTree parent;
	ArrayList<GameTree> child_nodes;
	public GameTree(Board board, Boolean is_white, GameTree parent, ArrayList<ArrayList<Integer>> action) {
		this.board = board;
		this.next_move_white = is_white;
		this.heuristic = this.board.get_heuristic(next_move_white);
		this.parent = parent;
		this.action_from_parent = action;
		this.child_nodes = null; 
	}
	
	public ArrayList<GameTree> generate_child_nodes() {
		ArrayList<GameTree> children = new ArrayList<GameTree>();
		MoveList move_list = new MoveList();
		ArrayList<ArrayList<Integer>> queens = this.board.get_queen_locations(this.next_move_white);
		ArrayList<ArrayList<ArrayList<Integer>>> moves = move_list.get_moves(this.board, this.next_move_white, queens);
		
		ArrayList<Integer> board_copy = this.board.board_array_list();
		for(ArrayList<ArrayList<Integer>> move : moves) {
			Board child_board = new Board(board_copy);
			child_board.update_game_board(move.get(0), move.get(1), move.get(2));
			GameTree child_tree = new GameTree(child_board, !this.next_move_white, this, move);
			children.add(child_tree);
		}
		return children;
	}
	
	public void set_child_nodes() {
		this.child_nodes = this.generate_child_nodes();
	}
	
	public ArrayList<ArrayList<Integer>> next_move() {
		this.set_child_nodes();
		return best_move();
	}
	
	public static void sort(ArrayList<GameTree> list)
    {
  
        list.sort((o1, o2)
                      -> o1.heuristic.compareTo(
                          o2.heuristic));
    }
	
	public static void sort_other(ArrayList<GameTree> list)
    {
  
        list.sort((o1, o2)
                      -> o1.other_heuristic().compareTo(
                          o2.other_heuristic()));
    }

	public ArrayList<ArrayList<Integer>> best_move() {
		int max_heuristic = 0;
		ArrayList<ArrayList<Integer>> move = null;
		for(GameTree child : child_nodes) {
			if(child.heuristic > max_heuristic) {
				max_heuristic = child.heuristic;
				move = child.action_from_parent;
			}
		}
		return move;
	}
	
	public ArrayList<ArrayList<Integer>> depth_limited_next_move() {
		this.set_child_nodes();
		return depth_limited_get_move(depth_limited(0));
	}
	
	public ArrayList<ArrayList<Integer>> depth_limited_get_move(GameTree game_tree) {
		if(game_tree.parent == null) {
			return game_tree.best_move();
		}
		return depth_limited_get_move(game_tree.parent);
	}
	
	public GameTree depth_limited(Integer depth) {
		ArrayList<GameTree> game_trees = new ArrayList<GameTree>(); 
		if(depth == DEPTH_LIMIT) {
			return this;
		}
		for(GameTree child : child_nodes) {
			child.set_child_nodes();
			GameTree best_child = child.depth_limited(depth + 1);
			game_trees.add(best_child);
		}
		int max_heuristic = 0;
		GameTree gt = null;
		for(GameTree game_tree : game_trees) {
			if(game_tree.heuristic > max_heuristic) {
				max_heuristic = game_tree.heuristic;
				gt = game_tree;
			}
		}
		return gt;
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Integer> best_move_pair() {
		int max_heuristic = 0;
		ArrayList<ArrayList<Integer>> move = null;
		for(GameTree child : child_nodes) {
			if(child.heuristic > max_heuristic) {
				max_heuristic = child.heuristic;
				move = child.action_from_parent;
			}
		}
		return Pair.with(move, max_heuristic);
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Integer> best_move_pair_other() {
		int max_heuristic = 0;
		ArrayList<ArrayList<Integer>> move = null;
		for(GameTree child : child_nodes) {
			if(child.other_heuristic() > max_heuristic) {
				max_heuristic = child.other_heuristic();
				move = child.action_from_parent;
			}
		}
		return Pair.with(move, max_heuristic);
	}
	
	public ArrayList<ArrayList<Integer>> alpha_beta_next_move() {
		Pair<ArrayList<ArrayList<Integer>>, Integer> move_value = Max_Value(-1, 200, 0); 
		return move_value.getValue0();
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Integer> Max_Value(int alpha, int beta, int depth) {
		this.set_child_nodes();
		if(depth == DEPTH_LIMIT) {
			return this.best_move_pair();
		}
		int v = -1;
		ArrayList<ArrayList<Integer>> move = null;
		sort_other(this.child_nodes);
		for(GameTree child : this.child_nodes) {
			Pair<ArrayList<ArrayList<Integer>>, Integer> move_value = child.Min_Value(alpha, beta, depth+1);
			ArrayList<ArrayList<Integer>> a2 = move_value.getValue0();
			Integer v2 = move_value.getValue1();
			if(v2 > v) {
				v = child.heuristic;
				move = child.action_from_parent;
				alpha = Math.max(alpha, v);
			}
			if(v >= beta) {
				return Pair.with(move, v);
			}
		}
		return Pair.with(move, v);
	}
	
	public Pair<ArrayList<ArrayList<Integer>>, Integer> Min_Value(int alpha, int beta, int depth) {
		this.set_child_nodes();
		if(depth == DEPTH_LIMIT) {
			return this.best_move_pair_other();
		}
		int v = 200;
		ArrayList<ArrayList<Integer>> move = null;
		sort(this.child_nodes);
		for(GameTree child : this.child_nodes) {
			Pair<ArrayList<ArrayList<Integer>>, Integer> move_value = child.Max_Value(alpha, beta, depth+1);
			ArrayList<ArrayList<Integer>> a2 = move_value.getValue0();
			Integer v2 = move_value.getValue1();
			if(child.other_heuristic() < v) {
				v = v2;
				move = child.action_from_parent;
				beta = Math.min(beta, v);
			}
			if(v <= alpha) {
				return Pair.with(move, v);
			}
		}
		return Pair.with(move, v);
	}
	
	public Integer other_heuristic() {
		return this.board.get_heuristic(!next_move_white);
	}
	
	public void game_over() {
		int white_heuristic = board.get_heuristic(true);
		int black_heurisitc = board.get_heuristic(false);
		System.out.println("The white team as " + white_heuristic + " points");
		System.out.println("The black team as " + black_heurisitc + " points");
	}
	
}
