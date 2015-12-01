import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Life extends JFrame {

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 1000;

    private long tickCount = 0;

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

        // Display the window
        this.setVisible(true);



        //// Begin the runtime loop
        Timer timer = new Timer(60, ActionEvent -> {
            System.out.println("tick");


            // Update the tick counter
            tickCount++;

            // Run the update() subroutine
            update();

            // Repaint the main window
            repaint();


        });
        timer.start();

    }

    public void update(){
        System.out.println(this.tickCount);
    }

     




}
