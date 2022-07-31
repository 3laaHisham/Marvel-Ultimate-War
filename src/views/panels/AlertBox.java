package views.panels;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	
	public static void display(String title, String message2) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setMinWidth(400);
		stage.setMinHeight(150);
		
		Text message = new Text(message2);
		message.setFont(Font.font("Verdana", FontWeight.BOLD,16));
		Button close = new Button("Close");
		if(title.equals("Game Over"))
			close.setOnAction(e -> System.exit(0));
		else
			close.setOnAction(e -> stage.close());
		
		VBox layout = new VBox();
		layout.getChildren().addAll(message,close);
		layout.setSpacing(30);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		stage.getIcons().add(new Image("file:./assests/icons/Error.png"));
		stage.setScene(scene);
		stage.showAndWait();
		
	}

}
