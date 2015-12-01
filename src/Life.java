import javax.swing.*;
import java.awt.*;

public class Life extends JFrame {

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 1000;

    /** The main simulation used by the window. */
    private SimulationDisplay mainDisplay;


    public static void main(String args[]){
        new Life().run();
    }


    /**
     * Instantiates and displays the main window. Sets up the window parameters, and initiates the run loop.
     */
    public void run(){
        System.out.println("We're aboard!");

        // Set the window parameters
        this.setTitle("Life 2.0 — We're RePast That™");
        this.setBackground(new Color(31, 31, 31));
        this.setResizable(false);

        // Set the size of the window
        this.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // Add the simulation display to the frame
        mainDisplay = new SimulationDisplay(this);
        this.add(mainDisplay);
        mainDisplay.setBounds(this.getBounds());

        // Display the window
        this.setVisible(true);
    }




}
