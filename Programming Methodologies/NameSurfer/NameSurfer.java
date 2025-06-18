/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;

import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	private NameSurferDataBase nameDataBase = new NameSurferDataBase(NAMES_DATA_FILE);
	private JLabel namesLabel;
	private JTextField nameField;
	private JButton graphButton;
	private JButton clearButton;
	private NameSurferGraph graph;
	private NameSurferEntry entry;


/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		graph = new NameSurferGraph();
		add(graph);
		graph.update();

		namesLabel = new JLabel("Name: ");
		add(namesLabel, SOUTH);

		nameField = new JTextField(TEXT_FIELD_SIZE);
		add(nameField, SOUTH);
		nameField.addActionListener(this);

		graphButton = new JButton("Graph");
		add(graphButton, SOUTH);

		clearButton = new JButton("Clear");
		add(clearButton, SOUTH);

		addActionListeners();
	}


/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == graphButton) {
			NameSurferEntry entry = nameDataBase.findEntry(nameField.getText());
			if (nameDataBase.findEntry(nameField.getText()) != null) {
				graph.addEntry(entry);
				graph.update();
			}
		} else if (e.getSource() == clearButton) {
			graph.clear();
		} else if (e.getSource() == nameField) {
			NameSurferEntry entry = nameDataBase.findEntry(nameField.getText());
			if (nameDataBase.findEntry(nameField.getText()) != null) {
				graph.addEntry(entry);
				graph.update();
			}
		}
	}
}
