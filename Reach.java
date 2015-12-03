public class Reach {

	public double sourceX;
	public double sourceY;
	public double length;
	public double sinkX;
	public double sinkY;

	private int reachID;
	private int nextID;

	public Reach(int reachID, double sourceX, double sourceY, double sinkX,
			double sinkY, int nextID) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;

		this.sinkX = sinkX;
		this.sinkY = sinkY;
		this.length = Math.sqrt(Math.pow(sinkX - sourceX, 2)
				+ Math.pow(sinkY - sourceY, 2)); //distance formula
		this.nextID = nextID;
	}

	public double getSourceX() {
		return sourceX;
	}

	public void setSourceX(double sourceX) {
		this.sourceX = sourceX;
	}

	public double getSourceY() {
		return sourceY;
	}

	public void setSourceY(double sourceY) {
		this.sourceY = sourceY;
	}

	public double getSinkX() {
		return sinkX;
	}

	public void setSinkX(double sinkX) {
		this.sinkX = sinkX;
	}

	public double getSinkY() {
		return sinkY;
	}

	public void setSinkY(double sinkY) {
		this.sinkY = sinkY;
	}

	public int getReachID() {
		return reachID;
	}

	public void setReachID(int reachID) {
		this.reachID = reachID;
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

	public double getLength() {
		return length;
	}


}
