package AI;

import java.util.ArrayList;

public class MoveChecker {
	// TODO change this to ArrayList<ArrayList<Integer>> to match rest of the code
	private int state[][] = new int[10][10];
	private int N;
	// takes N which is the width and height of the Board
	public MoveChecker(int N, int[] initState) {
		 state = new int[N][N];
		 this.N = N;
		 this.setState(initState);
	}
	
	// set state indexed {x, y}
	private void setState(int[] initState) {
		for(int i = 0; i < initState.length; i++) {
			// using module N will give the column value and /N will give the row value 
			state[i%N][i/N] = initState[i];
		}
	}

	// if valid move update state 
	private void updateState(int lastX, int lastY, int newX, int newY, int arrowX, int arrowY ) {
		// set value in newX and newY to the value the old x,y location
		state[newX][newY] = state[lastX][lastY];
		// set old x, and y value to 0 
		state[lastX][lastY] = 0;
		// set arrow positon to be 3
		state[arrowX][arrowY] = 3;
//		can be uncommented to see changes to state
//		for(int i = 0; i < N; i++) {
//			for(int j = 0; j < N; j++) {
//				System.out.printf(state[j][i] + ", ");
//				if(j == N-1) {
//					System.out.println();
//				}
//			}
//		}
	}
	
	public int[][] getState(){
		return state; 
	}
// TODO doesn't check if there was anything in the way
	// TODO doesn't check the the correct piece was moved
	// checks if the move made by oppenent is valid
	public boolean isValid(int lastX, int lastY, int newX, int newY, int arrowX, int arrowY ) {
		// check the queen move is valid
		// then check arrow move is valid arrow is show from newX, and newY
		if(isValidHelper(lastX, lastY, newX, newY) && isValidHelper(newX, newY, arrowX, arrowY)) {
			// if valid set update state and return true
			updateState(lastX, lastY, newX, newY, arrowX, arrowY);
			return true;
		}
		return false;
		
	}
	
	private boolean isValidHelper(int lastX, int lastY, int newX, int newY) {
				// make sure move is valid
				boolean moveValid; 
				// find changes in x and y coords of queen
				int changeX = newX - lastX;
				int changeY = newY - lastY;
				
				// if there is a change in x and y it must be a diangol move
				if(changeX != 0 && changeY != 0) {
					moveValid = diagnolMove(lastX, lastY, newX, newY);
				}
				// if there is a change in x and not in y it must be a horizontal move
				else if(changeX != 0 && changeY == 0){
					moveValid = horziontalMove(lastX, newX, newY);
				}
				// if there is a change in y and not in x it must be a vertical move
				else if(changeX == 0 && changeY != 0){
					moveValid = verticalMove(lastY, newX, newY);
				}
				// if none of the prior conditions are met then 
				//the move was set to the current square which is invalid
				else {
					moveValid = false;
				}
				// retun if the move is valid
				return moveValid;
	}
	
	// checks if there are any pieces in between a move in a vertical direction if not returns true
	private boolean verticalMove( int lastY, int newX, int newY) {
		// newY > then lastY then iterate up the board to check if valid move
		if(newY > lastY) {
			// iterate from lastY to newY if any squares in between have a piece or an arrow return false
			for(int i = lastY + 1; i <= newY ; i++ ) {
				if(state[newX][i] != 0)
					return false;
			}
		}else {
			// iterate from lastY to newY if any squares in between have a piece or an arrow return false
			for(int i = lastY - 1; i >= newY ; i-- ) {
				if(state[newX][i] != 0)
					return false;
			}
			
		}
		
		return true;
	}

	// checks if there are any pieces in between a move in a horizontal direction if not returns true
	private boolean horziontalMove(int lastX, int newX, int newY) {

		// newX > then lastX then iterate right to left if valid move
		if(newX > lastX) {
			// iterate from lastY to newY if any squares in between have a piece or an arrow return false
			for(int i = lastX + 1; i <= newX ; i++ ) {
				if(state[i][newY] != 0)
					return false;
			}
			
		}else {
			// iterate from lastX to newX if any squares in between have a piece or an arrow return false
			for(int i = lastX - 1; i >= newX ; i-- ) {
				if(state[i][newY] != 0)
					return false;
			}
			
		}
		
		return true;
	}
	// checks if there are any pieces in between a move in a diagnolMove direction if not returns true
	public boolean diagnolMove(int lastX, int lastY, int newX, int newY){
		int changeX = newX - lastX;
		int changeY = newY - lastY; 
		// if absolute value of change x and change y is not equal
		//then the move is not on to one thus the move is not valid
		if(Math.abs(changeX) != Math.abs(changeY))
			return false;
		
		
		// based on direction check if a piece blocks a specific move if they do return false
		if(changeX > 0 && changeY > 0 ) {
			for(int i = lastX + 1; i <= newX; i++) {
				for(int j = lastY + 1; j <= newY; j++) {
					if(state[i][j] != 0)
						return false;
				}
			}
			
		}else if(changeX > 0 && changeY < 0){
			for(int i = lastX + 1; i <= newX; i++) {
				for(int j = lastY - 1; j >= newY; j--) {
					if(state[i][j] != 0)
						return false;
				}
			}
			
		}else if(changeX < 0 && changeY > 0) {
			for(int i = lastX - 1; i >= newY; i--) {
				for(int j = lastY + 1; j <= newY; j++) {
					if(state[i][j] != 0)
						return false;
				}
			}
		}else {
			for(int i = lastX - 1; i >= newY; i--) {
				for(int j = lastY - 1; j >= newY; j--) {
					if(state[i][j] != 0)
						return false;
				}
			}
		}	
		return true;
	}
	
	
	
	
}
