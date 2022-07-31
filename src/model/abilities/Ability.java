package model.abilities;
import java.util.ArrayList;
import model.world.*;


public abstract class Ability {
	
	private String name;	
	private int manaCost;
	private int baseCooldown;
	private int currentCooldown;
	private int castRange;
	private int requiredActionPoints;
	private AreaOfEffect castArea;
	
	public Ability(String name, int manaCost, int baseCooldown, int castRange,
		 AreaOfEffect castArea ,int requiredActionPoints) {
		this.name = name;
		this.manaCost = manaCost;
		this.baseCooldown = baseCooldown;
		this.currentCooldown = 0;
		this.castRange = castRange;
		this.requiredActionPoints = requiredActionPoints;
		this.castArea = castArea;
	}
	
	public abstract void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException ;

	public int getCurrentCooldown() {
		return currentCooldown;
	}


	public void setCurrentCooldown(int currentCooldown) {
		if(currentCooldown>baseCooldown) {
			this.currentCooldown = baseCooldown;
		}
		else
		this.currentCooldown = Math.max(0, currentCooldown);
	}


	public String getName() {
		return name;
	}


	public int getManaCost() {
		return manaCost;
	}


	public int getBaseCooldown() {
		return baseCooldown;
	}


	public int getCastRange() {
		return castRange;
	}


	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}


	public AreaOfEffect getCastArea() {
		return castArea;
	}

	@Override
	public String toString() {
		return "- Name is " + name +", Mana Cost=" + manaCost + "\n"+ "- Base Cooldown= " + baseCooldown
				+ ", Cast Range= " + castRange + "\n" + "- Required Action Points= "+ requiredActionPoints +"\n" + "- Cast Area= " + castArea +"\n";
	}
	
	
	public String toString2() {
		return "- Name is " + name +" , Mana Cost=" + manaCost + "\n"+ "- Base Cooldown= " + baseCooldown +" , Current Cooldown= " + currentCooldown
				+  "\n"+ "- Cast Range= " + castRange + " , Required Action Points= "+ requiredActionPoints +"\n" + "- Cast Area= " + castArea +"\n";
	}
	
}
