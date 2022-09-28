package views.views;


import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JButton;
import engine.Game;
import engine.Player;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.* ;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import model.world.Champion;
import views.buttons.DamageableButton;
import views.buttons.UnitButton;
import views.helpers.MyHeadText;
import views.helpers.MyText;
import views.helpers.PlaySound;
import views.panels.AlertBox;
import views.panels.ChooseLeader;
import views.panels.MyBackGround;


public class ChoiceView extends Application{
	private BorderPane root;
	private Scene scene;
	private Stage primaryStage;
	private Game game;
	private HBox player;
	private VBox champView;
	private VBox info;
	private FlowPane team;
	private FlowPane flow;
	private Player firstP;
	private Player secondP;
	private Player p;
	private MyHeadText text;
	private String fPlayer;
	private String sPlayer;
	private ArrayList<ImageView> icons;
	private ArrayList<ImageView> fotos;
	private ArrayList<ImageView> fotos2;
	private ArrayList<Champion> champs;
	private ArrayList<Button> buttons;
	
	@Override
	public void start(Stage primaryStage){
		root = new BorderPane();
		scene = new Scene(root,1600,850);
        this.primaryStage = primaryStage;
        
        String path1 = "./assests/windows/SecondView1.jpg";
        String path2	= "./assests/windows/SecondView2.jpg";
		root.setBackground(MyBackGround.get(path1, path2));
		
		String style = "-fx-background-color: rgba(218,159,28,0.2);"
        		+ "-fx-border-width: 2; -fx-border-color: rgba(236, 236, 236,0.6)";
		
		// Champions to choose from
        flow = new FlowPane(5,5);
        flow.setPadding(new Insets(5, 4, 15, 10));
        flow.setAlignment(Pos.CENTER);
        flow.setStyle(style);
        
        // The team (Right Panel)
        team = new FlowPane(5,5);
        team.setMinWidth(600);
        team.setPadding(new Insets(50, 5, 15, 20));
        team.setAlignment(Pos.CENTER);
        team.setPrefSize(600, 550);
        team.setStyle(style);
        
        //Info panel on the right
        info = new VBox();
        info.setStyle(style);
        info.setPadding(new Insets(20, 15, 0, 15));
        info.setPrefSize(500, 550);
        
        icons = new ArrayList<>();
        addicons();
        fotos = new ArrayList<>();
        addfotos();
        fotos2 = new ArrayList<>();
        addfotos2();
        
        // A preview of the champion in middle panel
		champView = new VBox();
		champView.setPadding(new Insets(20, 30,0, 50));
		champView.setStyle(style);
		champView.setMinSize(500, 450);
		champView.setPrefSize(600, 750);
		buttons = new ArrayList<>();
		firstP = new Player(fPlayer);
		secondP = new Player(sPlayer);
		p = firstP;
		try {
			game = new Game(firstP, secondP);
			game.loadAbilities("./assests/csv/Abilities.csv");
			game.loadChampions("./assests/csv/Champions.csv");
		} catch (Exception e) {}
		champs=new ArrayList<Champion>();
		champs=Game.getAvailableChampions();
        addchampions();
		
        // Player's name (Top panel)
		player = new HBox();
		player.setPadding(new Insets(10, 10, 2, 15));
		player.setSpacing(400);
		player.setStyle("-fx-background-color: rgba(210, 39, 39,0.5);" 
        				+ "-fx-border-width: 1; -fx-border-color:rgba(236, 236, 236,0.8)");
		text = new MyHeadText(fPlayer,45);
		text.setTextAlignment(TextAlignment.CENTER);
		UnitButton next = new UnitButton("Next",100,50);
		player.getChildren().addAll(text,next);
		next.setOnAction(nextHandler());
		
		root.setTop(player);
		root.setCenter(champView);
		root.setRight(info);
		root.setLeft(team);
		root.setBottom(flow);
		
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
	
	}
	
	
	
	
	public EventHandler<ActionEvent> nextHandler()
	{
		
		return (e ->
	{	
				PlaySound.playClick();
				if(p.getTeam().size()<3)
					AlertBox.display("Error","You must select 3 champions to continue");
				else {
						ChooseLeader.display(p, team.getChildren());
						text.setText(sPlayer);
						p = secondP;
						team.getChildren().clear();
			}
				if(p==secondP && p.getTeam().size()==3) {
					GameView view2 = new GameView();
					view2.setP1(firstP);
					view2.setP2(secondP);
					view2.start(primaryStage);
					primaryStage.setScene(view2.getScene());
				}
			});
	}
	
