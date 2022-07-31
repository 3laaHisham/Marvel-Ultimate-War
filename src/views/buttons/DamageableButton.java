package views.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DamageableButton extends Button {
	
	public DamageableButton(String txt,ImageView icon) {
		super(txt,icon );
        setAlignment(Pos.CENTER);
        setPrefSize(150, 100);
        setMaxSize(150, 100);
        setStyle("-fx-background-color: rgba(255,255,255,0.4);");
        setPadding(new Insets(1, 0, 1, 2));
	}
	public DamageableButton(String txt,ImageView icon,double w,double h) {
		super(txt, icon);
		setAlignment(Pos.CENTER);
		setPrefSize(w, h);
		setMinSize(w, h);
		setStyle("-fx-background-color: rgba(255,255,255,0.6);");
		setPadding(new Insets(1, 0, 1, 2));
	}
	
	public DamageableButton(String txt,ImageView icon,double w,double h,double opacity) {
		super(txt, icon);
		setAlignment(Pos.CENTER);
		setPrefSize(w, h);
		setStyle("-fx-background-color: rgba(255,255,255,"+ opacity+ ");");
		setPadding(new Insets(1, 0, 1, 2));
	}
}
