package com.nanovash.tictactoeai;

import lombok.*;

import javax.swing.*;
import java.awt.event.MouseListener;

@RequiredArgsConstructor
public class Tile {

	@NonNull @Getter @Setter JLabel label;
	@NonNull @Getter @Setter Location location;

	private @Getter Player owner;

	public String getText() {
		return label.getText();
	}

	public void setOwner(Player p) {
		label.setText(p == null ? "" : p.getSymbol());
		this.owner = p;
	}
}
