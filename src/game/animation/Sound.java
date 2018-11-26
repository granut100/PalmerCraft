package game.animation;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	Clip clip;
	boolean playedCompleted;
	String path;
	public Sound(String path) {
		this.path = path;
		File audioFile = new File(path);
		
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void play() {
		clip.start();
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void  loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void closeClip() {
		clip.close();
	}
	
	public void startClipOver() {
		clip.setMicrosecondPosition(0);
	}

}
