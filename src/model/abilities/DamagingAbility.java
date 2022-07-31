package model.abilities;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;

public class DamagingAbility extends Ability {

	private int damageAmount;

	public DamagingAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int requiredActionPoints, int damageAmount) {
		super(name, manaCost, baseCooldown, castRange, castArea, requiredActionPoints);
		this.damageAmount = damageAmount;
	}
	
	@Override
	public void execute(ArrayList<Damageable> targets) {

		for(Damageable damageable:targets) {
			if (damageable instanceof Champion) {
				Champion champion=(Champion) damageable;
				champion.setCurrentHP(damageable.getCurrentHP()-damageAmount);
				if(champion.getCurrentHP()==0)
					champion.setCondition(Condition.KNOCKEDOUT);
			}
			if (damageable instanceof Cover) {
				Cover cover=(Cover) damageable;
				cover.setCurrentHP(damageable.getCurrentHP()-damageAmount);
				}
			
				
	}
	}
	public int getDamageAmount() {
		return damageAmount;
	}

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	@Override
	public String toString() {
		return "\n" + "= DamagingAbility"+", Damage Amount= " + damageAmount +"\n"+ super.toString();
	}
	public String toString2() {
		return "\n" + "= DamagingAbility"+", Damage Amount= " + damageAmount +"\n"+ super.toString2();
	}
	
	

}
