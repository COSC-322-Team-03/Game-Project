package AI;

import java.util.ArrayList;

public class MoveGenerator {
	// generate new move
	public ArrayList<ArrayList<Integer>> generate_new_move(Board gameboard, Boolean is_white){
		GameTree gametree = new GameTree(gameboard, is_white);
		ArrayList<ArrayList<Integer>> move =  gametree.alpha_beta_search(is_white);
		if(move != null) {
			return move;
		}
		System.out.println("Game Over");
		gametree.game_over();
		return null;
	}
}
