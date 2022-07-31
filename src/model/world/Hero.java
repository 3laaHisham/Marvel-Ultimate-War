package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion {

@Override
public void useLeaderAbility(ArrayList<Champion> targets) {
	for(int i=0;i<targets.size();i++) {
		ArrayList<Effect> rEffects=new ArrayList<>();
		for(int j=0;j<targets.get(i).getAppliedEffects().size();j++) {
			if(targets.get(i).getAppliedEffects().get(j).getType()==EffectType.DEBUFF) {
				rEffects.add(targets.get(i).getAppliedEffects().get(j));
			}
		}
		for(Effect effect:rEffects)
			targets.get(i).getAppliedEffects().remove(effect);
		
		targets.get(i).getAppliedEffects().add(new Embrace(2));
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

