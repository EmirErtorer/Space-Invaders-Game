// Emir Devlet Ertörer 

import java.awt.*;
import javax.swing.*;

public class SpaceInvaders {

	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Center the frame on the screen
		frame.setLocationRelativeTo(null);
		frame.setBackground(new Color(24, 17, 55)); //RGB for background color shown in the video
		frame.setVisible(true);
	}
}
