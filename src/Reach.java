
public class Reach {

	private double[] source;
	private double[] sink;
	private double length;
	private Reach next;
	
	public Reach(double[] source, double[] sink, Reach next) {
		super();
		this.source = source;
		this.sink = sink;
		this.next = next;
		this.length = Math.sqrt(Math.pow(sink[0] - source[0], 2) + Math.pow(sink[1] - source[1], 2));
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
