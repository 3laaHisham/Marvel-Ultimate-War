package model.world;

import java.util.ArrayList;

public class Villain extends Champion {


	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) {
		for (Champion champ: targets) {
			if ((int)champ.getCurrentHP()/(int)champ.getMaxHP()<0.3)
				champ.setCondition(Condition.KNOCKEDOUT);
		}
		
	}

	public Villain(String name, int maxHP, int mana, int maxActionPointsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionPointsPerTurn, speed, attackRange, attackDamage);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "= Villain" + super.toString() ;
	}
	public String toString2() {
		return "= Villain" + super.toString2() ;
	}
	public String toString3() {
		return "= Villain" + super.toString3() ;
	}

}