	public void addchampions() {
		 for(int i=0;i<champs.size();i++) {
			 	Champion champ = champs.get(i);
				var b=new DamageableButton(champ.getName(), icons.get(i));
				b.setAlignment(Pos.CENTER);
				
				var charButton =new DamageableButton(champ.getName(), fotos2.get(i),100,200,0.3);
				
				b.setOnAction(e ->{
					if(p.getTeam().size()>=3) 
						AlertBox.display("Error","You cannot select more than 3 champions");
					else {
						PlaySound.play("./assests/sounds/"+ champ.getName() +".wav");
						flow.getChildren().remove(b);
						team.getChildren().add(charButton);
						p.getTeam().add(champ);
						p.getChildren().add(b);
						buttons.remove(b);
					}
				});
				buttons.add(b);
				flow.getChildren().add(b);

				charButton.setOnAction(e ->{
					PlaySound.playClick();
					flow.getChildren().add(b);
					team.getChildren().remove(charButton);
					p.getTeam().remove(champ);
					p.getChildren().remove(b);
					buttons.add(b);
					});
				
				
				ImageView img = fotos.get(i);
				MyText playerName = new MyText(champ.getName(), 40);
				champView.setMargin(playerName, new Insets(0,0,0,50));
				b.setOnMouseEntered(e-> {
					champView.getChildren().clear();
					champView.getChildren().addAll(playerName,img);
					info.getChildren().clear();
					info.getChildren().add(new MyText(champ.toString(),15) );
				});
			
			}
	}
	
	public void addicons() {
		ImageView icon0 = new ImageView(new Image("file:"+"./assests/icons/Captain America.png"));
		ImageView icon1 = new ImageView(new Image("file:"+"./assests/icons/Deadpool.png"));
		ImageView icon2 = new ImageView(new Image("file:"+"./assests/icons/Dr Strange.png"));
		ImageView icon3 = new ImageView(new Image("file:"+"./assests/icons/Electro.png"));
		ImageView icon4 = new ImageView(new Image("file:"+"./assests/icons/Ghost Rider.png"));
		ImageView icon5 = new ImageView(new Image("file:"+"./assests/icons/Hela.png"));
		ImageView icon6 = new ImageView(new Image("file:"+"./assests/icons/Hulk.png"));
		ImageView icon7 = new ImageView(new Image("file:"+"./assests/icons/Iceman.png"));
		ImageView icon8 = new ImageView(new Image("file:"+"./assests/icons/Ironman.png"));
		ImageView icon9 = new ImageView(new Image("file:"+"./assests/icons/Loki.png"));
		ImageView icon10 = new ImageView(new Image("file:"+"./assests/icons/Quicksilver.png"));
		ImageView icon11 = new ImageView(new Image("file:"+"./assests/icons/Spiderman.png"));
		ImageView icon12 = new ImageView(new Image("file:"+"./assests/icons/Thor.png"));
		ImageView icon13 = new ImageView(new Image("file:"+"./assests/icons/Venom.png"));
		ImageView icon14 = new ImageView(new Image("file:"+"./assests/icons/Yellowjacket.png"));
		icons.add(icon0);icons.add(icon1);icons.add(icon2);icons.add(icon3);icons.add(icon4);icons.add(icon5);
		icons.add(icon6);icons.add(icon7);icons.add(icon8);icons.add(icon9);icons.add(icon10);icons.add(icon11);
		icons.add(icon12);icons.add(icon13);icons.add(icon14);
	}

