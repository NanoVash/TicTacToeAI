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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AIManager implements Manager {

	private List<AI> AIs = new ArrayList<>();
	private File f = new File(System.getenv("APPDATA"), ".TicTacToeAI" + File.separator + "AIs");
    private Dialog dialog = new Dialog(TicTacToe.getWindow());

    public AIManager() {
        if(!f.exists())
            f.mkdirs();
        if(f.list().length == 0)
            try {
                new File(f, "AI#1.ai").createNewFile();
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

    private Component getComponent(JPanel panel, Class<?> clazz) {
        Component layoutComponent = ((BorderLayout) panel.getLayout()).getLayoutComponent(panel, BorderLayout.CENTER);
        if(layoutComponent != null)
            for (Component c : ((JPanel) layoutComponent).getComponents())
                if(c.getClass().equals(clazz))
                    return c;
        return null;
    }

    private boolean sort(JComboBox<String> box, String... selected) {
        boolean tr = !box.getSelectedItem().toString().equals(selected.length == 0 ? "" : selected[0]);
        List<String> list = new ArrayList<>();
        ComboBoxModel<String> model = box.getModel();
        for(int i = 0; i < model.getSize(); i++)
            list.add(model.getElementAt(i));
        Collections.sort(list);
        box.setModel(new DefaultComboBoxModel<>(list.toArray(new String[list.size()])));
        return tr;
    }

    @Override
    public JPanel getUIConfigurator(JPanel main, JPanel other) {
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
        for (int i = 0; i < AIs.size(); i++)
            names[i] = AIs.get(i).getName();

        JComboBox<String> list = new JComboBox<>(names);
        list.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        ((JLabel) list.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        list.setFont(UIWindow.createFont(list, 18));
        list.setBackground(Color.WHITE);
        list.setFocusable(false);
        list.addActionListener(e -> {
            Object name = ((JComboBox) e.getSource()).getSelectedItem();
            if(name != null)
                ((JCheckBox) getComponent(main, JCheckBox.class)).setSelected(getAI(name.toString()).isLocked());
        });

        JButton rename = new JButton("Rename");
        rename.addActionListener(e -> {
            JComboBox<String> first = ((JComboBox) getComponent(main, JComboBox.class));
            String selected = first.getSelectedItem().toString();
            String name = dialog.showInput("Choose a new name for \"" + selected + "\"", "Rename");
            if(name == null) return;
            List<String> namez = new ArrayList<>();
            for (AI ai : AIs)
                namez.add(ai.getName());
            if(namez.contains(name)) {
                dialog.showMessage("You already have an AI called \"" + name + "\"", "Couldn't rename");
                return;
            }
            AI oldAI = getAI(selected);
            AI newAI = createNewAI(name, oldAI.getGames());
            newAI.setLocked(oldAI.isLocked());
            deleteAI(selected);
            first.addItem(name);
            first.removeItem(selected);
            sort(first);
            first.setSelectedItem(name);
            JComboBox<String> second = ((JComboBox) getComponent(other, JComboBox.class));
            if(second != null) {
                second.addItem(name);
                second.removeItem(selected);
                if (sort(second, selected))
                    second.setSelectedItem(name);
            }
        });

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            JComboBox mainBox = ((JComboBox) getComponent(main, JComboBox.class));
            JComboBox otherBox = (JComboBox) getComponent(other, JComboBox.class);
            String selected = mainBox.getSelectedItem().toString();
            if(dialog.showYesOrNo("Are you sure you wanna delete \"" + selected + "\"?", "Delete")) {
                deleteAI(selected);
                ((JComboBox) getComponent(main, JComboBox.class)).removeItem(selected);
                if(otherBox != null)
                    otherBox.removeItem(selected);
            }
            if (mainBox.getItemCount() == 0) {
                for(JComboBox box : new JComboBox[] {mainBox, otherBox})
                    box.addItem(createNewAI("AI#1", new HashMap<>()).getName());
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

        JCheckBox locked = new JCheckBox("Locked");
        locked.setBackground(Color.WHITE);
        locked.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        locked.setFont(UIWindow.createFont(locked, 18));
        locked.setFocusPainted(false);
        locked.setSelected(getAI(list.getSelectedItem().toString()).isLocked());
        locked.setToolTipText("If an AI is locked it will be unable to learn");
        locked.addItemListener(e -> {
            String name = ((JComboBox) getComponent(main, JComboBox.class)).getSelectedItem().toString();
            getAI(name).setLocked(((JCheckBox) getComponent(main, JCheckBox.class)).isSelected());
            JComboBox comboBox = ((JComboBox) getComponent(other, JComboBox.class));
            if(comboBox != null && comboBox.getSelectedItem().toString().equals(name))
                ((JCheckBox) getComponent(other, JCheckBox.class)).setSelected(getAI(name).isLocked());
        });

        JButton newAI = new JButton("Create new AI");
        newAI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        newAI.setFocusPainted(false);
        newAI.setFont(UIWindow.createFont(newAI, 18));
        newAI.addActionListener(e -> {
            String name = dialog.showInput("Input a name for the new AI", "Create new AI");
            if(name == null) return;
            List<String> namez = new ArrayList<>();
            for (AI ai : AIs)
                namez.add(ai.getName());
            if(namez.contains(name)) {
                dialog.showMessage("You already have an AI called \"" + name + "\"", "Couldn't create new AI");
                return;
            }
            createNewAI(name, null);
            JComboBox mainBox = ((JComboBox) getComponent(main, JComboBox.class));
            JComboBox otherBox = ((JComboBox) getComponent(other, JComboBox.class));
            for(JComboBox box : new JComboBox[] {mainBox, otherBox})
                if(box != null) {
                    box.addItem(name);
                    box.setSelectedItem(name);
                }
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
    public Player getChosen(Game game, JPanel panel) {
        return getAI(((JComboBox) getComponent(panel, JComboBox.class)).getSelectedItem().toString());
    }

    @Override
    public String getPlayerDisplayName() {
        return "AI";
    }
}