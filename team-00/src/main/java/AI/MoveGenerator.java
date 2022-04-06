package AI;

import java.util.ArrayList;

public class MoveGenerator {
	// generate new move
	public ArrayList<ArrayList<Integer>> generate_new_move(Board gameboard, Boolean is_white){
		GameTree gametree = new GameTree(gameboard, is_white, null, null);
		ArrayList<ArrayList<Integer>> move = gametree.alpha_beta_next_move(); // alpha_beta depth_limited next move
//		ArrayList<ArrayList<Integer>> move = gametree.depth_limited_next_move(); // next move based off depth-limited searhc
//		ArrayList<ArrayList<Integer>> move = gametree.next_move(); // next move based off best heurisitc at current depth
		if(move != null) {
			return move;
		}
		System.out.println("Game Over");
		gametree.game_over();
		return null;
	}
}
