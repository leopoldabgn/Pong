package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import model.Ball;
import model.Pad;

public class Ground extends JPanel implements ActionListener {
	private static final long serialVersionUID = 163816386723817L;
	public static final int PAD_SPACE = 30;

	private Pad leftPad,
			rightPad;
	private Ball ball;
	private Timer tm = new Timer(15, this);
	private int scoreJ1, scoreJ2;
	private int scoreSize = 50, scoreY = 70;
	private int collisions = 0;
	private long KICKOFF_START_TIME = -1;
	private long KICKOFF_DELAY = 2000;

	public Ground(int w, int h) {
		this.setSize(new Dimension(w, h));
		this.setBackground(Window.BACKGROUND_COLOR);

		leftPad = new Pad(this, "LEFT", PAD_SPACE, PAD_SPACE, 20, 200);
		rightPad = new Pad(this,"RIGHT", w - 20 - PAD_SPACE, PAD_SPACE, 20, 200);
		ball = new Ball(this, 350, 350, 20, 7);

		// setupBalls(50, 50, getWidth()-100, getHeight()-100, 200, 1, 0, 11, 2);
		tm.start();
		kickoff(true);
	}

	public static int getRandomInt(int min, int max) {
		return (new Random()).nextInt(max - min + 1) + min;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
        Font font = new Font("Arial", Font.BOLD, scoreSize);

        // Appliquez la police au Graphics2D
        g2d.setFont(font);
		g2d.drawString(scoreJ1+"", getWidth() / 2 - 60, scoreY);
		g2d.drawString(scoreJ2+"", getWidth() / 2 + 60, scoreY);

		leftPad.draw(g2d);
		rightPad.draw(g2d);
		ball.draw(g2d);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Pad[] pads = new Pad[]{leftPad, rightPad};
		System.out.println(collisions);

		// Kickoff en cours
		if(KICKOFF_START_TIME != -1) {
			if(System.currentTimeMillis() - KICKOFF_START_TIME < KICKOFF_DELAY)
				return;
			leftPad.startMoving();
			rightPad.startMoving();
			ball.startMoving();
			KICKOFF_START_TIME = -1; // Fin du Kickoff
		}

		if(collisions == 3 || collisions == 7 || collisions == 11) {
			ball.addSpeed(3);
			collisions++;
		}

		ball.move();
		if (ball.isOut() != 0)
			putBallIn(ball);

		for(Pad p : pads) {
			if(p.canMove())
				p.move();
			if(collision(p, ball))
				collisions++;
		}

		if(ball.getX() > rightPad.getX() + rightPad.getWidth()) {
			// J2 perd le point
			scoreJ1++;
			kickoff(false);
		}
		else if( ball.getX() < leftPad.getX()) {
			// J1 perd le point
			scoreJ2++;
			kickoff(true);
		}
		
		this.repaint();
	}

	public void kickoff(boolean J1) {
		collisions = 0;
		ball.setSpeed(Ball.DEFAULT_SPEED);
		KICKOFF_START_TIME = System.currentTimeMillis();

		leftPad.stopMoving();
		rightPad.stopMoving();
		ball.stopMoving();

		leftPad.goToMiddle();
		rightPad.goToMiddle();

		int ballLeft = leftPad.getX() + leftPad.getWidth() + 10;
		int ballRight = rightPad.getX() - 10;

		ball.setY(getHeight() / 2);

		if(J1) {
			ball.setX(ballLeft);
		}
		else {
			ball.setX(ballRight);
		}

		leftPad.startMoving();
		rightPad.startMoving();
		ball.startMoving();
	}

	public static boolean collision(Pad pad, Ball ball) {
		if(!pad.isOnMe(ball))
			return false;

        if (pad.getType().equals("LEFT")) {
            ball.setSens(getRandomSens("UR", "R", "DR", ball.getSens()));
        } else if (pad.getType().equals("RIGHT")) {
            ball.setSens(getRandomSens("UL", "L", "DL", ball.getSens()));
        }

		return true;
    }

	public static void putBallIn(Ball b) {
		if (b.isOut() == 0)
			return;
		char first = b.getSens().charAt(0);
		char letter = b.getSens().charAt(b.getSens().length() - 1);

		if (b.isOut() == 'R') {
			b.setSens((first != letter ? "" + first : "") + "L");
		} else if (b.isOut() == 'L') {
			b.setSens((first != letter ? "" + first : "") + "R");
		} else if (b.isOut() == 'U') {
			b.setSens("D" + (letter != first ? "" + letter : ""));
		} else if (b.isOut() == 'D') {
			b.setSens("U" + (letter != first ? "" + letter : ""));
		}
	}

	public static String getRandomSens(String s1, String s2, String s3, String stop) {
		String sens;
		do {
			switch (getRandomInt(0, 2)) {
				case 0:
					sens = s1;
					break;
				case 1:
					sens = s2;
					break;
				case 2:
					sens = s3;
					break;
				default:
					sens = stop;
					System.out.println("ERROR, GETRANDOMSENS");
					break;
			}
		} while (sens.equals(stop));

		return sens;
	}

	public void refreshSizes(Dimension windowSize) {
		windowSize.setSize(windowSize.getWidth(), windowSize.getHeight() - 37);
		this.setSize(windowSize);
		rightPad.setX((int)windowSize.getWidth() - rightPad.getWidth() - PAD_SPACE);
	}

	public void stopBalls(int nb) {
		ball.stopMoving();
	}

	public Timer getTimer() {
		return tm;
	}

	public int getBallSpeed() {
		return ball.getSpeed();
	}

	public int getBallSize() {
		return ball.getSize();
	}

	public void setLeftPadVelocity(int x, int y) {
		leftPad.setVelocity(new Point(x, y));
	}

	public void setRightPadVelocity(int x, int y) {
		rightPad.setVelocity(new Point(x, y));
	}

	public Pad getLeftPad() {
		return leftPad;
	}

	public Pad getRightPad() {
		return rightPad;
	}

	public void addScoreJ1(int add) {
		this.scoreJ1 += add;
	}

	public void addScoreJ2(int add) {
		this.scoreJ2 += add;
	}

	public int getScoreJ1() {
		return scoreJ1;
	}

	public int getScoreJ2() {
		return scoreJ2;
	}

}
