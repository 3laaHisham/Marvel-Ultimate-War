package engine;

import java.awt.Point;
import model.world.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.border.EtchedBorder;
import exceptions.*;
import model.abilities.*;
import model.effects.*;
import java.lang.Math;

public class Game {
	private Player firstPlayer;
	private Player secondPlayer;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private Object[][] board;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private PriorityQueue turnOrder;
	private static final int BOARDHEIGHT = 5;
	private static final int BOARDWIDTH = 5;

	public Game(Player firstPlayer, Player secondPlayer) throws Exception {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.board = new Object[5][5];
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		firstLeaderAbilityUsed = false;
		secondLeaderAbilityUsed = false;
		turnOrder = new PriorityQueue(6);
		loadAbilities("./assests/csv/Abilities.csv");
		loadChampions("./assests/csv/Champions.csv");
		placeCovers();
		placeChampions();
		prepareChampionTurns();
	}

	public Champion getCurrentChampion() {
		return (Champion) turnOrder.peekMin();
	}

	public Player checkGameOver() {
		boolean player1Won = true;
		boolean player2Won = true;
		for (Champion champion : firstPlayer.getTeam())
			if (champion.getCondition() != Condition.KNOCKEDOUT)
				player2Won = false; // Player1 won or the game is on
		for (Champion champion : secondPlayer.getTeam())
			if (champion.getCondition() != Condition.KNOCKEDOUT)
				player1Won = false; // Player2 won or the game is on

		if (player1Won && !player2Won)
			return firstPlayer;
		else if (!player1Won && player2Won)
			return secondPlayer;
		return null;
	}

