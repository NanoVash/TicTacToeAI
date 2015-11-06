package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.util.Location;
import com.nanovash.tictactoeai.util.Tile;
import com.nanovash.tictactoeai.windows.GameWindow;
import com.nanovash.tictactoeai.windows.UIWindow;
import lombok.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@RequiredArgsConstructor
public class Game {

	private @NonNull @Getter GameWindow window;
	private @Setter Player[] players;
	private @Getter Tile[][] tiles = new Tile[3][3];
	private @Getter Random random = new Random();
	private @Getter Player p1;
	private @Getter Player p2;
	private int order = -1;
    private @Getter @Setter boolean done = false;

	public static final char PLAYER_1_CHAR = 'X';
	public static final char PLAYER_2_CHAR = 'O';

    //Initializes the game
	public void init() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(UIWindow.createFont(label, 150));
				label.setBackground(Color.WHITE);
				tiles[x][y] = new Tile(label, new Location(x, y));
				window.getContentPane().add(label);
			}
		}
		window.getContentPane().repaint();
		window.getContentPane().revalidate();
	}

	public void start() {
		order = -1;
		int r = getRandom().nextInt(2);
		p1 = players[r];
		p2 = players[1 - r];
		p1.changeName(this);
		p2.changeName(this);
		p1.startGame(this);
		p2.startGame(this);
		Player won;
		boolean tie = false;
		while ((won = won()) == null) {
			findTile(p1.startTurn(this)).setOwner(p1, ++order, PLAYER_1_CHAR);
			if ((won = won()) != null || (tie = isTie())) break;
			findTile(p2.startTurn(this)).setOwner(p2, ++order, PLAYER_2_CHAR);
		}
		p1.endGame(this);
		p2.endGame(this);
		JOptionPane.showMessageDialog(null, new JLabel(tie ? "Tie" : won.getName() + " won.", JLabel.CENTER), "Game over!", JOptionPane.PLAIN_MESSAGE);
	}

	public void clear() {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				tiles[x][y].setOwner(null, -1, ' ');
	}

	public boolean isTie() {
		boolean tie = false;
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				tie |= tiles[x][y].getOwner() == null;
		return !tie;
	}

	public Tile findTile(Location l) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				if (tiles[x][y].getLocation().equals(l))
					return tiles[x][y];
		return null;
	}

	public Location findLocation(int i) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				if(tiles[x][y].getOrder() == i)
					return tiles[x][y].getLocation();
		return null;
	}

    //Checks if a player won and if one did it returns it
	private Player won() {
		for (int x = 0; x < 3; x++) {
			String p = tiles[x][1].getText();
			if (p == null) continue;
			if (tiles[x][0].getText().equals(p) && tiles[x][2].getText().equals(p))
				return tiles[x][1].getOwner();
			if (x == 1)
				if ((tiles[0][0].getText().equals(p) && tiles[2][2].getText().equals(p)) || (tiles[0][2].getText().equals(p) && tiles[2][0].getText().equals(p)))
					return tiles[x][1].getOwner();
		}
		for (int y = 0; y < 3; y++) {
			String p = tiles[1][y].getText();
			if (p != null && tiles[0][y].getText().equals(p) && tiles[2][y].getText().equals(p))
				return tiles[1][y].getOwner();
		}
		return null;
	}

	public String toString() {
		StringBuilder game = new StringBuilder();
		for(int i = 0; i <= order; i++) {
			Location l = findLocation(i);
			game.append(l.getX()).append(l.getY());
		}
		return game.toString();
	}
}