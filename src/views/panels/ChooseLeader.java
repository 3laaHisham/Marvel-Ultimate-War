package views.panels;

import java.util.ArrayList;
import java.util.List;
import engine.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.world.Champion;
import views.buttons.DamageableButton;
import views.helpers.MyText;

public class ChooseLeader {
	
	public static void display(Player p, List<Node> Children) {
		
	Stage stage = new Stage();
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.setTitle("Leader");
	stage.setMinWidth(900);
	stage.setMinHeight(450);
	
	FlowPane myTeam = new FlowPane(10,10);
	
	
	myTeam.setBackground(new Background(
            new BackgroundFill(
                    new LinearGradient(0, 0, 0, 1, true,
                            CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#4568DC")),
                            new Stop(1, Color.web("#B06AB3"))
                    ), CornerRadii.EMPTY, Insets.EMPTY
            ),
            new BackgroundFill(
                    new ImagePattern(
                            new Image("file:./assests/windows/Stars_128.png"),
                            0, 0, 128, 128, false
                    ), CornerRadii.EMPTY, Insets.EMPTY),
            new BackgroundFill(
                    new RadialGradient(
                            0, 0, 0.5, 0.5, 0.5, true,
                            CycleMethod.NO_CYCLE,
                            new Stop(0, Color.web("#FFFFFF33")),
                            new Stop(1, Color.web("#00000033"))),
                    CornerRadii.EMPTY, Insets.EMPTY
            )
    ));
	
	myTeam.setPadding(new Insets(20, 20, 20, 20));
	myTeam.setAlignment(Pos.CENTER);
	MyText choose = new MyText("Please choose your Leader",50);
	myTeam.getChildren().add(choose);
	
	for(Node btn: Children) {
		DamageableButton button = new DamageableButton( 
				((DamageableButton)btn).getText(),(ImageView) ((DamageableButton)btn).getGraphic()) ;
		myTeam.getChildren().add(button);
		button.setOnAction(e->{
			p.setLeader(p.getTeam().stream().filter(champ-> button.getText()
					.equals(champ.getName() )).findAny().get());
			stage.close();
		});
	}
	
	Scene scene = new Scene(myTeam);
	stage.setScene(scene);
	stage.showAndWait();
	}
}
