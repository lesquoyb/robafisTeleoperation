package view;

import model.BluetoothManager;
import org.omg.CORBA.INITIALIZE;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import controller.PhaseController;

public class PhasePanel  extends JPanel{


	public BufferedImage image;
	public static final String FOLLOWL_IMG_PATH = "resources/follow.jpg";
	public static final String INIT_IMG_PATH = "resources/init.jpg";
	public static final String TELEOP_IMG_PATH = "resources/teleop.jpg";
	public static final String END_IMG_PATH  = "resources/end.jpg";



	public PhasePanel(BluetoothManager bluetoothManager){
		super();
		new PhaseController(this, bluetoothManager);
		add(new JLabel("état"));
		loadImage(INIT_IMG_PATH);
	}

	private void loadImage(String s){
		try {
			image = ImageIO.read(new File(s));
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight() +30));
			super.repaint();
		}
		catch(Exception e){
			System.out.println("impossible de charger l'image: " + e.getMessage());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 30, null);
	}

	public void loadFollowingLine(){
		loadImage(FOLLOWL_IMG_PATH);
	}

	public void loadTeleoperation(){
		loadImage(TELEOP_IMG_PATH);
	}

	public void loadEnd(){
		loadImage(END_IMG_PATH);
	}

}
