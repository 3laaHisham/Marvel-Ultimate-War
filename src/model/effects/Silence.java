package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	@Override
	public String toString() {
		return "Silence " + super.toString();
	}

	public Silence(int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(Champion c) {
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+2);
	}

	@Override
	public void remove(Champion c) {
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);

	}
	
	

}
