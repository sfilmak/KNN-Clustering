package knn;

import java.util.ArrayList;
import java.util.Arrays;

public class Cluster {
    private final String name;
    private final int size;
    private double[] centroid;
    private ArrayList<Point> points;

    Cluster(String name, int size, double[] centroid) {
        this.name = name;
        this.size = size;
        this.centroid = centroid;
    }

    Cluster(String name, int size, ArrayList<Point> points) {
        this.name = name;
        this.size = size;
        this.points = points;
    }


    int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return size == cluster.size &&
                name.equals(cluster.name) &&
                Arrays.equals(centroid, cluster.centroid);
    }
}
