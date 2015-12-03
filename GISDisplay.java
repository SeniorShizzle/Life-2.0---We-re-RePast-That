import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GISDisplay {

    private ArrayList<Reach> reaches = new ArrayList<>();


    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    private BufferedImage cachedMapData;


    public GISDisplay(){
        // do stuff here
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

        } catch (Exception e){
            System.out.println(e);
        }
        finally {
            setBounds();
        }


    }

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


    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }


}