	public void move(Direction direction) throws UnallowedMovementException, NotEnoughResourcesException {

		Champion currChamp = getCurrentChampion();

		if (currChamp.getCondition() == Condition.ROOTED)
			throw new UnallowedMovementException();
		if (currChamp.getCurrentActionPoints() < 1)
			throw new NotEnoughResourcesException();

		Point point = currChamp.getLocation();
		int x = point.x;
		int y = point.y;

		switch (direction) {

		case UP:
			if (x == 4)
				throw new UnallowedMovementException();
			if (board[x + 1][y] != null)
				throw new UnallowedMovementException();

			point.x += 1;
			board[x + 1][y] = currChamp;

			break;

		case DOWN:
			if (x == 0)
				throw new UnallowedMovementException();
			if (board[x - 1][y] != null)
				throw new UnallowedMovementException();

			point.x -= 1;
			board[x - 1][y] = currChamp;

			break;

		case RIGHT:
			if (y == 4)
				throw new UnallowedMovementException();
			if (board[x][y + 1] != null)
				throw new UnallowedMovementException();

			board[x][y + 1] = currChamp;
			point.y += 1;

			break;

		case LEFT:
			if (y == 0)
				throw new UnallowedMovementException();
			if (board[x][y - 1] != null)
				throw new UnallowedMovementException();

			point.y -= 1;
			board[x][y - 1] = currChamp;

			break;

		default:
			return;
		}

		board[x][y] = null;
		currChamp.setLocation(point);
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 1);
	}

	public void attack(Direction direction) throws ChampionDisarmedException, NotEnoughResourcesException {

		Champion currChamp = getCurrentChampion();

		if (currChamp.getCurrentActionPoints() < 2)
			throw new NotEnoughResourcesException();
		for (Effect effect : currChamp.getAppliedEffects())
			if (effect instanceof Disarm)
				throw new ChampionDisarmedException();

		int range = currChamp.getAttackRange();
		int x = currChamp.getLocation().x;
		int y = currChamp.getLocation().y;

		switch (direction) {

		case UP:
			int rangeUp = (x + range) >= 4 ? 4 : x + range;
			for (int i = x + 1; i <= rangeUp; i++)
				if (board[i][y] != null) {
					validateAttack(i, y);
					break;
				}

			break;

		case DOWN:
			int rangeDown = (x - range) <= 0 ? 0 : x - range;
			for (int i = x - 1; i >= rangeDown; i--)
				if (board[i][y] != null) {
					validateAttack(i, y);
					break;
				}

			break;

		case RIGHT:
			int rangeRight = (y + range) >= 4 ? 4 : y + range;
			for (int j = y + 1; j <= rangeRight; j++)
				if (board[x][j] != null) {
					validateAttack(x, j);
					break;
				}

			break;

		case LEFT:
			int rangeLeft = (y - range) <= 0 ? 0 : y - range;
			for (int j = y - 1; j >= rangeLeft; j--)
				if (board[x][j] != null) {
					validateAttack(x, j);
					break;
				}

			break;

		default:
			return;
		}

		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints() - 2);
	}

	public Player whichPlayer(Champion champion) {
		return firstPlayer.getTeam().contains(champion) ? firstPlayer : secondPlayer;
	}

	public boolean checkTeam(Champion champion, Champion target) {
		ArrayList<Champion> mine;
		if (firstPlayer.getTeam().contains(champion))
			mine = firstPlayer.getTeam();
		else
			mine = secondPlayer.getTeam();

		return mine.contains(target);
	}

	public void validateAttack(int x, int y) {

		Champion champion = getCurrentChampion();
		Damageable damagable = (Damageable) board[x][y];
		int targetHP = damagable.getCurrentHP();
		int champAttack = champion.getAttackDamage();

		if (damagable instanceof Cover) {
			Cover target = (Cover) damagable;
			target.setCurrentHP(targetHP - champAttack);
		} else {
			Champion target = (Champion) damagable;

			for (Effect e : target.getAppliedEffects()) {
				if (e instanceof Dodge && Math.round(Math.random()) == 1)
					return;
				if (e instanceof Shield) {
					e.remove(target);
					return;
				}
			}
			int extra = champion.getAttackDamage() / 2;

			boolean specialDamage = false;
			if (champion instanceof Hero)
				if (target instanceof Villain || target instanceof AntiHero)
					specialDamage = true;

			if (champion instanceof Villain)
				if (target instanceof Hero || target instanceof AntiHero)
					specialDamage = true;

			if (champion instanceof AntiHero)
				if (target instanceof Hero || target instanceof Villain)
					specialDamage = true;

			if (specialDamage)
				target.setCurrentHP(targetHP - (champAttack + extra));
			else
				target.setCurrentHP(targetHP - champAttack);
		}

		if (damagable.getCurrentHP() == 0)
			kill(damagable);

	}

	public void kill(Damageable damagable) {
		int x = damagable.getLocation().x;
		int y = damagable.getLocation().y;
		board[x][y] = null;

		if (damagable instanceof Champion) {
			Champion target = (Champion) damagable;

			target.setLocation(null);
			target.setCondition(Condition.KNOCKEDOUT);

			if (firstPlayer.getTeam().contains(target))
				firstPlayer.getTeam().remove(target);
			else
				secondPlayer.getTeam().remove(target);

			PriorityQueue pq = new PriorityQueue(6);
			while (!turnOrder.isEmpty()) {
				Champion champ = (Champion) turnOrder.remove();
				if (champ != target)
					pq.insert(champ);
			}
			turnOrder = pq;

		}
	}

	public void endTurn() {
		turnOrder.remove();
		if (turnOrder.isEmpty())
			prepareChampionTurns();

		Champion champion = getCurrentChampion();
		while (champion.getCondition() == Condition.INACTIVE) {
			updates(champion);
			turnOrder.remove();
			champion = getCurrentChampion();
		}

		updates(champion);
	}

	public void updates(Champion champion) {
		ArrayList<Effect> removeEffects = new ArrayList<>();
		for (Effect effect : champion.getAppliedEffects()) {
			if (effect.getDuration() <= 1)
				removeEffects.add(effect);
			else
				effect.setDuration(effect.getDuration() - 1);
		}
		for (Effect effect : removeEffects) {
			champion.getAppliedEffects().remove(effect);
			effect.remove(champion);
		}

		for (Ability a : champion.getAbilities())
			a.setCurrentCooldown(a.getCurrentCooldown() - 1);

		champion.setCurrentActionPoints(champion.getMaxActionPointsPerTurn());
	}

	private void prepareChampionTurns() {
		for (Champion c : firstPlayer.getTeam())
			if (c.getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(c);
		for (Champion c : secondPlayer.getTeam())
			if (c.getCondition() != Condition.KNOCKEDOUT)
				turnOrder.insert(c);
	}

	public void castAbility(Ability ability) throws AbilityUseException, InvalidTargetException,
			NotEnoughResourcesException, CloneNotSupportedException {

		Champion currChamp = getCurrentChampion();
		int champCurrPoints = currChamp.getCurrentActionPoints();
		int abilityMaxPoints = ability.getRequiredActionPoints();
		int champCurrMana = currChamp.getMana();
		int abilityCostMana = ability.getManaCost();

		if (ability.getCurrentCooldown() != 0)
			throw new AbilityUseException();
		for (Effect eff : currChamp.getAppliedEffects())
			if (eff instanceof Silence)
				throw new AbilityUseException();
		if (champCurrMana < abilityCostMana || champCurrPoints < abilityMaxPoints)
			throw new NotEnoughResourcesException();

		int x = currChamp.getLocation().x;
		int y = currChamp.getLocation().y;

		ArrayList<Damageable> targets = new ArrayList<>();

		switch (ability.getCastArea()) {

		case SELFTARGET:
			targets.add(currChamp);

			break;

		case TEAMTARGET:
			for (Champion champ : firstPlayer.getTeam())
				if (Math.abs(y - champ.getLocation().y) + Math.abs(x - champ.getLocation().x) <= ability.getCastRange())
					try {
						validateAndAdd(ability, champ, targets);
					} catch (InvalidTargetException e) {
					}

			for (Champion champ : secondPlayer.getTeam())
				if (Math.abs(y - champ.getLocation().y) + Math.abs(x - champ.getLocation().x) <= ability.getCastRange())
					try {
						validateAndAdd(ability, champ, targets);
					} catch (InvalidTargetException e) {
					}

			break;

		case SURROUND:
			int xLowerLimit = (x == 0) ? 0 : x - 1;
			int xUpperLimit = (x == 4) ? 4 : x + 1;
			int yLowerLimit = (y == 0) ? 0 : y - 1;
			int yUpperLimit = (y == 4) ? 4 : y + 1;

			for (int i = xLowerLimit; i <= xUpperLimit; i++)
				for (int j = yLowerLimit; j <= yUpperLimit; j++)
					if ((x == i && y == j) || board[i][j] == null)
						continue;
					else
						try {
							validateAndAdd(ability, (Damageable) board[i][j], targets);
						} catch (InvalidTargetException e) {
						}

			break;

		}

		ability.execute(targets);
		for (Damageable target : targets)
			if (target.getCurrentHP() == 0)
				kill(target);

		currChamp.setCurrentActionPoints(champCurrPoints - abilityMaxPoints);
		currChamp.setMana(champCurrMana - abilityCostMana);
		ability.setCurrentCooldown(ability.getBaseCooldown());
	}

	public void castAbility(Ability ability, Direction direction) throws AbilityUseException, InvalidTargetException,
			NotEnoughResourcesException, CloneNotSupportedException {

		Champion currChamp = getCurrentChampion();
		int champCurrPoints = currChamp.getCurrentActionPoints();
		int abilityMaxPoints = ability.getRequiredActionPoints();
		int champCurrMana = currChamp.getMana();
		int abilityCostMana = ability.getManaCost();

		for (Effect eff : currChamp.getAppliedEffects())
			if (eff instanceof Silence)
				throw new AbilityUseException();
		if (ability.getCurrentCooldown() != 0)
			throw new AbilityUseException();
		if (champCurrMana < abilityCostMana || champCurrPoints < abilityMaxPoints)
			throw new NotEnoughResourcesException();

		int range = ability.getCastRange();
		int x = currChamp.getLocation().x;
		int y = currChamp.getLocation().y;
		ArrayList<Damageable> targets = new ArrayList<>();

		switch (direction) {

		case UP:
			int rangeUP = (x + range) >= 4 ? 4 : x + range;
			for (int i = x + 1; i <= rangeUP; i++)
				if (board[i][y] != null)
					try {
						validateAndAdd(ability, (Damageable) board[i][y], targets);
					} catch (InvalidTargetException e) {
					}

			break;

		case DOWN:
			int rangeDown = (x - range) <= 0 ? 0 : x - range;
			for (int i = x - 1; i >= rangeDown; i--)
				if (board[i][y] != null)
					try {
						validateAndAdd(ability, (Damageable) board[i][y], targets);
					} catch (InvalidTargetException e) {
					}

			break;

		case RIGHT:
			int rangeRight = (y + range) >= 4 ? 4 : y + range;
			for (int j = y + 1; j <= rangeRight; j++)
				if (board[x][j] != null)
					try {
						validateAndAdd(ability, (Damageable) board[x][j], targets);
					} catch (InvalidTargetException e) {
					}

			break;

		case LEFT:
			int rangeLeft = (y - range) <= 0 ? 0 : y - range;
			for (int j = y - 1; j >= rangeLeft; j--)
				if (board[x][j] != null)
					try {
						validateAndAdd(ability, (Damageable) board[x][j], targets);
					} catch (InvalidTargetException e) {
					}

			break;

		default:
			return;

		}
		ability.execute(targets);
		for (Damageable target : targets) {
			if (target.getCurrentHP() == 0)
				kill(target);
		}
		currChamp.setCurrentActionPoints(champCurrPoints - abilityMaxPoints);
		currChamp.setMana(champCurrMana - abilityCostMana);
		ability.setCurrentCooldown(ability.getBaseCooldown());
	}

	public void castAbility(Ability ability, int x, int y) throws AbilityUseException, InvalidTargetException,
			NotEnoughResourcesException, CloneNotSupportedException {

		Champion currChamp = getCurrentChampion();
		int champCurrPoints = currChamp.getCurrentActionPoints();
		int abilityMaxPoints = ability.getRequiredActionPoints();
		int champCurrMana = currChamp.getMana();
		int abilityCostMana = ability.getManaCost();
		int xChamp = currChamp.getLocation().x;
		int yChamp = currChamp.getLocation().y;

		if (ability.getCurrentCooldown() != 0)
			throw new AbilityUseException();
		for (Effect eff : currChamp.getAppliedEffects())
			if (eff instanceof Silence)
				throw new AbilityUseException();
		if (champCurrMana < abilityCostMana || champCurrPoints < abilityMaxPoints)
			throw new NotEnoughResourcesException();
		if (board[x][y] == null)
			throw new InvalidTargetException();
		if (Math.abs(y - yChamp) + Math.abs(x - xChamp) > ability.getCastRange())
			throw new AbilityUseException();

		ArrayList<Damageable> targets = new ArrayList<>();
		Damageable damageable = (Damageable) board[x][y];

		validateAndAdd(ability, damageable, targets);

		ability.execute(targets);
		for (Damageable target : targets) {
			if (target.getCurrentHP() == 0)
				kill(target);
		}

		currChamp.setCurrentActionPoints(champCurrPoints - abilityMaxPoints);
		currChamp.setMana(champCurrMana - abilityCostMana);
		ability.setCurrentCooldown(ability.getBaseCooldown());

	}

	public void validateAndAdd(Ability a, Damageable damageable, ArrayList<Damageable> targets)
			throws InvalidTargetException {

		Champion currChamp = getCurrentChampion();

		if (a instanceof DamagingAbility) {
			if (damageable instanceof Champion) {
				Champion target = (Champion) damageable;
				if (checkTeam(currChamp, target))
					throw new InvalidTargetException();
				else {
					for (Effect eff : target.getAppliedEffects())
						if (eff instanceof Shield) {
							eff.remove(target);
							return;
						}
					targets.add(target);
				}

			} else
				targets.add((Cover) damageable);
		}

		if (a instanceof HealingAbility) {
			if (damageable instanceof Cover)
				throw new InvalidTargetException();
			else {
				Champion target = (Champion) damageable;
				if (checkTeam(currChamp, target))
					targets.add(target);
				else
					throw new InvalidTargetException();
			}
		}

		if (a instanceof CrowdControlAbility) {
			if (damageable instanceof Cover)
				throw new InvalidTargetException();

			Champion target = (Champion) damageable;
			Effect cca = ((CrowdControlAbility) a).getEffect();
			if (checkTeam(currChamp, target))
				if (cca.getType() == EffectType.DEBUFF)
					throw new InvalidTargetException();
				else
					targets.add(target);
			else if (cca.getType() == EffectType.BUFF)
				throw new InvalidTargetException();
			else
				targets.add(target);
		}

	}

	// add Kill the enemy champion when Villain
	public void useLeaderAbility() throws LeaderNotCurrentException, LeaderAbilityAlreadyUsedException {
		Champion currChamp = getCurrentChampion();
		Player player = whichPlayer(currChamp);
		ArrayList<Champion> targets = null;

		if (!player.getLeader().equals(currChamp))
			throw new LeaderNotCurrentException();
		if (player.equals(firstPlayer) && firstLeaderAbilityUsed)
			throw new LeaderAbilityAlreadyUsedException();
		if (player.equals(secondPlayer) && secondLeaderAbilityUsed)
			throw new LeaderAbilityAlreadyUsedException();

		if (currChamp instanceof Hero)
			targets = player.getTeam();

		if (currChamp instanceof Villain)
			if (player.equals(secondPlayer))
				targets = firstPlayer.getTeam();
			else
				targets = secondPlayer.getTeam();

		if (currChamp instanceof AntiHero) {
			for (Champion champ : firstPlayer.getTeam())
				if (!firstPlayer.getLeader().equals(champ))
					targets.add(champ);
			for (Champion champ : secondPlayer.getTeam())
				if (!secondPlayer.getLeader().equals(champ))
					targets.add(champ);
		}

		currChamp.useLeaderAbility(targets);

		if (player.equals(firstPlayer))
			firstLeaderAbilityUsed = true;
		else
			secondLeaderAbilityUsed = true;

	}

	public Player getFirstPlayer() {

		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public Object[][] getBoard() {
		return board;
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	private void placeCovers() {
		for (int i = 0; i < 5;) {
			int x = (int) Math.floor(Math.random() * 3) + 1;
			int y = (int) Math.floor(Math.random() * 5);
			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}
	}

	private void placeChampions() {
		if (firstPlayer.getTeam().size() == 3)
			for (int i = 0; i < 3; i++) {
				Point point = new Point(0, i + 1);
				board[0][i + 1] = firstPlayer.getTeam().get(i);
				firstPlayer.getTeam().get(i).setLocation(point);
			}
		if (secondPlayer.getTeam().size() == 3)
			for (int i = 0; i < 3; i++) {
				Point point = new Point(4, i + 1);
				board[4][i + 1] = secondPlayer.getTeam().get(i);
				secondPlayer.getTeam().get(i).setLocation(point);
			}
	}

	public static void loadAbilities(String filePath) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String[] ability = new String[45];
		availableAbilities = new ArrayList<Ability>();
		while (br.ready()) {
			ability = br.readLine().split(",");
			AreaOfEffect castarea = AreaOfEffect.valueOf(ability[5]);

			switch (ability[0]) {
			case "DMG":
				availableAbilities.add(new DamagingAbility(ability[1], Integer.parseInt(ability[2]),
						Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
						Integer.parseInt(ability[6]), Integer.parseInt(ability[7])));
				break;

			case "HEL":
				availableAbilities.add(new HealingAbility(ability[1], Integer.parseInt(ability[2]),
						Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
						Integer.parseInt(ability[6]), Integer.parseInt(ability[7])));
				break;

			case "CC":
				switch (ability[7]) {
				case "Disarm":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Disarm(Integer.parseInt(ability[8]))));
					break;
				case "Dodge":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Dodge(Integer.parseInt(ability[8]))));
					break;
				case "Embrace":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Embrace(Integer.parseInt(ability[8]))));
					break;
				case "PowerUp":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new PowerUp(Integer.parseInt(ability[8]))));
					break;
				case "Root":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Root(Integer.parseInt(ability[8]))));
					break;
				case "Shield":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Shield(Integer.parseInt(ability[8]))));
					break;
				case "Shock":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Shock(Integer.parseInt(ability[8]))));
					break;
				case "Silence":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Silence(Integer.parseInt(ability[8]))));
					break;
				case "SpeedUp":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new SpeedUp(Integer.parseInt(ability[8]))));
					break;
				case "Stun":
					availableAbilities.add(new CrowdControlAbility(ability[1], Integer.parseInt(ability[2]),
							Integer.parseInt(ability[4]), Integer.parseInt(ability[3]), castarea,
							Integer.parseInt(ability[6]), new Stun(Integer.parseInt(ability[8]))));
					break;
				}
				break;
			}
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String[] str = new String[15];
		availableChampions = new ArrayList<Champion>();
		while (br.ready()) {
			str = br.readLine().split(",");
			Champion champ = null;
			switch (str[0]) {
			case "H":
				champ = new Hero(str[1], Integer.parseInt(str[2]), Integer.parseInt(str[3]),
						Integer.parseInt(str[4]), Integer.parseInt(str[5]), Integer.parseInt(str[6]),
						Integer.parseInt(str[7]));
				
				break;

			case "A":
				champ = new AntiHero(str[1], Integer.parseInt(str[2]), Integer.parseInt(str[3]),
						Integer.parseInt(str[4]), Integer.parseInt(str[5]), Integer.parseInt(str[6]),
						Integer.parseInt(str[7]));
				
				break;

			case "V":
				champ = new Villain(str[1], Integer.parseInt(str[2]), Integer.parseInt(str[3]),
						Integer.parseInt(str[4]), Integer.parseInt(str[5]), Integer.parseInt(str[6]),
						Integer.parseInt(str[7]));
				
				break;
			}
			
			for (int i = 8; i < 11; i++) 
				for (int j = 0; j < availableAbilities.size(); j++) 
					if (str[i].equals(availableAbilities.get(j).getName())) {
						champ.getAbilities().add(availableAbilities.get(j));
						break;
					}
				
			availableChampions.add(champ);
		}
		br.close();
	}

}
