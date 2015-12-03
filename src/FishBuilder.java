import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FishBuilder {
	Bag fishBag = new Bag();
	
	FileReader fishStateTransitionTable;
	List<String[]> allRows;
	ArrayList<Rule> ruleTable = new ArrayList<Rule>();
	
	InterfacePanel steveThing = InterfacePanel.getInstance();

	try {
		// parse the CSV
		// format: Life Stage, LS (stage ID#), January, February, ... December (double probabilities)
		// eg: "Spawning", 2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.68, 1.0, 0.0, 0.0

		fishStateTransitionTable = new FileReader(steveThing.getTransitionTable());
		allRows = new CSVReader(fishStateTransitionTable, CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, 1).readAll();
		LOGGER.log(Level.INFO, "" + allRows.size());

		// for each record
		// System.out.println(row);
		for (String[] row : allRows) {
			LOGGER.log(Level.INFO, row.toString());
			for (int i = 0; i < row.length; i++) row[i] = row[i].trim();
			
			int currentState = Integer.parseInt(row[0]);
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

}
}
