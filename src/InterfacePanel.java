import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import java.util.ArrayList;

public class InterfacePanel extends JPanel {

    /**
     * This is the list of our delegate objects. We should notify all of them when appropriate
     */
    private ArrayList<InterfacePanelDelegate> delegates = new ArrayList<>();

    /**
     * {@code true} if the interface buttons have already been established on the User Panel
     */
    private boolean hasCreatedUserPanel = false;

    /**
     * The ArcGIS Shape file (.shp) containing map data in the form of rivers
     */
    private File mapFile;

    /**
     * The number of agents (e.g., fish) specified by the User Panel text field
     */
    private long numberOfAgents = 500;

    /**
     * The transition state table of rules as defined by JuicyJ™
     */
    private File transitionTable;

    /** The singleton instance of the InterfacePanel for access */
    private static InterfacePanel sharedInstance = new InterfacePanel();

    /** Returns the shared singleton instance of the InterfacePanel */
    public static InterfacePanel getInstance(){
        return sharedInstance;
    }

    /**
     * The singleton constructor is private. Please get the shared instance via {@link #getInstance()}
     */
    private InterfacePanel() {

        // this is the ultimate container
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setLayout(new GridLayout(5,1));
        setPreferredSize(new Dimension(500, 700));
        setMinimumSize(new Dimension(400, 600));


        JButton shapeFileButton = new JButton("Select Map File");
        JLabel fileName = new JLabel("No File Selected");

        // This file panel will contain the shape file button and file confirmation text
        JPanel filePanel = new JPanel();


        //// Handle the "shape file" selection
        shapeFileButton.addActionListener(ActionListener -> {

            JFileChooser juicyJ = new JFileChooser();

            juicyJ.setCurrentDirectory(new File("./")); // Set directory to Home
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "ArcGIS™ Shape Files", "shp", "zip");
            juicyJ.setFileFilter(filter); // Add the file filter to the chooser

            // Returns a constant value depending on if the user selected a file or not
            int result = juicyJ.showOpenDialog(filePanel);

            if (result == JFileChooser.APPROVE_OPTION) {
                // user selects a file
                mapFile = juicyJ.getSelectedFile();
                System.out.println("Selected file: " + mapFile.getAbsolutePath());
                fileName.setText(mapFile == null ? "No File Selected" : mapFile.getName());

                //// PARSE THE MAP FILE
                GISDisplay mapDisplay = GISDisplay.getInstance();
                try {
                    mapDisplay.parseMapFile(mapFile);
                } catch (Exception e){
                    System.out.println(e);
                    e.printStackTrace();
                }

                try { // Adding the checkboxes and reach lists

                    JPanel reachPanel = new JPanel(); // Create a new panel to fill the frame

                    // Actionlistener that gets called every time you click a button
//                    ActionListener reachListener = EventHandler.create(ActionListener.class, this, "reachSelected");
//                    ActionListen reachListener = new ActionListener();
                    reachPanel.setLayout(new GridLayout(GISDisplay.getInstance().getReaches().size() + 1, 1));

                    // Call to ReachBoxes automagically populates the ReachPanel with checkboxes
                    //TODO: Must redefine features
//                    ReachBoxes(GISDisplay.getInstance().getReaches(), reachPanel, reachListener, 0);
                    ReachBoxes(GISDisplay.getInstance().getReaches(), reachPanel, 0);


                    // NOTE: At this point, ReachPanel will be populated with children JCheckBox objects
                    // Add the Done button
                    JButton done = new JButton("Done");
                    done.setBounds(0, 0, 30, 10);
                    reachPanel.add(done);

                    // The JPanel is added to the ScrollPane, which automatically handles panning operations
                    JScrollPane scrollPane = new JScrollPane(reachPanel);
                    scrollPane.setPreferredSize(new Dimension(300, 300)); // Set the size of the scrolling pane
                    this.add(scrollPane); // Add the scroll pane to the InterfacePanel window


                    done.addActionListener(event -> {
                        SwingUtilities.getWindowAncestor(this).dispose(); // Closes the window!
                        System.out.println("Reaches selected.");
                    });


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {
                System.out.println("No file was selected.");
            }
            filePanel.repaint();

        });

        fileName.setText(mapFile == null ? "No File Selected" : mapFile.getName());
        filePanel.add(shapeFileButton);
        filePanel.add(fileName);

        // DAKOTA and STEVEN's Terrific Transition Table
        // "Fish State Transition Rules"

        JButton dakotaButton = new JButton("Select Transition Rules");
        JLabel  stevenLabel  = new JLabel("No.");

        JPanel stekotaPanel  = new JPanel();
        stekotaPanel.add(dakotaButton);
        stekotaPanel.add(stevenLabel);

        // The ActionListener for the button opens a "juicy" JFileChooser
        dakotaButton.addActionListener(ActionListener -> {
            JFileChooser juicyJ = new JFileChooser();
            File file = new File("./src/fish/stateProbabilities.csv");
            juicyJ.setCurrentDirectory(file);
            juicyJ.setSelectedFile(file);
            // juicyJ.setCurrentDirectory(new File(System.getProperty("user.home"))); // Set directory to Home
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "CSV Files", "csv", "txt");
            juicyJ.setFileFilter(filter); // Add the file filter to the chooser

            // Returns a constant value depending on if the user selected a file or not
            int result = juicyJ.showOpenDialog(filePanel);

            if (result == JFileChooser.APPROVE_OPTION) {
                // user selects a file. woohoo!
                transitionTable = juicyJ.getSelectedFile();
                System.out.println("Selected Transition Table: " + transitionTable.getAbsolutePath());
                stevenLabel.setText(transitionTable == null ? "No File Selected" : transitionTable.getName());

            } else {
                System.out.println("No transition table was selected.");
            }

            stekotaPanel.repaint();
        });


        //creates a drop-down menus for selecting different types of fish
        //needs an action listener, but I am not sure what the ultimate goal is so
        //I left it out for now
        String[] fishTypes = {"test1", "test2", "Rodger", "quail", "Oink etc."};
        JComboBox<String> fishListDropDown = new JComboBox<String>(fishTypes);
        fishListDropDown.setSelectedIndex(0);
        fishListDropDown.setEditable(true);
        fishListDropDown.setMaximumSize(new Dimension(300, 30));
        JLabel fishTypeLabel = new JLabel("What type of fish would you like to focus on?");


        String[] fishStages = FishLifeState.strings();
        JComboBox<String> fishStagesListDropDown = new JComboBox<>(fishStages);
        fishStagesListDropDown.setEditable(true);
        fishStagesListDropDown.setSelectedIndex(0);
        fishStagesListDropDown.setMaximumSize(new Dimension(300, 30));
        JLabel fishStageLabel = new JLabel("What type of life stage would you like to focus on?");


        // Select GIS button created, GIS file imported theoretically
        JLabel spawnLabel = new JLabel("Number of agents ");
        spawnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField spawnCountField = new JTextField("500", 5);
        spawnCountField.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnCountField.setMaximumSize(new Dimension(300, 30));
        JButton spawnEnterButton = new JButton("Set");
        spawnEnterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnEnterButton.addActionListener(ActionListener -> {
            // Save the value inside spawnCountField to numberOfAgents




            long tempAgentCount = 0;
            try {
                tempAgentCount = Long.parseLong(spawnCountField.getText());
            } catch (NumberFormatException e) {
                // The user did not enter a valid number. Prompt them to try again.
                JOptionPane.showMessageDialog(null, "Please enter a valid positive number");
            }
            if (tempAgentCount < 0 || tempAgentCount >= Long.MAX_VALUE) {
                JOptionPane.showMessageDialog(null, "Please enter a valid positive number less than 9 quintillion");
            } else {
                numberOfAgents = tempAgentCount;
            }

            for (InterfacePanelDelegate delegate : delegates) {
                delegate.updateModel();
            }
        });

        // Create the panel for the spawn count and set button
        JPanel spawnCountPanel = new JPanel();
        spawnCountPanel.setLayout(new BoxLayout(spawnCountPanel, BoxLayout.Y_AXIS));
        spawnCountPanel.add(spawnLabel);
        spawnCountPanel.add(spawnCountField);
        spawnCountPanel.add(spawnEnterButton);
        spawnCountPanel.setAlignmentX(Component.CENTER_ALIGNMENT);



        add(filePanel);
        add(stekotaPanel);
        add(spawnCountPanel);

        // Sincerest apologies to Jessie Jackson
        // RSApplication.getRSApplicationInstance().addCustomUserPanel(this);

        hasCreatedUserPanel = true;


    }

