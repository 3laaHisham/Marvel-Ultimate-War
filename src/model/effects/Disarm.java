package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {

	@Override
	public void apply(Champion c) {
		c.getAbilities().add(new DamagingAbility("Punch", 0, 1, 1, AreaOfEffect.SINGLETARGET, 1, 50));
	}

	@Override
	public void remove(Champion c) {
		ArrayList<Ability> abArr = c.getAbilities();
		for (int i = 0; i < abArr.size(); i++) {
			if( abArr.get(i).getName()=="Punch") {
				abArr.remove(i);
				break;
			}
		}
		
	}


	public Disarm(int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
	}

	@Override
	public String toString() {
		return "Disarm " + super.toString();
	}
	
}
