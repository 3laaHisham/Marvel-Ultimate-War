package views.helpers;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MyHeadText extends Text {
	LinearGradient gradient =  new LinearGradient(0, 0, 0, 1, true,
            CycleMethod.NO_CYCLE,
            new Stop(0,Color.BLUE),
            new Stop(1, Color.DARKRED)
    );
	
	public MyHeadText() {
		super();
		setFont(Font.font("Marvel", FontWeight.BOLD, 40));
        setFill(gradient);
        setStroke(Color.CADETBLUE); 
        setStrokeWidth(1);
	}

	public MyHeadText(String arg0) {
		super(arg0);
		setFont(Font.font("Marvel", FontWeight.BOLD, 40));
        setFill(gradient);
        setStroke(Color.CADETBLUE); 
        setStrokeWidth(1);
	}

	
	public MyHeadText(String arg0,double size) {
		super(arg0);
		setFont(Font.font("Marvel", FontWeight.BOLD, size));
        setFill(gradient);
        setStroke(Color.CADETBLUE); 
        setStrokeWidth(1);
	}
}
