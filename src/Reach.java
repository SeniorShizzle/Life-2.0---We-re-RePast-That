import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;

import java.util.ArrayList;

public class Reach {

	//// OLD
	public double sourceX;
	public double sourceY;
	public double length;
	public double sinkX;
	public double sinkY;
	//// END OLD

	private final int reachID;
	private int nextID;

    /** An ArrayList of reaches that come after this one. Wow reaches */
    private ArrayList<Reach> nextReaches;


	/** A primitive array of PointData objects representing GIS data points as a polyline */
    private PointData[] points;


	public Reach(int reachID, double sourceX, double sourceY, double sinkX, double sinkY, int nextID) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;

		this.reachID = reachID;

		this.sinkX = sinkX;
		this.sinkY = sinkY;
		this.length = Math.sqrt(Math.pow(sinkX - sourceX, 2)
				+ Math.pow(sinkY - sourceY, 2)); //distance formula
		this.nextID = nextID;
	}


	/**
	 * Constructs a Reach object with the given points representing a polyline in GIS space, and a reachID, from the
	 * GIS data.
	 * @param points the array of {@code Point} in GIS space
	 * @param reachID the integer ID of the reach
	 */
	public Reach(PointData[] points, int reachID){
        this.reachID = reachID;
		this.points = points;
	}

	/** Returns the first point in the reach object */
	public PointData getSource(){
		return this.points[0];
	}

	/** Returns the last point in the reach object */
	public PointData getSink(){
		return this.points[points.length - 1];
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

	public int getNextID() {
		return nextID ;
	}

	public double getLength() {
		return length;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

    public PointData[] getPoints() {
        return this.points;
    }

    public ArrayList<Reach> getNextReaches() {
        return nextReaches;
    }
    
    public void addNextReach(Reach reach){
        this.nextReaches.add(reach);
    }
}
