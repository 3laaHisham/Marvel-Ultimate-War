package views.views;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.* ;
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
import views.buttons.UnitButton;
import views.helpers.MyHeadText;
import views.helpers.MyText;
import views.helpers.PlaySound;
import views.panels.AlertBox;
import views.panels.MyBackGround;


public class StartView extends Application {
	private GridPane root;
	private Scene scene;
	private MyHeadText text1;
	private static TextField field1;
	private MyHeadText text2;
	private static TextField field2;
	private UnitButton btn;
    
	@Override
    public void start(Stage primaryStage) throws Exception{
        root = new GridPane();
        scene = new Scene(root,1600,900);
        
        String path1 = "./assests/windows/StartView.jpg";
        String path2	= "./assests/windows/StartView2.jpg";
		root.setBackground(MyBackGround.get(path1, path2));
        
        text1 = new MyHeadText("Enter First Player's name");
        text2 = new MyHeadText("Enter Second Player's name");
        
        field1 = new TextField("First Player");
        field1.setMaxWidth(250);
        field1.setPrefHeight(30);
        field1.setStyle("-fx-text-fill: Black; -fx-font-size: 16px;");
        
        field2 = new TextField("Second Player");
        field2.setMaxWidth(250);
        field2.setPrefHeight(30);
        field2.setStyle("-fx-text-fill: Black; -fx-font-size: 16px;");
        
        btn = new UnitButton("Launch");
        btn.setOnAction( e -> {
        	PlaySound.play("./assests/sounds/Launch.wav");
        	if(field1.getText().isBlank()||field2.getText().isBlank())
        		AlertBox.display("Error","Names cannot be empty");
        	else {
        		ChoiceView view2 = new ChoiceView();
        		view2.setfPlayer(field1.getText());
        		view2.setsPlayer(field2.getText());
				view2.start(primaryStage);
        		primaryStage.setScene(view2.getScene());
        	}
        });
    
        VBox panel = new VBox();
        panel.setStyle("-fx-background-color: rgba(218,159,28,0.2)");
        panel.setBorder(new Border(new BorderStroke(Color.BLACK, 
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	    panel.setSpacing(40);
        panel.setPadding(new Insets(80, 50, 50, 70));
        panel.setMargin(text1, new Insets(0, 0, 0, 10));
        panel.setMargin(field1, new Insets(0, 0, 0, 100));
        panel.setMargin(text2, new Insets(30, 0, 0, 0));
        panel.setMargin(field2, new Insets(0, 0, 0, 100));
        panel.setMargin(btn, new Insets(40, 0, 0, 170));
        
        panel.getChildren().addAll(text1,field1,text2,field2,btn);
        
        root.setHgap(60);
        root.setVgap(60);
        root.setPadding(new Insets(10, 20, 10, 20));
        root.add(panel,1,2);
        
        primaryStage.setTitle("Marvel Ultimate War");
        primaryStage.getIcons().add(new Image("file:./assests/icons/marvel.jpg"));
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }
	
		public Scene getScene() {
		return scene;
	}

	public static TextField getField1() {
		return field1;
	}

	public static TextField getField2() {
		return field2;
	}

		public static void main(String[] args) {
			launch(args);
		}
		
}
