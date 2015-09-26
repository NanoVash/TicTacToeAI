package com.nanovash.tictactoeai;

import lombok.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

@RequiredArgsConstructor
public class Game {

	private @NonNull @Getter UIWindow window;
	private @Setter Player[] players;
	private @Getter Tile[][] tiles = new Tile[3][3];
	private @Getter Random random = new Random();
	private Player p1;
	private Player p2;
	private int order = -1;

	public static final char PLAYER_1_CHAR = 'X';
	public static final char PLAYER_2_CHAR = 'O';

	public void init() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(new Font("Dudu Calligraphy", Font.PLAIN, 150));
				label.setBackground(Color.WHITE);
				tiles[x][y] = new Tile(label, new Location(x, y));
				window.contentPane.add(label);
			}
		}
		window.contentPane.repaint();
		window.contentPane.revalidate();
	}

	public void start() {
		order = -1;
		int r = getRandom().nextInt(2);
		p1 = players[r];
		p2 = players[1 - r];
		p1.setSymbol(PLAYER_1_CHAR);
		p2.setSymbol(PLAYER_2_CHAR);
		p1.setName();
		p2.setName();
		Player won;
		boolean tie = false;
		while ((won = won()) == null) {
			findTileByLocation(p1.startTurn()).setOwner(p1, ++order);
			if ((won = won()) != null || (tie = isTie())) break;
			findTileByLocation(p2.startTurn()).setOwner(p2, ++order);
		}
		p1.endGame();
		p2.endGame();
		JOptionPane.showMessageDialog(window, new JLabel(tie ? "Tie" : won.getName() + " won.", JLabel.CENTER), "Game over!", JOptionPane.PLAIN_MESSAGE);
	}

	public void clear() {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				tiles[x][y].setOwner(null, -1);
	}

	public boolean isTie() {
		boolean tie = false;
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				tie |= tiles[x][y].getOwner() == null;
		return !tie;
	}

	public Tile findTileByLocation(Location l) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				if (tiles[x][y].getLocation().equals(l))
					return tiles[x][y];
		return null;
	}

	public Location findLocationByOrder(int i) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				if(tiles[x][y].getOrder() == i)
					return tiles[x][y].getLocation();
		return null;
	}

	private Player won() {
		for (int x = 0; x < 3; x++) {
			Player p = tiles[x][1].getOwner();
			if (p == null) continue;
			if (tiles[x][0].getOwner() == p && tiles[x][2].getOwner() == p)
				return p;
			if (x == 1)
				if ((tiles[0][0].getOwner() == p && tiles[2][2].getOwner() == p) || (tiles[0][2].getOwner() == p && tiles[2][0].getOwner() == p))
					return p;
		}
		for (int y = 0; y < 3; y++) {
			Player p = tiles[1][y].getOwner();
			if (p != null && tiles[0][y].getOwner() == p && tiles[2][y].getOwner() == p)
				return p;
		}
		return null;
	}

	public String toString() {
		StringBuilder game = new StringBuilder();
		for(int i = 0; i <= order; i++) {
			Location l = findLocationByOrder(i);
			game.append(l.getX()).append(l.getY());
		}
		return game.toString();
	}
}