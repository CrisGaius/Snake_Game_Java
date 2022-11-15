package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Snake extends JPanel implements ActionListener, KeyListener{

	private static final long serialVersionUID = 1L;
	private int snakeDirection = 2;
	private int comidaX=19;
	private int comidaY=10;
	private int[] xSnake=new int[500];
	private int[] ySnake=new int[500];
	private int tamanho=1;
	private boolean morreu = false;
	private boolean reiniciar = false;
	private boolean blocked = false;
	private int pontos = 0;
	private Timer timer;
	
	public Snake() {
		setFocusable(true);
		setDoubleBuffered(true);
		addKeyListener(this);
		startTimer();
		iniciar();
	}
	private void iniciar() {
		snakeDirection = 2;
		pontos=0;
		comidaX=10;
		comidaY=10;
		xSnake=new int[500];
		ySnake=new int[500];
		tamanho=3;
		morreu = false;
		blocked = false;
		reiniciar = false;
	}
	private void startTimer() {
		// TODO Auto-generated method stub
		timer = new Timer(100, this);
		timer.start();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
		if(System.getProperty("os.name").equals("Linux"))
			Toolkit.getDefaultToolkit().sync();
	}

	public void paint(Graphics g) {
		if(morreu) {
			g.setColor(new Color(28,28,28,100));
			g.fillRect(-100, -100, 800, 800);
			g.setFont(new Font("Arial", Font.PLAIN, 50)); 
			g.setColor(Color.RED);
			g.drawString("Faleceu!", 100, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 15)); 
			g.setColor(Color.RED);
			g.drawString("Aperte ENTER para reiniciar ...!", 90, 230);
		}else {
			//Pinta fundo
			g.setColor(new Color(100,250,200));
			g.fillRect(-100, -100, 800, 800);
			
			//Pinta pontos
			g.setFont(new Font("Arial", Font.BOLD, 20)); 
			g.setColor(Color.WHITE);
			g.drawString("Pontos: "+pontos, 260, 20);
			
			//Pinta Snake
			for (int i = 0; i < tamanho; i++) {
				if(i==0) {
					g.setColor(new Color(34,139,34));
					g.fillOval(xSnake[i]*20, ySnake[i]*20, 20, 20);
				}else {
					g.setColor(new Color(0,100,0));
					g.fillOval(xSnake[i]*20, ySnake[i]*20, 20, 20);
					g.setColor(new Color(0,255,0));
					g.fillOval(xSnake[i]*20+1, ySnake[i]*20+1, 18, 18);
					g.setColor(new Color(34,139,34));
					g.fillOval(xSnake[i]*20+4, ySnake[i]*20+1, 12, 12);
				}
			}
			
			g.setColor(Color.RED);
			g.fillOval(comidaX*20, comidaY*20, 20, 20);
			g.setColor(Color.pink);
			g.fillOval(comidaX*20+4, comidaY*20+4, 12, 12);
		}
	}

	private void update() {
		
		if(morreu==true && reiniciar==true) {//Reinicia o jogo caso o jogadore tenha perdido e aperte espaço para reiniciar
			iniciar();
		}
		
		if(morreu) //Não atualiza o jogo caso tenha morrido
			return;
		
		int xAntigo = xSnake[0];
		int yAntigo = ySnake[0];
		switch (snakeDirection) {
		case 0://cima
			ySnake[0]-=1;
			if(ySnake[0]<0)
				morreu = true;
			break;
		case 1://baixo
			ySnake[0]+=1;
			if(ySnake[0]>19)
				morreu = true;
			break;
		case 2://direita
			xSnake[0]+=1;
			if(xSnake[0]>19)
				morreu=true;
			break;
		case 3://esquerda
			xSnake[0]-=1;
			if(xSnake[0]<0)
				morreu=true;
			break;
		default:
			break;
		}
		
		if(xSnake[0]==comidaX && ySnake[0]==comidaY) {
			int[] novaPosicaopComida = getNovaPosicaoComida();
			comidaX = novaPosicaopComida[0];
			comidaY = novaPosicaopComida[1];
			tamanho++;
			pontos++;
		}
		
		for (int i = 1; i < tamanho; i++) {
			int xAntigo2 = xSnake[i];
			int yAntigo2 = ySnake[i];
			xSnake[i]=xAntigo;
			ySnake[i]=yAntigo;
			xAntigo=xAntigo2;
			yAntigo=yAntigo2;
		}
		
		if(verificarColisaoComCorpo()) {
			morreu=true;
		}
		
		blocked=false;
	}
	
	private int[] getNovaPosicaoComida() {
		int[] xPossivel = new int[500];
		int[] yPossivel = new int[500];
		int quantidade = 0;
		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if(!verificarColisaoComCobra(i, j)) {
					xPossivel[quantidade]=i;
					yPossivel[quantidade]=j;
					quantidade++;
				}
			}
		}
		Random random = new Random();
		int rIndex = random.nextInt(quantidade);
		int[] ret = {xPossivel[rIndex],yPossivel[rIndex]}; 
		return ret;
	}
	private boolean verificarColisaoComCorpo() {
		for (int i = 1; i < tamanho; i++) {
			if(xSnake[0]==xSnake[i] && ySnake[0]==ySnake[i])
				return true;
		}
		return false;
	}
	
	private boolean verificarColisaoComCobra(int x, int y) {
		for (int i = 0; i < tamanho; i++) {
			if(x==xSnake[i] && y==ySnake[i])
				return true;
		}
		return false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(blocked && morreu==false)
			return;
			
		int keyCode = e.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_UP:
			if(snakeDirection!=0 && snakeDirection!=1)
				snakeDirection = 0;
			break;
		case KeyEvent.VK_DOWN:
			if(snakeDirection!=0 && snakeDirection!=1)
				snakeDirection = 1;
			break;
		case KeyEvent.VK_RIGHT:
			if(snakeDirection!=2 && snakeDirection!=3)
				snakeDirection = 2;
			break;
		case KeyEvent.VK_LEFT:
			if(snakeDirection!=2 && snakeDirection!=3)
				snakeDirection = 3;
			break;
		case KeyEvent.VK_ENTER:
			if(morreu)
				reiniciar = true;
			break;

		default:
			break;
		}
		blocked = true;

	}
	@Override
	public void keyReleased(KeyEvent e) {

	}
}
