// Emir Devlet Ertörer

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class SpaceShip {

	ArrayList<Rectangle> lasers = new ArrayList<>(); // to store the lasers shot from the spaceship
	
	int x, y; // coordinates of the spaceship
	int SHIP_WIDTH;
	int SHIP_HEIGHT;
	int xSpeed, ySpeed; // x and y dimensional speed of the spaceship
	int speed = 10; 
	int check; // check if the direction is x or y. 0 for y, 1 for x
	static int health = 3; // Ship's health
	ImageIcon spaceShipImage; // Image for the spaceship
	
	public SpaceShip(int x, int y, int SHIP_WIDTH, int SHIP_HEIGHT) {
		this.x = x;
		this.y = y;
		this.SHIP_HEIGHT = SHIP_HEIGHT;
		this.SHIP_WIDTH = SHIP_WIDTH;
		spaceShipImage = new ImageIcon("Images/spaceship.GIF"); // load the spaceship image
		// scaling the image to the specifies dimensions:
		spaceShipImage = new ImageIcon(spaceShipImage.getImage().getScaledInstance(SHIP_WIDTH, SHIP_HEIGHT, Image.SCALE_DEFAULT));
	}

	public void keyPressed(KeyEvent e) {
        // Move the ship based on input
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            setYdirection(-speed); //move 10 pixels up
            check = 0; // direction is in y axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            setYdirection(speed); //move 10 pixels down
            check = 0; // direction is in y axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            setXdirection(-speed); //move 10 pixels to the left
            check = 1; // direction is in x axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            setXdirection(speed); //move 10 pixels to the right
            check = 1; // direction is in x axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        	// calls the shoot method when the space key is pressed
        	shoot();
        }
	}
	
	public void keyReleased(KeyEvent e) {
        // Move the ship based on input
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            setYdirection(0); // stop moving
            check = 0; // direction is in y axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            setYdirection(0); // stop moving
            check = 0; // direction is in y axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            setXdirection(0); // stop moving
            check = 1; // direction is in x axis
            move(); // call the move function
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            setXdirection(0); // stop moving
            check = 1; // direction is in x axis
            move(); // call the move function
        }
	}
	
	public void mouseMoved(MouseEvent e) {
		int currentY = e.getY(); // x coordinate of the mouse
		int currentX = e.getX(); // y coordinate of the mouse
		int deltaY = currentY - y; // Calculate the difference in y
	    int deltaX = currentX - x; // Calculate the difference in x
	    
		if(currentY > y) {
			setYdirection(-deltaY / 5); // slower so that it moves smoother
            check = 0; // direction is in y axis
            move(); // call the move function
		}
		else if(currentY < y) {
			setYdirection(deltaY / 5); // slower so that it moves smoother
            check = 0; // direction is in y axis
            move(); // call the move function
		}
		else if(currentX > x) {
			setYdirection(-deltaX /5); // slower so that it moves smoother
            check = 0; // direction is in y axis
            move(); // call the move function
		}
		else if(currentX < x) {
			setYdirection(deltaX /5); // slower so that it moves smoother
            check = 0; // direction is in y axis
            move(); // call the move function
		}
		// update the current coordinates:
		y = currentY; 
		x = currentX;
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) // left mouse key
			shoot();
	}
	
	public void setYdirection(int yDirection) {
		ySpeed = yDirection; // specify how much to move along the y axis
	}
	
	public void setXdirection(int xDirection) {
		xSpeed = xDirection; // specify how much to move along the x axis
	}
	
	public void move() {
		if (check == 0) y = y + ySpeed;
		else if(check == 1)x = x + xSpeed;
	} 
	
    private boolean canShoot = true; // Flag to control shooting

    public void shoot() {
        if (canShoot) {
            int laserWidth = 10;
            int laserHeight = 40;

            Rectangle laser = new Rectangle(x + SHIP_WIDTH / 2 - laserWidth, y, laserWidth, laserHeight);
            lasers.add(laser); // add the individual laser to the list

            playSound("Audios/laser_blast.wav");

            canShoot = false; // Set the flag to false to prevent shooting until the delay is over

            Timer delayTimer = new Timer(300, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    canShoot = true; // Set the flag to true after the delay
                }
            });
            delayTimer.setRepeats(false); // Set to run only once
            delayTimer.start();
        }
    }

	
	public void playSound(String filePath) {
		 try {
		        File audioFile = new File(filePath);
		        Clip clip = AudioSystem.getClip();
		        clip.open(AudioSystem.getAudioInputStream(audioFile));
		        clip.start();
		    }
		 catch (Exception e) {
		        e.printStackTrace();
		    }
	}

	public void draw(Graphics g) {
		spaceShipImage.paintIcon(null, g, x, y); // draw the spaceship
	}
	
	public void reset() {
	    x = x / 2 - SHIP_WIDTH / 2;
	    y = y;
	    lasers.clear();
	    health = 3;
	}
}

