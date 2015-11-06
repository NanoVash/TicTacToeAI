package com.nanovash.tictactoeai;

import com.nanovash.tictactoeai.windows.UIWindow;
import lombok.Getter;

import javax.swing.*;

public class TicTacToe {

    private static @Getter UIWindow window;

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            window = new UIWindow();
            window.setVisible(true);
        });
	}
}