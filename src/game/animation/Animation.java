package game.animation;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Image;

public class Animation {
	
	int frameCount;
	int frameDelay;
	int currFrame;
	int animDir;
	int totalFrames;
	
	boolean stopped;
	
	List<Frame> frames = new ArrayList<Frame>();
	
	public Animation(Image[] frames, int frameDelay) {
		this.frameDelay = frameDelay;
		this.stopped = true;
		
		for(int i = 0;i < frames.length;i++) {
			addFrame(frames[i], frameDelay);
		}
		this.frameCount = 0;
		this.frameDelay = frameDelay;
		this.currFrame = 0;
		this.animDir = 1;
		this.totalFrames = this.frames.size();
	}
	
	public void addFrame(Image frame, int duration) {
		if(duration <= 0) {
			System.err.println("How long it last ya boob?" + "Duration= " + duration);
			throw new RuntimeException("Invalid duration: " + duration);
		}
		frames.add(new Frame(frame, duration));
		currFrame = 0;
	}
	
	public void update() {
		if(!stopped) {
			frameCount++;
			
			if(frameCount > frameDelay) {
				frameCount = 0;
				currFrame += animDir;
				
				if(currFrame > totalFrames - 1) {
					currFrame = 0;
				} else if(currFrame < 0) {
					currFrame = totalFrames - 1;
				}
			}
		}
	}
	
	public Image getSprite() {
		return frames.get(currFrame).getFrame();
	}
	
	public void start() {
		if(!stopped) {
			return;
		}
		
		if(frames.size() == 0) {
			return;
		}
		
		stopped = true;
	}
	
	public void restart() {
		if(frames.size() == 0) {
			return;
		}
		stopped = false;
		currFrame = 0;
	}
	
	public void reset() {
		this.stopped = true;
		this.frameCount = 0;
		this.currFrame = 0;
	}
	

}
