package knn;

public class Centroid {

    private final String name;
    private final double[] values;

    Centroid(String name, double[] values) {
        this.values = values;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    double[] getValues() {
        return values;
    }
}
