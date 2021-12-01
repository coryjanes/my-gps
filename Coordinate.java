public class Coordinate {
    private int vertex;
    private long latitude;
    private long longtitude;

    public Coordinate(int vertex, long latitude, long longtitude) {
        this.vertex = vertex;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongtitude() {
        return longtitude;
    }

    public int getVertex() {
        return vertex;
    }


}
