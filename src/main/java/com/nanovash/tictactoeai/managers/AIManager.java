package com.nanovash.tictactoeai.managers;

import com.nanovash.tictactoeai.Game;
import com.nanovash.tictactoeai.Manager;
import com.nanovash.tictactoeai.Player;
import com.nanovash.tictactoeai.TicTacToe;
import com.nanovash.tictactoeai.players.AI;
import com.nanovash.tictactoeai.util.Dialog;
import com.nanovash.tictactoeai.windows.UIWindow;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIManager implements Manager {

	private List<AI> AIs = new ArrayList<>();
    private JComboBox<String> list;
    private JCheckBox locked;
	private File f = new File(System.getenv("APPDATA"), ".TicTacToeAI" + File.separator + "AIs");
    private Dialog dialog = new Dialog(TicTacToe.getWindow());

    public AIManager() {
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
                HashMap<String, Integer> allLines = new HashMap<>();
                boolean locked = false;
                for(String l : Files.readAllLines(ai.toPath())) {
                    switch (l) {
                        case "true":
                        case "false":
                            locked = Boolean.valueOf(l);
                            break;
                        case "":
                            continue;
                        default:
                            String[] split = l.split("\\|");
                            allLines.put(split[0], Integer.parseInt(split[1]));
                    }
                }
                AI a = new AI(ai.getName().replaceFirst("[.][^.]+$", ""), allLines);
                a.setLocked(locked);
                AIs.add(a);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AI getAI(String name) {
		for(AI ai : AIs)
			if(ai.getName().equals(name))
				return ai;
		return null;
	}

	public boolean save(AI ai, File dir) {
        boolean success = true;
        File file = new File(dir, ai.getName() + ".ai");
        try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(ai.isLocked() + "\r\n");
			for(String s : ai.getGames().keySet())
                writer.write(s + "|" + ai.getGames().get(s) + "\r\n");
			writer.close();
		} catch (IOException e) {
			success = false;
		}
        System.out.println("Saved!");
        return success;
    }

    private AI createNewAI(String name, HashMap<String, Integer> brain) {
        AI ai = new AI(name, brain == null ? new HashMap<>() : brain);
        AIs.add(ai);
        return ai;
    }

    private void deleteAI(String name) {
        AIs.remove(getAI(name));
        new File(f, name + ".ai").delete();
    }

    @Override
    public JPanel getUIConfigureer() {
        JPanel data = new JPanel();
        data.setBackground(Color.WHITE);
        data.setLayout(new BoxLayout(data, BoxLayout.Y_AXIS));

        JLabel choose = new JLabel("Choose an AI", SwingConstants.CENTER);
        choose.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        choose.setAlignmentX(Component.LEFT_ALIGNMENT);
        choose.setFont(UIWindow.createFont(choose, 18));

        JLabel configure = new JLabel("Configure the above AI", SwingConstants.CENTER);
        configure.setFont(UIWindow.createFont(configure, 18));
        configure.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.X_AXIS));
        editPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        editPanel.setBackground(Color.WHITE);

        String[] names = new String[AIs.size()];
        for (int i = 0; i < AIs.size(); i++) {
            names[i] = AIs.get(i).getName();
        }

        list = new JComboBox<>(names);
        list.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        ((JLabel) list.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        list.setFont(UIWindow.createFont(list, 18));
        list.setBackground(Color.WHITE);
        list.setFocusable(false);
        list.addActionListener(e -> {
            Object name = list.getSelectedItem();
            if(name != null)
                locked.setSelected(getAI(name.toString()).isLocked());
        });

        JButton rename = new JButton("Rename");
        rename.addActionListener(e -> {
            Object selected = list.getSelectedItem();
            String name = dialog.showInput("Choose a new name for \"" + selected + "\"", "Rename");
            if(name == null) return;
            AI oldAI = getAI(selected.toString());
            AI newAI = createNewAI(name, oldAI.getGames());
            newAI.setLocked(oldAI.isLocked());
            deleteAI(selected.toString());
            list.addItem(name);
            list.removeItem(selected);
            list.setSelectedItem(name);
        });

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String selected = list.getSelectedItem().toString();
            if(dialog.showYesOrNo("Are you sure you wanna delete \"" + selected + "\"?", "Delete") == 0) {
                deleteAI(selected);
                list.removeItem(selected);
            }
            if (list.getItemCount() == 0) {
                list.addItem(createNewAI("AI", new HashMap<>()).getName());
                dialog.showMessage("A new AI has been automatically created for you since you had no AIs", "New AI has been created");
            }
        });

        JButton extract = new JButton("Extract");
        extract.addActionListener(e -> {
            String selected = list.getSelectedItem().toString();
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = fc.showSaveDialog(TicTacToe.getWindow());
            if(i == JFileChooser.APPROVE_OPTION) {
                File folder = fc.getSelectedFile();
                if(save(getAI(selected), folder))
                    dialog.showMessage("Saved \"" + selected + "\" successfully in " + folder, "Saved successfully");
                else
                    dialog.showError("Couldn't extract \"" + selected + "\"", "Something went wrong");
            }
        });

        for (JButton b : new JButton[]{rename, delete, extract}) {
            editPanel.add(b);
            b.setFocusPainted(false);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        }

        locked = new JCheckBox("Locked");
        locked.setBackground(Color.WHITE);
        locked.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        locked.setFont(UIWindow.createFont(locked, 18));
        locked.setFocusPainted(false);
        locked.setSelected(getAI(list.getSelectedItem().toString()).isLocked());
        locked.setToolTipText("If an AI is locked it will be unable to learn");
        locked.addItemListener(e -> {
            getAI(list.getSelectedItem().toString()).setLocked(locked.isSelected());
        });

        JButton newAI = new JButton("Create new AI");
        newAI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        newAI.setFocusPainted(false);
        newAI.setFont(UIWindow.createFont(newAI, 18));
        newAI.addActionListener(e -> {
            String name = dialog.showInput("Input a name for the new AI", "Create new AI");
            if(name == null) return;
            createNewAI(name, null);
            list.addItem(name);
            list.setSelectedItem(name);
        });

        for (Component component : new Component[]{choose, list, configure, editPanel, locked, newAI})
            data.add(component);

        for(Component c : data.getComponents())
            ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);

        return data;
    }

    @Override
    public void exit() {
        for(AI ai : AIs)
            if(!save(ai, f))
                dialog.showError("Couldn't save the AI \"" + ai.getName() + "\"", "Something went wrong");
    }

    @Override
    public Player getChosen(Game game) {
        return getAI(list.getSelectedItem().toString());
    }

    @Override
    public String getPlayerDisplayName() {
        return "AI";
    }
}