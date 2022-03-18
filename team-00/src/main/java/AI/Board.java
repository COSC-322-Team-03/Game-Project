package AI;

import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable {
	ArrayList<ArrayList<Integer>> game_board;
	public Board(ArrayList<Integer> board) {
		System.out.println("Board: " + board);
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
	
	public Integer get_heuristic(Boolean is_white) {
		int blackpoints = 0;
		int whitepoints = 0;
		int neutralpoints = 0;
		for(int x = 0; x < 10; x++) {
			for(int y =0; y < 10; y++) {
				ArrayList<Integer> point = new ArrayList<>(Arrays.asList(x, y));
				ArrayList<Integer> black = get_nearest_black_point(point);
				ArrayList<Integer> white = get_nearest_white_point(point);
				double db = dist(point, black);
				double dw = dist(point, white);
				if(db < dw) {
					blackpoints = blackpoints + 1;
				} else if(db > dw) {
					whitepoints = whitepoints +1;
				} else {
					neutralpoints = neutralpoints + 1;
				}
			}
		}
		if(is_white) {
			return whitepoints;
		}
		return blackpoints;
	}
	
	public double dist(ArrayList<Integer> point1, ArrayList<Integer> point2) {
		double distance;
		int x1 = point1.get(0);
		int y1 = point1.get(1);
		int x2 = point2.get(0);
		int y2 = point2.get(1);
		distance = Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		return distance;
	}
	
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
	
	
	public ArrayList<ArrayList<Integer>> get_game_board(){
		return game_board;
	}
	
	public void update_game_board(ArrayList<Integer> old_pos, ArrayList<Integer> new_pos, ArrayList<Integer> arrow_pos) {
		Integer val = game_board.get(old_pos.get(1)).get(old_pos.get(0));
		update_value(old_pos.get(1), old_pos.get(0), 0); // move current queen off old space
		update_value(new_pos.get(1), new_pos.get(0), val); // move current queen to new space
		update_value(arrow_pos.get(1), arrow_pos.get(0), 3); // set arrow
	}
	
	public void update_value(int col, int row, Integer new_val){
		ArrayList<Integer> curr_row = game_board.get(row);
		curr_row.set(col, new_val);
		game_board.set(row, curr_row);
	}
	
	public ArrayList<ArrayList<Integer>> get_queen_locations(Boolean is_white) {
		ArrayList<ArrayList<Integer>> queen_locations = new ArrayList<ArrayList<Integer>>(4);
		Integer queen_val = 1;
		if(is_white) {
			queen_val = 2;
		}
		for(int row=0; row < 10; row++) {
			for(int col=0; col <10; col++) {
				int value = game_board.get(row).get(col);
				if(game_board.get(row).get(col) == queen_val) {
					queen_locations.add(new ArrayList<>(Arrays.asList(col, row)));
				}
			}
		}
		return queen_locations;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
