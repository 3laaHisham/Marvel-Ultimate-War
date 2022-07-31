package views.panels;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class MyBackGround {
	private static String path;
	private static Image img;
    private static BackgroundImage bImg;
    private static Background bGround;
    
	public MyBackGround() {
		
	}

    public static Background get(String path1,String path2) {
    	path = Math.round(Math.random())==1? path1: path2 ;
        img = new Image("file:" + path);
        ImageView iv2 = new ImageView();
        bImg = new BackgroundImage(img,
                                       BackgroundRepeat.NO_REPEAT,
                                       BackgroundRepeat.NO_REPEAT,
                                       BackgroundPosition.DEFAULT,
                                       BackgroundSize.DEFAULT);
        bGround = new Background(bImg);
    	return bGround;
    }

}
