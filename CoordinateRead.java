import java.io.FileInputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CoordinateRead {
    private int V;
    private Bag<Coordinate>[] coordinatesList;
    private long minLat = Long.MAX_VALUE, minLong = Long.MAX_VALUE;
    private long maxLat = Long.MIN_VALUE, maxLong = Long.MIN_VALUE;


    public CoordinateRead(FileInputStream input) {
        if (input == null) throw new IllegalArgumentException("argument is null");
        try {
            Scanner inputFile = new Scanner(input);
            boolean foundP = false;
            int index = 0;
            while (inputFile.hasNext()) {
                String[] curString = inputFile.nextLine().split(" ");
/*                for (int i = 0; i < curString.length; i++) {
                    System.out.print(curString[i] + i + " ");
                }
                System.out.println();*/
                char type = curString[0].charAt(0);
                if (type == 'p' && !foundP) {
                    V = Integer.parseInt(curString[4]);
                    coordinatesList = (Bag<Coordinate>[]) new Bag[V];
                    for (int v = 0; v < V; v++) {
                        coordinatesList[v] = new Bag<Coordinate>();
                    }
                    foundP = true;
                } else if (type == 'v') {
                    int tempV = Integer.parseInt(curString[1]);
                    long tempLat = Integer.parseInt(curString[2]);
                    if (tempLat < minLat) {
                        minLat = tempLat;
                    }
                    if (tempLat > maxLat) {
                        maxLat = tempLat;
                    }
                    long tempLong = Integer.parseInt(curString[3]);
                    if (tempLong < minLong) {
                        minLong = tempLong;
                    }
                    if (tempLong > maxLong) {
                        maxLong = tempLong;
                    }
                    coordinatesList[index].add(new Coordinate(tempV, tempLat, tempLong));
                    index++;
                } else if ((type == 'p') && (foundP)) {
                    throw new IllegalArgumentException("Another vector and edges input found");
                }
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in CoordinateRead constructor", e);
        }
    }

    public long getMinLat() {
        return minLat;
    }

    public long getMinLong() {
        return minLong;
    }

    public long getMaxLat() {
        return maxLat;
    }

    public long getMaxLong() {
        return maxLong;
    }

    public double scaleDownX(int index, double xScale) {
        return (coordinatesList[index].getElement().getLatitude() - this.getMinLat()) * xScale;
    }

    public double scaleDownY(int index, double yScale) {
        return (coordinatesList[index].getElement().getLongtitude() - this.getMinLong()) * yScale;
    }

    public double scaleDownX(int index, long minLat, double xScale) {
        return (coordinatesList[index].getElement().getLatitude() - minLat) * xScale;
    }

    public double scaleDownY(int index, long minLong, double yScale) {
        return (coordinatesList[index].getElement().getLongtitude() - minLong) * yScale;
    }

    public Coordinate getLongLat(int index) {
        return coordinatesList[index].getElement();
    }

    public int length() {
        return coordinatesList.length;
    }
}
