# A Two-player Tic Tac Toe Game

A GUI-enabled 2-player Tic Tac Toe Game.

Implemented by Java and socket programming.

Designed for 2022-23 2nd Sem COMP2396 A4.

Can be played between two players over a network. 

  
  
**How To Run the Game**

- To run from an IDE

  1) Compile Server.java and Client.java files.
  2) Run Server.java on the host machine
  3) Run Client.java on Player 1's machine
  4) Run Client.java on Player 2's machine


- To run from the terminal
  1) From the terminal, navigate to the directory containing Server.java
  2) Enter the following commands to set up the server

       javac Server.java

       java Server

  3) For Player 1, navigate to the directory containing Client.java 

      Enter the following commands to set up the game for Player 1:

       javac Client.java 

       java Client

  4) Repeat the last two steps to set the game for Player 2. 

      Enter the following commands on the terminal.
      
       javac Client.java 

       java Client

**Basic Information for the Game Play**

- This is a game for two players.

- The game board will be displayed on the client's screen.

- Players' move is indicated by X or O. 

- Clients can make their move by clicking on the corresponding box on the game board. 

- The game will continue until one player wins, or the game ends in a draw.

- If 2 players are all disconnected, the server will restart before starting a new game.
