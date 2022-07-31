package model.world;
import java.awt.*;
import java.lang.Math;

public class Cover implements Damageable {
 private int currentHP;
 private Point location;
	
	public Cover(int x, int y) {
		location = new Point(x,y);
		currentHP= (int) (Math.random()*(900)+100);
	}
	@Override
	public int getCurrentHP() {
		return currentHP;
	}
	@Override
	public void setCurrentHP(int currentHP) {
		this.currentHP = Math.max(0, currentHP);
	}

	@Override
	public Point getLocation() {
		return location;
	}

	
}
