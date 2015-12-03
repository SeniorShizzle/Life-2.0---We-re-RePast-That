import java.util.ArrayList;


public class Fish {

	private int ID;
	private double birthday;
	private double age;
	private double x;
	private double y;
	private double [] position = new double [2];
	private double currentTick;
	private ArrayList<Rule> rules;
	
	private FishLifeState currentState;
	
	/**
	 * Life Stage enums
	 * @author De
	 *
	 */
	
	/**
	 * Fish constructor
	 * @param birthday
	 */
	public Fish(double birthday, int ID, ArrayList<Rule> rules){
		this.ID = ID;
		this.birthday = birthday;
//		this.age = currentTick - birthday;
		this.currentState = FishLifeState.EGG_INCUBATION;
		this.rules = rules;
	}
	
	/**
	 * Will call our state transition table and update
	 * fish's current state, and position.
	 * @param: ??
	 */
	public void update(long tick){
//		update position arr
//		update state
		this.currentTick = tick;
	}
	
	public int consultLifeTable(ArrayList<Rule> rules){
		int state = 0; double rand;
		int currentState = FishLifeState.valueOf(this.getState().toString()).ordinal(); //returns the fish's state as int
//		double currentAge = this.getBirthday() - RepastEssentials.GetTickCount(); 
		double currentAge = age;
		for (int j = 0; j < rules.size(); j++){
			rand = Math.random();
			if (rules.get(j).currentState == currentState && rules.get(j).maxAge > currentAge){
				for (int i = currentState; i < FishLifeState.values().length; i++){
					if (rand < rules.get(j).getTransitionProbMatrix()[i]){
						state = i;
						this.setLifeState(FishLifeState.values()[state]);
						break;
					} else {
						state = currentState;
						rand -= rules.get(j).transitionProbMatrix[i];
					}
				}
			}
		}
		return state;
	}

	public double getBirthday() {
		return birthday;
	}

	public void setLifeState(FishLifeState state) {
		this.currentState = state;
	}

	public FishLifeState getState() {
		return currentState;
	}
	
	/**
	 * Gets all info needed for CSV save file.
	 * @return all info as String Array
	 */
	public String[] getCSVInfo(){
    	
    	String tick = Double.toString(currentTick);
		String id = Integer.toString(this.ID);
		String age = Double.toString(this.age);
//		if(RepastEssentials.GetTickCount() - this.birthday > 0){
//		String age = (RepastEssentials.GetTickCount() - this.birthday > 0) ? Double.toString(RepastEssentials.GetTickCount() - this.birthday) :"0";
//		}
//		else{
//			age = Double.toString(0.0);
//		}
		String state = FishLifeState.valueOf(this.getState().toString()).toString();
		String location = "null";
		String distanceToOrigin = "null";
		String distanceToTerminal = "null";
		
		String info[] = {tick, id, age, state , location, distanceToOrigin, distanceToTerminal, "\n"};
		
		return info;
	}
	
}
