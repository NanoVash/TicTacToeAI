# TicTacToeAI
This Tic Tac Toe game has support for the creation of additional players and it comes with 2 built in players, a Human and an AI.
The Human, obviously has to be a human, as it sets up listeners in the window waiting for a click by the user to continue the game, the AI however, knows nothing at the beginning and learns by playing, it saves all the games that it played in a file and uses those games based on the current board state to progress in the game.
Every player has its own Manager which is responsible for things like saving or deleting if needed and for providing the configuration UI.
