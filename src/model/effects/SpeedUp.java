package model.effects;

import model.world.Champion;

public class SpeedUp extends Effect {

	@Override
	public String toString() {
		return "SpeedUp " + super.toString();
	}

	public SpeedUp(int duration) {
		super("SpeedUp", duration,EffectType.BUFF);
	}

	@Override
	public void apply(Champion c) {
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
		c.setSpeed((int)(c.getSpeed()*1.15));
		
	}

	@Override
	public void remove(Champion c) {
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		c.setSpeed((int)(c.getSpeed()/1.15));

	}

}
