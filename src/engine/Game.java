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
		availableChampions = new ArrayList<Champion>() ;
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
		for(Champion champion :firstPlayer.getTeam())
			if( champion.getCondition() != Condition.KNOCKEDOUT )
				player2Won = false;		//Player1 won or the game is on
		for(Champion champion : secondPlayer.getTeam())
			if( champion.getCondition() != Condition.KNOCKEDOUT )
				player1Won = false;		//Player2 won or the game is on
		
		if(player1Won && !player2Won)
			return firstPlayer;
		else
			if(!player1Won && player2Won)
				return secondPlayer;
		return null;
	}
	
	public void move(Direction d) throws UnallowedMovementException,NotEnoughResourcesException {
		
		Champion currChamp=getCurrentChampion();
		
		if(currChamp.getCondition()==Condition.ROOTED)
			throw new UnallowedMovementException();
		
		if(currChamp.getCurrentActionPoints()<1)
			throw new NotEnoughResourcesException();
		
		Point point = currChamp.getLocation();
		int x = point.x;
		int y = point.y;
		switch (d) {
		
		case UP:
			if(point.x==4)
				throw new UnallowedMovementException();
			else 
				if(board[x+1][y]!=null)
					throw new UnallowedMovementException();
				
				else {
					board[x][y]=null;
					point.x+=1;
					board[x+1][y]=currChamp;
		          currChamp.setLocation(point);
				}
			break;
			
		case DOWN:
			if(point.x==0)
				throw new UnallowedMovementException();
			else 
				if(board[x-1][y]!=null)
					throw new UnallowedMovementException();
				
				else {
					board[x][y]=null;
					point.x-=1;
					board[x-1][y]=currChamp;
		          currChamp.setLocation(point);
				}
			break;
			
		case RIGHT:
			if(point.y==4)
				throw new UnallowedMovementException();
			else 
				if(board[x][y+1]!=null)
					throw new UnallowedMovementException();
				
				else {
					board[x][y]=null;
					board[x][y+1]=currChamp;
					point.y+=1;
		          currChamp.setLocation(point);
				}
			break;
			
			case LEFT:
				if(point.y==0)
					throw new UnallowedMovementException();
				else 
					if(board[x][y-1]!=null)
						throw new UnallowedMovementException();
					
					else {
						board[x][y]=null;
						point.y-=1;
						board[x][y-1]=currChamp;
			          currChamp.setLocation(point);
					}
				break;
				
			default:
				 return;
		}
		
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints()-1);
	}
	
	public void attack(Direction d) throws ChampionDisarmedException,NotEnoughResourcesException {
		
		 Champion currChamp= getCurrentChampion();
		 
		 if(currChamp.getCurrentActionPoints()<2)
			 throw new NotEnoughResourcesException();
		 for(Effect effect:currChamp.getAppliedEffects()) 
			 if(effect instanceof Disarm)
					 throw new ChampionDisarmedException ();
		 
		 int range = currChamp.getAttackRange();
		 int x= currChamp.getLocation().x;
		 int y= currChamp.getLocation().y;
		 switch(d) {
		 
			 case UP:
				 int range1= (x+range)>4 ? 4 : x+range ;
				 
				  for(int x1 =x+1; x1<=range1;x1++) {
					 if (board[x1][y]!=null) 
						 if(board[x1][y] instanceof Champion) {
							 Champion c=(Champion)board[x1][y];
							 if(checkTeam(currChamp, c))
								 continue;
							 validate(x1, y);
							 break;
						 }
						 else {
							 validate(x1,y);
							 break;
						 }
				 }
					  break;
				 
				 case DOWN:
					 int range2= (x-range)<0 ? 0 : x-range;
					 
					 for(int x1 =x-1; x1>=range2;x1--) {
						 if (board[x1][y]!=null) 
							 if(board[x1][y] instanceof Champion) {
								 Champion c=(Champion)board[x1][y];
								 if(checkTeam(currChamp, c))
									 continue;
								 validate(x1, y);
								 break;
							 }
							 else {
								 validate(x1,y);
								 break;
							 }
					 }
						  break;
						  
				 case RIGHT:
					 int range3= (y+range)>4 ? 4 : y+range;
					 
					 for(int y1 =y+1; y1<=range3;y1++) {
						 if (board[x][y1]!=null) 
							 if(board[x][y1] instanceof Champion) {
								 Champion c=(Champion)board[x][y1];
								 if(checkTeam(currChamp, c))
									 continue;
								 validate(x, y1);
								 break;
							 }
							 else {
								 validate(x,y1);
								 break;
							 }
					 }
						  break;
					 
				 case LEFT:
					 int range4= (y-range)<0 ? 0 : y-range;
					 
					 for(int y1 =y-1; y1>=range4;y1--) {
						 if (board[x][y1]!=null) 
							 if(board[x][y1] instanceof Champion) {
								 Champion c=(Champion)board[x][y1];
								 if(checkTeam(currChamp, c))
									 continue;
								 validate(x, y1);
								 break;
							 }
							 else {
								 validate(x,y1);
								 break;
							 }
					 }
						  break;
						  
				 default:
					 return;
		 }
		 
		 currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints()-2);
	}
	
	public Player whichTeam(Champion champion) {
		 if(firstPlayer.getTeam().contains(champion)) 
			 return firstPlayer;
		 else
			 return secondPlayer;
	}
	
	public boolean checkTeam(Champion champion, Champion target) {
		ArrayList<Champion> mine = new ArrayList<Champion>();
		 if(firstPlayer.getTeam().contains(champion)) 
			 mine = firstPlayer.getTeam();
		 else
			 mine = secondPlayer.getTeam();
		 
		 return ((mine.contains(target)));
	}
	
	public void validate(int x,int y) {
		
		Champion champion=getCurrentChampion();
		
		if(board[x][y] instanceof Cover) {
			Cover target= (Cover)board[x][y];
			target.setCurrentHP((int)(target.getCurrentHP()-champion.getAttackDamage()));
			
			if(target.getCurrentHP()==0)
				board[x][y] =null;
		}
		
		if(board[x][y] instanceof Champion) {
			Champion target = (Champion) board[x][y] ;
			
			for (Effect e : target.getAppliedEffects()) {
				if(e instanceof Dodge && Math.round(Math.random())==1 )
					return;
				if(e instanceof Shield) {
					e.remove(target);
					return;
				}
			}
			int extra=champion.getAttackDamage()/2;
			
			if (champion instanceof Hero) {
				if(target instanceof Villain || target instanceof AntiHero)
					
					target.setCurrentHP(target.getCurrentHP() -( champion.getAttackDamage()+extra));
				else
					target.setCurrentHP(target.getCurrentHP() - champion.getAttackDamage());
			}
			if (champion instanceof Villain) {
				if(target instanceof Hero || target instanceof AntiHero )
					target.setCurrentHP(target.getCurrentHP() - (champion.getAttackDamage()+extra));
				else
					target.setCurrentHP(target.getCurrentHP() - champion.getAttackDamage());
			}
			if (champion instanceof AntiHero) {
				if(target instanceof Hero || target instanceof Villain)
					target.setCurrentHP(target.getCurrentHP() - (champion.getAttackDamage()+extra));
				else
					target.setCurrentHP(target.getCurrentHP() - champion.getAttackDamage());
			}
				
			if(target.getCurrentHP()==0) 
				kill(target);
			
		}
			
	}
	
		public void kill(Damageable target) {
			if (target instanceof Champion) {
				board[target.getLocation().x][target.getLocation().y] =null;
				
				((Champion) target).setCondition(Condition.KNOCKEDOUT);
				
				if(firstPlayer.getTeam().contains((Champion) target))
					firstPlayer.getTeam().remove((Champion) target);
				else
					secondPlayer.getTeam().remove((Champion) target);
				((Champion) target).setLocation(null);
			
				PriorityQueue pq = new PriorityQueue(6);
				while(!turnOrder.isEmpty()) {
					Champion champ = (Champion) turnOrder.remove();
					if(champ!= ((Champion) target) )
						pq.insert(champ);
				}
				turnOrder = pq;
			}
			if (target instanceof Cover) {
				board[target.getLocation().x][target.getLocation().y] =null;
			}
		}
	

		public void endTurn() {

			turnOrder.remove();
			if(turnOrder.isEmpty())
				prepareChampionTurns();
			
			Champion champion = getCurrentChampion();
			while(champion.getCondition() == Condition.INACTIVE ) {
				updates(champion);
				turnOrder.remove();
				champion = getCurrentChampion();
				
			  		}
			
			updates(champion);
			
		}
		public void updates(Champion champion) {
			ArrayList<Effect> rEffects=new ArrayList<>();
			for(Effect effect: champion.getAppliedEffects()) {
				if(effect.getDuration()<=1) 
					rEffects.add(effect);
				else
					effect.setDuration( effect.getDuration()-1 );
			}
			for(Effect effect:rEffects) {
				champion.getAppliedEffects().remove(effect);
				effect.remove(champion);
			}
			for(Ability a: champion.getAbilities()) 
				a.setCurrentCooldown(a.getCurrentCooldown()-1);
			
			champion.setCurrentActionPoints(champion.getMaxActionPointsPerTurn());
		}
		
	private void prepareChampionTurns() {
		
		for(Champion c: firstPlayer.getTeam()) 
			if( c.getCondition() != Condition.KNOCKEDOUT )
				turnOrder.insert(c);
		for(Champion c: secondPlayer.getTeam()) 
			if( c.getCondition() != Condition.KNOCKEDOUT )
				turnOrder.insert( c );
		
	}
	
	public void castAbility(Ability a) throws AbilityUseException,InvalidTargetException
									,NotEnoughResourcesException, CloneNotSupportedException{
		
		Champion currChamp =getCurrentChampion();
		
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException();
		
		for(Effect eff: currChamp.getAppliedEffects())
			if(eff instanceof Silence)
				throw new AbilityUseException();
		
		if(currChamp.getMana()<a.getManaCost()|| currChamp.getCurrentActionPoints()<a.getRequiredActionPoints() )
			throw new NotEnoughResourcesException();
		
		 int range = a.getCastRange();
		 int x= currChamp.getLocation().x;
		 int y= currChamp.getLocation().y;
		 
		 ArrayList<Damageable> targets=new ArrayList<>();
		switch(a.getCastArea()) {
		
		case SELFTARGET:
			targets.add(currChamp);
			 a.execute(targets);
				for(Damageable target:targets) {
					if(target.getCurrentHP()==0)
						kill(target);
				}
			break;
			
		case TEAMTARGET:
			for(Champion champ: firstPlayer.getTeam()) 
				if(Math.abs(y-champ.getLocation().y)+Math.abs(x-champ.getLocation().x) <= a.getCastRange())
					try {
						validateAndAdd(a, champ, targets);
					} catch (InvalidTargetException e) { }
			for(Champion champ: secondPlayer.getTeam()) 
				if(Math.abs(y-champ.getLocation().y)+Math.abs(x-champ.getLocation().x) <= a.getCastRange())
					try {
						validateAndAdd(a, champ, targets);
					} catch (InvalidTargetException e) { }
			
			 a.execute(targets);
				for(Damageable target:targets) {
					if(target.getCurrentHP()==0)
						kill(target);
				}
			break;
			
		case SURROUND:
			int xLowerLimit = (x<=0) ? 0 : x-1;
			int xUpperLimit = (x>=4)? 4 : x+1;
			int yLowerLimit= (y<=0)? 0 : y-1;
			int yUpperLimit = (y>=4)? 4 : y+1 ;
			
			for(int x1=xLowerLimit; x1<=xUpperLimit ;x1++)
				for(int y1=yLowerLimit; y1<=yUpperLimit ;y1++)
					if( (x==x1 && y==y1) || board[x1][y1]==null )
						continue;
					else
						try {
							validateAndAdd(a, (Damageable) board[x1][y1] , targets);
						} catch (InvalidTargetException e) { }
			
			 a.execute(targets);
				for(Damageable target:targets) {
					if(target.getCurrentHP()==0)
						kill(target);
				}
			break;
			
		}
		
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints()-a.getRequiredActionPoints());
		currChamp.setMana(currChamp.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	public void castAbility(Ability a, Direction d) throws AbilityUseException,InvalidTargetException
									,NotEnoughResourcesException, CloneNotSupportedException {
		
		Champion currChamp=getCurrentChampion();
	
		for(Effect eff: currChamp.getAppliedEffects())
			if(eff instanceof Silence)
				throw new AbilityUseException();
		
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException();
		
		if(currChamp.getMana()<a.getManaCost()|| currChamp.getCurrentActionPoints()<a.getRequiredActionPoints() )
			throw new NotEnoughResourcesException();
		
		 int range = a.getCastRange();
		 int x= currChamp.getLocation().x;
		 int y= currChamp.getLocation().y;
		 ArrayList<Damageable> targets=new ArrayList<>();
		 
		switch(d) {
		
		 case UP:
			 int range1= (x + range)>4? 4 : x+range ;
			
			 for(int x1 =x+1; x1<=range1;x1++) {
				 if (board[x1][y]!=null) {
					 try {
						 validateAndAdd(a, (Damageable) board[x1][y], targets);
					 }catch(InvalidTargetException e){}
				 }
			 }
			 
			 break;
			 
		 case DOWN:
			 int range2= (x - range)<0? 0 : x-range ;
			 
			 for(int x1 =x-1; x1>=range2;x1--) {
				 if (board[x1][y]!=null) 
					 try {
						 validateAndAdd(a, (Damageable) board[x1][y], targets);
					 }catch(InvalidTargetException e){ }
			 }
			 
			 break;
			 
		 case RIGHT:
			 int range3= (y+ range)>4? 4 : y+range;
			 
			 for(int y1 =y+1; y1<=range3;y1++) {
				 if (board[x][y1]!=null) 
					 try {
						 validateAndAdd(a, (Damageable) board[x][y1], targets);
					 }catch(InvalidTargetException e){}
			 }
			 
			 break;
			 
		 case LEFT:
			 int range4= (y- range)<0? 0 : y-range;
			 
			 for(int y1 =y-1; y1>=range4;y1--) {
				 if (board[x][y1]!=null) 
					 try {
						 validateAndAdd(a, (Damageable) board[x][y1], targets);
					 }catch(InvalidTargetException e){}
			 }
			 
			 break;
			 
		 default:
			 return;
			 
	}
		 a.execute(targets);
			for(Damageable target:targets) {
				if(target.getCurrentHP()==0)
					kill(target);
			}
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints()-a.getRequiredActionPoints());
		currChamp.setMana(currChamp.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
	}
	
	public void castAbility(Ability a, int x, int y) throws AbilityUseException,InvalidTargetException
									,NotEnoughResourcesException, CloneNotSupportedException {
		
		Champion currChamp=getCurrentChampion();
		
		if(a.getCurrentCooldown()!=0)
			throw new AbilityUseException();
		
		for(Effect eff: currChamp.getAppliedEffects())
			if(eff instanceof Silence)
				throw new AbilityUseException();
		
		if(currChamp.getMana()<a.getManaCost()|| currChamp.getCurrentActionPoints()<a.getRequiredActionPoints() )
			throw new NotEnoughResourcesException();
		
		if(board[x][y]==null)
			throw new InvalidTargetException();
		
		if(Math.abs(y-currChamp.getLocation().y)+Math.abs(x-currChamp.getLocation().x)>a.getCastRange())
			throw new AbilityUseException();
		
		ArrayList<Damageable> targets=new ArrayList<>();
		Damageable damageable = (Damageable) board[x][y];
		
		validateAndAdd(a, damageable, targets);
		
		a.execute(targets);
		for(Damageable target:targets) {
			if(target.getCurrentHP()==0)
				kill(target);
		}
			
		currChamp.setCurrentActionPoints(currChamp.getCurrentActionPoints()-a.getRequiredActionPoints());
		currChamp.setMana(currChamp.getMana()-a.getManaCost());
		a.setCurrentCooldown(a.getBaseCooldown());
		
	}
	
	public void validateAndAdd(Ability a,Damageable damageable,ArrayList<Damageable> targets) throws InvalidTargetException {
		
		Champion currChamp=getCurrentChampion();
		
		if(a instanceof DamagingAbility ) {
			
			if(damageable instanceof Champion) {
				Champion target = (Champion) damageable;
				
				if( checkTeam(currChamp, target) )
					throw new InvalidTargetException();
				else {
					for(Effect eff: target.getAppliedEffects())
						if(eff instanceof Shield) {
							eff.remove(target);
							return;
						}
						targets.add(target);
				}
			}
			else
				targets.add((Cover) damageable);
			
		}
		
		if(a instanceof HealingAbility) {
			
			if (damageable instanceof Cover)
				throw new InvalidTargetException();
			
			Champion target = (Champion) damageable;
			if( checkTeam(currChamp, target )) 
				targets.add(target);
			else
				throw new InvalidTargetException();	
			
		}
		
		if(a instanceof CrowdControlAbility) {

			if (damageable instanceof Cover)
				throw new InvalidTargetException();
			
			Champion target = (Champion) damageable;
			Effect cca =  ((CrowdControlAbility) a).getEffect();
			if(checkTeam(currChamp, target))
				if(cca.getType()==EffectType.DEBUFF)
					throw new InvalidTargetException();
				else 
					targets.add(target);
				
			if(!(checkTeam(currChamp, target)))
				if(cca.getType()==EffectType.BUFF)
					throw new InvalidTargetException();
				else 
					targets.add(target);
					
		}

	}
	//add Kill the enemy champion when Villain 
	public void useLeaderAbility()throws LeaderNotCurrentException,LeaderAbilityAlreadyUsedException{
		Champion currChamp=getCurrentChampion();
		ArrayList<Champion> targts=new ArrayList<>();
		if(currChamp instanceof Hero) {
		if(firstPlayer.getTeam().contains(currChamp)) {
			if(!firstPlayer.getLeader().equals(currChamp))
				throw new LeaderNotCurrentException();
			if(firstLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			currChamp.useLeaderAbility(firstPlayer.getTeam());
			firstLeaderAbilityUsed=true;
			
		}
		else
			if(secondPlayer.getTeam().contains(currChamp)) {
				if(!secondPlayer.getLeader().equals(currChamp))
					throw new LeaderNotCurrentException();
				if(secondLeaderAbilityUsed)
					throw new LeaderAbilityAlreadyUsedException();
				
				currChamp.useLeaderAbility(secondPlayer.getTeam());
				secondLeaderAbilityUsed=true;
		}
		
		
	}
		if(currChamp instanceof Villain) {
			if(firstPlayer.getTeam().contains(currChamp)) {
				if(!firstPlayer.getLeader().equals(currChamp))
					throw new LeaderNotCurrentException();
				if(firstLeaderAbilityUsed)
					throw new LeaderAbilityAlreadyUsedException();
				currChamp.useLeaderAbility(secondPlayer.getTeam());
				firstLeaderAbilityUsed=true;
			}
			else
				if(secondPlayer.getTeam().contains(currChamp)) {
					if(!secondPlayer.getLeader().equals(currChamp))
						throw new LeaderNotCurrentException();
					if(secondLeaderAbilityUsed)
						throw new LeaderAbilityAlreadyUsedException();
					
					currChamp.useLeaderAbility(firstPlayer.getTeam());
					secondLeaderAbilityUsed=true;
			}
			
			
		}
		if(currChamp instanceof AntiHero) {
			if(firstPlayer.getTeam().contains(currChamp)) {
				if(!firstPlayer.getLeader().equals(currChamp))
					throw new LeaderNotCurrentException();
				if(firstLeaderAbilityUsed)
					throw new LeaderAbilityAlreadyUsedException();
				firstLeaderAbilityUsed=true;
			}
			else
				if(secondPlayer.getTeam().contains(currChamp)) {
					if(!secondPlayer.getLeader().equals(currChamp))
						throw new LeaderNotCurrentException();
					if(secondLeaderAbilityUsed)
						throw new LeaderAbilityAlreadyUsedException();
					secondLeaderAbilityUsed=true;
					
				
			}
		
			for(Champion currChamp2:firstPlayer.getTeam() ) {
				if(!firstPlayer.getLeader().equals(currChamp2))
					targts.add(currChamp2);
				
				
			}
		
			
			for(Champion currChamp2:secondPlayer.getTeam() ) {
				if(!secondPlayer.getLeader().equals(currChamp2))
					targts.add(currChamp2);
				
			}
			
			currChamp.useLeaderAbility(targts);
			
			
		}
		
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
		Point[] arr=new Point[5];
		int i=0;
		while( i < 5) {
			Point point=new Point((int) Math.floor(Math.random()*(3)+1),(int) Math.floor(Math.random()*(5)));
			boolean flag=false;
			for (int j = 0; j < i; j++) {
				if (arr[j].x==point.getX() && arr[j].y==point.getY()) {
					flag=true;
					break;}
										}
			if(!flag) {
				arr[i]=point;
				i++;		
					  }
					 } 
		for (int j = 0; j < arr.length; j++) {
			board[(int)arr[j].getX()][(int)arr[j].getY()]=new Cover((int)arr[j].getX(), (int)arr[j].getY()); 
											 }
											
	}
	
	private void placeChampions() {
			if(firstPlayer.getTeam().size()==3)
			for(int i=0;i<3;i++) {
				Point point=new Point(0,i+1);
				board[0][i+1]=firstPlayer.getTeam().get(i);
				firstPlayer.getTeam().get(i).setLocation(point);
			
			}
			if(secondPlayer.getTeam().size()==3)
			for(int i=0;i<3;i++){
				Point point=new Point(4,i+1);
				board[4][i+1]=secondPlayer.getTeam().get(i);
				secondPlayer.getTeam().get(i).setLocation(point);
			}		
		}
	

	public static void loadAbilities(String filePath) throws Exception {
		 BufferedReader br= new BufferedReader(new FileReader(filePath));
		 String[] ability = new String[45];
		 availableAbilities=new ArrayList<Ability>();
		 while (br.ready()) {
			 ability = br.readLine().split(",");
			 AreaOfEffect castarea=AreaOfEffect.valueOf(ability[5]);
			 
		
			 switch(ability[0]) {
			 case "DMG":
				 availableAbilities.add(new DamagingAbility(ability[1],Integer.parseInt(ability[2]),
						 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
						 Integer.parseInt(ability[6]),Integer.parseInt(ability[7])));
				 break;
			
			 case "HEL":
				 availableAbilities.add(new HealingAbility(ability[1],Integer.parseInt(ability[2]),
						 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
						 Integer.parseInt(ability[6]),Integer.parseInt(ability[7])));
				 break;
			
				 
			 case "CC":
				 switch(ability[7]) {
		           	case "Disarm":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Disarm(Integer.parseInt(ability[8]))));
						 break;
		           	case "Dodge":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Dodge(Integer.parseInt(ability[8]))));
						 break;
		           	case "Embrace":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Embrace(Integer.parseInt(ability[8]))));
						 break;
		           	case "PowerUp":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new PowerUp(Integer.parseInt(ability[8]))));
						 break;
		           	case "Root":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Root(Integer.parseInt(ability[8]))));
						 break;
		           	case "Shield":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Shield(Integer.parseInt(ability[8]))));
						 break;
		           	case "Shock": 
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Shock(Integer.parseInt(ability[8]))));
						 break;
		           	case "Silence":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Silence(Integer.parseInt(ability[8]))));
						 break;
		           	case "SpeedUp":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new SpeedUp(Integer.parseInt(ability[8]))));
						 break;
		           	case "Stun":
						 availableAbilities.add(new CrowdControlAbility(ability[1],Integer.parseInt(ability[2]),
								 Integer.parseInt(ability[4]),Integer.parseInt(ability[3]),castarea,
								 Integer.parseInt(ability[6]),new Stun(Integer.parseInt(ability[8]))));
						 break;
		           	}
				 break;
			 }
		 }
		 br.close();
				}
	
	public static void loadChampions(String filePath) throws Exception {
		BufferedReader br= new BufferedReader(new FileReader(filePath));
		String[] str=new String[15];
		availableChampions=new ArrayList<Champion>();
		while(br.ready()) {
			str = br.readLine().split(",");
			
			switch(str[0]) {
			case "H":
				Hero hero = new Hero(str[1],Integer.parseInt(str[2])
						,Integer.parseInt(str[3]),Integer.parseInt(str[4])
						,Integer.parseInt(str[5]),Integer.parseInt(str[6])
						,Integer.parseInt(str[7]));
				for(int i=8; i<11; i++) {
					for (int j = 0; j<availableAbilities.size() ; j++) {
						if(str[i].equals(availableAbilities.get(j).getName())) {
							hero.getAbilities().add(availableAbilities.get(j));
							break;
						}
					}	 
				}
				availableChampions.add(hero);
				break;
				
			case "A":
				AntiHero antiHero = new AntiHero(str[1],Integer.parseInt(str[2])
						,Integer.parseInt(str[3]),Integer.parseInt(str[4])
						,Integer.parseInt(str[5]),Integer.parseInt(str[6])
						,Integer.parseInt(str[7]));
				for(int i=8; i<11; i++) {
					for (int j = 0; j<availableAbilities.size() ; j++) {
						if(str[i].equals(availableAbilities.get(j).getName())) {
							antiHero.getAbilities().add(availableAbilities.get(j));
							break;
						}
					}	 
				}
				availableChampions.add(antiHero);
				break;
				
			case "V":
				Villain villain = new Villain(str[1],Integer.parseInt(str[2])
						,Integer.parseInt(str[3]),Integer.parseInt(str[4])
						,Integer.parseInt(str[5]),Integer.parseInt(str[6])
						,Integer.parseInt(str[7]));
				for(int i=8; i<11; i++) {
					for (int j = 0; j<availableAbilities.size() ; j++) {
						if(str[i].equals(availableAbilities.get(j).getName())) {
							villain.getAbilities().add(availableAbilities.get(j));
							break;
						}
					}	 
				}
				availableChampions.add(villain);
				break;
			}
		}
		br.close();
	}

	
	}
	
	
	
