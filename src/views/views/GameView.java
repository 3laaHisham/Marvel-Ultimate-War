package views.views;

import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import java.util.Arrays;
import java.util.List;

import javax.print.DocFlavor.STRING;

import engine.Game;
import engine.Player;
import engine.PriorityQueue;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.world.Champion;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import views.buttons.DamageableButton;
import views.buttons.UnitButton;
import views.helpers.MyHeadText;
import views.helpers.MyText;
import views.helpers.PlaySound;
import views.panels.AlertBox;
import views.panels.MyBackGround;
import exceptions.*;

public class GameView extends Application {
	private BorderPane root;
	private Scene scene;
	private Object[][] board;
	private GridPane grid;
	private Game game;
	private Player p1;
	private Player p2;
	private VBox currDetail;
	private VBox OverDetail;
	private GridPane playersInfo;
	private FlowPane turnOrder;
	private FlowPane buttons;
	private MyText LeaderUsed1;
	private MyText LeaderUsed2;
	private String team;
	private MyText message;
	private Button attack;
	private Button endTurn;
	private Button useLeaderAbility;
	private UnitButton castAbility;
	private ArrayList<ImageView> icons;

	// Use Toggle Button for refactor
	@Override
	public void start(Stage primaryStage) {
		root = new BorderPane();
		scene = new Scene(root);

		String path2 = "./assests/windows/GameView2.jpg";
		root.setBackground(MyBackGround.get(path2, path2));
		
		
		grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		grid.setPadding(new Insets(1, 1, 1, 1));
		// grid.setPrefSize(500, 300);
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: rgba(218,159,28,0.2);"
				+ "-fx-border-width: 1; -fx-border-color: rgba(236, 236, 236,0.6);");
		grid.setFocusTraversable(false);
		try {
			game = new Game(p1, p2);
		} catch (Exception e) {
		}
		
		// Current Champion details (Left Panel)
		currDetail = new VBox();
		currDetail.setPadding(new Insets(20, 5, 0, 5));
		currDetail.setSpacing(10);
		Champion c = game.getCurrentChampion();
		Boolean leader = game.whichTeam(c).getLeader().equals(c) ? true : false;
		team = game.whichTeam(c).equals(game.getFirstPlayer()) ? "Red Team" : "Blue Team";
		currDetail.getChildren().addAll(new MyText("Current HP", 14), c.getPbHealth(),
				new MyText(team + " Leader? " + leader + "\n" + "\n" + c.toString2(), 15));
		
		// Mouse-over champion details (Right Panel)
		OverDetail = new VBox();
		OverDetail.setPadding(new Insets(50, 10, 0, 5));
		OverDetail.setSpacing(10);
		OverDetail.getChildren().add(new MyText("Over", 30));

		board = game.getBoard();
		fillBoard();

		LeaderUsed1 = new MyText("Leader Ability used? " + Boolean.toString(game.isFirstLeaderAbilityUsed()), 15);
		LeaderUsed2 = new MyText("Leader Ability used? " + Boolean.toString(game.isSecondLeaderAbilityUsed()), 15);
		
		// Turn Order in the Top panel
		turnOrder = new FlowPane();
		turnOrder.setHgap(5);
		turnOrder.setVgap(5);
		turnOrder.setPadding(new Insets(1, 1, 1, 1));
		turnOrder.setMaxSize(700, 80);
		turnOrder.setMinSize(500, 70);
		turnOrder.setAlignment(Pos.CENTER);
		turnOrder.setStyle("-fx-background-color: rgba(218,159,28,0.2);"
				+ "-fx-border-width: 1; -fx-border-color: rgba(236, 236, 236,0.6);");

		turnOrder.setFocusTraversable(false);
		PriorityQueue store = new PriorityQueue(6);
		icons = new ArrayList<>();
		addicons();
		while (!game.getTurnOrder().isEmpty()) {
			Champion champ = (Champion) game.getTurnOrder().remove();
			Player p = game.whichTeam(champ);
			DamageableButton btn = p.getChildren().stream().filter(child -> child.getText().equals(champ.getName()))
					.findAny().get();
			int i;
			for (i = 0; i < game.getAvailableChampions().size(); i++)
				if (game.getAvailableChampions().get(i).compareTo(champ) == 0)
					break;
			DamageableButton button = new DamageableButton(btn.getText(), icons.get(i), 75, 50);
			turnOrder.getChildren().add(button);
			store.insert(champ);
		}
		while (!store.isEmpty())
			game.getTurnOrder().insert(store.remove());
		
