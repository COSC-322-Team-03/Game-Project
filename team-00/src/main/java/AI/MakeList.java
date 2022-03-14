package AI;

import java.util.*;

public class MakeList {
	private final int N;
	public MakeList(int N) {
		 this.N = N;
	}
	
	public ArrayList<int[]> createList(int[] initPos, int[] boardState) {
		ArrayList<int[]> movelist = new ArrayList<int[]>();
		int[] temp = new int[4];

		for (int Xq = 0; Xq < N; Xq++) {
			MoveChecker mc = new MoveChecker(N, boardState);
			temp[0] = Xq;
			for (int Yq = 0; Yq < N; Yq++) {
				temp[1] = Yq;
				for (int Xa = 0; Xa < N; Xa++) {
					temp[2] = Xa;
					for (int Ya = 0; Ya < N; Ya++) {
						temp[3] = Ya;
						if (mc.isValid(initPos[0], initPos[1], Xq, Yq,Xa,Ya)) movelist.add(temp);
					}
				}
			}
		}
		return movelist;
	}
}