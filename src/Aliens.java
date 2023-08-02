// Emir Devlet Ertörer

import java.awt.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.event.*;
import java.io.File;

public class Aliens {
	
	ArrayList<Rectangle> alienLasers = new ArrayList<>(); // to store the lasers shot from the aliens
	double  x, y; // coordinates of the enemies
	int ALIEN_WIDTH, ALIEN_HEIGHT; // dimensions of the aliens
	double xSpeed, ySpeed;
	private static double speed = 0.37;
	ImageIcon alien1, alien2, alien3, alien4, alien5, alien6;
	int type; // which alien model to spawn
	private int health;
	
	private Clip laserClip;
	public Timer shootTimer;
	
	private static boolean canShoot = true;

	public Aliens(double  x, double  y, int ALIEN_WIDTH, int ALIEN_HEIGHT, int type) {
		this.x = x;
		this.y = y;
		this.ALIEN_WIDTH = ALIEN_WIDTH;
		this.ALIEN_HEIGHT = ALIEN_HEIGHT;
		this.type = type;
		
		alien1 = new ImageIcon("Images/green_alien1_animated.GIF"); // load the green alien 1 image
		alien2 = new ImageIcon("Images/green_alien6_animated.GIF"); // load the green alien 2 image
		alien3 = new ImageIcon("Images/purple_alien1_animated.GIF"); // load the purple alien1 1 image
		alien4 = new ImageIcon("Images/purple_alien2_animated.GIF"); // load the purple alien 2 image
		alien5 = new ImageIcon("Images/blue_alien1_animated.GIF"); // load the blue alien image
		alien6 = new ImageIcon("Images/special_green_alien_animated.GIF"); // load the special green alien image
		
		// scaling the images to the specifies dimensions:
		alien1 = new ImageIcon(alien1.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		alien2 = new ImageIcon(alien2.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		alien3 = new ImageIcon(alien3.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		alien4 = new ImageIcon(alien4.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		alien5 = new ImageIcon(alien5.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		alien6 = new ImageIcon(alien6.getImage().getScaledInstance(ALIEN_WIDTH, ALIEN_HEIGHT, Image.SCALE_DEFAULT));
		setXdirction(speed);
		setYdirction(speed);
		move();
		
		// Start the shooting timer with a random delay
		int shootingDelay = getRandomShootingDelay();
		shootTimer  = new Timer(shootingDelay, new ActionListener() { 
	        public void actionPerformed(ActionEvent e) {
	            shoot(canShoot); // shoot every x seconds
	            int nextDelay = getRandomShootingDelay();
	            shootTimer.setDelay(nextDelay);
	        }
	    });
		shootTimer.start();
	}
	
	public int getRandomShootingDelay() {
        // Generate a random delay between 3 and 8 seconds
        return (int) (Math.random() * 5000) + 3000;
    }
	
	public void shoot(boolean canShoot) {
		if(canShoot) {
			int laserWidth = 10;
			int laserHeight = 40;
			
			Rectangle laser = new Rectangle((int)x + ALIEN_WIDTH/2 - laserWidth, (int)y, laserWidth, laserHeight);
			alienLasers.add(laser); // add the individual laser to the list
			
			playSound("Audios/alien_shot.wav");
		}
		
	}
	
	public static void setCanShoot(boolean canItShoot) {
		canShoot = canItShoot;
	}
	
	public void setHealth(int newHealth) {
		health = newHealth;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void playSound(String filePath) {
		 try {
		        File audioFile = new File(filePath);
		        Clip laserClip = AudioSystem.getClip();
		        laserClip.open(AudioSystem.getAudioInputStream(audioFile));
		        laserClip.start();
		    }
		 catch (Exception e) {
		        e.printStackTrace();
		    }
	}
	
	
	public void stopSound() {
        if (laserClip != null) {
            laserClip.stop();
            setCanShoot(false);
            shootTimer.stop();
        }
    }
	
	public static void setSpeed(double newSpeed) {
		speed = newSpeed;
	}
	
	public void setYdirction(double  yDirection) {
		ySpeed = yDirection;
	}
	
	public void setXdirction(double  xDirection) {
		xSpeed = xDirection;
	}
	
	public void move() {
		y = y + ySpeed/2;
		x = x + xSpeed;
	} 
	
	public void draw(Graphics g) {
		
		switch(type) {
			case 1:
				alien1.paintIcon(null, g, (int)x, (int)y);
				break;
			case 2:
				alien2.paintIcon(null, g, (int)x, (int)y);
				break;
			case 3:
				alien3.paintIcon(null, g, (int)x, (int)y);
				break;
			case 4:
				alien4.paintIcon(null, g, (int)x, (int)y);
				break;
			case 5:
				alien5.paintIcon(null, g, (int)x, (int)y);
				break;
			case 6:
				alien6.paintIcon(null, g, (int)x, (int)y);
				break;
		}

	}
}
