package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

@Override
public void useLeaderAbility(ArrayList<Champion> targets) {
	for(int i=0;i<targets.size();i++) {
		ArrayList<Effect> effects = targets.get(i).getAppliedEffects();
		ArrayList<Effect> removeEffects=new ArrayList<>();
		
		for(int j=0;j<effects.size();j++) 
			if(effects.get(j).getType()==EffectType.DEBUFF) 
				removeEffects.add(effects.get(j));
		
		for(Effect effect:removeEffects)
			effects.remove(effect);
		
		effects.add(new Embrace(2));
		Embrace e=new Embrace(2);
		e.apply(targets.get(i));
		
	}
}
	
	
	public Hero(String name, int maxHP, int mana, int maxActionPointsPerTurn, int speed, int attackRange,
			int attackDamage) {
		super(name, maxHP, mana, maxActionPointsPerTurn, speed, attackRange, attackDamage);
	}

	@Override
	public String toString() {
		return "= Hero" + super.toString() ;
	}
	@Override
	public String toString2() {
		return "= Hero" + super.toString2() ;
	}
	@Override
	public String toString3() {
		return "= Hero" + super.toString3() ;
	}
}

