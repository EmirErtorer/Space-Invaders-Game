// Emir Devlet Ertörer 

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameFrame extends JFrame {
	
	private JPanel menuPanel;
	JTextPane scoreTextPane;
	GameFrame gameFrame;
	GamePanel gamePanel;
	public ArrayList<String> userNames = new ArrayList<String>(); // stores the user names
	public ArrayList<Integer> userPasswords = new ArrayList<Integer>();
	public ArrayList<Integer> highScores = new ArrayList<Integer>();
	
	private Clip audioClip;
	private AudioInputStream audioInputStream;

	
	public GameFrame() {
		menuPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponents(g);
				ImageIcon backgroundImage = new ImageIcon("Images/menu_screen.PNG");
				g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		
		menuPanel.setLayout(null);
		menuPanel.setPreferredSize(new Dimension(800, 600));
		gamePanel = new GamePanel(this);
		gamePanel.setVisible(false); // Hide the game panel initially
		
		
	    
	    setContentPane(menuPanel); // Set the menu panel as the content pane

		//this.add(menuPanel); // this is the loading screen
		this.setTitle("CSE 212 Term Project - Space Invaders Game");
		//this.setResizable(false);
		this.pack();
		
		 try {
			 	audioInputStream = AudioSystem.getAudioInputStream(new File("Audios/menu_song.wav")); 
		        audioClip = AudioSystem.getClip();
		        audioClip.open(audioInputStream);
		        audioClip.loop(Clip.LOOP_CONTINUOUSLY); // Play the audio in a loop
		    }
		 catch (Exception e) {
		        e.printStackTrace();
		    }
        
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
        // Create MenuItems
        JMenuItem register = new JMenuItem("Register");
        JMenuItem play = new JMenuItem("Play Game");
        JMenuItem score = new JMenuItem("High Score");
        JMenuItem quit = new JMenuItem("Quit");
        JMenuItem about = new JMenuItem("About");

        // Add MenuItems to JMenu
        fileMenu.add(register);
        fileMenu.add(play);
        fileMenu.add(score);
        fileMenu.add(quit);
        helpMenu.add(about);
        // Add JMenu to JMenuBar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Add JMenuBar to JFrame
        setJMenuBar(menuBar);
        
        
        // Action Listener for buttons:
        
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// not yet figured out
            	String userName= JOptionPane.showInputDialog("Enter your username");
            	userNames.add(userName); // add the username to the arraylist
            	
            	//NOTE TO SELF: CHANGE THIS TO PASSWORD FIELD
            	String userPassword= JOptionPane.showInputDialog("Enter your password using numeric digits only");
            	int password = Integer.parseInt(userPassword); // convert the password to int
                userPasswords.add(password); // add the password to the passwords ArrayList
                
                String message = "Successfully registered!\nEnter your username and password"
                        + " you just created to start playing the game. Have fun!";

                JOptionPane.showMessageDialog(null, message, "Registration Success", JOptionPane.PLAIN_MESSAGE);
                
            }
        });

        play.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	boolean matched = true;
            	do {
            		String userName= JOptionPane.showInputDialog("Enter your username that you used to register:");
                	String userPassword= JOptionPane.showInputDialog("Enter your password that you used to register:");
                	int password = Integer.parseInt(userPassword); // convert the password to int
                	
                	if(userNames.contains(userName) == false) {
                		JOptionPane.showMessageDialog(null, "The user name you entered was not registered. Please try again");
                	}
                	if(userNames.contains(userName) && userPasswords.contains(password) == false) {
                		JOptionPane.showMessageDialog(null, "The password you entered does not match the username"
                				+ " that was registered. Please try again");
                	}
                	if(userNames.contains(userName) && userPasswords.contains(password)) {
						matched = false;
					}
            		
            	}while(matched == true);
            	
            	String difficulty= JOptionPane.showInputDialog("Enter your preffered dificulty; 1, 2, or 3");
            	int difficultyChoice = Integer.parseInt(difficulty);
            	GamePanel.setDifficulty(difficultyChoice);
            	
            	audioClip.stop(); // Stop playing the audio clip
            	
            	// Reset the game panel before starting a new game
                gamePanel.resetGame();
                
                gamePanel.newAliens();
                gamePanel.newSpaceShip();
                
                gamePanel.setGameStarted(); // Set the game as started
                
                if (gamePanel.gameStarted) {
                	gamePanel.timer1.start();
                	gamePanel.timer2.start();
                	gamePanel.timer3.start();
                	gamePanel.timer4.start();
                }
               
                setContentPane(gamePanel); // Set the existing gamePanel
                gamePanel.setVisible(true);
                gamePanel.requestFocusInWindow(); // Request keyboard focus for the game panel
                revalidate(); // Refresh the frame
            }
        });
        
        score.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	scoreTextPane= new JTextPane();
            	scoreTextPane.setBackground(Color.black);
            	scoreTextPane.setForeground(Color.white); // set the font color
            	
            	// Installed a custom font to blend it with the game's style
            	try {
					Font pixelatedFont = Font.createFont(Font.TRUETYPE_FONT, new File("Pixelated_Font.ttf"));
					Font customFont = pixelatedFont.deriveFont(Font.BOLD, 24);
					scoreTextPane.setFont(customFont);
				}
            	catch (FontFormatException | IOException err2) {
					
					err2.printStackTrace();
				}
            	
            	try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            	    int maxNameLength = 0;
            	    for (String name : userNames) {
            	        maxNameLength = Math.max(maxNameLength, name.length());
            	    }

            	    for (int i = 0; i < userNames.size(); i++) {
            	        writer.write(String.valueOf(i + 1));
            	        writer.write("  ");
            	        writer.write(userNames.get(i));

            	        int numSpaces = maxNameLength - userNames.get(i).length();
            	        for (int j = 0; j < numSpaces; j++) {
            	            writer.write(" ");
            	        }

            	        writer.write("  ");
            	        writer.write(String.valueOf(highScores.get(i)));
            	        writer.newLine(); // Add a new line after each element
            	    }
            	    System.out.println("Data has been written to the file.");
            	} catch (IOException err) {
            	    System.out.println("An error occurred while writing to the file: " + err.getMessage());
            	}
            	
            	scoreTextPane.setEditable(false);
            	scoreTextPane.setPreferredSize(new Dimension(800, 600));
            	setContentPane(scoreTextPane);
            	
            	try (BufferedReader reader = new BufferedReader(new FileReader("output.txt"))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append('\n');
                    }
                    scoreTextPane.setText(content.toString());
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
        });
        
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0); // exit the program
            }
        });   
        
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JOptionPane.showMessageDialog(null, "Author:\nEmir Devlet Ertörer\n20210702040\n"
            			+ "emirdevlet.ertorer@std.yeditepe.edu.tr");
            }
        });

        
	}
	
	public void switchToMenuPanel() {
	    gamePanel.gameOver = true; // Reset the game over flag in the game panel
	    menuPanel.setVisible(true); // Show the menu panel
	    gamePanel.setVisible(false); // Hide the game panel
	    menuPanel.requestFocusInWindow(); // Request keyboard focus for the menu panel
	    setContentPane(menuPanel); // Set the menu panel as the content pane
	    revalidate(); // Refresh the frame
	}
	
}
