import java.util.*;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;
/**
 * @filename: Server.java
 * @author LEE
 * @category Server program
 */
public class Server {
	/**
	 * @param moveOfThePlayer	The player number whose move it is. Receives value from server
	 * @param winner			The winner's player number (0 if no winner yet, 3 if draw) Receives value from server
	 * @param numOfPlayers		The number of players
	 * @param moveCounter		The total number of moves
	 * @param board[][]			3x3 array which indicates the game board
	 * @param result 			stores a message which is shown to the clients
	 */
	int moveOfThePlayer, winner, numOfPlayers, moveCounter;
	int board[][];
	String result;
	private ServerSocket serverSocket;
	private Set<PrintWriter> writers = new HashSet<>();
	/**
	 * @Constructor Server()	establish a server for the game
	 * @param serverSocket		Initialize instance variables
	 */
	Server(ServerSocket serverSocket) throws IOException{
		moveOfThePlayer = 1;
		winner = 0;
		numOfPlayers = 0;
		moveCounter = 0;
		board = new int[3][3];
		for(int i=0; i<3; i++) {
			for(int j=0;j<3; j++) {
				board[i][j] = 0;
			}
		}
		this.serverSocket = serverSocket;
		result = "";
	}
	/**
	 * @Method makeMove()		Makes the player move if the move is valid
	 * 							Calls sendToClients() to update the message which is shown to the clients
	 * @param str				Takes in str which is in the format - <playerNumber><row><column>
	 */
	void makeMove(String str) {
		if(numOfPlayers==2) {
			int whichPlayer = Integer.parseInt(String.valueOf(str.charAt(0)));
			int row = Integer.parseInt(String.valueOf(str.charAt(1)));
			int col = Integer.parseInt(String.valueOf(str.charAt(2)));
			if(whichPlayer == moveOfThePlayer && board[row][col]==0) {
				board[row][col] = whichPlayer;
				moveCounter++;
				togglePlayer();
			}
			result = sendToClients();
		}
	}
	/**
	 * @Method togglePlayer()		Change a player to another player after a move is made 
	 */
	void togglePlayer() {
		if(moveOfThePlayer == 1) {
			moveOfThePlayer = 2;
		} else if(moveOfThePlayer == 2) {
			moveOfThePlayer = 1;
		}
	}
	/**
	 * @Method checkWinner()		Determines the winner or decides whether the game is draw
	 * 								Calls checkRows(), checkColumns() and checkDiagonals()
	 * 								winner = 1 if player one wins
	 * 								winner = 2 if player two wins
	 * 								winner = 3 if draw 
	 */
	void checkWinner() {
		checkRows();
		checkColumns();
		checkDiagonals();
		if(winner==0 && moveCounter==9) {
			winner = 3;
		}
	}
	/**
	 * @Method checkRows()		Check the rows to see if there's a winner
	 */
	void checkRows() {
		if(board[0][0]==board[0][1] && board[0][1]==board[0][2]) {
			if(board[0][0]==1) {
				winner = 1;
			} else if(board[0][0]==2) {
				winner = 2;
			}
		}
		else if(board[1][0]==board[1][1] && board[1][1]==board[1][2]) {
			if(board[1][0]==1) {
				winner = 1;
			} else if(board[1][0]==2) {
				winner = 2;
			}
		}
		else if(board[2][0]==board[2][1] && board[2][1]==board[2][2]) {
			if(board[2][0]==1) {
				winner = 1;
			} else if(board[2][0]==2) {
				winner = 2;
			}
		}
	}
	/**
	 * @Method checkColumns()		Check the columns to see if there's a winner
	 */
	void checkColumns() {
		if(board[0][0]==board[1][0] && board[1][0]==board[2][0]) {
			if(board[0][0]==1) {
				winner = 1;
			} else if(board[0][0]==2) {
				winner = 2;
			}
		}
		else if(board[0][1]==board[1][1] && board[1][1]==board[2][1]) {
			if(board[0][1]==1) {
				winner = 1;
			} else if(board[0][1]==2) {
				winner = 2;
			}
		}
		else if(board[0][2]==board[1][2] && board[1][2]==board[2][2]) {
			if(board[0][2]==1) {
				winner = 1;
			} else if(board[0][2]==2) {
				winner = 2;
			}
		}
	}
	/**
	 * @Method checkDiagonals()		Check the diagonals to see if there's a winner
	 */
	void checkDiagonals() {
		if(board[1][1]==board[0][0] && board[1][1]==board[2][2]) {
			if(board[1][1]==1) {
				winner = 1;
			} else if(board[1][1]==2) {
				winner = 2;
			}
		}
		else  if(board[1][1]==board[0][2] && board[1][1]==board[2][0]) {
			if(board[1][1]==1) {
				winner = 1;
			} else if(board[1][1]==2) {
				winner = 2;
			}
		}
	}
	/**
	 * @Method reset()		Reset the instance variable values
	 */
	void reset() {
		moveOfThePlayer = 1;
		winner = 0;
		numOfPlayers = 0;
		moveCounter = 0;
		result = "";
		for(int i=0; i<3; i++) {
			for(int j=0;j<3; j++) {
				board[i][j] = 0;
			}
		}
	}
	/**
	 * @Method sendToClients()		Returns a string that will be shown to the clients as a message
	 * 								Format: <values of the 9 board cells - 0 for empty/1 for X/2 for O><moveOfThePlayer><winner>
	 */
	String sendToClients() {
		String str = "";
		for(int i=0; i<3;i++) {
			for(int j=0; j<3; j++) {
				str += Integer.toString(board[i][j]);
			}
		}
		str += Integer.toString(moveOfThePlayer);
		checkWinner();
		str += Integer.toString(winner);
		return str;
	}
	/**
	 * @Method main()		Set up the server
	 */
	public static void main(String args[]) {
		System.out.println("Server is running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server stopped.");
			}
		}));
		
		try (var listener = new ServerSocket(55277)) {
			Server myServer = new Server(listener);
			myServer.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * @Method start()		Start the threads after connecting the clients
	 */
	public void start() {
		var pool = Executors.newFixedThreadPool(200);
		int clientCount = 0;
		while (true) {
			try {
				Socket s1 = serverSocket.accept();
				System.out.println("Connected to client " + ++clientCount);
				PrintWriter output1 = new PrintWriter(s1.getOutputStream(), true);
				output1.println("1");
				Socket s2 = serverSocket.accept();
				System.out.println("Connected to client " + ++clientCount);
				PrintWriter output2 = new PrintWriter(s2.getOutputStream(), true);
				output2.println("2");
				Scanner input1 = new Scanner(s1.getInputStream());
				Scanner input2 = new Scanner(s2.getInputStream());
				while(!input1.hasNextLine()) {}
				numOfPlayers++;
				String temp = input1.nextLine();
				while(!input2.hasNextLine()) {}
				numOfPlayers++;
				temp = input2.nextLine();
				writers.add(output1);
				writers.add(output2);
				output1.println("Players ready.");
				output2.println("Players ready.");
				pool.execute(new Handler(s1, output1, input1));
				pool.execute(new Handler(s2, output2, input2));				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class Handler implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;
		
		public Handler(Socket socket, PrintWriter output, Scanner input) {
			this.socket = socket;
			this.output = output;
			this.input = input;
		}
		
		@Override
		public void run() {
			System.out.println("Connected: "+socket);
			try {
				while(input.hasNextLine()) {
					numOfPlayers = 2;
					String command = input.nextLine();
					makeMove(command);
					for(PrintWriter writer : writers) {
						writer.println(result);
					}
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			} finally {
				if(output != null) {
					writers.remove(output);
					for(PrintWriter writer : writers) {
						writer.println("Game ends. The other player left.");
					}
					numOfPlayers--;
					System.out.println("Player left.");
					reset();
				}
			}
		}
	}
}
