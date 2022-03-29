package AI;

import java.util.ArrayList;

public class MoveGenerator {
	// generate new move
	public ArrayList<ArrayList<Integer>> generate_new_move(Board gameboard, Boolean is_white){
		GameTree gametree = new GameTree(gameboard, is_white, null, null);
		ArrayList<ArrayList<Integer>> move = gametree.alpha_beta_next_move(); // gametree.depth_limited_next_move();// gametree.next_move();
		if(move != null) {
			return move;
		}
		System.out.println("Game Over");
		gametree.game_over();
		return null;
	}
}
