
public class Reach {

	private double[] source;
	private double[] sink;
	private double length;
	private Reach next;

	public double sourceX;
	public double sourceY;

	public double sinkX;
	public double sinkY;

	private int reachID;
	private int nextID;


	public Reach(double[] source, double[] sink, Reach next, int id) {
		super();
		this.source = source;
		this.sink = sink;
		this.next = next;
		this.length = Math.sqrt(Math.pow(sink[0] - source[0], 2) + Math.pow(sink[1] - source[1], 2));
	}

	public Reach(int reachID, double sourceX, double sourceY, double sinkX, double sinkY, int nextID){
		this.sourceX = sourceX;
		this.sourceY = sourceY;

		this.sinkX   = sinkX;
		this.sinkY   = sinkY;

		this.nextID  = nextID;
	}

	public int getReachID(){
		return this.reachID;
	}

	public double[] getSource() {
		return source;
	}

	public void setSource(double[] source) {
		this.source = source;
	}

	public double[] getSink() {
		return sink;
	}

	public void setSink(double[] sink) {
		this.sink = sink;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Reach getNext() {
		return next;
	}

	public void setNext(Reach next) {
		this.next = next;
	}


}
