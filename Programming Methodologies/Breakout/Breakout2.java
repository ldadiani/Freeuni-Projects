/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;

import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout2 extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** diameter of the ball */
	public static final int diameter = 2 * BALL_RADIUS;

	/* pause of the game */
	private int PAUSE = 15;
	/* BRICKX is counter for x coordinate of brick */
	int BRICKX = 0;
	/* BRICKY is counter for y coordinate of brick */
	int BRICKY = 0;
	/* BRICKN is counter for bricks */
	int BRICKN = NBRICKS_PER_ROW * NBRICK_ROWS;
	/* TURNS counts the number of turns */
	int TURNS = 0;
	/* REMOVEDBRICKS counts how many bricks removed the wall */
	int REMOVEDBRICKS = 0;

	/* POINT counts points of the player */
	int POINT;

	// Random Generator
	private RandomGenerator rgen = RandomGenerator.getInstance();

	private GRect paddle;
	private GOval ball;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();

		setInterface();
		addPaddle();
		addClickLabel();
		waitForClick();
		addBall();
		moveBall();
	}

	// this void can help you to restart game if you lost it,it also changes some
	// integers that are necessary
	private void tryAgain() {
		waitForClick();
		PAUSE=15;
		REMOVEDBRICKS = 0;
		TURNS = 0;
		POINT = 0;
		removeAll();
		run();
	}

	private GLabel click;
	private GImage hellogif;

	// program will tell you to click the mouse before starting the game
	private void addClickLabel() {

		hellogif = new GImage("hellogif.gif");
		hellogif.setSize(200, 200);
		add(hellogif, (getWidth() - hellogif.getWidth()) / 2, (getHeight() - hellogif.getHeight()) / 2);

		click = new GLabel("CLICK TO START");
		click.setFont("-20");
		add(click, (getWidth() - click.getWidth()) / 2, hellogif.getY() + hellogif.getHeight() + click.getHeight());

	}

	// program will wait t start until you click the mouse
	public void mouseClicked(MouseEvent e) {
		remove(click);
		remove(hellogif);
	}

	// if you used all your turns and lost all of it the game ends and tells you
	// that you lost the game
	private void lostGame() {
		GImage losergif = new GImage("loser.gif");
		losergif.setSize(200, 200);
		add(losergif, (getWidth() - losergif.getWidth()) / 2, (getHeight() - losergif.getHeight()) / 2);

		GLabel loser = new GLabel("YOU LOST :(");
		loser.setFont("-20");
		add(loser, (getWidth() - loser.getWidth()) / 2, losergif.getY() + losergif.getHeight() + loser.getHeight());
		GLabel tryAgain = new GLabel("CLICK TO TRY AGAIN");
		add(tryAgain, (getWidth() - tryAgain.getWidth()) / 2, loser.getY() + loser.getHeight());
		add(tryAgain);
	}

	/*
	 * setInterface method sets background and draws bricks. j stands for row number
	 * and i stands for brick number in a row. this method draws BRICK_PER_ROW
	 * number of bricks in a row and then it changes the row number and does it
	 * again until it is done
	 */
	private void setInterface() {
		
		this.setBackground(Color.magenta);

		for (int j = 0; j < NBRICK_ROWS; j++) {
			BRICKY = BRICK_Y_OFFSET + (BRICK_HEIGHT + BRICK_SEP) * j;
			for (int i = 0; i < NBRICKS_PER_ROW; i++) {
				BRICKX = (BRICK_SEP + BRICK_WIDTH) * i;
				GRect bricks = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				add(bricks, BRICKX, BRICKY);
				if (j == 0 || j == 1) {
					bricks.setFilled(true);
					bricks.setColor(Color.RED);
				}
				if (j == 2 || j == 3) {
					bricks.setFilled(true);
					bricks.setColor(Color.ORANGE);
				}
				if (j == 4 || j == 5) {
					bricks.setFilled(true);
					bricks.setColor(Color.YELLOW);
				}
				if (j == 6 || j == 7) {
					bricks.setFilled(true);
					bricks.setColor(Color.GREEN);
				}
				if (j == 8 || j == 9) {
					bricks.setFilled(true);
					bricks.setColor(Color.CYAN);
				}
			}
		}
	}
	// interface is ready, bricks are drown

	// addPaddle draws the paddle in the center of the canvas
	private void addPaddle() {

		int PADDLEY = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		add(paddle, getWidth() / 2 - PADDLE_WIDTH / 2, PADDLEY);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);

	}
	// paddle is drawn

	/*
	 * mouseMoved void moves the paddle. It changes x coordinate of the paddle in
	 * the direction of the mouse, so when you move the mouse paddle moves in the
	 * same direction . Y coordinate of the paddle does not change .
	 */
	public void mouseMoved(MouseEvent e) {
		double x = e.getX() - PADDLE_WIDTH / 2;

		if (e.getX() < PADDLE_WIDTH / 2) {
			x = 0;
		}
		if (e.getX() > getWidth() - PADDLE_WIDTH / 2) {
			x = getWidth() - PADDLE_WIDTH;
		}
		double paddleY = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle.setLocation(x, paddleY);

	}
	/*
	 * now we can move the paddle. paddle can not go over the screen. mouse is
	 * holding on the center of the paddle
	 */

	/* this void adds ball in the center of the canvas */
	private void addBall() {
		ball = new GOval(diameter, diameter);
		int ballX = (getWidth() - diameter) / 2;
		int ballY = (getHeight() - diameter) / 2;
		add(ball, ballX, ballY);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
	}

	/*
	 * getColliding objects tells us if the ball collided an object. it checks four
	 * points of the ball if there is an object the method returns it and also it
	 * changes direction of the ball depended on which point object was detected ,
	 * if there is not it returns null
	 */
	private double vx;
	private double vy;

	private GObject getCollidingObject() {
		if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 1) != null
				&& getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 1) != point
				&& getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 1) != pointlabel) {
			vy = -vy;
			return (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() - 1));
		} else if (getElementAt(ball.getX() + diameter + 1, ball.getY() + BALL_RADIUS) != null
				&& getElementAt(ball.getX() + diameter + 1, ball.getY() + BALL_RADIUS) != point
				&& getElementAt(ball.getX() + diameter + 1, ball.getY() + BALL_RADIUS) != pointlabel) {
			vx = -vx;
			return (getElementAt(ball.getX() + diameter + 1, ball.getY() + BALL_RADIUS));
		} else if (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + diameter + 1) != null
				&& getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + diameter + 1) != point
				&& getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + diameter + 1) != pointlabel) {
			vy = -vy;
			return (getElementAt(ball.getX() + BALL_RADIUS, ball.getY() + diameter + 1));
		} else if (getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS) != null
				&& getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS) != point
				&& getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS) != pointlabel) {
			vx = -vx;
			return (getElementAt(ball.getX() - 1, ball.getY() + BALL_RADIUS));
		} else {
			return null;
		}
	}

	/*
	 * vx and vy are chosen randomly to move the ball.
	 */

	private void moveBall() {
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
		vy = 3.0;

		// walls of the canvas rebound the ball
		while (true) {
			pause(PAUSE);
			ball.move(vx, vy);

			/*
			 * if ball touched the ground floor you can continue playing until you have a
			 * chance. also if you lost all the chances you can try again and restart the
			 * game NTURNS shows how many turns you have.
			 */
			if (ball.getY() > getHeight()) {
				TURNS = TURNS + 1;
				if (TURNS == NTURNS) {
					lostGame();
					tryAgain();
				} else if (TURNS < NTURNS) {
					continueLabel();
					ball.setLocation((getWidth() - diameter) / 2, (getHeight() - diameter) / 2);
					vx = rgen.nextDouble(1.0, 3.0);
					vy = 3.0;
					if (rgen.nextBoolean(0.5)) {
						vx = -vx;
					}
				}
			}
			if (ball.getX() > getWidth() - diameter) {
				vx = -vx;
			}
			if (ball.getY() < 0) {
				vy = -vy;
			}
			if (ball.getX() < 0) {
				vx = -vx;
			}

			GObject collider = getCollidingObject();
			/*
			 * if the ball collides with the paddle paddle rebounds the ball // but if the
			 * ball collides sidewall of the paddle, it changes the x coordinate of the ball
			 */
			if (collider == paddle) {
				if (ball.getY() + diameter > paddle.getY()) {
					ball.setLocation(ball.getX(), ball.getY() + PADDLE_HEIGHT);
				} else {
					AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
					bounceClip.play();
				}

			}

			if (collider == null || collider == paddle)
				continue;

			// points are removed when you break another brick to renew the point
			if (POINT != 0) {
				remove(point);
			}

			// ball will have bounce voice when it will collide with brick
			AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
			bounceClip.play();

			// points will be differend depended on which brick you removed
			if (collider.getColor() == Color.CYAN) {
				POINT = POINT + 5;
			} else if (collider.getColor() == Color.GREEN) {
				POINT = POINT + 10;
			} else if (collider.getColor() == Color.YELLOW) {
				POINT = POINT + 15;
			} else if (collider.getColor() == Color.ORANGE) {
				POINT = POINT + 20;
			} else if (collider.getColor() == Color.RED) {
				POINT = POINT + 25;
			}

			showPoint();

			remove(collider);
			REMOVEDBRICKS = REMOVEDBRICKS + 1;

			// in every 10 removed brick the game will get faster and faster
			if (REMOVEDBRICKS % 10 == 0) {
				PAUSE = PAUSE - 1;
			}

			/*
			 * if the player removed all the bricks from the canvas the game stops and tells
			 * him that he won the game
			 */
			if (REMOVEDBRICKS == NBRICKS_PER_ROW * NBRICK_ROWS) {
				wonGame();
				remove(ball);
				break;
			}
		}
	}

	private GLabel Continue;
	private GLabel clickToContinue;

	// continueLabel tells you to continue the game until you have a chance, it also
	// tells you how many turns(chances) are left
	private void continueLabel() {
		GImage continuegif = new GImage("Continue.gif");
		continuegif.setSize(200, 200);
		add(continuegif, (getWidth() - continuegif.getWidth()) / 2, (getHeight() - continuegif.getHeight()) / 2);

		if (NTURNS - TURNS == 1) {
			Continue = new GLabel("YOU HAVE " + (NTURNS - TURNS) + "  MORE CHANCE");
		} else {
			Continue = new GLabel("YOU HAVE " + (NTURNS - TURNS) + "  MORE CHANCES");
		}
		Continue.setFont("-15");
		add(Continue, (getWidth() - Continue.getWidth()) / 2,
				continuegif.getY() + continuegif.getHeight() + Continue.getHeight());
		clickToContinue = new GLabel("CLICK TO CONTINUE");
		add(clickToContinue, (getWidth() - clickToContinue.getWidth()) / 2, Continue.getY() + Continue.getHeight());

		waitForClick();
		remove(continuegif);
		remove(Continue);
		remove(clickToContinue);

	}

	private GLabel point;
	private GLabel pointlabel;

	// showPoint shows how many points you got while playing
	private void showPoint() {
		point = new GLabel(" " + POINT);
		pointlabel = new GLabel("your point is: ");
		add(pointlabel, 0, pointlabel.getHeight());
		add(point, pointlabel.getWidth(), pointlabel.getHeight());
	}

	// wonGame tells you that you won the game
	private void wonGame() {
		GImage happygif = new GImage("winner.gif");
		happygif.setSize(200, 200);
		add(happygif, (getWidth() - happygif.getWidth()) / 2, (getHeight() - happygif.getHeight()) / 2);

		GLabel winner = new GLabel("YOU WON !!!");
		winner.setFont("-20");
		add(winner, (getWidth() - winner.getWidth()) / 2, happygif.getY() - winner.getHeight());
	}
}
