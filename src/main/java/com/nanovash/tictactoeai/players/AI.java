package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.Location;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.Tile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AI extends Player {

	private @Getter List<String> games;

	public AI(Game game, String name, List<String> games) {
		super(game);
		setName(name);
		this.games = games;
	}

	@Override
	public Location startTurn() {
		String game = getGame().toString();
		List<String> locations = new ArrayList<>();
		for(String g : games)
			if(((g.length() % 4 != 0) == (getSymbol() == Game.PLAYER_1_CHAR)) && g.startsWith(game)) {
				System.out.println(g.substring(game.length(), game.length() + 2));
				locations.add(g.substring(game.length(), game.length() + 2));
			}
		if(!locations.isEmpty())
			return Location.fromString(locations.get(getGame().getRandom().nextInt(locations.size())));
		return markRandomly();
	}

	public Location markRandomly() {
		List<Tile> tiles = new ArrayList<>();
		Tile t;
		for(int x = 0; x < 3; x++)
			for(int y = 0; y < 3; y++)
				if((t = getGame().getTiles()[x][y]).getOwner() == null)
					tiles.add(t);
		return tiles.get(getGame().getRandom().nextInt(tiles.size())).getLocation();
	}

	private <T> T chooseWithChance(T[] el, int[] chance) {
		if (el.length != chance.length) throw new IllegalArgumentException("el and chance have to be of the same length!");
		long total = 0;
		for (int i : chance) total += i;
		double[] pChance = new double[chance.length];
		for (int i = 0; i < chance.length; i++)
			pChance[i] = chance[i] / total;
		for (int i = 0; i < el.length; i++)
			if (Math.random() < pChance[i])
				return el[i];
		return el[getGame().getRandom().nextInt(el.length)];
	}

	@Override
	public void endGame() {
		String game = getGame().toString();
		System.out.println(game);
		if(!games.contains(game) && !getGame().isTie())
			games.add(game);
	}

	@Override
	public void setName() {}
}