package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.Location;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.Tile;
import lombok.Getter;

import java.util.*;

public class AI extends Player {

	private @Getter HashMap<String, Integer> games;

	public AI(Game game, String name, HashMap<String, Integer> games) {
		super(game);
		setName(name);
		this.games = games;
	}

	@Override
	public Location startTurn() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String game = getGame().toString();
		List<String> el = new ArrayList<>();
		List<Integer> chance = new ArrayList<>();
		for(String g : games.keySet())
			if(((g.length() % 4 != 0) == (getSymbol() == Game.PLAYER_1_CHAR)) && g.startsWith(game)) {
				System.out.println(g.substring(game.length(), game.length() + 2));
				el.add(g.substring(game.length(), game.length() + 2));
				chance.add(games.get(g));
			}
		if(!el.isEmpty())
			return Location.fromString(chooseWithChance(el, chance));
		return markRandomly();
	}

	private Location markRandomly() {
		List<Tile> tiles = new ArrayList<>();
		Tile t;
		for(int x = 0; x < 3; x++)
			for(int y = 0; y < 3; y++)
				if((t = getGame().getTiles()[x][y]).getOwner() == null)
					tiles.add(t);
		return tiles.get(getGame().getRandom().nextInt(tiles.size())).getLocation();
	}

	private String chooseWithChance(List<String> el, List<Integer> chance) {
		if(el.size() != chance.size()) throw new IllegalArgumentException("el and chance have to be of the same length!");
		long total = 0;
		for(int i : chance) total += i;
		System.out.println(total);
		double[] pChance = new double[chance.size()];
		for(int i = 0; i < chance.size(); i++)
			pChance[i] = (double) chance.get(i) / total;
		System.out.println(Arrays.toString(pChance));
		double random = Math.random();
		for(int i = 0; i < el.size(); i++)
			if(random < pChance[i])
				return el.get(i);
		return el.get(getGame().getRandom().nextInt(el.size()));
	}

	@Override
	public void endGame() {
		String game = getGame().toString();
		System.out.println(game);
		if(getGame().isTie()) return;
		if(games.keySet().contains(game))
			games.put(game, games.get(game) + (((game.length() % 4 != 0) == (getSymbol() == Game.PLAYER_1_CHAR)) ? 1 : 0));
		else
			games.put(game, 1);
	}

	@Override
	public void setName() {}
}