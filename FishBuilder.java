import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

//CSV
//import au.com.bytecode.opencsv.CSVParser;
//import au.com.bytecode.opencsv.CSVReader;
//import au.com.bytecode.opencsv.CSVWriter;



public class FishBuilder {
	FileReader fishStateTransitionTable;
	List<String[]> allRows;
	ArrayList<Rule> ruleTable = new ArrayList<Rule>();
	
	InterfacePanel steveThing = InterfacePanel.getInstance();
	{

	try {
		// parse the CSV
		// format: Life Stage, LS (stage ID#), January, February, ... December (double probabilities)
		// eg: "Spawning", 2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.68, 1.0, 0.0, 0.0

		fishStateTransitionTable = new FileReader(steveThing.getTransitionTable());
//		CSVReader allRows = new CSVReader(fishStateTransitionTable, CsvParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1).readAll();
		CSVReader allRows = new CSVReader(fishStateTransitionTable);
		//		Logger.log(Level.INFO, "" + allRows.size());

		// for each record
		// System.out.println(row);
		for (String[] row : allRows) {
//			Logger.log(Level.INFO, row.toString());
			for (int i = 0; i < row.length; i++) row[i] = row[i].trim();
			
			int currentState = Integer.parseInt(row[0]);
			StdOut.println("Current State : " + currentState + "muhfucka");
			double maxAge = Double.parseDouble(row[1]);
			int targetState = Integer.parseInt(row[2]);
			double probability = Double.parseDouble(row[3]);
			
			// check for existing row (matching currentstate and maxage)
			Rule foundRule = null;
			for (Rule rule : ruleTable) {
				if (rule.getCurrentState() == currentState
					&& rule.getMaxAge() == maxAge
				) {
					// found it!
					foundRule = rule;
					break;
				}
			}
			// if we don't have a rule, make a new one
			if (foundRule == null) {
				foundRule = new Rule(currentState, maxAge, new double[5]);
				ruleTable.add(foundRule);
			}
			// insert/update the probability for this target state
			double[] transitionProbMatrix = foundRule.getTransitionProbMatrix();
			transitionProbMatrix[targetState] = probability;
			foundRule.setTransitionProbMatrix(transitionProbMatrix);				
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	// Create and display the user interface panel
	JFrame customInterfacePanel2 = new JFrame("Save Simulation");
    customInterfacePanel2.add(InterfacePanel.getInstance());
    JButton save = new JButton("Save");
    customInterfacePanel2.add(save);
    save.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e){
    		//TODO: Fix order of CSV File
    		Date now = new Date();
    		//Saves date format to the file name
    		SimpleDateFormat date = new SimpleDateFormat("E_yyyy.MM.dd_HH.mm");
    		String home = System.getProperty("user.home");
    		String csv = home + "\\Population_of_" + date.format(now) + ".csv";
    		try{
    			//creates CSV File
    		CSVWriter writer = new CSVWriter(new FileWriter(csv));
    		List<String[]> data = new ArrayList<String[]>();
    		//Adds Column labels to CSV
    		data.add(new String[] {"Current Tick" , "Fish ID", "Age", "State", "Location", "DFO", "DFT", "\n"});
    		//for each fish in the space
    		/*
    		for(Object obj : context){	
    		Fish f = (Fish) obj;
    		//interrogates fish for its info
    		data.add(f.getCSVInfo());
    		}
    		*/
    		writer.writeAll(data);
    		writer.close();
			}
    		catch(IOException error){
    			System.out.println(error.getLocalizedMessage());
    		}
    	}
    });
    customInterfacePanel2.pack();
    customInterfacePanel2.setVisible(true);

	
	Fish newFish = new Fish(ruleTable);

}

}

