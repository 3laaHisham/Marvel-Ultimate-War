package model.effects;

import model.abilities.Ability;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.world.Champion;

public class PowerUp extends Effect {

	public PowerUp(int duration) {
		super("PowerUp", duration,EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		for(Ability a : c.getAbilities()) {
			if( a instanceof DamagingAbility) {
				DamagingAbility damagingAbility = (DamagingAbility) a;
				damagingAbility.setDamageAmount((int)(damagingAbility.getDamageAmount()*1.2));
			}
				 else if(a instanceof HealingAbility) {
					 HealingAbility healingAbility=(HealingAbility) a;
					 healingAbility.setHealAmount((int)(healingAbility.getHealAmount()*1.2));
				 }
		}
	}
		
	@Override
	public void remove(Champion c) {

		for(Ability a : c.getAbilities()) {
			if( a instanceof DamagingAbility) {
				DamagingAbility damagingAbility = (DamagingAbility) a;
				damagingAbility.setDamageAmount((int)(damagingAbility.getDamageAmount()/1.2));
			}
				 else if(a instanceof HealingAbility) {
					 HealingAbility healingAbility=(HealingAbility) a;
					 healingAbility.setHealAmount((int)(healingAbility.getHealAmount()/1.2));
				 }
		}
		

	}

	@Override
	public String toString() {
		return "PowerUp " + super.toString() ;
	}
	
}
