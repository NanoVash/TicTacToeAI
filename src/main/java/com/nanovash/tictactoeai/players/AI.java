package com.nanovash.tictactoeai.players;

import com.nanovash.tictactoeai.*;
import com.nanovash.tictactoeai.util.Location;
import com.nanovash.tictactoeai.util.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class AI extends Player {

	private @Getter HashMap<String, Integer> games;
	private String fg;
	private @Getter @Setter boolean locked = false;

	public AI(String name, HashMap<String, Integer> games) {
		setName(name);
		this.games = games;
	}

	@Override
	public Location startTurn(Game game) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String g = game.toString();
		List<String> el = new ArrayList<>();
		List<Integer> chance = new ArrayList<>();
		for(String key : games.keySet()) {
			if (((key.length() % 4 != 0) == game.getP1().equals(this) && key.startsWith(g))) {
				System.out.println(key);
				el.add(key);
				chance.add(games.get(key));
			}
		}
		if(!el.isEmpty()) {
			fg = chooseWithChance(el, chance, game);
			System.out.println("set " + fg);
			return new Location(fg.substring(g.length(), g.length() + 2));
		}
		return markRandomly(game );
	}

	private Location markRandomly(Game game) {
		List<Tile> tiles = new ArrayList<>();
		Tile t;
		for(int x = 0; x < 3; x++)
			for(int y = 0; y < 3; y++)
				if((t = game.getTiles()[x][y]).getOwner() == null)
					tiles.add(t);
		return tiles.get(game.getRandom().nextInt(tiles.size())).getLocation();
	}

	//Chooses the next move for the AI based on each move's chance of being selected
	private String chooseWithChance(List<String> el, List<Integer> chance, Game game) {
		if(el.size() != chance.size()) throw new IllegalArgumentException("el and chance have to be of the same length!");
		long time = System.currentTimeMillis();
		Collections.shuffle(el, new Random(time));
		Collections.shuffle(chance, new Random(time));
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
		return el.get(game.getRandom().nextInt(el.size()));
	}

	@Override
	public void startGame(Game game) {
		fg = null;
	}

	@Override
	public void endGame(Game game) {
        if(locked)
            return;
		String g = game.toString();
		System.out.println(g + " game");
		System.out.println(fg + " fg");
		if(game.isTie()) return;
		if(games.keySet().contains(fg) && fg != null) {
			add(fg, game);
			add(g, game);
		}
		else
			games.put(g, 1);
	}

    private void add(String s, Game game) {
		games.put(s, (games.containsKey(s) ? games.get(s) : 1) + (((s.length() % 4 != 0) == game.getP1().equals(this) ? (games.containsKey(s) ? 1 : 0): 0)));
	}

	@Override
	public void changeName(Game game) {}

    //debug
    public String toString() {
        return getName();
    }
}