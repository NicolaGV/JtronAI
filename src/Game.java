import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game implements ActionListener, KeyListener {

	public static Game game;
	private Render rend;

	private JFrame frame;

	public final static int WIDTH = 40, HEIGHT = 25; // Include game borders
	public final static int NBROBSTACLES = 150;
	public final static int SC = 40;

	private Dimension screensize = new Dimension(SC * WIDTH, SC * HEIGHT);

	private Timer timer = new Timer(7, this);
	private int ticks = 0;
	
	private ArrayList<Point> obstacles = new ArrayList<>();
	private ArrayList<Point> obstaclesP1 = new ArrayList<>();
	private ArrayList<Point> obstaclesP2 = new ArrayList<>();

	private Player1AI player1;
	private Player2AI player2;

	private boolean player1End = false;
	private boolean player2End = false;

	// LEFT = 1, RIGHT = 2, UP = 3, DOWN = 4;
	private int dirP1;
	private int dirP2;

	public Game() {

		frame = new JFrame("Tron AI Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		rend = new Render();

		rend.setPreferredSize(screensize);
		frame.add(rend);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		startGame();
		timer.start();

	}

	public void startGame() {

		int randx = -1;
		int randy = -1;
		boolean pass;

		// Borders

		for (int n = 0; n < HEIGHT; n++) { // Left border
			obstacles.add(new Point(0, n));
		}

		for (int n = 1; n < WIDTH; n++) { // Up border
			obstacles.add(new Point(n, 0));
		}

		for (int n = 1; n < WIDTH; n++) { // Down border
			obstacles.add(new Point(n, HEIGHT - 1));
		}

		for (int n = 1; n < HEIGHT; n++) { // Right border
			obstacles.add(new Point(WIDTH - 1, n));
		}

		// Random obstacles

		for (int n = 0; n < NBROBSTACLES; n++) {

			pass = false;
			while (!pass) {

				pass = true;
				randx = (int) (Math.random() * (WIDTH - 1));
				randy = (int) (Math.random() * (HEIGHT - 1));

				for (Point point : obstacles) {
					if (randx == point.x && randy == point.y) {
						pass = false;
					}
				}
			}

			obstacles.add(new Point(randx, randy));
		}

		// Player 1

		pass = false;
		while (!pass) {
			pass = true;
			randx = (int) (Math.random() * (WIDTH - 1));
			randy = (int) (Math.random() * (HEIGHT - 1));

			for (Point point : obstacles) {
				if (randx == point.x && randy == point.y) {
					pass = false;
				}
			}
		}
		player1 = new Player1AI(new Point(randx, randy));

		// Player 2

		pass = false;
		while (!pass) {
			pass = true;
			randx = (int) (Math.random() * (WIDTH - 1));
			randy = (int) (Math.random() * (HEIGHT - 1));

			if (randx == player1.getPoint().x && randy == player1.getPoint().y) {
				pass = false;
			}

			for (Point point : obstacles) {
				if (randx == point.x && randy == point.y) {
					pass = false;
				}
			}
		}
		player2 = new Player2AI(new Point(randx, randy));

	}

	public static void main(String[] args) {
		game = new Game();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (ticks % 20 == 0) {
			player1.update();
			player2.update();
		}

		if (ticks % 20 == 10) {

			obstacles.add(new Point(player1.getPoint()));
			obstaclesP1.add(new Point(player1.getPoint()));

			obstacles.add(new Point(player2.getPoint()));
			obstaclesP2.add(new Point(player2.getPoint()));

			dirP1 = player1.getDirection();
			dirP2 = player2.getDirection();

			switch (dirP1) {

			case 1:
				player1.getPoint().x--;
				break;
			case 2:
				player1.getPoint().y--;
				break;
			case 3:
				player1.getPoint().x++;
				break;
			case 4:
				player1.getPoint().y++;
				break;
			}

			switch (dirP2) {

			case 1:
				player2.getPoint().x--;
				break;
			case 2:
				player2.getPoint().y--;
				break;
			case 3:
				player2.getPoint().x++;
				break;
			case 4:
				player2.getPoint().y++;
				break;
			}

		}

		if (ticks % 20 == 11) {

			for (Point point : obstacles) {

				if (player1.getPoint().x == point.x && player1.getPoint().getY() == point.y) {
					player1End = true;
				}

				if (player2.getPoint().x == point.x && player2.getPoint().getY() == point.y) {
					player2End = true;
				}
			}

			if (player1.getPoint().x == player2.getPoint().x && player1.getPoint().y == player2.getPoint().y) {
				player1End = true;
				player2End = true;
			}

			if (player2End || player1End) {

				if (player1.getPoint().x == player2.getPoint().x && player1.getPoint().y == player2.getPoint().y) {
					System.out.println("Draw!");
				} else if (player1End && player2End) {
					System.out.println("Draw");
				} else if (player1End) {
					System.out.println("Red wins!");
				} else {
					System.out.println("Blue wins!");
				}
				timer.stop();
			}

		}

		rend.repaint();
		ticks++;

	}

	@SuppressWarnings("serial")
	class Render extends JPanel {
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, SC * Game.WIDTH, SC * Game.HEIGHT);

			for (Point point : obstacles) {

				g.setColor(Color.GRAY);

				for (Point pointP1 : obstaclesP1) {
					if (point.x == pointP1.x && point.y == pointP1.y)
						g.setColor(Color.BLUE);
				}

				for (Point pointP2 : obstaclesP2) {
					if (point.x == pointP2.x && point.y == pointP2.y)
						g.setColor(Color.RED);
				}

				g.fillRect(SC * point.x, SC * point.y, SC, SC);
			}

			g.setColor(Color.BLUE);
			if (player1End) {
				g.setColor(Color.YELLOW);
			}
			g.fillRect(SC * player1.getPoint().x, SC * player1.getPoint().y, SC, SC);

			g.setColor(Color.RED);
			if (player2End) {
				g.setColor(Color.YELLOW);
			}
			g.fillRect(SC * player2.getPoint().x, SC * player2.getPoint().y, SC, SC);

		}
	}

	public static ArrayList<Point> getObstacles() {

		return Game.game.obstacles;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public Point getPlayer1() {
		Point p = new Point(player1.getPoint());
		return p;
	}
	
	public Point getPlayer2() {
		Point p = new Point(player2.getPoint());
		return p;
	}
	


}