		// Players' details and Turn Order
		playersInfo = new GridPane();
		playersInfo.setStyle("-fx-background-color: rgba(210, 39, 39,0.8);"
				+ "-fx-border-width: 1; -fx-border-color:rgba(236, 236, 236,0.8)");
		playersInfo.setHgap(60);
		playersInfo.setVgap(5);
		playersInfo.setPadding(new Insets(5, 5, 5, 5));
		playersInfo.add(new MyHeadText(p1.getName(), 40), 1, 0);
		playersInfo.add(new MyHeadText(p2.getName(), 40), 12, 0);
		playersInfo.add(turnOrder, 5, 0, 5, 3);
		playersInfo.add(LeaderUsed1, 1, 2);
		playersInfo.add(LeaderUsed2, 12, 2);
		
		
		// Buttons (Bottom Panel)
		attack = new UnitButton("Attack");
		attack.setOnAction(attackHandler());
		attack.setFocusTraversable(false);
		endTurn = new UnitButton("End Turn", 130);
		endTurn.setOnAction(endTurnHandler());
		endTurn.setFocusTraversable(false);
		useLeaderAbility = new UnitButton("Use Leader Ability", 200);
		useLeaderAbility.setOnAction(useLeaderAbilityHandler());
		useLeaderAbility.setFocusTraversable(false);
		message = new MyText("", 20);
		message.setStyle("-fx-background-color: rgba(210, 39, 39,0.4);");
		buttons.setMargin(message, new Insets(0, 10, 0, 10));

		buttons = new FlowPane(10, 10);
		buttons.setPadding(new Insets(20, 20, 20, 30));
		buttons.setAlignment(Pos.CENTER);
		buttons.setStyle("-fx-background-color: rgba(41, 52, 98,0.8);");
		buttons.getChildren().addAll(attack, endTurn, useLeaderAbility, message);

		int k = 0;
		while (true) {
			k++;
			castAbility = new UnitButton(game.getCurrentChampion().getAbilities().get(k - 1).getName(), 170);
			castAbility.setFocusTraversable(false);
			castAbility.setOnAction(castAbilityHandler(castAbility, k));
			buttons.getChildren().add(castAbility);
			if (k >= 3)
				break;
		}

		scene.setOnKeyPressed(moveHandler());

