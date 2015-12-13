import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SimulationDisplay extends JPanel implements InterfacePanelDelegate {

	/**
	 * The serial primary incrementer used to control simulation progress.
	 * Incremented once per frame.
	 */
	private long tickCount = 0;

	/** The parent JFrame to this panel */
	private JFrame parent;

	/** a bouncy ball for testing */
	private Ball ball;

	/** The main GISDisplay which controls displaying the map image */
	private GISDisplay gisDisplay;

	/** The main interface panel, for selecting interface parameters */
	private InterfacePanel interfacePanel;

	/** The ArrayList of fish objects */
	private ArrayList<Fish> fishies = new ArrayList<>();

	public SimulationDisplay(JFrame parent) {
		this.parent = parent;

		this.ball = new Ball(70);

		gisDisplay = GISDisplay.getInstance();

		interfacePanel = InterfacePanel.getInstance();
		interfacePanel.registerDelegate(this);

		JFrame fishHell = new JFrame("Simulation Parameters");
		fishHell.add(interfacePanel);
		fishHell.pack();

		fishHell.setVisible(true);
		fishHell.toFront();

		run();
	}

	/**
	 * The main runloop. Instantiates a new timer which controls the speed of
	 * the run loop.
	 */
	private void run() {
		// // Begin the runtime loop
		Timer timer = new Timer(17, ActionEvent -> {
			// Each iteration of the timer:

				// Update the tick counter
				tickCount++;

				// Run the update() subroutine
				this.update();

				// Repaint the main display
				this.repaint();

			});

		// Start the timer
		timer.start();
	}

	public void update() {

		// TODO: Update each fish based on the tick count
		ball.update();
		for (Fish fishy : fishies) {
			fishy.update(this.tickCount);
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Cast the g object to a g2d for better performance
		Graphics2D g2d = (Graphics2D) g;

		// // Paint the window here
		g2d.setColor(new Color(254, 51, 86));

		// TODO: Draw each fish to the display by calling Fish.x and Fish.y on
		// the FishManager.getFishes()

		// // Draw the cached map data
		g2d.drawImage(gisDisplay.getMapImage(), 0, 0, null);

		// // Draw the bouncy ball (for fun)
		Image img1 = Toolkit.getDefaultToolkit().getImage(
				tickCount % 60 > 30 ? "./data/fish.png"
						: "./data/fish_down.png");
		g2d.drawImage(img1, (int) ball.x, (int) ball.y, null);

		g2d.drawString("So long, and thanks for all the fish!", 100, 100);
		// g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);

		Image fishImg = Toolkit.getDefaultToolkit().getImage(
				tickCount % 60 > 30 ? "./data/fish_small.png"
						: "./data/fish_small_down.png");
		for (Fish fishy : fishies) {
			g2d.drawImage(fishImg, x(fishy.x), y(fishy.y), null);
		}

	}

	/**
	 * - * Normalizes an X coordinate from GIS space to window space. - * - * @param
	 * x the double X coordinate to be translated - * @return the coordinate
	 * translated to the windowspace -
	 */
	private int x(double x) {
		return (int) ((x - gisDisplay.getMinX()) * (Life.WINDOW_WIDTH / (gisDisplay
				.getMaxX() - gisDisplay.getMinX())));
	}

	/**
	 * Normalizes a Y coordinate from GIS space to window space.
	 *
	 * @param y
	 *            the double Y coordinate to be translated
	 * @return the coordinate translated to the windowspace
	 */
	private int y(double y) {
		return (int) (Life.WINDOW_HEIGHT - (y - gisDisplay.getMinY())
				* (Life.WINDOW_HEIGHT / (gisDisplay.getMaxY() - gisDisplay
						.getMinY())));
	}

	/**
	 * /** Returns the tick count
	 */
	public long getTickCount() {
		return this.tickCount;
	}

	/** Updates the model. Delegate method called by our GISDisplay object */
	@Override
	public void updateModel() {
		// TODO: update model parameters based on interface panel settings

		System.out.println(interfacePanel.getNumberOfAgents());

		for (int i = 0; i < 100; i++) {
			ArrayList<Rule> rules = new ArrayList<Rule>();
			rules = FishBuilder.ruleTable;
			fishies.add(new Fish(rules));
		}
	}
}
