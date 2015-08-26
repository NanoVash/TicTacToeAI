package com.nanovash.tictactoeai;

import lombok.*;

import javax.swing.*;
import java.awt.event.MouseListener;

@RequiredArgsConstructor
public class Tile {

	@NonNull @Getter @Setter JLabel label;
	@NonNull @Getter @Setter Location location;

	@Getter private Player owner;

	public String getText() {
		return label.getText();
	}

	public void setOwner(Player p) {
		label.setText(p.getSymbol());
		for(MouseListener ml : label.getMouseListeners())
			label.removeMouseListener(ml);
		this.owner = p;
	}
}
