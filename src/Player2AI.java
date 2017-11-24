import java.awt.Point;
import java.util.ArrayList;

public class Player2AI {

	private int LEFT = 1, UP = 2, RIGHT = 3, DOWN = 4;
	private int direction;
	private int WIDTH, HEIGHT, NBROBSTACLES;

	private Point me;

	boolean noLeft, noUp, noRight, noDown;

	public Player2AI(Point p) {

		WIDTH = Game.WIDTH;
		HEIGHT = Game.HEIGHT;
		NBROBSTACLES = Game.NBROBSTACLES;
		me = new Point(p.x, p.y);
	}

	public void update() {

		// Start

		noLeft = false;
		noUp = false;
		noRight = false;
		noDown = false;

		for (Point p : Game.getObstacles()) {

			if (me.x - 1 == p.x && me.y == p.y) {
				noLeft = true;
			}

			if (me.x == p.x && me.y - 1 == p.y) {
				noUp = true;
			}

			if (me.x + 1 == p.x && me.y == p.y) {
				noRight = true;
			}

			if (me.x == p.x && me.y + 1 == p.y) {
				noDown = true;
			}
		}

		direction = LEFT;
		if (noLeft)
			direction = UP;
		if (direction == UP && noUp)
			direction = RIGHT;
		if (direction == RIGHT && noRight)
			direction = DOWN;

		// Finish

	}

	public Point getPoint() {
		return me;
	}

	public void setPoint(int x, int y) {
		me.x = x;
		me.y = y;
	}

	public int getDirection() {
		return direction;
	}
}
