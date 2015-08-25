package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.players.*;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class Game {

	public UIWindow window;
	public Tile[][] tiles = new Tile[3][3];
	public Player p1;
	public Player p2;

	public Game(UIWindow window) {
		this.window = window;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				JLabel label = new JLabel();
				label.setOpaque(true);
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setFont(new Font("Dudu Calligraphy", Font.PLAIN, 150));
				tiles[x][y] = new Tile(label, new Location(x, y));
				window.contentPane.add(label);
			}
		}
		/*List<Player> temp = new ArrayList<>();
		temp.add(new Human(window, this));
		temp.add(new AI(window, this));
		p1 = temp.get(new Random().nextInt(2));
		temp.remove(p1);
		p2 = temp.get(0);*/
		p1 = new Human(this, "X");
		p2 = new AI(this, "O");
		Player won;
		boolean tie = false;
		while ((won = won()) == null) {
			findTile(p1.startTurn()).setOwner(p1);
			if ((won = won()) != null || (tie = isTie())) break;
			findTile(p2.startTurn()).setOwner(p2);
		}
		JOptionPane.showMessageDialog(window, tie ? "Tie" : won);
	}

	private boolean isTie() {
		boolean tie = false;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				tie |= tiles[x][y].getOwner() == null;
			}
		}
		return !tie;
	}

	public Tile findTile(Location l) {
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				if (tiles[x][y].location.equals(l))
					return tiles[x][y];
		return null;
	}

	public Player won() {
		for (int x = 0; x < 3; x++) {
			Player p = tiles[x][1].getOwner();
			if (p == null) continue;
			if (tiles[x][0].getOwner() == p && tiles[x][2].getOwner() == p) {
				return p;
			}
			if (x == 1) {
				if ((tiles[0][0].getOwner() == p && tiles[2][2].getOwner() == p) || (tiles[0][2].getOwner() == p && tiles[2][0].getOwner() == p)) {
					return p;
				}
			}
		}
		for (int y = 0; y < 3; y++) {
			Player p = tiles[1][y].getOwner();
			if (p != null && tiles[0][y].getOwner() == p && tiles[2][y].getOwner() == p) {
				return p;
			}
		}
		return null;
	}
}
