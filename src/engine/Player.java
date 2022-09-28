package engine;

import java.util.ArrayList;
import java.util.List;
import model.world.Champion;
import views.buttons.DamageableButton;

public class Player {
	private String name;
	private Champion leader;
	private ArrayList<Champion> team;
	private List<DamageableButton> children;
	
	public Player(String name) {
		team = new ArrayList<Champion>();
		children = new ArrayList<DamageableButton>();
		this.name = name;
	}
	
	public Champion getLeader() {
		return leader;
	}

	public void setLeader(Champion leader) {
		this.leader = leader;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Champion> getTeam() {
		return team;
	}

	public List<DamageableButton> getChildren() {
		return children;
	}
	
	public DamageableButton getChampionButton(Champion champ) {
		DamageableButton btn = this.getChildren().stream().filter(child -> child.getText().equals(champ.getName()))
				.findAny().get();
		return btn;
	}

}
