package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import view.Ground;

public class Ball 
{
	public static int DEFAULT_SPEED = 7;
	private int x, y, size, speed = DEFAULT_SPEED;
	private String sens = "";
	private Ground gd;
	private boolean moving = true;
	
	public Ball(Ground gd, int x, int y, int size, int speed)
	{
		this.x = x;
		this.y = y;
		this.sens = "R";
		this.gd = gd;
		this.size = size;
		this.speed = speed;
	}
	
	public static int getRandomInt(int max)
	{
		return (new Random()).nextInt(max);
	}
	
	public void setRandomSens()
	{
		switch(getRandomInt(8))
		{
		case 0:
			sens = "R";
			break;
		case 1:
			sens = "L";
			break;
		case 2:
			sens = "U";
			break;
		case 3:
			sens = "D";
			break;
		case 4:
			sens = "UL";
			break;
		case 5:
			sens = "UR";
			break;
		case 6:
			sens = "DL";
			break;
		case 7:
			sens = "DR";
			break;
		default:
			sens = "";
			break;
		}
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillOval(x, y, size, size);
	}
	
	public void move()
	{
		if(!isMoving())
			return;
		switch(sens)
		{
		case "R":
			x += speed;
			break;
		case "L":
			x -= speed;
			break;
		case "U":
			y -= speed;
			break;
		case "D":
			y += speed;
			break;
		case "UL":
			y -= speed;
			x -= speed;
			break;
		case "UR":
			y -= speed;
			x += speed;
			break;
		case "DL":
			y += speed;
			x -= speed;
			break;
		case "DR":
			y += speed;
			x += speed;
			break;
		}
	}

	public boolean isOnMe(Ball b)
	{
		return ((b.getX()+b.getSize() >= x && b.getX() <= x+size) && (b.getY()+b.getSize() >= y && b.getY() <= y+size));
	}
	
	public char isOut()
	{
		if(x+size > gd.getWidth() && (y >= 0 && y <= gd.getHeight()))
			return 'R';
		else if(x < 0 && (y >= 0 && y <= gd.getHeight())) 
			return 'L';
		else if(y < 0 && (x >= 0 && x <= gd.getWidth()))
			return 'U';
		else if(y+size > gd.getHeight() && (x >= 0 && x <= gd.getWidth()))
			return 'D';
		return 0;
	}
	
	public void stopMoving()
	{
		this.moving = false;
	}
	
	public void startMoving()
	{
		this.moving = true;
	}
	
	public boolean isMoving()
	{
		return this.moving;
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

	public int getSize() 
	{
		return size;
	}

	public void setSize(int size) 
	{
		this.size = size;
	}

	public void setSens(String str)
	{
		this.sens = str;
	}
	
	public String getSens()
	{
		return sens;
	}
	
	public void addSpeed(int add) {
		this.speed += add;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed()
	{
		return this.speed;
	}

}
