package model.world;

import java.util.ArrayList;

import model.effects.Stun;

public class AntiHero extends Champion {

	public AntiHero(String name, int maxHP, int mana, int maxActionPointsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionPointsPerTurn, speed, attackRange, attackDamage);
	}

	public void useLeaderAbility(ArrayList<Champion> targets) {
	
		
			for (int i = 0; i < targets.size(); i++) {
				
				
				Stun stun=new Stun(2);
				targets.get(i).getAppliedEffects().add(stun);
				stun.apply(targets.get(i));
				
			
			}
			
	}

	@Override
	public String toString() {
		return "= AntiHero" + super.toString() ;
	}

	public String toString2() {
		return "= AntiHero" + super.toString2() ;
	}
	public String toString3() {
		return "= AntiHero" + super.toString3() ;
	}
}
