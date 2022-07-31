package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Damageable;

public class HealingAbility extends Ability{
	private int healAmount;

	
	public HealingAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int requiredActionPoints, int healAmount) {
		super(name, manaCost, baseCooldown, castRange, castArea, requiredActionPoints);
		this.healAmount = healAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		// TODO Auto-generated method stub
		for(Damageable damageable:targets) {
			Champion champion=(Champion) damageable;
			champion.setCurrentHP(damageable.getCurrentHP()+healAmount);
		}
		
	}

	@Override
	public String toString() {
		return "\n" + "= HealingAbility"+", Heal Amount= " + healAmount +"\n"+ super.toString() ;
	}
	public String toString2() {
		return "\n" + "= HealingAbility"+", Heal Amount= " + healAmount +"\n"+ super.toString2() ;
	}
	

}
