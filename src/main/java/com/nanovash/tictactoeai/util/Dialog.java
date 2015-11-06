package com.nanovash.tictactoeai.util;

import javax.swing.*;
import java.awt.*;

public class Dialog {

    Component c;

    public Dialog(Component c) {
        this.c = c;
    }

    public void showMessage(Object message, String title) {
        JOptionPane.showMessageDialog(c, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public void showError(Object message, String title) {
        JOptionPane.showMessageDialog(c, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public int showYesOrNo(Object message, String title) {
        return JOptionPane.showConfirmDialog(c, message, title, JOptionPane.YES_NO_OPTION);
    }

    public String showInput(Object message, String title) {
        return JOptionPane.showInputDialog(c, message, title, JOptionPane.PLAIN_MESSAGE);
    }
}
