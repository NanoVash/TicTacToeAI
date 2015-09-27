package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.players.AI;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIManager {

	private List<AI> ais = new ArrayList<AI>();
	private File f = new File(System.getenv("APPDATA"), ".TicTacToeAI");

	public AIManager(Game game) {
		if(!f.exists())
			f.mkdirs();
		if(f.list().length == 0)
			try {
				new File(f, "AI.ai").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		for(File ai : f.listFiles()) {
			if(ai.isDirectory()) continue;
			try {
				HashMap<String, Integer> allLines= new HashMap<>();
				for(String l : Files.readAllLines(ai.toPath())) {
					String[] split = l.split("\\|");
					allLines.put(split[0], Integer.parseInt(split[1]));
				}
				ais.add(new AI(game, ai.getName().replaceFirst("[.][^.]+$", ""), allLines));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		TicTacToe.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (AI ai : ais)
					save(ai);
			}
		});
	}

	public AI getAI(String name) {
		for(AI ai : ais)
			if(ai.getName().equals(name))
				return ai;
		return null;
	}

	public void save(AI ai) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(f, ai.getName() + ".ai")));
			for(String s : ai.getGames().keySet())
				writer.write(s + "|" + ai.getGames().get(s) +"\r\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}