	public void addfotos() {
		ImageView foto0 = new ImageView(new Image("file:"+"./assests/chars/Captain America.png"));
		ImageView foto1 = new ImageView(new Image("file:"+"./assests/chars/Deadpool.png"));
		ImageView foto2 = new ImageView(new Image("file:"+"./assests/chars/Dr Strange.png"));
		ImageView foto3 = new ImageView(new Image("file:"+"./assests/chars/Electro.png"));
		ImageView foto4 = new ImageView(new Image("file:"+"./assests/chars/Ghost Rider.png"));
		ImageView foto5 = new ImageView(new Image("file:"+"./assests/chars/Hela.png"));
		ImageView foto6 = new ImageView(new Image("file:"+"./assests/chars/Hulk.png"));
		ImageView foto7 = new ImageView(new Image("file:"+"./assests/chars/Iceman.png"));
		ImageView foto8 = new ImageView(new Image("file:"+"./assests/chars/Ironman.png"));
		ImageView foto9 = new ImageView(new Image("file:"+"./assests/chars/Loki.png"));
		ImageView foto10 = new ImageView(new Image("file:"+"./assests/chars/Quicksilver.png"));
		ImageView foto11 = new ImageView(new Image("file:"+"./assests/chars/Spiderman.png"));
		ImageView foto12 = new ImageView(new Image("file:"+"./assests/chars/Thor.png"));
		ImageView foto13 = new ImageView(new Image("file:"+"./assests/chars/Venom.png"));
		ImageView foto14 = new ImageView(new Image("file:"+"./assests/chars/Yellowjacket.png"));
		fotos.add(foto0);fotos.add(foto1);fotos.add(foto2);fotos.add(foto3);fotos.add(foto4);fotos.add(foto5);
		fotos.add(foto6);fotos.add(foto7);fotos.add(foto8);fotos.add(foto9);fotos.add(foto10);fotos.add(foto11);
		fotos.add(foto12);fotos.add(foto13);fotos.add(foto14);
	}
	
	public void addfotos2() {
		int w = 150;
		int h = 250;
		ImageView foto0 = new ImageView(new Image("file:"+"./assests/chars/Captain America.png", w, h, false, true, true));
		ImageView foto1 = new ImageView(new Image("file:"+"./assests/chars/Deadpool.png", w, h, false, true, true));
		ImageView foto2 = new ImageView(new Image("file:"+"./assests/chars/Dr Strange.png", w, h, false, true, true));
		ImageView foto3 = new ImageView(new Image("file:"+"./assests/chars/Electro.png", w, h, false, true, true));
		ImageView foto4 = new ImageView(new Image("file:"+"./assests/chars/Ghost Rider.png", w, h, false, true, true));
		ImageView foto5 = new ImageView(new Image("file:"+"./assests/chars/Hela.png", w, h, false, true, true));
		ImageView foto6 = new ImageView(new Image("file:"+"./assests/chars/Hulk.png", w, h, false, true, true));
		ImageView foto7 = new ImageView(new Image("file:"+"./assests/chars/Iceman.png", w, h, false, true, true));
		ImageView foto8 = new ImageView(new Image("file:"+"./assests/chars/Ironman.png", w, h, false, true, true));
		ImageView foto9 = new ImageView(new Image("file:"+"./assests/chars/Loki.png", w, h, false, true, true));
		ImageView foto10 = new ImageView(new Image("file:"+"./assests/chars/Quicksilver.png", w, h, false, true, true));
		ImageView foto11 = new ImageView(new Image("file:"+"./assests/chars/Spiderman.png", w, h, false, true, true));
		ImageView foto12 = new ImageView(new Image("file:"+"./assests/chars/Thor.png", w, h, false, true, true));
		ImageView foto13 = new ImageView(new Image("file:"+"./assests/chars/Venom.png", w, h, false, true, true));
		ImageView foto14 = new ImageView(new Image("file:"+"./assests/chars/Yellowjacket.png", w, h, false, true, true));
		fotos2.add(foto0);fotos2.add(foto1);fotos2.add(foto2);fotos2.add(foto3);fotos2.add(foto4);fotos2.add(foto5);
		fotos2.add(foto6);fotos2.add(foto7);fotos2.add(foto8);fotos2.add(foto9);fotos2.add(foto10);fotos2.add(foto11);
		fotos2.add(foto12);fotos2.add(foto13);fotos2.add(foto14);
	}
	
	public Scene getScene() {
		return scene;
	}

	public void setfPlayer(String fPlayer) {
		this.fPlayer = fPlayer;
	}

	public void setsPlayer(String sPlayer) {
		this.sPlayer = sPlayer;
	}

}
