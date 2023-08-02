// Emir Devlet Ertörer

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.Timer;

public class GamePanel extends JPanel implements Runnable {
	
	private GameFrame gameFrame; // Reference to the parent GameFrame
	
	private static int numOfAliens = 60;
	
	int current_score = 0; // current score set to 0
	
	public boolean gameStarted = false;
	public boolean gameOver = false;
	public boolean gameReseted = false;
	
	static final int GAME_WIDTH = 800;
	static final int GAME_HEIGHT = 600;
	static final int SHIP_WIDTH = 60;
	static final int SHIP_HEIGHT = 60;
	static final int ALIEN_HEIGHT = 60;
	static final int ALIEN_WIDTH = 60;
	static final Dimension SCREEN_SIZE  = new Dimension(GAME_WIDTH, GAME_HEIGHT); // screen size
	static final int SHIP_SIZE = 60;
	
	Thread gameThread;
	Graphics graphics;
	Image image;
	SpaceShip spaceShip;
	ArrayList<Aliens> aliens = new ArrayList<Aliens>();
	Random random = new Random();
	
	private Clip audioClip;
	private AudioInputStream audioInputStream;
	
	public Timer timer1, timer2, timer3, timer4;
	
	public GamePanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		newSpaceShip();
		newAliens();
		Aliens.setCanShoot(true);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.addMouseMotionListener(new ML());
		this.addMouseListener(new ML());
		this.setPreferredSize(SCREEN_SIZE);
		setBackground(new Color(24, 17, 55));
		
