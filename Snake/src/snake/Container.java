package snake;

import javax.swing.JFrame;

public class Container extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Container(){
		add(new Snake());
		setTitle("Snake 2.0");
		setSize(400,440);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Container();
	}
}

