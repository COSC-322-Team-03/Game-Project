
package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import AI.MoveChecker;
import sfs2x.client.entities.Room;
import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GameMessage;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.AmazonsGameMessage;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;
    private MoveChecker moveChecker = null;
	private int[] initState; 
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {		
    	//HumanPlayer player = new HumanPlayer();
    	COSC322Test player = new COSC322Test(args[0], args[1]);
    	
    	
    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
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
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);
    	// on creation make moveChecker
    	
    	initState = new int[]{0, 0, 0, 1, 0, 0, 1, 0, 0, 0,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    			0, 0, 0, 2, 0, 0, 2, 0, 0, 0};
    			
    			
    	// on creation make moveChecker
    	moveChecker = new MoveChecker(10, initState);
    	
    }
 


    @Override
    public void onLogin() {
//    	System.out.println("Congratualations!!! "
//    			+ "I am called because the server indicated that the login is successfully");
//    	System.out.println("The next step is to find a room and join it: "
//    			+ "the gameClient instance created in my constructor knows how!"); 
//    	List<Room> rooms = this.gameClient.getRoomList();
//    	for (Room room : rooms) {
//    		System.out.println(room);
//    	}
//    	this.gameClient.joinRoom(rooms.get(0).getName());
    	
    	System.out.println("after initialization");
    	this.userName = gameClient.getUserName();
    	
    	if(gamegui != null) {
    		gamegui.setRoomInformation(gameClient.getRoomList());
    	}
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
	
    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	//System.out.println(messageType);
    	
    
    	
    	switch(messageType) {
			case GameMessage.GAME_STATE_BOARD:
				this.gamegui.setGameState((ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.GAME_STATE));
				break;
			case GameMessage.GAME_ACTION_MOVE:
				// input for valid function
				int[] input = new int[6];
		    	int cur = 0; 
		    	
		    	System.out.println("msgDetails "+ msgDetails);
		    	// read in moves to array of input for is move valid function
		    	for(Map.Entry<String, Object> m:msgDetails.entrySet()){   
		    		ArrayList<Integer> in = (ArrayList<Integer>) m.getValue();
		    		// every move is indexed {y, x} so convert to {x, y}
		    		// every move is also indexed 1-10. so convert to 1-9 by subtracting 1
		    		input[cur++] = in.get(1) - 1;
		    		input[cur++] = in.get(0) - 1;
		    	}  
		    	// all moves are indexed from 1-10 so subtract 1 to make 0-9
		    	System.out.println(moveChecker.isValid(input[0], input[1], input[2], input[3], input[4], input[5]));
		    	
		    	this.gamegui.updateGameState(msgDetails);
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

 
}//end of class
