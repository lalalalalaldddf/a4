import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
/**
 * @filename: Client.java
 * @author LEE
 * @category Client program
 */

public class Client {
	/**
	 * @param moveOfThePlayer	stores the player number whose move it is. Receives value from server
	 * @param winner			stores the winner's player number (0 if no winner yet, 3 if draw) Receives value from server
	 * @param playerNo			stores the player number of the client
	 * @param String n			stores the player name
	 * @param playersMove		stores the message to send to the server, indicating the player's move
	 */
	JMenuBar menu;
	JMenu control, help;
	JMenuItem exit, instruction;
	JLabel info;
	JFrame frame;
	JTextField name;
	JButton submit;
	JButton b00, b01, b02, b10, b11, b12, b20, b21, b22;
	JPanel grid;
	int moveOfThePlayer;
	int winner;
	int playerNo;
	String n, playersMove;
	
	Client() {
		/**
		 * Constructor name: Client()
		 * Set up the GUI
		 * @param help				stores a value for the "Help" tab on the menu
		 * @param exit				stores a value for the "Exit" tab on the menu 
		 */
		frame = new JFrame("Tic Tac Toe");
		menu = new JMenuBar();
		control = new JMenu("Control");
		help = new JMenu("Help");
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		instruction = new JMenuItem("Instruction");
		instruction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Some information about the game:\nCriteria for a valid move:\n-The move is not occupied by any mark.\n-The move is made in the player's turn.\n-The move is made within the 3 x 3 board.\nThe game would continue and switch among the opposite player until it reaches either one of the following conditions:\n-Player 1 wins.\n-Player 2 wins.\n-Draw.");
			}
		});
		
		control.add(exit);
		help.add(instruction);
		menu.add(control);
		menu.add(help);
		/**
		 *  **Set up the info label and the game board
		 * @param info				stores a value for the label to let the users know to enter the player name
		 * @param grid				stores a value for the panel for the game board  
		 */
		info = new JLabel("Enter your player name...");
		info.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
		infoPanel.add(info);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(infoPanel);

		grid = new JPanel();
		grid.setSize(400, 400);
		b00 = new JButton();
		b01 = new JButton();
		b02 = new JButton();
		b10 = new JButton();
		b11 = new JButton();
		b12 = new JButton();
		b20 = new JButton();
		b21 = new JButton();
		b22 = new JButton();
		b00.setEnabled(false);
		b01.setEnabled(false);
		b02.setEnabled(false);
		b10.setEnabled(false);
		b11.setEnabled(false);
		b12.setEnabled(false);
		b20.setEnabled(false);
		b21.setEnabled(false);
		b22.setEnabled(false);
		grid.setLayout(new GridLayout(3,3));
		grid.add(b00);
		grid.add(b01);
		grid.add(b02);
		grid.add(b10);
		grid.add(b11);
		grid.add(b12);
		grid.add(b20);
		grid.add(b21);
		grid.add(b22);
		mainPanel.add(grid);
		/**
		 * @param bottom			set up the Bottom panel including Submit button
		 * @param frame				set up the frame
		 */
		JPanel bottom = new JPanel();
		bottom.setSize(20,20);
		name = new JTextField(20);
		submit = new JButton("Submit");
		bottom.add(name);
		bottom.add(submit);
		mainPanel.add(bottom);
		
		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 500);
		frame.getContentPane().add(mainPanel);
		frame.setVisible(true);
		moveOfThePlayer = 1;
		winner = 0;
		playersMove = "";
	}
	/**  
	 * @method getBoard()		Set up the game board using details. format: <values of 9 cells><moveOfThePlayer><winner>
	 * @param details			import the board details from the server
	 */
	void getBoard(String details) {
		changeButton(b00, details.charAt(0));
		changeButton(b01, details.charAt(1));
		changeButton(b02, details.charAt(2));
		changeButton(b10, details.charAt(3));
		changeButton(b11, details.charAt(4));
		changeButton(b12, details.charAt(5));
		changeButton(b20, details.charAt(6));
		changeButton(b21, details.charAt(7));
		changeButton(b22, details.charAt(8));
		moveOfThePlayer = Integer.parseInt(String.valueOf(details.charAt(9)));
		winner = Integer.parseInt(String.valueOf(details.charAt(10)));
	}
	/**  
	 * @method changeButton()	Change a button to the corresponding mark(X or O) if a player's move is valid
	 * @param JButton btn		A button that a move of the player is performed
	 * @param char plr 			A player that made the move
	 */
	
	void changeButton(JButton btn, char plr) {
		if(plr=='1') {
			btn.setFont(new Font("Arial", Font.BOLD, 36));
			btn.setText("X");
			btn.setForeground(Color.GREEN);
			btn.setEnabled(true);
		} else if(plr=='2') {
			btn.setFont(new Font("Arial", Font.BOLD, 36));
			btn.setText("O");
			btn.setForeground(Color.RED);
			btn.setEnabled(true);
		}
	}
	/**  
	 * @method main()	Main method. Set up the client's thread 
	 * 					and connect to the server with the help of inner classes
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Client tttc = new Client();
				Controller controller = new Controller(tttc);
				controller.start();
			}
		});
	}
	
	public static class Controller {
		private Socket socket;
		private Scanner in;
		private PrintWriter out;
		private Client obj;
		
		Controller(Client t) {
			obj = t;
		}
		
		public void start() {
			try {
				this.socket = new Socket("127.0.0.1", 55277);
				this.in = new Scanner(socket.getInputStream());
				this.out = new PrintWriter(socket.getOutputStream(), true);
			} catch(UnknownHostException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			Thread handler = new ClinetHandler(socket);
			handler.start();
		}
		
		class ClinetHandler extends Thread {
			private Socket socket;
			
			public ClinetHandler(Socket socket) {
				this.socket = socket;
			}
			
			@Override
			public void run() {
				try {
					obj.playerNo = Integer.parseInt(in.nextLine());
					writeNameToServer();
					readFromServer();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		public void writeNameToServer() throws Exception {
			try {
				obj.submit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!obj.name.getText().contentEquals("")) {
							obj.n = obj.name.getText();
							obj.name.setEditable(false);
							obj.submit.setEnabled(false);
							obj.frame.setTitle("Tic Tac Toe - Player: "+obj.n);
							obj.info.setText("Welcome, "+obj.n+"!");
							obj.playersMove = obj.n;
							out.println(obj.playersMove);
							obj.playersMove = "";
							
						}
					}
				});
				writeCommandToServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void writeCommandToServer() throws Exception {
			try {
				obj.b00.addActionListener(new MoveListener());
				obj.b01.addActionListener(new MoveListener());
				obj.b02.addActionListener(new MoveListener());
				obj.b10.addActionListener(new MoveListener());
				obj.b11.addActionListener(new MoveListener());
				obj.b12.addActionListener(new MoveListener());
				obj.b20.addActionListener(new MoveListener());
				obj.b21.addActionListener(new MoveListener());
				obj.b22.addActionListener(new MoveListener());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		class MoveListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				obj.playersMove = "";
				if(obj.moveOfThePlayer == obj.playerNo) {
					obj.playersMove += Integer.toString(obj.playerNo);
					if((JButton) e.getSource()==obj.b00) {
						obj.playersMove += "00";
					} else if((JButton) e.getSource()==obj.b01) {
						obj.playersMove += "01";
					} else if((JButton) e.getSource()==obj.b02) {
						obj.playersMove += "02";
					} else if((JButton) e.getSource()==obj.b10) {
						obj.playersMove += "10";
					} else if((JButton) e.getSource()==obj.b11) {
						obj.playersMove += "11";
					} else if((JButton) e.getSource()==obj.b12) {
						obj.playersMove += "12";
					} else if((JButton) e.getSource()==obj.b20) {
						obj.playersMove += "20";
					} else if((JButton) e.getSource()==obj.b21) {
						obj.playersMove += "21";
					} else if((JButton) e.getSource()==obj.b22) {
						obj.playersMove += "22";
					}
					out.println(obj.playersMove);
				}
				
			}
		}
		
		public void readFromServer() throws Exception {
			try {
				while(!in.hasNextLine()) {}
				while(in.hasNextLine()) {
					String command  = in.nextLine();
					if(command.contentEquals("Players ready.")) {
						obj.b00.setEnabled(true);
						obj.b01.setEnabled(true);
						obj.b02.setEnabled(true);
						obj.b10.setEnabled(true);
						obj.b11.setEnabled(true);
						obj.b12.setEnabled(true);
						obj.b20.setEnabled(true);
						obj.b21.setEnabled(true);
						obj.b22.setEnabled(true);
					}
					else if(command.equals("Game ends. One of the players left.")) {
						JOptionPane.showMessageDialog(obj.frame, command);
						socket.close();
						System.exit(0);
					} else {
						obj.getBoard(command);
						if(obj.playerNo==obj.moveOfThePlayer) {
							obj.info.setText("Your opponent has moved, now is your turn.");
							writeCommandToServer();
						} else {
							obj.info.setText("Valid move, wait for your opponent.");
						}
						if(obj.winner!=0) {
							if(obj.winner==3) {
								JOptionPane.showMessageDialog(obj.frame, "Draw.");
							} else if(obj.winner==obj.playerNo) {
								JOptionPane.showMessageDialog(obj.frame, "Congratulations. You Win.");
							} else {
								JOptionPane.showMessageDialog(obj.frame, "You lose.");
							}
							System.exit(0);
						}
					}
					while(!in.hasNextLine()) {}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}
}