		String backGroundStyle = "-fx-background-color: rgba(218,159,28,0.2);"
				+ "-fx-border-width: 1; -fx-border-color: rgba(236, 236, 236,0.6)";
		grid.setStyle(backGroundStyle);
		currDetail.setStyle(backGroundStyle);
		OverDetail.setStyle(backGroundStyle);
		root.setLeft(currDetail);
		root.setTop(playersInfo);
		root.setRight(OverDetail);
		root.setCenter(grid);
		root.setBottom(buttons);

		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");
		primaryStage.show();
		scene.getRoot().requestFocus();
		root.setFocusTraversable(true);
	}

	public void update() {
		//Update Grid
		grid.getChildren().clear();
		fillBoard();
		
		//Update Current Champion Info
		currDetail.getChildren().clear();
		Champion c = game.getCurrentChampion();
		Boolean leader = game.whichTeam(c).getLeader().equals(c) ? true : false;
		currDetail.getChildren().addAll(new MyText("Current HP", 14), c.getPbHealth(),
				new MyText(team + " Leader? " + leader + "\n" + "\n" + c.toString2(), 15));
		

		Player winner = game.checkGameOver();
		if (winner != null) {
			AlertBox.display("Game Over", "Congraulations, " + winner.getName() + " Won");
			System.exit(0);
		}
	}

	public EventHandler<KeyEvent> moveHandler() {
		message.setText("Please choose a direction to move");

		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Direction d = getDirection(event);
				try {
					game.move(d);
					PlaySound.play("./assests/sounds/onTheMove.wav");
				} catch (NotEnoughResourcesException ee) {
					AlertBox.display("Exception", "You do not have enough resources");
				} catch (UnallowedMovementException ee) {
					AlertBox.display("Exception", "You can not move this direction / Your Champion is rooted");
				} catch (Exception ee) {
					System.out.println("Use correct inputs");
				}

				update();
			}
		};
	}

	public EventHandler<ActionEvent> attackHandler() {
		return (e -> {
			attack.setStyle("-fx-border-color: blue; -fx-background-color: lightgray;" + "fx-border:20px;");
			message.setText("Please choose a direction to attack");
			scene.setOnKeyPressed((event) -> {

				Direction d = getDirection(event);
				PlaySound.play("./assests/sounds/Attacking.wav");
				try {
					game.attack(d);
				} catch (NotEnoughResourcesException ee) {
					AlertBox.display("Exception", "You do not have enough resources");
				} catch (ChampionDisarmedException ee) {
					AlertBox.display("Exception", "Your champion is Disarmed");
				} catch (Exception ee) {
					System.out.println("Use correct inputs");
				}
				event.consume();
				scene.setOnKeyPressed(moveHandler());

				attack.setStyle("-fx-background-color: #920138; -fx-text-fill: #ffffff");
				update();
			});
		});

	}

	public EventHandler<ActionEvent> endTurnHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PlaySound.play("./assests/sounds/EndTurn.wav");
				Champion c = game.getCurrentChampion();
				game.endTurn();
				update();

				// Update reference of abilities to new Current Champion
				int k = 0;
				for (Node cast : buttons.getChildren()) {
					if (cast instanceof MyText)
						continue;
					UnitButton castAbility = (UnitButton) cast;
					String name = c.getAbilities().get(k).getName();
					if (castAbility.getText().equals(name)) {
						castAbility.setText(game.getCurrentChampion().getAbilities().get(k).getName());
						castAbility.setOnAction(castAbilityHandler(castAbility, k + 1));
						k++;
					}
				}
				Node node = turnOrder.getChildren().remove(0);
				turnOrder.getChildren().add(node);
			}
		};
	}

	public EventHandler<ActionEvent> useLeaderAbilityHandler() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				PlaySound.play("./assests/sounds/LeaveNonAlive.wav");
				try {
					game.useLeaderAbility();
				} catch (LeaderNotCurrentException e) {
					AlertBox.display("Error", "Current champion is not the leader");
				} catch (LeaderAbilityAlreadyUsedException e) {
					AlertBox.display("Error", "Leader Ability Already Used");
				}

				//Update Leader Ability used or not
				LeaderUsed1.setText("Leader Ability used? " + Boolean.toString(game.isFirstLeaderAbilityUsed()));
				LeaderUsed2.setText("Leader Ability used? " + Boolean.toString(game.isSecondLeaderAbilityUsed()));

				update();
			}
		};
	}

	public EventHandler<ActionEvent> castAbilityHandler(UnitButton castAbility, int abilityNum) {
		return (e -> {

			Ability ability = game.getCurrentChampion().getAbilities().get(abilityNum - 1);
			String pressedStyle = "-fx-border-color: blue; -fx-background-color: lightgray;" + "fx-border:20px;";
			String defaultStyle = "-fx-background-color: #920138; -fx-text-fill: #ffffff";

			switch (ability.getCastArea()) {

			case SELFTARGET:
			case SURROUND:
			case TEAMTARGET:
				doCast(ability);
				update();
				break;

			case DIRECTIONAL:
				castAbility.setStyle(pressedStyle);
				message.setText("Please choose a direction to cast ability");

				scene.setOnKeyPressed((event) -> {
					message.setText("Choose Direction");
					Direction d = getDirection(event);
					doCast(ability, d);

					event.consume();
					scene.setOnKeyPressed(moveHandler());
					castAbility.setStyle(defaultStyle);
				});
				update();
				break;

			case SINGLETARGET:
				message.setText("Press the button you wish to cast on");
				castAbility.setStyle(pressedStyle);

				for (Node btn : grid.getChildren()) {
					DamageableButton button = (DamageableButton) btn;
					button.setOnAction(ee -> {
						int x = 4 - GridPane.getRowIndex(btn);
						int y = GridPane.getColumnIndex(btn);
						doCast(ability, x, y);

						castAbility.setStyle(defaultStyle);
						btn.setOnMouseEntered(mouseDrag((Champion) board[x][y]));
						button.setOnAction(null);
						update();
					});
				}
				break;
			}
		});
	}

	public void doCast(Ability ability) {
		try {
			game.castAbility(ability);
		} catch (AbilityUseException eee) {
			AlertBox.display("Error", "Out of Range / You are restricted from doing this action");
		} catch (InvalidTargetException eee) {
			AlertBox.display("Error", "You cannot cast on a friend or empty cell");
		} catch (NotEnoughResourcesException eee) {
			AlertBox.display("Error", "Not Enough Resources");
		} catch (CloneNotSupportedException eee) {
			AlertBox.display("Error", "CloneNotSupportedException");
		}
		PlaySound.play("./assests/sounds/castingAbility.wav");
	}

	public void doCast(Ability ability, Direction d) {
		try {
			game.castAbility(ability, d);
		} catch (AbilityUseException eee) {
			AlertBox.display("Error", "Out of Range / You are restricted from doing this action");
		} catch (InvalidTargetException eee) {
			AlertBox.display("Error", "You cannot cast on a friend or empty cell");
		} catch (NotEnoughResourcesException eee) {
			AlertBox.display("Error", "Not Enough Resources");
		} catch (CloneNotSupportedException eee) {
			AlertBox.display("Error", "CloneNotSupportedException");
		}
		PlaySound.play("./assests/sounds/castingAbility.wav");
	}

	public void doCast(Ability ability, int x, int y) {
		try {
			game.castAbility(ability, x, y);
		} catch (AbilityUseException eee) {
			AlertBox.display("Error", "Out of Range / You are restricted from doing this action");
		} catch (InvalidTargetException eee) {
			AlertBox.display("Error", "You cannot cast on a friend or empty cell");
		} catch (NotEnoughResourcesException eee) {
			AlertBox.display("Error", "Not Enough Resources");
		} catch (CloneNotSupportedException eee) {
			AlertBox.display("Error", "CloneNotSupportedException");
		}
		PlaySound.play("./assests/sounds/castingAbility.wav");
	}

	public Direction getDirection(KeyEvent event) {
		Direction direction = null;
		switch (event.getCode()) {
		
		case UP:
			direction = Direction.UP;
			break;
		case DOWN:
			direction = Direction.DOWN;
			break;
		case RIGHT:
			direction = Direction.RIGHT;
			break;
		case LEFT:
			direction = Direction.LEFT;
			break;

		}
		return direction;

	}

	public void fillBoard() {
		int ii = 4;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++)
				if (board[i][j] != null)
					if (board[i][j] instanceof Champion) {
						
						Champion c = (Champion) board[i][j];
						Player p = game.whichTeam(c);
						DamageableButton btn = p.getChildren().stream()
								.filter(child -> child.getText().equals(c.getName())).findAny().get();
						btn.setFocusTraversable(false);
						btn.setOnAction(null);
						c.getPbHealth().setProgress((double) c.getCurrentHP() / c.getMaxHP());
						btn.setOnMouseEntered(mouseDrag(c));
						
						// Color buttons depending on which team and currChamp
						if (game.getCurrentChampion().equals(c))
							btn.setStyle("-fx-background-color:rgba(50,205,50,0.9);\r\n"
									+ "    -fx-background-radius:0;\r\n" + "    -fx-border-color:black;\r\n"
									+ "    -fx-border-width: 0 3 3 0;\r\n" + "    -fx-background-insets: 0;");
						else if (p.equals(game.getFirstPlayer()))
							btn.setStyle("-fx-background-color:rgba(255,64,64,0.9);\r\n"
									+ "    -fx-background-radius:0;\r\n" + "    -fx-border-color:black;\r\n"
									+ "    -fx-border-width: 0 3 3 0;\r\n" + "    -fx-background-insets: 0;");
						else
							btn.setStyle("-fx-background-color:rgba(171,205,255,0.9);\r\n"
									+ "    -fx-background-radius:0;\r\n" + "    -fx-border-color:black;\r\n"
									+ "    -fx-border-width: 0 3 3 0;\r\n" + "    -fx-background-insets: 0;");

						grid.add(btn, j, ii);
						
					} else {
						DamageableButton btn = new DamageableButton("Cover",
								new ImageView(new Image("file:./assests/icons/Cover.png")));
						btn.setFocusTraversable(false);
						Cover cover = (Cover) board[i][j];
						
						btn.setOnMouseEntered(e -> {
							OverDetail.getChildren().clear();
							MyText coverHP = new MyText(" Cover's HP= " + Integer.toString(cover.getCurrentHP()), 29);
							coverHP.setTextAlignment(TextAlignment.CENTER);
							OverDetail.getChildren().add(coverHP);
							OverDetail.setMargin(coverHP, new Insets(100, 2, 0, 5));
						});
						
						grid.add(btn, j, ii);
					}
				else {
					DamageableButton btn = new DamageableButton("", null, 150, 100);
					grid.add(btn, j, ii);
					btn.setFocusTraversable(false);
				}
			ii--;
		}
	}

	public EventHandler<MouseEvent> mouseDrag(Champion c) {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				OverDetail.getChildren().clear();
				Boolean leader = game.whichTeam(c).getLeader().equals(c) ? true : false;
				OverDetail.getChildren().addAll(new MyText("Current HP", 16), c.getPbHealth(),
						new MyText("Leader? " + leader + "\n" + "\n" + "\n" + c.toString3(), 20));
			}
		};
	}

	public void addicons() {
		int w = 75;
		int h = 50;
		ImageView icon0 = new ImageView(new Image("file:" + "./assests/icons/Captain America.png", w, h, false, true, true));
		ImageView icon1 = new ImageView(new Image("file:" + "./assests/icons/Deadpool.png", w, h, false, true, true));
		ImageView icon2 = new ImageView(new Image("file:" + "./assests/icons/Dr Strange.png", w, h, false, true, true));
		ImageView icon3 = new ImageView(new Image("file:" + "./assests/icons/Electro.png", w, h, false, true, true));
		ImageView icon4 = new ImageView(new Image("file:" + "./assests/icons/Ghost Rider.png", w, h, false, true, true));
		ImageView icon5 = new ImageView(new Image("file:" + "./assests/icons/Hela.png", w, h, false, true, true));
		ImageView icon6 = new ImageView(new Image("file:" + "./assests/icons/Hulk.png", w, h, false, true, true));
		ImageView icon7 = new ImageView(new Image("file:" + "./assests/icons/Iceman.png", w, h, false, true, true));
		ImageView icon8 = new ImageView(new Image("file:" + "./assests/icons/Ironman.png", w, h, false, true, true));
		ImageView icon9 = new ImageView(new Image("file:" + "./assests/icons/Loki.png", w, h, false, true, true));
		ImageView icon10 = new ImageView(new Image("file:" + "./assests/icons/Quicksilver.png", w, h, false, true, true));
		ImageView icon11 = new ImageView(new Image("file:" + "./assests/icons/Spiderman.png", w, h, false, true, true));
		ImageView icon12 = new ImageView(new Image("file:" + "./assests/icons/Thor.png", w, h, false, true, true));
		ImageView icon13 = new ImageView(new Image("file:" + "./assests/icons/Venom.png", w, h, false, true, true));
		ImageView icon14 = new ImageView(new Image("file:" + "./assests/icons/Yellowjacket.png", w, h, false, true, true));
		icons.add(icon0);icons.add(icon1);icons.add(icon2);icons.add(icon3);icons.add(icon4);
		icons.add(icon5);icons.add(icon6);icons.add(icon7);icons.add(icon8);icons.add(icon9);
		icons.add(icon10);icons.add(icon11);icons.add(icon12);icons.add(icon13);icons.add(icon14);
	}



	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	public Scene getScene() {
		return scene;
	}

}
