package model.abilities;
import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	
	public CrowdControlAbility(String name, int manaCost, int baseCooldown, int castRange, AreaOfEffect castArea,
			int requiredActionPoints, Effect effect) {
		super(name, manaCost, baseCooldown, castRange, castArea, requiredActionPoints);
		this.effect = effect;
	}


	public Effect getEffect() {
		return effect;
	}


	@Override
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		
		Effect newEffect;
		for(Damageable champ: targets) {
			Champion champion=(Champion)champ;
			newEffect= (Effect) this.getEffect().clone();
			newEffect.apply( champion);
			champion.getAppliedEffects().add(newEffect );
			}
		
	}


	@Override
	public String toString() {
		return "\n" +"= CrowdControlAbility, "+"\n" + effect + "\n"+ super.toString() ;
	}
	public String toString2() {
		return "\n" +"= CrowdControlAbility, "+"\n" + effect + "\n"+ super.toString2() ;
	}
	
}
