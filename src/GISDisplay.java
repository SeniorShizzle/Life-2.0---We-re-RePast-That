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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GISDisplay {

    private ArrayList<Reach> reaches = new ArrayList<>();

    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    /** The cached map image to display */
    private BufferedImage cachedMapData;

    /** The map file to parse */
    private File mapFile;

    private static GISDisplay instance;

    /** TRUE if the map data has been parsed */
    private boolean hasParsedMapData = false;

    public static GISDisplay getInstance(){
        if (instance == null) instance = new GISDisplay();

        return instance;
    }


    /**
     * Singleton GISDisplay should be accessed by calling {@code GISDisplay.getInstance()}
     */
    @Deprecated
    private GISDisplay(){
        // Private for singletondom
    }

    public void parseMapFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) scanner.nextLine(); // the first line is header information

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] vals = line.replace(" ", "").split(","); // clear whitespace
                if (vals.length != 6) continue; // improperly formatted line

                int id         = Integer.parseInt(vals[0]);
                double sourceX = Double.parseDouble(vals[1]);
                double sourceY = Double.parseDouble(vals[2]);
                double sinkX   = Double.parseDouble(vals[3]);
                double sinkY   = Double.parseDouble(vals[4]);
                int nextID     = Integer.parseInt(vals[5]);


                reaches.add(new Reach(id, sourceX, sourceY, sinkX, sinkY, nextID));
            }

            hasParsedMapData = true;

        } catch (Exception e){

            System.out.println(e);
            hasParsedMapData = false;
        }
        finally {
            setBounds();
            sortReaches();

            try {brokenMethod();} catch (Exception e){}
        }


    }

    private void brokenMethod() throws Exception{
        FileInputStream is = new FileInputStream("./data/reaches_edt.shp");
        ShapeFileReader r = new ShapeFileReader(is);

        ShapeFileHeader h = r.getHeader();
        System.out.println("The shape type of this files is " + h.getShapeType());

        // Set the boundaries
        this.minX = h.getBoxMinX();
        this.minY = h.getBoxMinY();

        this.maxX = h.getBoxMaxX();
        this.maxY = h.getBoxMaxY();

        int total = 0;
        AbstractShape s;
        while ((s = r.next()) != null) {

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
                    // System.out.println("I read a Polyline with " + aPolyline.getNumberOfParts() + " parts and "  + aPolyline.getNumberOfPoints() + " points");
                    for (int i = 0; i < aPolyline.getNumberOfParts(); i++) {

                        // Create each polyline
                        PointData[] points = aPolyline.getPointsOfPart(i);

                        // System.out.println("- part " + i + " has " + points.length + " points.");
                    }
                    break;
                default:
                    System.out.println("Read other type of shape.");
            }
            total++;
        }

        System.out.println("Total shapes read: " + total);

        is.close();
    }

    /**
     * assumptions: no forks towards the ocean, lines have consistent source to
     * sink order, they intersect, ignores ocean
     */
    private void sortReaches() {
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

    /** Sets the bounds by iterating over the reach objects. Deprecated if using ArcMap GIS data */
    @Deprecated
    private void setBounds(){
        for (Reach reach : reaches){
            if (reach.sourceX < minX){
                minX = (int)reach.sourceX;
                continue;
            }
            if (reach.sourceX > maxX){
                maxX = (int)reach.sourceX;
                continue;
            }
            if (reach.sourceY < minY){
                minY = (int)reach.sourceY;
                continue;
            }
            if (reach.sourceY > maxY){
                maxY = (int)reach.sourceY;
                continue;
            }


            if (reach.sinkX < minX) {
                minX = (int)reach.sinkX;
                continue;
            }
            if (reach.sinkX > maxX) {
                maxX = (int)reach.sinkX;
                continue;
            }
            if (reach.sinkY < minY) {
                minY = (int)reach.sinkY;
                continue;
            }
            if (reach.sinkY > maxY) {
                maxY = (int)reach.sinkY;
            }
        }

        // the boundaries should be set now
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
            // TODO: Normalize the coordinates to the windowspace and manage conversion to integers
            g2d.drawLine((int) reach.sourceX * 100, (int) reach.sourceY * 100, (int) reach.sinkX * 100, (int) reach.sinkY * 100);
        }

        return cachedMapData;
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
