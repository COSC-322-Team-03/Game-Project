
package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	private String blackUser;
	private String whiteUser;
	private Object gameState;
	private Board gameboard;
	private MoveGenerator mover;
	private Boolean is_white;
	private ArrayList<Integer> initState;
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
		// White 1
		// Black 2
		// Index 0,0 Bottom Left
		// generate a new board
		this.initState = new ArrayList<Integer>(Arrays.asList(
				0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 2, 0, 0, 2, 0, 0, 0));
		this.gameboard = new Board((ArrayList<Integer>) initState);

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
		this.gameClient.joinRoom(rooms.get(16).getName());

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
		System.out.println("msg type: " + messageType);
		System.out.println("msg details: " + msgDetails);
		switch (messageType) {
		case GameMessage.GAME_STATE_BOARD:
			this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
			System.out.println("Game State Board");
			break;
		case GameMessage.GAME_ACTION_MOVE:
			// This message is sent after the other player has made a move
			
			// Check if we are white or black
			Boolean is_white = this.whiteUser.equals(this.userName);
			// TODO check if game move is valid
			// Update game state
			this.gamegui.updateGameState(msgDetails);
			// get the move from the other players
			ArrayList<Integer> QueenPosCurMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
			ArrayList<Integer> QueenPosNextMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_NEXT);
			ArrayList<Integer> ArrowPosMsg = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

			// our system indexes at 0; the msgDetails indexes at 1
			ArrayList<Integer> QueenPosCur = new ArrayList<>(Arrays.asList(QueenPosCurMsg.get(1) - 1, QueenPosCurMsg.get(0) - 1));
			ArrayList<Integer> QueenPosNext = new ArrayList<>(Arrays.asList(QueenPosNextMsg.get(1) - 1, QueenPosNextMsg.get(0) - 1));
			ArrayList<Integer> ArrowPos = new ArrayList<>(Arrays.asList(ArrowPosMsg.get(1) - 1, ArrowPosMsg.get(0) - 1));
			// validate opponents move 
			if(this.gameboard.isValid(QueenPosCur, QueenPosNext, ArrowPos, !is_white)) {
				System.out.println("Valid");
			} else {
				System.out.println("INVALID MOVE GAME OVER ");
				break;
			}
			// update our internal game board
			this.gameboard.update_game_board(QueenPosCur, QueenPosNext, ArrowPos);
			System.out.println("Opponent Move Game Board");
			this.gameboard.print_game_board();
//			// generate a new move
			ArrayList<ArrayList<Integer>> moveDetails = this.mover.generate_new_move(this.gameboard, is_white);
			if(moveDetails == null) {
				break;
			}
			ArrayList<Integer> generatedQueenPosCur = (ArrayList<Integer>) moveDetails.get(0);
			ArrayList<Integer> generatedQueenPosNew = (ArrayList<Integer>) moveDetails.get(1);
			ArrayList<Integer> generatedArrowPos = (ArrayList<Integer>) moveDetails.get(2);
			// update our internal game board
			this.gameboard.update_game_board(generatedQueenPosCur, generatedQueenPosNew, generatedArrowPos);
			// our system indexes at 0; the sendMoveMessage indexes at 1
			System.out.println("Updated Move Game Board");
			this.gameboard.print_game_board();
			ArrayList<Integer> genQueenPosCur = new ArrayList<>(Arrays.asList(generatedQueenPosCur.get(1) + 1, generatedQueenPosCur.get(0) + 1));
			ArrayList<Integer> genQueenPosNew = new ArrayList<>(Arrays.asList(generatedQueenPosNew.get(1) + 1, generatedQueenPosNew.get(0) + 1));
			ArrayList<Integer> genArrowPos = new ArrayList<>(Arrays.asList(generatedArrowPos.get(1) + 1, generatedArrowPos.get(0) + 1));
			// let users know we have moved and what our move is; for testing purposes
			System.out.println("Generated Move");
			System.out.println(genQueenPosCur);
			System.out.println(genQueenPosNew);
			System.out.println(genArrowPos);
			System.out.println("New Move Game Board");
			this.gameboard.print_game_board();
			this.gameClient.sendMoveMessage(genQueenPosCur, genQueenPosNew, genArrowPos);
			// update gamegui of our new move
			Map<String, Object> movemsgDetails = new HashMap();
			movemsgDetails.put(AmazonsGameMessage.QUEEN_POS_CURR, genQueenPosCur);
			movemsgDetails.put(AmazonsGameMessage.QUEEN_POS_NEXT, genQueenPosNew);
			movemsgDetails.put(AmazonsGameMessage.ARROW_POS, genArrowPos);
			this.gamegui.updateGameState(movemsgDetails);
			break;
		case GameMessage.GAME_ACTION_START:
			// this is called when a game has just started
			this.mover = new MoveGenerator();
			this.moveChecker = new MoveChecker(10, this.initState, 1);
			// determine which colour our players is
			this.blackUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_BLACK);
			this.whiteUser = (String) msgDetails.get(AmazonsGameMessage.PLAYER_WHITE);
			// print out who is each colour for testing and human player purposes
			System.out.println("Black Players: " + this.blackUser);
			System.out.println("White Players: " + this.whiteUser);
			
			if(this.blackUser.equals(this.userName)) {
				// if we are the black player we move first
				// generate the first move
				System.out.println(this.whiteUser.equals(this.userName));
				ArrayList<ArrayList<Integer>> openningMoveDetails = this.mover.generate_new_move(this.gameboard, this.whiteUser.equals(this.userName));
				if(openningMoveDetails == null) {
					break;
				}
				ArrayList<Integer> initalQueenPosCur = (ArrayList<Integer>) openningMoveDetails.get(0);
				ArrayList<Integer> initalQueenPosNew = (ArrayList<Integer>) openningMoveDetails.get(1);
				ArrayList<Integer> initalArrowPos = (ArrayList<Integer>) openningMoveDetails.get(2);
				// update our internal game board
				this.gameboard.update_game_board(initalQueenPosCur, initalQueenPosNew, initalArrowPos);
				// our system indexes at 0; the sendMoveMessage indexes at 1
				ArrayList<Integer> initQueenPosCur = new ArrayList<>(Arrays.asList(initalQueenPosCur.get(1) + 1, initalQueenPosCur.get(0) + 1));
				ArrayList<Integer> initQueenPosNew = new ArrayList<>(Arrays.asList(initalQueenPosNew.get(1) + 1, initalQueenPosNew.get(0) + 1));
				ArrayList<Integer> initArrowPos = new ArrayList<>(Arrays.asList(initalArrowPos.get(1) + 1, initalArrowPos.get(0) + 1));
				// let users know we have moved and what our move is; for testing purposes
				System.out.println("Generated Move");
				System.out.println(initQueenPosCur);
				System.out.println(initQueenPosNew);
				System.out.println(initArrowPos);
				this.gameClient.sendMoveMessage(initQueenPosCur, initQueenPosNew, initArrowPos);
				Map<String, Object> initMovemsgDetails = new HashMap();
				initMovemsgDetails.put(AmazonsGameMessage.QUEEN_POS_CURR, initQueenPosCur);
				initMovemsgDetails.put(AmazonsGameMessage.QUEEN_POS_NEXT, initQueenPosNew);
				initMovemsgDetails.put(AmazonsGameMessage.ARROW_POS, initArrowPos);
				this.gamegui.updateGameState(initMovemsgDetails);
				break;
			}
			// if we are not the black player wait for the other player to make the opening move
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
