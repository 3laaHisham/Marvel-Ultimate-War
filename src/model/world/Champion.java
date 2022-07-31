package model.world;
import java.util.*;
import engine.*;
import javafx.scene.control.ProgressBar;

import java.awt.*;
import model.abilities.*;
import model.effects.*;

public abstract class Champion implements Comparable,Damageable{
	
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	private ProgressBar pbHealth;
	public Champion(String name, int maxHP, int mana, int maxActionPointsPerTurn,int speed,
			int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = maxHP;
		this.currentActionPoints = maxActionPointsPerTurn;
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.speed = speed;
		this.condition=Condition.ACTIVE;
		abilities = new ArrayList<Ability>();
		appliedEffects = new ArrayList<Effect>();
		pbHealth = new ProgressBar(1);
		pbHealth.setStyle("-fx-accent: red;");
		pbHealth.setMinWidth(100);
	}
	
	public ProgressBar getPbHealth() {
		return pbHealth;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public abstract void useLeaderAbility(ArrayList<Champion> targets);
	
	@Override
	public int compareTo(Object o) {
		Champion champion=(Champion)o;
		if( this.getSpeed()==champion.getSpeed())
			return (this.getName().compareTo(champion.getName()));
		else
		return champion.getSpeed()-this.getSpeed();
	}
	
	@Override
	public int getCurrentHP() {
		return currentHP;
	}
	@Override
	public void setCurrentHP(int currentHP) {
		if(currentHP>maxHP) 
			this.currentHP = maxHP;
		else
		this.currentHP = Math.max(0, currentHP);
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}
	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}
	public int getCurrentActionPoints() {
		return currentActionPoints;
	}
	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints>maxActionPointsPerTurn) {
			this.currentActionPoints = maxActionPointsPerTurn;
		}
		else {
			this.currentActionPoints = Math.max(0, currentActionPoints);
		}
	}
	public int getAttackDamage() {
		return attackDamage;
	}
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public Condition getCondition() {
		return condition;
	}
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	@Override
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public int getAttackRange() {
		return attackRange;
	}
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	@Override
	public String toString() {
		return " ,Name is " + name +"\n"+ 
				"- Max HP= " + maxHP + "/n"+ ", Mana= " + mana
				+ "\n" + "- Max Action Points Per Turn= " + maxActionPointsPerTurn 
				+ "\n" +"- Attack Range= " + attackRange + " ,Attack Damage= " + attackDamage  +"\n"+"- Speed=" + speed
				+"\n"+ "\n"+ "- Abilities are" + "\n" + abilities;
	}
	
	public String toString2() {
		return " ,Name is " + name + "\n"+ 
				"- Current HP= " + currentHP + "/n"+ " , Mana= " + mana
				+ "\n" + "- Current Action Points= " + currentActionPoints 
				+ "\n" +"- Attack Range= " + attackRange + " , Attack Damage= " + attackDamage +"\n"+" ,Speed=" + speed
				+"\n"+ "\n"+ "- Abilities are" + "\n" + abilities.get(0).toString2()+ abilities.get(1).toString2()+ abilities.get(2).toString2()
				+ "\n"+ "Applied Effects are "+"\n" + appliedEffects;
	}
	public String toString3() {
		return 
				 "\n"+ "-Name is " + name + "\n" 
				+ "- Current HP= " + currentHP 
				+ "\n"+ "- Mana= " + mana
				+ "\n" + "- Current Action Points= " + currentActionPoints 
				+ "\n" +"- Attack Range= " + attackRange 
				+ "\n" + "- Attack Damage= " + attackDamage 
				+ "\n"+"-Speed=" + speed
				+ "\n"+ "\n"+ "- Abilities are" 
				+ "\n" + "\n" + abilities.get(0).getName()
				+ "\n" + abilities.get(1).getName()
				+ "\n"+ abilities.get(2).getName() 
				+ "\n" + "\n"+ "Applied Effects are "+"\n" + appliedEffects;
	}
	


	}

	
	