		if(gameReseted == false) gameThread = new Thread(this);
		gameThread.start(); // start the thread

	}
	
	public static void setDifficulty(int difficulty) {
		if(difficulty == 1) {
			numOfAliens = 40;
			Aliens.setSpeed(0.37);
		}
		if(difficulty == 2) {
			numOfAliens = 65;
			Aliens.setSpeed(0.58);
		}
		if(difficulty == 3) {
			numOfAliens = 90;
			Aliens.setSpeed(0.8);
		}
	}
	
	public void setGameStarted() {
		gameStarted = true;
		gameOver = false;
	}
	
	public void newSpaceShip() {
		spaceShip = new SpaceShip(GAME_WIDTH/2 - SHIP_WIDTH/2, GAME_HEIGHT , SHIP_WIDTH, SHIP_HEIGHT);
	}
	
	private int alienCount = 0;
	
	public void newAliens() {
		// Create new aliens periodically	
		int greenSpawn = getRandomSpawngDelay(1500, 2000);
		timer1 = new Timer(greenSpawn, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (!gameStarted) return; // Stop spawning if gameStarted is false
	            int x = 0;
	            int y = 0;
	            Aliens newAlien = new Aliens(x, y, ALIEN_WIDTH, ALIEN_HEIGHT, 1); // regular green aliens
	            newAlien.setHealth(3);
	            aliens.add(newAlien);
	            alienCount++;
	            int nextSpawn = getRandomSpawngDelay(2500, 7000);
	            timer1.setDelay(nextSpawn);
	            
	            if (alienCount >= numOfAliens) {
	                timer1.stop();
	            }
	        }
            
	    });

	    timer2 = new Timer(10000, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	if (!gameStarted) return; // Stop spawning if gameStarted is false
	            int x = GAME_WIDTH;
	            int y = 0;
	            Aliens newAlien = new Aliens(x, y, ALIEN_WIDTH, ALIEN_HEIGHT, 3); // purple aliens
	            newAlien.setHealth(4);
	            newAlien.setXdirction(-0.5);  // Set the direction of the new Alien to move from right to left
	            aliens.add(newAlien);
	            alienCount++;
	            
	            if (alienCount >= numOfAliens) {
	                timer2.stop();
	            }
	        }
	    });

	    int randomSpawn = getRandomSpawngDelay(15000, 15000);
	    timer3 = new Timer(randomSpawn, new ActionListener() {
	        int alienCreated = 0; // Keep track of the number of aliens created
	        public void actionPerformed(ActionEvent e) {
	        	if (!gameStarted) return; // Stop spawning if gameStarted is false
	            int x = 0;
	            int y = 0;
	            for (int i = 0; i < GAME_WIDTH; i += 5) {
	                Aliens newAlien = new Aliens(x + i*15, y, ALIEN_WIDTH, ALIEN_HEIGHT, 5); // wall of blue aliens
	                newAlien.setXdirction(0); // They don't move in x
	                newAlien.setHealth(10);
	                aliens.add(newAlien);
	                alienCount++;
	                alienCreated++;
	                if(alienCreated == getRandomNumber()) i += 8;
	                if (alienCreated >= 9) {
	                    ((Timer) e.getSource()).stop(); // Stop the timer after creating 9 aliens
	                    break;
	                }
	            }
	            // Generate a random delay between 15 and 30 seconds
	            int nextSpawn = getRandomSpawngDelay(15000, 15000);
	            timer3.setDelay(nextSpawn);
	            
	            if (alienCount >= numOfAliens) {
	                timer3.stop();
	            }
	        }
	    });
	    
	    int randomSpecialGreen = getRandomSpawngDelay(15000, 5000);
	    timer4 = new Timer(randomSpecialGreen, new ActionListener() {
	        
	        public void actionPerformed(ActionEvent e) {
	        	if (!gameStarted) return; // Stop spawning if gameStarted is false
	            int x = GAME_WIDTH/2;
	            int y = 0;
	            Aliens newAlien = new Aliens(x, y, ALIEN_WIDTH, ALIEN_HEIGHT, 6); // special green aliens
	            newAlien.setHealth(6);
	            newAlien.setXdirction(-0.5);  // Set the direction of the new Alien to move from right to left
	            aliens.add(newAlien);
	            alienCount++;
	            // Generate a random delay between 15 and 30 seconds
	            int nextSpawn = getRandomSpawngDelay(15000, 15000);
	            timer4.setDelay(nextSpawn);
	            
	            if (alienCount >= numOfAliens) {
	                timer4.stop();
	            }
	        }
	    });
	    
 
	    timer3.setRepeats(false);
	    
	    if(gameStarted) {
	    	timer1.start();
		    timer2.start();
		    timer3.start();
		    timer4.start();
	    }

	    // Stop the first timer after 60 seconds
	    Timer stopTimer = new Timer(60000, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	timer1.stop();
	        }
	    });
	    stopTimer.setRepeats(false); // Set to run only once
	    stopTimer.start();
	     
	}
	
	private int getRandomSpawngDelay(int intervalA, int intervalB) {
        // Generate a random delay between 15 and 30 seconds
        return (int) (Math.random() * intervalA) + intervalB;
    }
	
	private int getRandomNumber() {
		return (int) (Math.random() * 7) +  1;
	}
	
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
	    graphics = image.getGraphics();

	    super.paint(graphics);

	    for (Rectangle laser : spaceShip.lasers) {
	        graphics.setColor(new Color(112,190,208));
	        graphics.fillRect(laser.x, laser.y, laser.width, laser.height);
	    }
	    
	    for(Aliens alien : aliens) {
	    	for (Rectangle laser : alien.alienLasers) {
		        graphics.setColor(Color.RED);
		        graphics.fillRect(laser.x, laser.y, laser.width, laser.height);
		    }
	    }
	    

	    draw(graphics); 
	    // Installed a custom font to blend it with the game's style
    	

	    g.drawImage(image, 0, 0, this); // Draw the image on the panel
	    
	    
	    int y = 30; // Fixed y-coordinate
	    
	    // Draw the health
	    g.setColor(Color.red);
	    g.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
	    g.drawString("❤️ x" + spaceShip.health, 30, y);
	    
	    try {
			Font pixelatedFont = Font.createFont(Font.TRUETYPE_FONT, new File("Pixelated_Font.ttf"));
			Font customFont = pixelatedFont.deriveFont(Font.BOLD, 18);
			g.setColor(Color.red);
		    FontMetrics fontMetrics = g.getFontMetrics();
		    int textWidth = fontMetrics.stringWidth("Score: " + current_score);
		    int x = (getWidth() - textWidth)/3 + 55;  // Centered x-coordinate
			g.setFont(customFont);
			g.drawString("Score: " + current_score, x, y);
		}
    	catch (FontFormatException | IOException err2) {
			
			err2.printStackTrace();
		}
	    // Draw the score on the screen	    
	}
	
	
	public void draw(Graphics g) {
		spaceShip.draw(g);
	
		for(int i = 0; i < aliens.size(); i++) {
			Aliens alien = aliens.get(i);
			alien.draw(g);		
		}
	}
	
	
	public void move() {
		
		spaceShip.move();
		
		for(int i = 0; i < aliens.size(); i++) {
			Aliens alien = aliens.get(i);
			alien.move();
		}
		
		for(Rectangle laser : spaceShip.lasers) {
			laser.y -= 5; // adjusting the speed of the laser
		}
		
		for(Aliens alien : aliens) {
			for(Rectangle laser : alien.alienLasers) {
				laser.y += 5; // adjusting the speed of the laser
			}
		}
		
		// Remove aliens that have moved off-screen
	    aliens.removeIf(alien -> alien.y > GAME_HEIGHT);

	    // Remove lasers that have moved off-screen
	    spaceShip.lasers.removeIf(laser -> laser.y + laser.height < 0);
	  
	    laserCollision();
	    
		repaint();
	}
	
	
	public void checkCollision() {
		// stops spaceship at edges
		if (spaceShip.y <= GAME_HEIGHT/2) spaceShip.y = GAME_HEIGHT/2;
		if(spaceShip.y > (GAME_HEIGHT - SHIP_HEIGHT)) spaceShip.y = GAME_HEIGHT - SHIP_HEIGHT;
		if (spaceShip.x <= 0) spaceShip.x = 0;
		if(spaceShip.x > (GAME_WIDTH - SHIP_WIDTH)) spaceShip.x = GAME_WIDTH - SHIP_WIDTH;
		
		//spaceship colliding with the aliens
		for (Aliens newAlien : aliens) {
			 if (spaceShip.y + SHIP_HEIGHT > newAlien.y && spaceShip.y < newAlien.y + ALIEN_HEIGHT
		                && spaceShip.x + SHIP_WIDTH > newAlien.x && spaceShip.x < newAlien.x + ALIEN_WIDTH) {
		            // collision between spaceship and alien 

		            // adjusted spaceship position to prevent overlap
		            if (spaceShip.y <= newAlien.y) {
		                spaceShip.y = (int) (newAlien.y - SHIP_HEIGHT);
		            }
		            else {
		                spaceShip.y = (int) (newAlien.y + ALIEN_HEIGHT);
		            }

		            if (spaceShip.x <= newAlien.x) {
		                spaceShip.x = (int) (newAlien.x - SHIP_WIDTH);
		            }
		            else {
		                spaceShip.x = (int) (newAlien.x + ALIEN_WIDTH);
		            }

		            break; 
		        }
	    }
		
		//bouncing aliens at the edges
		for(int i = 0; i < aliens.size(); i++) {
			Aliens alien = aliens.get(i);
			
			if (alien.x <= 0) {
				alien.setXdirction(0.37);
			}
			if(alien.x > (GAME_WIDTH - ALIEN_WIDTH)) {
				alien.setXdirction(-0.37);
			};
		}
		
	}

	public void laserCollision() {
		ArrayList<Aliens> aliensToRemove = new ArrayList<>();
		ArrayList<Rectangle> lasersToRemove = new ArrayList<>();
		ArrayList<Rectangle> alienLasersToRemove = new ArrayList<>(); // lasers from aliens to remove
			
		  Iterator<Aliens> aliensIterator = aliens.iterator();
		    while (aliensIterator.hasNext()) {
		        Aliens alien = aliensIterator.next();

		        for (Rectangle laser : spaceShip.lasers) {
		            if (laser.intersects(alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT)) {
		                alien.setHealth(alien.getHealth() - 1); // decrement the alien health
		                if (alien.type == 1) current_score += 10;
		                if (alien.type == 2 || alien.type == 3 || alien.type == 4 || alien.type == 5) current_score += 20;
		                // play audio when laser collides
		                try {
		                    audioInputStream = AudioSystem.getAudioInputStream(new File("Audios/laser_collided.wav"));
		                    audioClip = AudioSystem.getClip();
		                    audioClip.open(audioInputStream);
		                    audioClip.start();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }

		                if (alien.getHealth() == 0) {
		                    aliensToRemove.add(alien);
		                    alien.setHealth(3); // reset the alien health
		                }

		                lasersToRemove.add(laser);
		            }
		            if (laser.y >= GAME_HEIGHT) {
		                lasersToRemove.add(laser);
		            }
		        }

		        for (Rectangle alienLaser : alien.alienLasers) {
		            if (alienLaser.intersects(spaceShip.x, spaceShip.y, SHIP_WIDTH, SHIP_HEIGHT)) {
		                SpaceShip.health = SpaceShip.health - 1; // decrement the spaceship health

		                try {
		                    audioInputStream = AudioSystem.getAudioInputStream(new File("Audios/laser_collided.wav"));
		                    audioClip = AudioSystem.getClip();
		                    audioClip.open(audioInputStream);
		                    audioClip.start();
		                } catch (Exception e) {
		                    e.printStackTrace();
		                }

		                if (SpaceShip.health == 0 || ((spaceShip.health > 0) && (alienCount >= numOfAliens) )) {
		                    // remove spaceship
		                    gameOver = true;
		                    timer1.stop();
		                    timer2.stop();
		                    timer3.stop();
		                    timer4.stop();
		                    gameStarted = false;
		                    Aliens.setCanShoot(false);
		                }
		                alienLasersToRemove.add(alienLaser);
		                alien.stopSound();

		            }

		            if (alienLaser.y <= -GAME_HEIGHT) {
		                alienLasersToRemove.add(alienLaser);
		            }
		        }

		        alien.alienLasers.removeAll(alienLasersToRemove);

		        if (alien.getHealth() == 0) {
		            aliensToRemove.add(alien);
		        }
		    }

		    aliens.removeAll(aliensToRemove);
		    spaceShip.lasers.removeAll(lasersToRemove);
	    
	}
	
	public void run() {
		// game loop
		while (!gameOver) {
	        move();
	        checkCollision();
	        repaint();
	        try {
	            Thread.sleep(17); // for approximately 60 FPS
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }

		gameFrame.highScores.add(current_score); // save the current score

		// Switch to the menu panel
		SwingUtilities.invokeLater(() -> gameFrame.switchToMenuPanel());
	}
	
	public void resetGame() {
		
		gameReseted = true;
		
		if (audioClip != null && audioClip.isRunning()) {
	        audioClip.stop(); // Stop the audio clip
	    }
		for (Aliens alien : aliens) {
	        alien.stopSound();
	        alien.alienLasers.clear();
	        alien.shootTimer.stop();
	    }
		
		aliens.clear();

	    // Reinitialize the spaceship and start spawning aliens again
	    spaceShip.health = 3;
  
	    // Restart the timers
	    
	    newAliens();
	    newSpaceShip();
	    Aliens.setCanShoot(true);

	    // Reset other game-related variables
	    current_score = 0;
	    gameOver = false;

	    // Request focus for the game panel
	    requestFocusInWindow();
	    
	    if (!gameThread.isAlive()) {
	        gameThread = new Thread(this);
	        gameThread.start(); // Start the thread if it's not already running
	    }

	}
	
	class AL extends KeyAdapter{ // inner class action listener
		public void keyPressed(KeyEvent e) {
			spaceShip.keyPressed(e);
		} // calling the method from SpaceShip
		
		public void keyReleased(KeyEvent e) {
			spaceShip.keyReleased(e);
		} // calling the method from SpaceShip
	}
	
	class ML extends MouseAdapter{
		public void mouseMoved(MouseEvent e) {
			spaceShip.mouseMoved(e);
		}
		public void mouseClicked(MouseEvent e) {
			spaceShip.mouseClicked(e);
		}
	
	}
	
}
