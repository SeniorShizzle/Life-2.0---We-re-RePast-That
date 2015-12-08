
public class Fish {
	
	private static double birthday;
	private static double age; 
	private static FishLifeState lifeState;
	private static double x;
	private static double y;
	private static double[] position = new double [2];

	public fish()
	
	public int consultLifeTable(ArrayList<Rule> rules){
		int state = 0; double rand;
		int currentState = FishLifeState.valueOf(this.getState().toString()).ordinal(); //returns the fish's state as int
		double currentAge = this.getBirthday() - RepastEssentials.GetTickCount(); 
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
	
public String[] getCSVInfo(){
    	
    	String tick = Double.toString(RepastEssentials.GetTickCount());
		String id = Integer.toString(this.ID);
//		String age;
//		if(RepastEssentials.GetTickCount() - this.birthday > 0){
		String age = (RepastEssentials.GetTickCount() - this.birthday > 0) ? Double.toString(RepastEssentials.GetTickCount() - this.birthday) :"0";
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


