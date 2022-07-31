package views.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UnitButton extends Button {
	
	public UnitButton() {
		super();
	}

	public UnitButton(String txt) {
		super(txt);
        setAlignment(Pos.CENTER);
        setPrefSize(100, 30);
        setFont(Font.font("Fira Code",FontWeight.BOLD,20));
        setStyle("-fx-background-color: #920138; -fx-text-fill: #ffffff");
        setPadding(new Insets(10, 10, 10, 10));
	}

	public UnitButton(String txt,int prefW) {
		super(txt);
        setAlignment(Pos.CENTER);
        setPrefSize(prefW, 30);
        setFont(Font.font("Fira Code",FontWeight.BOLD,20));
        setStyle("-fx-background-color: #920138; -fx-text-fill: #ffffff");
        setPadding(new Insets(10, 10, 10, 10));
	}
	public UnitButton(String txt,int prefW,int prefH) {
		super(txt);
		setAlignment(Pos.CENTER);
		setPrefSize(prefW, prefH);
		setFont(Font.font("Fira Code",FontWeight.BOLD,20));
		setStyle("-fx-background-color: #920138; -fx-text-fill: #ffffff");
		setPadding(new Insets(10, 10, 10, 10));
	}
	
}
