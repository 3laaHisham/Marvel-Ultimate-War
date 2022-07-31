package model.effects;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root(int duration) {
		super("Root", duration,EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		if(c.getCondition()==Condition.ACTIVE)
				c.setCondition(Condition.ROOTED);
	}

	@Override
	public void remove(Champion c) {
		
		if(c.getCondition()==Condition.ROOTED) {
			for(Effect e : c.getAppliedEffects()) 
				if(e instanceof Root) {
					return;
		}
			c.setCondition(Condition.ACTIVE);

			
	}
		}

	@Override
	public String toString() {
		return "Root " + super.toString();
	}

}
