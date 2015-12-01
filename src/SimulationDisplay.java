import javax.swing.*;
import java.awt.*;

public class SimulationDisplay extends JPanel {

    /** The serial primary incrementer used to control simulation progress. Incremented once per frame.*/
    private long tickCount = 0;

    /** The parent JFrame to this panel */
    JFrame parent;


    Ball ball;


    public SimulationDisplay(JFrame parent){
        this.parent = parent;

        this.ball = new Ball(100);

        run();
    }


    /**
     * The main runloop. Instantiates a new timer which controls the speed of the run loop.
     */
    private void run(){
        //// Begin the runtime loop
        Timer timer = new Timer(10, ActionEvent -> {
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
        g2d.drawLine(0,0,300,300);

        // TODO: Draw each fish to the display by calling Fish.x and Fish.y

        g2d.fillOval(ball.x, ball.y, ball.radius, ball.radius);

    }
}
