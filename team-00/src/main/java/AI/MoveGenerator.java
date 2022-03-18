package AI;

import java.util.ArrayList;

public class MoveGenerator {
	
	public ArrayList<ArrayList<Integer>> generate_new_move(Board gameboard, Boolean is_white){
		GameTree gametree = new GameTree(gameboard, is_white);
		return gametree.next_move();
	}
}
