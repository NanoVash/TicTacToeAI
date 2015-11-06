package com.nanovash.tictactoeai.util;

import com.nanovash.tictactoeai.Player;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@RequiredArgsConstructor
public class Tile {

	private @NonNull @Getter @Setter JLabel label;
	private @NonNull @Getter @Setter Location location;
	private @Getter Player owner = null;
	private @Getter int order = -1;

	public void setOwner(Player p, int i, char symbol) {
		if(owner == null && symbol != ' ') {
            label.setText(p == null ? "" : Character.toString(symbol));
            owner = p;
            order = i;
        }
        else if(symbol == ' ') {
            label.setText("");
            owner = null;
            order = -1;
        }
	}

    public String getText() {
        return getLabel().getText();
    }
}