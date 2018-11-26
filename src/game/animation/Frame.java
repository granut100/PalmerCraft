package game.animation;

import org.newdawn.slick.Image;

public class Frame {
	
	Image frame;
	int duration;
	
	public Frame(Image frame, int duration) {
		this.frame = frame;
		this.duration = duration;
	}
	
	public Image getFrame() {
		return frame;
	}
	
	public void setFrame(Image frame) {
		this.frame = frame;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

}
