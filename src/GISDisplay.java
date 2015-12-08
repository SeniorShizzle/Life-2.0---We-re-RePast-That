import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.MultiPointZShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PointShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


public class GISDisplay {

    /** The ArrayList of Reach objects represented by our GIS Data */
    private ArrayList<Reach> reaches = new ArrayList<>();

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    /** The cached map image to display */
    private BufferedImage cachedMapData;

    /** The map file to parse */
    private File mapFile;

    /** The singleton instance. Accessed through {@code GISDisplay.getInstance()} */
    private static GISDisplay instance;

    /** TRUE if the map data has been parsed */
    private boolean hasParsedMapData = false;

    /** Returns the shared instance of the GISDisplay class */
    public static GISDisplay getInstance(){
        if (instance == null) instance = new GISDisplay();

        return instance;
    }


    /**
     * Singleton GISDisplay should be accessed by calling {@code GISDisplay.getInstance()}
     */
    private GISDisplay(){
        // Private for singletondom
    }

    /**
     * Parses the GIS file data from a ".shp" file. Creates Reach objects.
     *
     * @param file the File object representing an ArcGIS Shape File
     * @throws Exception if the file data is corrupt or an improper file format is passed
     */
    public void parseMapFile(File file) throws Exception {

        this.mapFile = file;

        FileInputStream is = new FileInputStream(file);
        ShapeFileReader r = new ShapeFileReader(is);

        ShapeFileHeader h = r.getHeader();
        System.out.println("The shape type of this files is " + h.getShapeType());

        // Set the boundaries
        this.minX = h.getBoxMinX();
        this.minY = h.getBoxMinY();

        this.maxX = h.getBoxMaxX();
        this.maxY = h.getBoxMaxY();


        int total = 0;
        AbstractShape s; // this will be reused to create each item
        while ((s = r.next()) != null) {

            s.getHeader().getRecordNumber();

            switch (s.getShapeType()) {
                case POINT:
                    PointShape aPoint = (PointShape) s;
                    // Do something with the point shape...
                    break;
                case MULTIPOINT_Z:
                    MultiPointZShape aMultiPointZ = (MultiPointZShape) s;
                    // Do something with the MultiPointZ shape...
                    break;
                case POLYGON:
                    PolygonShape aPolygon = (PolygonShape) s;
                    System.out.println("I read a Polygon with "
                            + aPolygon.getNumberOfParts() + " parts and "
                            + aPolygon.getNumberOfPoints() + " points");
                    for (int i = 0; i < aPolygon.getNumberOfParts(); i++) {
                        PointData[] points = aPolygon.getPointsOfPart(i);
                        System.out.println("- part " + i + " has " + points.length
                                + " points.");
                    }
                    break;


                //// OUR PRIMARY CASE - Build the Reach objects here
                case POLYLINE:
                    PolylineShape aPolyline = (PolylineShape) s;
                    for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {
                        // Create each polyline
                        reaches.add(new Reach(aPolyline.getPointsOfPart(i), s.getHeader().getRecordNumber()));
                    }
                    break;

                default:
                    System.out.println("Read other type of shape.");
            }
            total++;
        }

        System.out.println("Total shapes read: " + total);

        hasParsedMapData = true;

        is.close(); // close the input stream
    }

    /**
     * assumptions: no forks towards the ocean, lines have consistent source to
     * sink order, they intersect, ignores ocean
     */
    private void sortReaches() {
        //TODO: Correct this method for using the new Reaches GIS style
         double x, y;
         for (int i = 0; i < reaches.size(); i++) {
            x = reaches.get(i).getSinkX();
            y = reaches.get(i).getSinkY();
             for (Reach reache : reaches) {
                 if (reache.getSourceX() == x && reache.getSourceY() == y) {
                     reaches.get(i).setNextID(reache.getReachID());

                 }

             }

        }

    }




    public BufferedImage getMapImage(){
        // If we haven't yet parsed the map data, don't attempt to create and cache the map image
        if (!hasParsedMapData) return null;

        if (cachedMapData != null) return cachedMapData; // it's already created and cached, lets return this

        // Let's create the buffered image to cache
        cachedMapData = new BufferedImage(Life.WINDOW_WIDTH * 2, Life.WINDOW_HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cachedMapData.createGraphics();

        // Set up the graphics context with a clear background color
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(179, 200, 153));
        g2d.fillRect(0, 0, Life.WINDOW_WIDTH * 2, Life.WINDOW_HEIGHT * 2);

        g2d.setColor(new Color(0, 60, 128));

        // Draw the line data to the graphics context
        for (Reach reach : reaches) {
            PointData[] points = reach.getPoints();

            for (int i = 0; i < points.length - 1; i++) {
                PointData point = points[i];
                PointData nextPoint = points[i+1];

                // Draw a line for each segment of the polyline
                g2d.drawLine(x(point.getX()), y(point.getY()), x(nextPoint.getX()), y(nextPoint.getY()));

            }
        }

        return cachedMapData;
    }

    /**
     * Normalizes an X coordinate from GIS space to window space.
     *
     * @param x the double X coordinate to be translated
     * @return the coordinate translated to the windowspace
     */
    private int x(double x){
        return (int) ((x - minX) * (Life.WINDOW_WIDTH / (maxX - minX)));

    }

    /**
     * Normalizes a Y coordinate from GIS space to window space.
     *
     * @param y the double Y coordinate to be translated
     * @return the coordinate translated to the windowspace
     */
    private int y(double y){
        return (int) (Life.WINDOW_HEIGHT - (y - minY) * (Life.WINDOW_HEIGHT / (maxY - minY)));
    }


    public ArrayList<Reach> getReaches(){
        return this.reaches;
    }


    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }


}
