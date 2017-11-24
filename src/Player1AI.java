import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class Player1AI { // Blue

	private int LEFT = 1, UP = 2, RIGHT = 3, DOWN = 4;
	private int direction;
	private int WIDTH, HEIGHT, NBROBSTACLES;

	int sumLeft, sumUp, sumRight, sumDown;

	private Point me;

	int[][] distanceMapMe;

	boolean pass;

	public Player1AI(Point p) {

		WIDTH = Game.WIDTH;
		HEIGHT = Game.HEIGHT;
		NBROBSTACLES = Game.NBROBSTACLES;
		me = new Point(p.x, p.y);

		distanceMapMe = new int[WIDTH][HEIGHT];
	}

	public void update() {

		// Start

		boolean[] freeQuad = quadFree(me); // Free comments (Left, Up, Right, Down)
		
		int[] testTurns = new int[4];
		for (int n = 0; n < testTurns.length; n++) {
			testTurns[n] = 0;
		}

		Point newMe = me;
		ArrayList<Point> newObstacles = Game.getObstacles();
		
		for (int n = 0; n < freeQuad.length; n++) {

			newObstacles.add(me);

			// Assign new initial position to testPlayer
			if (freeQuad[n]) {

				switch (n) {
				case 1:
					newMe.x--;
					break;
				case 2:
					newMe.y--;
					break;
				case 3:
					newMe.x++;
					break;
				case 4:
					newMe.y++;
					break;
				}
			}	

			// Test the testPlayer

			boolean cont = true;
			while (cont) {

				testTurns[n]++;
				boolean[] quadFree = quadFree(newMe, newObstacles);

				int dir = 0;

				if (!quadFree[0])
					dir = 1;
				if (dir == 1 && !quadFree[1])
					dir = 2;
				if (dir == 2 && !quadFree[2])
					dir = 3;

				// Update position and obstacles
				
				switch (dir) {

				case 0:
					newMe.x--;
					break;
				case 1:
					newMe.y--;
					break;
				case 2:
					newMe.x++;
					break;
				case 3:
					newMe.y++;
					break;
				}

				for (Point q : newObstacles) {
					if (newMe.x == q.x && newMe.y == q.y)
						cont = false;
				}
				
				newObstacles.add(newMe);
			}

		}

		for (int n = 0; n < testTurns.length; n++) {
			System.out.println(testTurns[n]);
		}
		direction = getMaxPosition(testTurns) + 1;
		// Finish

	}

	// Return free spaces in quad around position

	private boolean[] quadFree(Point p) {

		boolean[] freeQuad = new boolean[4]; // Left, Up, Right, Down
		for (int n = 0; n < freeQuad.length; n++) {
			freeQuad[n] = true;
		}

		for (Point q : Game.getObstacles()) {

			if (p.x - 1 == q.x && p.y == q.y) {
				freeQuad[0] = false;
			}

			if (p.x == q.x && p.y - 1 == q.y) {
				freeQuad[1] = false;
			}

			if (p.x + 1 == q.x && p.y == q.y) {
				freeQuad[2] = false;
			}

			if (p.x == q.x && p.y + 1 == q.y) {
				freeQuad[3] = false;
			}

		}

		return freeQuad;
	}

	private boolean[] quadFree(Point p, ArrayList<Point> obstacles) {

		boolean[] freeQuad = new boolean[4]; // Left, Up, Right, Down
		for (int n = 0; n < freeQuad.length; n++) {
			freeQuad[n] = true;
		}

		for (Point q : obstacles) {

			if (p.x - 1 == q.x && p.y == q.y) {
				freeQuad[0] = false;
			}

			if (p.x == q.x && p.y - 1 == q.y) {
				freeQuad[1] = false;
			}

			if (p.x + 1 == q.x && p.y == q.y) {
				freeQuad[2] = false;
			}

			if (p.x == q.x && p.y + 1 == q.y) {
				freeQuad[3] = false;
			}

		}

		return freeQuad;
	}

	private int getMaxPosition(int[] arr) {
		int max = arr[0];
		int pos = 0;
		for (int n = 1; n < arr.length; n++) {
			if (arr[n] > max) {
				max = arr[n];
				pos = n;
			}
		}
		return pos;
	}

	// Accessed by Game

	public Point getPoint() {
		return me;
	}

	public int getDirection() {
		return direction;
	}
}
