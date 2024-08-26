package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import view.Ground;

public class Pad
{
	private int x, y, w, h;
	private Point velocity = new Point(0, 0); // speed
	private Ground gd;
	private String type = "LEFT";
	private boolean noMove;

    private long lastTimeMove;
    public static long DELAY_MOVE = 3; // 3ms
	public static int SPEED = 20;

	public Pad(Ground gd, String type, int x, int y, int w, int h)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.gd = gd;
		this.w = w;
		this.h = h;
	}
	
	public static int getRandomInt(int max)
	{
		return (new Random()).nextInt(max);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRoundRect(x, y, w, h, 10, 10);
	}

	public boolean canMove() {
		if(noMove)
			return false;
		int oldX = x;
		int oldY = y;

		x += getVelX();
		y += getVelY();

		char out = isOut();

		x = oldX;
		y = oldY;

		return out == 0;
	}

    public boolean move() {
        return move(true, false);
    }

    public boolean move(boolean restrictX, boolean restrictY) {
        if(velocity.getX() == 0 && velocity.getY() == 0)
            return false;
        if(System.currentTimeMillis() - lastTimeMove >= DELAY_MOVE) {
			this.x += getVelX();
			this.y += getVelY();
            lastTimeMove = System.currentTimeMillis();
            return true;
        }
        return false;
    }

	public boolean isOnMe(Ball b)
	{
		return ((b.getX()+b.getSize() >= x && b.getX() <= x+w) && (b.getY()+b.getSize() >= y && b.getY() <= y+h));
	}
	
	public char isOut()
	{
		if(x+w > gd.getWidth() && (y >= 0 && y <= gd.getHeight()))
			return 'R';
		else if(x < 0 && (y >= 0 && y <= gd.getHeight())) 
			return 'L';
		else if(y < 0 && (x >= 0 && x <= gd.getWidth()))
			return 'U';
		else if(y+h > gd.getHeight() && (x >= 0 && x <= gd.getWidth()))
			return 'D';
		return 0;
	}
	
	public void goToMiddle() {
		setY((gd.getHeight() / 2) - (getHeight() / 2));
	}

	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
	
    public void setVelocity(Point vel) {
        this.velocity = new Point(vel);
    }

    public void setVelX(int velX) {
        this.velocity.setLocation(velX, getVelY());
    }

    public void setVelY(int velY) {
        this.velocity.setLocation(getVelX(), velY);
    }

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

    public int getVelX() {
        return (int)velocity.getX();
    }

    public int getVelY() {
        return (int)velocity.getY();
    }

	public boolean isGoingUp() {
		return getY() < 0;
	}

	public boolean isGoingDown() {
		return getY() > 0;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}


	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public void stopMoving() {
		this.noMove = true;
	}

	public void startMoving() {
		this.noMove = false;
	}
}
