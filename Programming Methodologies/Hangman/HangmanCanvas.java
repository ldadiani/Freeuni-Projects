/*
* File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	GLabel word;
	GLabel update;
	GLabel dashes;
	GLabel incorrect;
	double shoulderY;
	double bodyY1;
	double bodyY2;
	int scaffoldX;
	double scaffoldY1;
	double scaffoldY2;

	/**
	 * Resets the display so that only the scaffold appears
	 * 
	 * @param nDash
	 */
	
	//this void resets the canvas
	public void reset(String randWordDashes) {
		removeAll();
		drawScaffold();
		drawDashes(randWordDashes);
	}

	//this void draws the dashes on the canvas
	private void drawDashes(String randWordDashes) {
		dashes = new GLabel(randWordDashes);
		dashes.setFont("-20");
		add(dashes, scaffoldX, scaffoldY1 + 50);
	}

	//this void draws the scaffold
	public void drawScaffold() {
		scaffoldX = getWidth() / 2 - BEAM_LENGTH;
		scaffoldY1 = (double) 3 / 4 * getHeight();
		scaffoldY2 = scaffoldY1 - SCAFFOLD_HEIGHT;
		GLine scaffold = new GLine(scaffoldX, scaffoldY1, scaffoldX, scaffoldY2);
		add(scaffold);
		GLine beam = new GLine(scaffoldX, scaffoldY2, scaffoldX + BEAM_LENGTH, scaffoldY2);
		add(beam);
		GLine rope = new GLine(scaffoldX + BEAM_LENGTH, scaffoldY2, scaffoldX + BEAM_LENGTH, scaffoldY2 + ROPE_LENGTH);
		add(rope);

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
		add(update, scaffoldX, scaffoldY1 + 50);
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
		add(incorrect, scaffoldX, scaffoldY1 + 70);

		if (count == 1) {
			drawHead();
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
			drawRightFoot();
		}
	}

	//this void draws the head
	public void drawHead() {
		GOval head = new GOval(2 * HEAD_RADIUS, 2 * HEAD_RADIUS);
		add(head, getWidth() / 2 - HEAD_RADIUS, scaffoldY2 + ROPE_LENGTH);

	}

	//this void draws the body
	public void drawBody() {
		bodyY1 = scaffoldY2 + ROPE_LENGTH + 2 * HEAD_RADIUS;
		bodyY2 = bodyY1 + BODY_LENGTH;
		GLine body = new GLine(getWidth() / 2, bodyY1, getWidth() / 2, bodyY2);
		add(body);
	}

	//this void draws left hand
	public void drawLeftHand() {
		drawleftShoulder();
		drawLeftWrist();

	}

	//this void draws left wrist
	private void drawLeftWrist() {
		GLine leftWrist = new GLine(getWidth() / 2 - UPPER_ARM_LENGTH, shoulderY, getWidth() / 2 - UPPER_ARM_LENGTH,
				shoulderY + LOWER_ARM_LENGTH);
		add(leftWrist);
	}

	//this void draws left shoulder
	private void drawleftShoulder() {
		shoulderY = bodyY1 + ARM_OFFSET_FROM_HEAD;
		GLine leftShoulder = new GLine(getWidth() / 2, shoulderY, getWidth() / 2 - UPPER_ARM_LENGTH, shoulderY);
		add(leftShoulder);
	}

	//this void draws right hand
	public void drawRightHand() {
		drawRightShoulder();
		drawRightWrist();
	}

	//this void draws right wrist
	private void drawRightWrist() {
		GLine rightWrist = new GLine(getWidth() / 2 + UPPER_ARM_LENGTH, shoulderY, getWidth() / 2 + UPPER_ARM_LENGTH,
				shoulderY + LOWER_ARM_LENGTH);
		add(rightWrist);
	}

	//this void draws right shoulder
	private void drawRightShoulder() {
		shoulderY = bodyY1 + ARM_OFFSET_FROM_HEAD;
		GLine rightShoulder = new GLine(getWidth() / 2, shoulderY, getWidth() / 2 + UPPER_ARM_LENGTH, shoulderY);
		add(rightShoulder);
	}

	//this void draws left leg
	public void drawLeftLeg() {
		drawLeftHip();
		drawLleg();
	}

	//this void draws left leg
	private void drawLleg() {
		GLine lLeg = new GLine(getWidth() / 2 - HIP_WIDTH, bodyY2, getWidth() / 2 - HIP_WIDTH, bodyY2+LEG_LENGTH);
		add(lLeg);
	}

	//this void draws left hip
	private void drawLeftHip() {
		GLine leftHip = new GLine(getWidth() / 2, bodyY2, getWidth() / 2 - HIP_WIDTH, bodyY2);
		add(leftHip);

	}

	//this void draws right leg
	public void drawRightLeg() {
		drawRightHip();
		drawRleg();
	}

	//this void draws right leg
	private void drawRleg() {
		GLine rLeg = new GLine(getWidth() / 2 + HIP_WIDTH, bodyY2, getWidth() / 2 + HIP_WIDTH, bodyY2+LEG_LENGTH);
		add(rLeg);
	}

	//this void draws right hip
	private void drawRightHip() {
		GLine rightHip = new GLine(getWidth() / 2, bodyY2, getWidth() / 2 + HIP_WIDTH, bodyY2);
		add(rightHip);
	}

	//this void draws left foot
	public void drawLeftFoot() {
		GLine leftFoot = new GLine(getWidth() / 2 - HIP_WIDTH, bodyY2+LEG_LENGTH, getWidth() / 2 - HIP_WIDTH - FOOT_LENGTH,
				bodyY2+LEG_LENGTH);
		add(leftFoot);
	}

	//this void draws right foot
	public void drawRightFoot() {
		GLine rightFoot = new GLine(getWidth() / 2 + HIP_WIDTH, bodyY2+LEG_LENGTH, getWidth() / 2 + HIP_WIDTH + FOOT_LENGTH,
				bodyY2+LEG_LENGTH);
		add(rightFoot);

	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 300;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 110;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 95;
	private static final int FOOT_LENGTH = 28;

}
