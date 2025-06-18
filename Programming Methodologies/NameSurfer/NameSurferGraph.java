/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;

import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {
	private ArrayList<NameSurferEntry> entryGraph = new ArrayList<NameSurferEntry>();

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		removeAll();
		drawGraph();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		if (isNotDrawn(entry)) {
			entryGraph.add(entry);
		}
	}

	/*
	 * Boolean: isNotDrawn(entry)
	 * 
	 * decides if the entry of the graph is already on the display and if it is the
	 * program will not draw the new one
	 */
	private boolean isNotDrawn(NameSurferEntry entry) {
		for (int n = 0; n < entryGraph.size(); n++) {
			if (entryGraph.get(n) == entry)
				return false;
		}
		return true;

	}

	/* Color of the entry */
	private Color color = null;
	/* number of the decade */
	private int i;
	/* y coordinate of GLine in entry */
	private int y1;
	/* x coordinate of GLine in entry */
	double x1;
	/* Label of the name in entry */
	private GLabel nameLabel;

	/* this method adds name labels to the entry */
	private void addNameLabels(NameSurferEntry entry) {
		if (entry.getRank(i - 1) != 0) {
			nameLabel = new GLabel(entry.getName() + " " + entry.getRank(i - 1));
		} else {
			nameLabel = new GLabel(entry.getName() + " *");
		}
		nameLabel.setColor(color);
		add(nameLabel, x1, y1);

	}

	/*
	 * Method : drawEntry(entry,count)
	 * 
	 * fully draws the entry, it uses the value from the massive we created in
	 * NameSurferEntry
	 */
	private void drawEntry(NameSurferEntry entry, int count) {
		double space = (double)getWidth() / NDECADES;
		for (i = 1; i < NDECADES; i++) {
			x1 = (i - 1) * space;
			double x2 = i * space;
			y1 = GRAPH_MARGIN_SIZE + (getHeight() - 2 * GRAPH_MARGIN_SIZE) * entry.getRank(i - 1) / MAX_RANK;
			if (entry.getRank(i - 1) == 0)
				y1 = getHeight() - GRAPH_MARGIN_SIZE;
			int y2 = GRAPH_MARGIN_SIZE + (getHeight() - 2 * GRAPH_MARGIN_SIZE) * entry.getRank(i) / MAX_RANK;
			if (entry.getRank(i) == 0)
				y2 = getHeight() - GRAPH_MARGIN_SIZE;
			GLine entryLine = new GLine(x1, y1, x2, y2);
			if (count % 4 == 0) {
				color = Color.BLACK;
			} else if (count % 4 == 1) {
				color = Color.RED;
			} else if (count % 4 == 2) {
				color = Color.BLUE;
			} else if (count % 4 == 3) {
				color = Color.GREEN;
			}
			entryLine.setColor(color);
			add(entryLine);
			addNameLabels(entry);
			if (i == NDECADES - 1) {
				addLastLabels(entry, space);
			}
		}

	}

	/*
	 * Method:addLastLabels(entry,space)
	 * 
	 * method adds the name labels of the last decade
	 */
	private void addLastLabels(NameSurferEntry entry, double space) {
		if (entry.getRank(i) != 0) {
			nameLabel = new GLabel(entry.getName() + " " + entry.getRank(i));
		} else {
			nameLabel = new GLabel(entry.getName() + " *");
		}
		nameLabel.setColor(color);
		x1 = i * space;
		y1 = GRAPH_MARGIN_SIZE + (getHeight() - 2 * GRAPH_MARGIN_SIZE)  * entry.getRank(i) / MAX_RANK;
		if (entry.getRank(i - 1) == 0)
			y1 = getHeight() - GRAPH_MARGIN_SIZE;
		add(nameLabel, x1, y1);
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		drawGraph();
		drawAllEntries();
	}

	/*
	 * Method: drawAllEntries
	 * 
	 * draws all entries which are in our arraylist of NameSurferEntries
	 */
	private void drawAllEntries() {
		for (int count = 0; count < entryGraph.size(); count++) {

			drawEntry(entryGraph.get(count), count);

		}

	}

	/*
	 * Method: drawGraph()
	 * 
	 * draws the lines that must be at the start of the program
	 */
	private void drawGraph() {
		drawHorizontalLines();
		drawVerticalLines();
		drawYearLabels();

	}

	/*
	 * Method: drawYearLabels() draws the yearlabels, it draws conformable label to
	 * each line
	 */
	private void drawYearLabels() {
		double space = getWidth() / NDECADES;
		for (int i = 0; i < NDECADES; i++) {
			String yearString = Integer.toString(START_DECADE + i * 10);
			GLabel year = new GLabel(yearString, i * space + 5, getHeight() - 5);
			add(year);
		}

	}

	/*
	 * Method:drawVerticalLines() 
	 * draws vertical lines
	 */
	private void drawVerticalLines() {
		double space = getWidth() / NDECADES;
		for (int i = 0; i < NDECADES; i++) {
			double x = i * space;
			int y1 = 0;
			int y2 = getHeight();
			GLine verticalLine = new GLine(x, y1, x, y2);
			add(verticalLine);
		}

	}

	/* Method:DrawHorizontalLines()
	 *  draws horizontal lines */
	private void drawHorizontalLines() {
		GLine toplLine = new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		add(toplLine);

		GLine bottomLine = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(bottomLine);

	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}
}
