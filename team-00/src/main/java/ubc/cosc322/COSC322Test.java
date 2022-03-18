
package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import AI.Board;
import AI.MoveChecker;
import AI.MoveGenerator;
import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
//import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * 
 * @author Yong Gao (yong.gao@ubc.ca) Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer {

	private GameClient gameClient = null;
	private BaseGameGUI gamegui = null;

	private String userName = null;
	private String passwd = null;
	private MoveChecker moveChecker = null;
	private int[] initState;
	private String blackUser;
	private String whiteUser;
	private Object gameState;
	private Board gameboard;
	private MoveGenerator mover;
	private Boolean is_white;
	/**
	 * The main method
	 * 
	 * @param args for name and passwd (current, any string would work)
	 */
	public static void main(String[] args) {
		// HumanPlayer player = new HumanPlayer();
		COSC322Test player = new COSC322Test(args[0], args[1]);

		if (player.getGameGUI() == null) {
			player.Go();
		} else {
			BaseGameGUI.sys_setup();
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					player.Go();
				}
			});
		}
	}

	/**
	 * Any name and passwd
	 * 
	 * @param userName
	 * @param passwd
	 */
	public COSC322Test(String userName, String passwd) {
		this.userName = userName;
		this.passwd = passwd;

		// To make a GUI-based player, create an instance of BaseGameGUI
		// and implement the method getGameGUI() accordingly
		this.gamegui = new BaseGameGUI(this);
		// on creation make moveChecker

		ArrayList<Integer> initState = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0,
				0));
		this.gameboard = new Board((ArrayList<Integer>) initState);
		System.out.println("Init Board " + gameboard.get_game_board());
		// on creation make moveChecker
//		moveChecker = new MoveChecker(10, initState);

	}

	@Override
	public void onLogin() {
		System.out.println(
				"Congratualations!!! " + "I am called because the server indicated that the login is successfully");
		System.out.println("The next step is to find a room and join it: "
				+ "the gameClient instance created in my constructor knows how!");
		List<Room> rooms = this.gameClient.getRoomList();
		for (Room room : rooms) {
			System.out.println(room);
		}
		this.gameClient.joinRoom(rooms.get(0).getName());

		System.out.println("after initialization");
		this.userName = gameClient.getUserName();

		if (gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}
	}

	@Override
	public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
		// This method will be called by the GameClient when it receives a game-related
		// message
		// from the server.

		// For a detailed description of the message types and format,
		// see the method GamePlayer.handleGameMessage() in the game-client-api
		// document.
		// System.out.println(messageType);
		System.out.println("msg type: " + messageType);
		System.out.println("msg details: " + msgDetails);
		switch (messageType) {
		case GameMessage.GAME_STATE_BOARD:
			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			System.out.println("Game State Board");
			break;
		case GameMessage.GAME_ACTION_MOVE:
			Boolean is_white = this.whiteUser.equals(this.userName);
			this.gamegui.updateGameState(msgDetails);
			ArrayList<Integer> QueenPosCurMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNextMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
			ArrayList<Integer> ArrowPosMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);
			ArrayList<Integer> QueenPosCur = new ArrayList<>(Arrays.asList(QueenPosCurMsg.get(0) - 1, QueenPosCurMsg.get(1) - 1));
			ArrayList<Integer> QueenPosNext = new ArrayList<>(Arrays.asList(QueenPosNextMsg.get(0) - 1, QueenPosNextMsg.get(1) - 1));
			ArrayList<Integer> ArrowPos = new ArrayList<>(Arrays.asList(ArrowPosMsg.get(0) - 1, ArrowPosMsg.get(1) - 1));
			this.gameboard.update_game_board(QueenPosCur, QueenPosNext, ArrowPos);
//			System.out.println("updated game state: " + this.gameboard.get_game_board());
//			
			ArrayList<ArrayList<Integer>> moveDetails = this.mover.generate_new_move(this.gameboard, is_white);
			ArrayList<Integer> generatedQueenPosCur = (ArrayList<Integer>) moveDetails.get(0);
			ArrayList<Integer> generatedQueenPosNew = (ArrayList<Integer>) moveDetails.get(1);
			ArrayList<Integer> generatedArrowPos = (ArrayList<Integer>) moveDetails.get(2);
			this.gameboard.update_game_board(generatedQueenPosCur, generatedQueenPosNew, generatedArrowPos);
			generatedQueenPosCur.set(0, generatedQueenPosCur.get(0) + 1);
			generatedQueenPosCur.set(1, generatedQueenPosCur.get(1) + 1);
			generatedQueenPosNew.set(0, generatedQueenPosNew.get(0) + 1);
			generatedQueenPosNew.set(1, generatedQueenPosNew.get(1) + 1);
			generatedArrowPos.set(0, generatedArrowPos.get(0) + 1);
			generatedArrowPos.set(1, generatedArrowPos.get(1) + 1);
			System.out.println("moved");
			System.out.println(generatedQueenPosCur);
			System.out.println(generatedQueenPosNew);
			System.out.println(generatedArrowPos);
			this.gameClient.sendMoveMessage(generatedQueenPosCur, generatedQueenPosNew, generatedArrowPos);
			break;
		case GameMessage.GAME_ACTION_START:
			this.mover = new MoveGenerator();
//			this.gameboard = new Board((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
			System.out.println("Black Players: " + this.blackUser);
			System.out.println("White Players: " + this.whiteUser);
			
			if(this.whiteUser.equals(this.userName)) {
				ArrayList<ArrayList<Integer>> initmoveDetails = this.mover.generate_new_move(this.gameboard, this.whiteUser.equals(this.userName));
				ArrayList<Integer> initgeneratedQueenPosCur = (ArrayList<Integer>) initmoveDetails.get(0);
				ArrayList<Integer> initgeneratedQueenPosNew = (ArrayList<Integer>) initmoveDetails.get(1);
				ArrayList<Integer> initgeneratedArrowPos = (ArrayList<Integer>) initmoveDetails.get(2);
				initgeneratedQueenPosCur.set(0, initgeneratedQueenPosCur.get(0) + 1);
				initgeneratedQueenPosCur.set(1, initgeneratedQueenPosCur.get(1) + 1);
				initgeneratedQueenPosNew.set(0, initgeneratedQueenPosNew.get(0) + 1);
				initgeneratedQueenPosNew.set(1, initgeneratedQueenPosNew.get(1) + 1);
				initgeneratedArrowPos.set(0, initgeneratedArrowPos.get(0) + 1);
				initgeneratedArrowPos.set(1, initgeneratedArrowPos.get(1) + 1);
				System.out.println("moved");
				System.out.println(initgeneratedQueenPosCur);
				System.out.println(initgeneratedQueenPosNew);
				System.out.println(initgeneratedArrowPos);
				this.gameClient.sendMoveMessage(initgeneratedQueenPosCur, initgeneratedQueenPosNew, initgeneratedArrowPos);
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public String userName() {
		return userName;
	}

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub
//		return  null;
		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		gameClient = new GameClient(userName, passwd, this);
	}

}// end of class
