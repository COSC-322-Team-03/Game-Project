package AI;

import java.util.ArrayList;
import java.util.Arrays;
// Board class for storing the game board internally
public class Board {
	ArrayList<ArrayList<Integer>> game_board; // take in an array list of 100 integers
	ArrayList<Integer> b;
	public Board(ArrayList<Integer> board) {
		this.b = board;
		game_board = new ArrayList<ArrayList<Integer>>(10);
		for(int row=0; row < 10; row++) {
			ArrayList<Integer> curr_row = new ArrayList<Integer>(10);
			for(int col=0; col < 10; col++) {
				int position = row*10 + col;
				curr_row.add(board.get(position));
			}
			game_board.add(curr_row);
		}
	}
	// calculate the heurisitc based on the min-distance heurisitic
	public Integer get_heuristic(Boolean is_white) {
		int blackpoints = 0;
		int whitepoints = 0;
		int neutralpoints = 0;
		// for each point
		for(int x = 0; x < 10; x++) {
			for(int y =0; y < 10; y++) {
				// find the point and the closest black and white points
				ArrayList<Integer> point = new ArrayList<>(Arrays.asList(x, y));
				ArrayList<Integer> black = get_nearest_black_point(point);
				ArrayList<Integer> white = get_nearest_white_point(point);
				// find the distance between the point the the closest black and white queens
				double db = dist(point, black);
				double dw = dist(point, white);
				// add a point to the closest colour
				if(db < dw) {
					blackpoints = blackpoints + 1;
				} else if(db > dw) {
					whitepoints = whitepoints +1;
				} else {
					neutralpoints = neutralpoints + 1; // if equal distance neurtal point
				}
			}
		} // return how many points we have
		if(is_white) {
			return whitepoints;
		}
		return blackpoints;
	}
	// calculate the distance between two points 
	public double dist(ArrayList<Integer> point1, ArrayList<Integer> point2) {
		double distance;
		int x1 = point1.get(0);
		int y1 = point1.get(1);
		int x2 = point2.get(0);
		int y2 = point2.get(1);
		distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		return distance;
	}
	// get the closests white queen 
	public ArrayList<Integer> get_nearest_white_point(ArrayList<Integer> point){
		ArrayList<ArrayList<Integer>> queens = get_queen_locations(true); // get white queen locations
		ArrayList<Integer> closest = queens.get(0);
		double min_dist = Double.POSITIVE_INFINITY;
		for(ArrayList<Integer> queen : queens) {
			double distance = dist(point, queen);
			if(distance < min_dist) {
				min_dist = distance;
				closest = queen;
			}
		}
		return closest;
	}
	// get the closest black queen
	public ArrayList<Integer> get_nearest_black_point(ArrayList<Integer> point){
		ArrayList<ArrayList<Integer>> queens = get_queen_locations(false); // get black queen locations
		ArrayList<Integer> closest = queens.get(0);
		double min_dist = Double.POSITIVE_INFINITY;
		for(ArrayList<Integer> queen : queens) {
			double distance = dist(point, queen);
			if(distance < min_dist) {
				min_dist = distance;
				closest = queen;
			}
		}
		return closest;
	}
	
	public void print_game_board() {
		for(int i = 9; i >= 0; i--) {
			System.out.println(game_board.get(i));
		}
	}
	
	// return the game board
	public ArrayList<ArrayList<Integer>> get_game_board(){
		return game_board;
	}
	// update the game baord based on a move
	public void update_game_board(ArrayList<Integer> old_pos, ArrayList<Integer> new_pos, ArrayList<Integer> arrow_pos) {
		Integer val = game_board.get(old_pos.get(0)).get(old_pos.get(1));
		update_value(old_pos.get(0), old_pos.get(1), 0); // move current queen off old space
		update_value(new_pos.get(0), new_pos.get(1), val); // move current queen to new space
		update_value(arrow_pos.get(0), arrow_pos.get(1), 3); // set arrow
	}
	// update a value on the game board
	public void update_value(int col, int row, Integer new_val){
		ArrayList<Integer> curr_row = game_board.get(row);
		curr_row.set(col, new_val);
		game_board.set(row, curr_row);
	}
	// get the location of all the queens (true white queens) (false black queens)
	public ArrayList<ArrayList<Integer>> get_queen_locations(Boolean is_white) {
		ArrayList<ArrayList<Integer>> queen_locations = new ArrayList<ArrayList<Integer>>(4);
		Integer queen_val = 2;
		if(is_white) {
			queen_val = 1;
		}
		for(int row=0; row < 10; row++) {
			for(int col=0; col <10; col++) {
				Integer value = game_board.get(row).get(col);
				if(value.equals(queen_val)) {
					queen_locations.add(new ArrayList<>(Arrays.asList(col, row)));
				}
			}
		}
		return queen_locations;
	}
	// return the board as an array list of 100 values for cloning purposes
	public ArrayList<Integer> board_array_list() {
		ArrayList<Integer> board_array_list = new ArrayList<Integer>();
		for(int row=0; row < 10; row++) {
			board_array_list.addAll(game_board.get(row));
		}
		return board_array_list;
	}

}