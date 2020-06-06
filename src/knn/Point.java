package knn;

import java.util.Arrays;

public class Point {

    private final double[] array;
    private double[] centroid;

    Point(double[] array) {
        this.array = array;
    }

    double[] getArray() {
        return array;
    }

    void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Arrays.equals(array, point.array) &&
                Arrays.equals(centroid, point.centroid);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(array);
        result = 31 * result + Arrays.hashCode(centroid);
        return result;
    }
}