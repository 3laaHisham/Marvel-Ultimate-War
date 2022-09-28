package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	@Override
	public String toString() {
		return "Stun " + super.toString();
	}

	public Stun(int duration) {
		super("Stun", duration,EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		c.setCondition(Condition.INACTIVE);
	}

	@Override
	public void remove(Champion c) {
		for(Effect e : c.getAppliedEffects()) 
			if(e instanceof Stun) 
				return;
		
		c.setCondition(Condition.ACTIVE);
		
		for(Effect e : c.getAppliedEffects()) 
			if(e instanceof Root) {
				c.setCondition(Condition.ROOTED);
				break;
			}

	}

}
