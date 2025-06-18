/*
* File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;
import acm.util.MediaTools;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.Color;

import javax.swing.plaf.synth.ColorType;

public class HangmanCoolCanvas extends GCanvas {
	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 320;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 35;
	private static final int HEAD_RADIUS = 45;
	private static final int BODY_LENGTH = 90;
	private static final int SCAFFOLD_WIDTH = 30;
	private static final int FOOT_LENGTH = 28;
	private static final Color BROWN = new Color(153, 102, 0);

	// face parameters
	private static final int FACE_WIDTH = 80;
	private static final int FACE_HEIGHT = 80;

	// antenna
	private static final int ANTENNA_BOX_HEIGHT = 12;
	private static final int ANTENNA_BOX_WIDTH = 16;
	private static final int ANTENNA_WIRE_LENGTH = 36;

	// mouth
	private static final int MOUTH_WIDTH = 40;
	private static final int MOUTH_HEIGHT = 16;
	private static final int MOUTH_FROOM_CHIN = 28;

	// eyes
	private static final int EYES_WIDTH = 24;
	private static final int EYES_HEIGHT = 24;
	private static final int EYES_FROM_CENTER = 8;
	private static final int EYES_FROM_HEAD = 12;
	private static final int EYEBALLS_FROM_SQUARE = 6;
	private static final int EYEBALL_WIDTH = 12;
	private static final int EYEBALL_HEIGHT = 12;

	// nose
	private static final int NOSE_WIDTH = 6;
	private static final int NOSE_HEIGHT = 6;

	// NECK
	private static final int NECK_WIDTH = 20;
	private static final int NECK_HEIGHT = 10;

	// HAND
	private static final int HAND_WIDTH = 20;
	private static final int HAND_HEIGHT = 100;

	private static final int ARM_WIDTH = 7;
	private static final int ARM_HEIGHT = 20;

	private static final int LEG_WIDTH = 30;
	private static final int LEG_HEIGHT = 70;

	private static final int FOOT_WIDTH = 35;
	private static final int FOOT_HEIGHT = 10;
	
	GLabel pointl;
	int eye1X;
	int eye2X;
	double eyeY;
	int mouthX;
	double mouthY;
	GOval EYEBALL1;
	GOval EYEBALL2;
	GArc arc1;
	GLine line2;
	GLabel word;
	GLabel update;
	GLabel dashes;
	GLabel incorrect;
	GLabel pointLabel;
	double shoulderY;
	double bodyY1;
	double bodyY2;
	int scaffoldX;
	double scaffoldY;
	int point;

	/**
	 * Resets the display so that only the scaffold appears
	 * 
	 * @param nDash
	 */
	//this void resets the canvas and daws only scaffold on it
	public void reset(String randWordDashes) {
		point=0;
		Color LIGHT_BLUE = new Color(51, 204, 255);
		this.setBackground(LIGHT_BLUE);
		removeAll();
		addPointLabel();
		drawScaffold();
		// drawChristmasTree();
		drawDashes(randWordDashes);
	}
	
	//this void adds the points if you guessed the wrd and shows on canvas
	public void addPoints() {
		remove (pointl);
		point=point+10;
		pointl = new GLabel (""+point);
		pointl.setFont("-15");
		add(pointl,20+pointLabel.getWidth(),20);
	}

	//this void substracts the points if your guess was incorrect
	public void losePoints() {
		remove (pointl);
		point=point-5;
		pointl = new GLabel (""+point);
		pointl.setFont("-15");
		add(pointl,20+pointLabel.getWidth(),20);
	}
	
	//this void adds the label which shows your point
	private void addPointLabel() {
		 pointLabel = new GLabel("YOUR POINT IS: ");
		pointLabel.setFont("-15");
		add(pointLabel,20,20);
		pointl = new GLabel (""+point);
		pointl.setFont("-15");
		add(pointl,20+pointLabel.getWidth(),20);
	}

	//this void draws the dashes 
	private void drawDashes(String randWordDashes) {
		dashes = new GLabel(randWordDashes);
		dashes.setFont("-20");
		add(dashes, scaffoldX, (double) 3 / 4 * getHeight() + 50);
	}

	//this void draws scaffold
	public void drawScaffold() {
		scaffoldX = getWidth() / 2 - BEAM_LENGTH;
		scaffoldY = (double) 3 / 4 * getHeight() - SCAFFOLD_HEIGHT;
		GRect scaffold = new GRect(SCAFFOLD_WIDTH, SCAFFOLD_HEIGHT);
		scaffold.setFilled(true);
		scaffold.setColor(Color.black);
		add(scaffold, scaffoldX, scaffoldY);
		GRect beam = new GRect(BEAM_LENGTH, SCAFFOLD_WIDTH / 2);
		beam.setFilled(true);
		beam.setColor(Color.black);
		add(beam, scaffoldX + SCAFFOLD_WIDTH, scaffoldY);
		GRect rope = new GRect(SCAFFOLD_WIDTH / 2, ROPE_LENGTH);
		rope.setFilled(true);
		rope.setColor(Color.black);
		add(rope, getWidth() / 2 - SCAFFOLD_WIDTH / 4, scaffoldY + SCAFFOLD_WIDTH / 2);

	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String replacedDashes) {
		if (update != null) {
			remove(update);
		}
		if (dashes != null) {
			remove(dashes);
		}
		update = new GLabel(replacedDashes);
		update.setFont("-20");
		add(update, scaffoldX, (double) 3 / 4 * getHeight() + 50);
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	public void noteIncorrectGuess(String incorrectGuess, int count) {
		if (incorrect != null) {
			remove(incorrect);
		}
		incorrect = new GLabel(incorrectGuess);
		add(incorrect, scaffoldX, (double) 3 / 4 * getHeight() + 70);

		if (count == 1) {
			
			drawRobotHead();
		} else if (count == 2) {
			drawBody();
		} else if (count == 3) {
			drawLeftHand();
		} else if (count == 4) {
			drawRightHand();
		} else if (count == 5) {
			drawLeftLeg();
		} else if (count == 6) {
			drawRightLeg();
		} else if (count == 7) {
			drawLeftFoot();
		} else if (count == 8) {
			drawNewFace();
			drawRightFoot();
		}
	}

	//this void draws sad face if you lost
	private void drawNewFace() {
		remove(arc1);
		remove(line2);
		remove(EYEBALL1);
		remove(EYEBALL2);

		GArc arc2 = new GArc(mouthX, mouthY + 10, MOUTH_WIDTH, MOUTH_HEIGHT, 10, 170);
		add(arc2);

		GLine line1 = new GLine(eye1X, eyeY, eye1X + EYES_WIDTH, eyeY + EYES_HEIGHT);
		add(line1);

		GLine line2 = new GLine(eye1X, eyeY + EYES_HEIGHT, eye1X + EYES_WIDTH, eyeY);
		add(line2);

		GLine line3 = new GLine(eye2X, eyeY + EYES_HEIGHT, eye2X + EYES_WIDTH, eyeY);
		add(line3);

		GLine line4 = new GLine(eye2X, eyeY, eye2X + EYES_WIDTH, eyeY + EYES_HEIGHT);
		add(line4);

	}

	//this void draws robots head
	private void drawRobotHead() {
		drawFace();
		drawAntenna();
		drawEyes();
		drawMouth();
		drawNose();
	}

	private void drawMouth() {
		mouthX = (getWidth() - MOUTH_WIDTH) / 2;
		mouthY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT - MOUTH_FROOM_CHIN;

		arc1 = new GArc(mouthX, mouthY, MOUTH_WIDTH, MOUTH_HEIGHT, 190, 170);
		line2 = new GLine(mouthX, mouthY + 7, mouthX + MOUTH_WIDTH, mouthY + 7);
		arc1.setColor(Color.RED);
		line2.setColor(Color.RED);
		add(line2);
		add(arc1);
	}

	private void drawNose() {
		double noseX = (getWidth() - NOSE_WIDTH) / 2;
		double noseY = ((scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH) / 2 + FACE_HEIGHT;

		GRect NOSE = new GRect(noseX, noseY, NOSE_WIDTH, NOSE_HEIGHT);
		NOSE.setFilled(true);
		NOSE.setFillColor(Color.LIGHT_GRAY);
		add(NOSE);
	}

	private void drawEyes() {
		eye1X = getWidth() / 2 - EYES_FROM_CENTER - EYES_WIDTH;
		eyeY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + EYES_FROM_HEAD;
		eye2X = getWidth() / 2 + EYES_FROM_CENTER;
		double eyeball1X = eye1X + EYEBALLS_FROM_SQUARE;
		double eyeballY = eyeY + EYEBALLS_FROM_SQUARE;
		double eyeball2X = eye2X + EYEBALLS_FROM_SQUARE;

		GRect EYE1 = new GRect(eye1X, eyeY, EYES_WIDTH, EYES_HEIGHT);
		EYE1.setFilled(true);
		EYE1.setFillColor(Color.LIGHT_GRAY);
		GRect EYE2 = new GRect(eye2X, eyeY, EYES_WIDTH, EYES_HEIGHT);
		EYE2.setFilled(true);
		EYE2.setFillColor(Color.LIGHT_GRAY);
		EYEBALL1 = new GOval(eyeball1X, eyeballY, EYEBALL_WIDTH, EYEBALL_HEIGHT);
		EYEBALL1.setFilled(true);
		EYEBALL1.setColor(Color.BLUE);
		EYEBALL2 = new GOval(eyeball2X, eyeballY, EYEBALL_WIDTH, EYEBALL_HEIGHT);
		EYEBALL2.setFilled(true);
		EYEBALL2.setColor(Color.BLUE);

		add(EYE1);
		add(EYE2);
		add(EYEBALL1);
		add(EYEBALL2);
	}

	private void drawAntenna() {
		int antennaX = (getWidth() - ANTENNA_BOX_WIDTH) / 2;
		double antennaY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH - ANTENNA_BOX_HEIGHT;

		GRect ANTENNA_BOX = new GRect(antennaX, antennaY, ANTENNA_BOX_WIDTH, ANTENNA_BOX_HEIGHT);
		ANTENNA_BOX.setFilled(true);
		ANTENNA_BOX.setColor(Color.blue);
		add(ANTENNA_BOX);
	}

	private void drawFace() {
		int faceX = (getWidth() - FACE_WIDTH) / 2;
		double faceY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH;

		GRect FACE = new GRect(faceX, faceY, FACE_WIDTH, FACE_HEIGHT);
		FACE.setFilled(true);
		FACE.setFillColor(Color.LIGHT_GRAY);
		add(FACE);
	}

	//this void draws robots body
	public void drawBody() {
		int neckX = (getWidth() - NECK_WIDTH) / 2;
		double neckY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT;

		GRect neck = new GRect(NECK_WIDTH, NECK_HEIGHT);
		neck.setFilled(true);
		neck.setFillColor(Color.LIGHT_GRAY);
		add(neck, neckX, neckY);

		int bodyX = (getWidth() - FACE_WIDTH) / 2;
		double bodyY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT;

		GRect body = new GRect(FACE_WIDTH, BODY_LENGTH);
		body.setFilled(true);
		body.setFillColor(Color.LIGHT_GRAY);
		add(body, bodyX, bodyY);

		GRect body1 = new GRect(FACE_WIDTH / 1.2, BODY_LENGTH / 1.2);
		body1.setFilled(true);
		body1.setFillColor(Color.blue);
		add(body1, bodyX + 1.0 * FACE_WIDTH / 12, bodyY + 1.0 * BODY_LENGTH / 12);

		GRect body2 = new GRect(FACE_WIDTH / 1.5, BODY_LENGTH / 1.5);
		body2.setFilled(true);
		body2.setFillColor(Color.red);
		add(body2, bodyX + 1.0 * FACE_WIDTH / 6, bodyY + 1.0 * BODY_LENGTH / 6);

	}

	//this void draws robots left hand
	public void drawLeftHand() {
		int lArmX = (getWidth() - FACE_WIDTH) / 2 - ARM_WIDTH;
		double lArmY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT;

		GRect lArm = new GRect(ARM_WIDTH, ARM_HEIGHT);
		lArm.setFilled(true);
		lArm.setFillColor(Color.red);
		add(lArm, lArmX, lArmY);

		int lHandX = lArmX - HAND_WIDTH;
		double lHandY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT;

		GRect lHand = new GRect(HAND_WIDTH, HAND_HEIGHT);
		lHand.setFilled(true);
		lHand.setFillColor(Color.LIGHT_GRAY);
		add(lHand, lHandX, lHandY);

	}

	//this void draws robots right hand
	public void drawRightHand() {
		int rArmX = (getWidth() + FACE_WIDTH) / 2;
		double rArmY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT;

		GRect rArm = new GRect(ARM_WIDTH, ARM_HEIGHT);
		rArm.setFilled(true);
		rArm.setFillColor(Color.red);
		add(rArm, rArmX, rArmY);

		int rHandX = (getWidth() + FACE_WIDTH) / 2 + ARM_WIDTH;
		double rHandY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT;

		GRect rHand = new GRect(HAND_WIDTH, HAND_HEIGHT);
		rHand.setFilled(true);
		rHand.setFillColor(Color.LIGHT_GRAY);
		add(rHand, rHandX, rHandY);
	}

	//this void draws robots left leg
	public void drawLeftLeg() {
		int lLegX = (getWidth() - FACE_WIDTH) / 2 + 5;
		double lLegY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT + BODY_LENGTH;

		GRect lLeg = new GRect(LEG_WIDTH, LEG_HEIGHT);
		lLeg.setFilled(true);
		lLeg.setFillColor(Color.LIGHT_GRAY);
		add(lLeg, lLegX, lLegY);

		GRect lLeg2 = new GRect(LEG_WIDTH / 1.5, LEG_HEIGHT);
		lLeg2.setFilled(true);
		lLeg2.setFillColor(Color.blue);
		add(lLeg2, lLegX + 1.0 * LEG_WIDTH / 6, lLegY);

	}

	//this void draws robots right leg
	public void drawRightLeg() {
		int rLegX = (getWidth() - FACE_WIDTH) / 2 + FACE_WIDTH - LEG_WIDTH - 5;
		double rLegY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT + BODY_LENGTH;

		GRect rLeg = new GRect(LEG_WIDTH, LEG_HEIGHT);
		rLeg.setFilled(true);
		rLeg.setFillColor(Color.LIGHT_GRAY);
		add(rLeg, rLegX, rLegY);

		GRect rLeg2 = new GRect(LEG_WIDTH / 1.5, LEG_HEIGHT);
		rLeg2.setFilled(true);
		rLeg2.setFillColor(Color.blue);
		add(rLeg2, rLegX + 1.0 * LEG_WIDTH / 6, rLegY);

	}

	//this void draws robots left foot
	public void drawLeftFoot() {
		int lFootX = (getWidth() - FACE_WIDTH) / 2 + 5 - (FOOT_WIDTH - LEG_WIDTH) / 2;
		double lFootY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT + BODY_LENGTH
				+ LEG_HEIGHT;

		GRect lFoot = new GRect(FOOT_WIDTH, FOOT_HEIGHT);
		lFoot.setFilled(true);
		lFoot.setFillColor(Color.RED);
		add(lFoot, lFootX, lFootY);
	}

	//this void draws robots right foot
	public void drawRightFoot() {
		int rFootX = (getWidth() - FACE_WIDTH) / 2 + FACE_WIDTH - LEG_WIDTH - 5 - (FOOT_WIDTH - LEG_WIDTH) / 2;
		double rFootY = (scaffoldY + SCAFFOLD_WIDTH / 2) + ROPE_LENGTH + FACE_HEIGHT + NECK_HEIGHT + BODY_LENGTH
				+ LEG_HEIGHT;

		GRect rFoot = new GRect(FOOT_WIDTH, FOOT_HEIGHT);
		rFoot.setFilled(true);
		rFoot.setFillColor(Color.RED);
		add(rFoot, rFootX, rFootY);
	}

}