    /**
     * Creates checkboxes for each reach.
     *
     * @parameters Reach, ActionListener
     */
//    public void ReachBoxes(ArrayList<Reach> features, JPanel reachPanel, ActionListener listener, int i) {
    public void ReachBoxes(ArrayList<Reach> features, JPanel reachPanel, int i) {

        if (i >= features.size()) {
            return;
        }

        Reach reach = features.get(i);
        String id = "" + reach.getReachID();

        JCheckBox choice = new JCheckBox(id);
//        choice.addActionListener(listener);
        reachPanel.add(choice);
      
        choice.addActionListener(event -> {
          Fish.setInitialReach(features.get(i));
       });
        if (i < features.size()) {
//            ReachBoxes(features, reachPanel, listener, i + 1); // Recursive. Consider replacing with loop later
            ReachBoxes(features, reachPanel, i + 1); // Recursive. Consider replacing with loop later

        }
    }

   /**
    * The number of agents specified by the user. Defaults to 500
    */
    public long getNumberOfAgents() {
        return this.numberOfAgents;

    }

    /**
     * Returns a File object of the transition table, hopefully in CSV, but no guarantees.
     */
    public File getTransitionTable(){
        return transitionTable;
    }

    /**
     * Registers an additional delegate
     */
    public void registerDelegate(InterfacePanelDelegate delegate) {
        this.delegates.add(delegate);
    }

}
