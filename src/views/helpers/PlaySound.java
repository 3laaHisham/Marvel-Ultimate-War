package views.helpers;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlaySound {
	private String path;
	

	public PlaySound() {
	}

	public static long play(String path){
		
		try {
		      AudioInputStream audio = null;
			try {
				audio = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      Clip clip = AudioSystem.getClip();
		      clip.close();
		      try {
				clip.open(audio);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      clip.start();

		      return clip.getMicrosecondLength();
		    } catch (UnsupportedAudioFileException | LineUnavailableException e) {}
		    return 0;
		    
		    
		  }

	public static void playClick(){
	      play("./assests/sounds/click.wav");
	}
	
	}