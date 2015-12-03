import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class SimulationDisplay extends JPanel implements InterfacePanelDelegate {

    /** The serial primary incrementer used to control simulation progress. Incremented once per frame.*/
    private long tickCount = 0;

    /** The parent JFrame to this panel */
    private JFrame parent;

    /** a bouncy ball for testing */
    private Ball ball;

    /** The main GISDisplay which controls displaying the map image*/
    private GISDisplay gisDisplay;

    /** The main interface panel, for selecting interface parameters */
    private InterfacePanel interfacePanel;



    public SimulationDisplay(JFrame parent){
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
     * The main runloop. Instantiates a new timer which controls the speed of the run loop.
     */
    private void run(){
        //// Begin the runtime loop
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

    public void update(){

        // TODO: Update each fish based on the tick count
        ball.update();


    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast the g object to a g2d for better performance
        Graphics2D g2d = (Graphics2D)g;

        //// Paint the window here
        g2d.setColor(new Color(254, 51, 86));

        // TODO: Draw each fish to the display by calling Fish.x and Fish.y on the FishManager.getFishes()

        //// Draw the cached map data
        g2d.drawImage(gisDisplay.getMapImage(), 0, 0, null);

        //// Draw the bouncy ball (for fun)
        Image img1 = Toolkit.getDefaultToolkit().getImage(tickCount % 60 > 30 ? "./data/fish.png" : "./data/fish_down.png");
        g2d.drawImage(img1, (int) ball.x, (int) ball.y, null);

        g2d.drawString("So long, and thanks for all the fish!", 100, 100);
        //g2d.fillOval((int)ball.x, (int)ball.y, ball.diameter, ball.diameter);


    }

    /**
     * Returns the tick count
     */
    public long getTickCount(){
        return this.tickCount;
    }


    /** Updates the model. Delegate method called by our GISDisplay object */
    @Override
    public void updateModel() {
        // TODO: update model parameters based on interface panel settings

        System.out.println(interfacePanel.getNumberOfAgents());
    }
}
