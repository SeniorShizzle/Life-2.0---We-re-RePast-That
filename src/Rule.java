
public class Rule {

	double maxAge;
	public double[] transitionProbMatrix;
	int currentState;
	
	
	/**
	 * Constructor for the transition matrix
	 * */
	public Rule(int current, double maxAge, double ... transitionProbMatrix){
		this.currentState = current;
		this.maxAge = maxAge;
		this.transitionProbMatrix = transitionProbMatrix;
	}
	

	public double getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(double maxAge) {
		this.maxAge = maxAge;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int current) {
		this.currentState = current;
	}


	public double[] getTransitionProbMatrix() {
		return transitionProbMatrix;
	}


	public void setTransitionProbMatrix(double[] transitionProbMatrix) {
		this.transitionProbMatrix = transitionProbMatrix;
	}
}
