package views.helpers;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MyText extends Text {

	public MyText() {
		super();
		setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		 setFill(Color.web("#FFD700"));
    //    setStroke(Color.web("#FEF9A7")); 
	}

	public MyText(String arg0) {
		super(arg0);
		setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        setFill(Color.web("#FFD700"));
   //     setStroke(Color.web("#FEF9A7")); 
	}

	
	public MyText(String arg0,double size) {
		setText(arg0);
		setFont(Font.font("Verdana", FontWeight.BOLD, size));
		 setFill(Color.web("#FFD700"));
	//	 setStroke(Color.web("#FEF9A7")); 
	}
	
}
