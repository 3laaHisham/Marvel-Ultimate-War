package model.effects;

import java.util.ArrayList;

import model.world.Champion;

public class Dodge extends Effect{

	@Override
	public void apply(Champion c) {
		c.setSpeed((int) (c.getSpeed()*1.05));
	}

	@Override
	public void remove(Champion c) {
		c.setSpeed((int)(c.getSpeed()/1.05));
	}

	public Dodge(int duration) {
		super("Dodge", duration,EffectType.BUFF);
	}

	@Override
	public String toString() {
		return "Dodge " + super.toString();
	}

}
