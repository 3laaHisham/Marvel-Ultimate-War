package model.effects;

import model.world.Champion;

public abstract class Effect implements Cloneable {
	private String name;
	private int duration;
	private EffectType type;
	
	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.duration = duration;
		this.type = type;
	}
	
	public abstract void apply(Champion c);
	
	public abstract void remove(Champion c);
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public EffectType getType() {
		return type;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		return "Effect [ " +"Duration=" + duration + ", Type=" + type + " ]";
	}

	
	
	

}
