package view;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class RobotPanel extends JPanel{

	public JLabel phase;
	public JLabel obstacle;
	public JLabel angle;
	public RobotPanel(){
	
		phase = new JLabel("le robot est actuellement en phase d'initialisation");
		obstacle = new JLabel("pas d'obstacle détecté");
		angle = new JLabel("l'angle depuis le point de départ est de 0°");

		setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		add(phase);
		add(Box.createHorizontalStrut(5));
		add(new JSeparator(SwingConstants.VERTICAL));
		add(Box.createHorizontalStrut(5));
		add(obstacle);
		add(Box.createHorizontalStrut(5));
		add(new JSeparator(SwingConstants.VERTICAL));
		add(Box.createHorizontalStrut(5));
		add(angle);
	}
	
}
