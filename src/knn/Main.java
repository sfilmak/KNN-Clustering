package knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final ArrayList<Point> trainingPoints = new ArrayList<>();
    private static final ArrayList<Cluster> clusterPoints = new ArrayList<>();
    private static final ArrayList<Centroid> centroids = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello! Please, enter number of clusters:");
        int numberOfClusters = scanner.nextInt();

        readFile();

        //Print all the points
        printPoints();

        //Divide into clusters
        divideIntoClusters(numberOfClusters);

       //Iterate over the clusters
        learning(numberOfClusters);
    }

    private static Point createPoint(String[] splittedInformation) {
        double[] doubles = new double[splittedInformation.length];
        for(int m = 0; m < doubles.length; m++) {
            doubles[m] = Double.parseDouble(splittedInformation[m]);
        }
        return new Point(doubles);
    }

    private static void readFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data/train.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] splittedInformation = line.split(",");
                for(int i = 0; i < splittedInformation.length; i = i + splittedInformation.length) {
                    trainingPoints.add(createPoint(splittedInformation));
                }
            }
        }
    }

    private static double[] getCentroid(ArrayList<Point> clusterPoints, int size) {
        double[] centroid = new double[clusterPoints.get(0).getArray().length];

        for (Point clusterPoint : clusterPoints) {
            for (int m = 0; m < centroid.length; m++) {
                //  clusterPoints.get(i).getArray()[m];
                centroid[m] = centroid[m] + clusterPoint.getArray()[m];
            }
        }

        for (int m = 0; m < centroid.length; m++) {
            centroid[m] = centroid[m]/size;
        }

        for (Point clusterPoint : clusterPoints) {
            clusterPoint.setCentroid(centroid);
        }
        return centroid;
    }

    private static double distanceFromCentroid(Point point, double[] centroid) {
        double distance = 0.0;
        for(int i = 0; i < centroid.length; i++) {
            distance = Math.pow((point.getArray()[i] - centroid[i]), 2);
        }
        return distance;
    }

    private static void printPoints() {
        for (Point trainingPoint : trainingPoints) {
            System.out.println(Arrays.toString(trainingPoint.getArray()));
        }
    }

    private static void divideIntoClusters(int numberOfClusters) {

        //Number of segments
        int numberOfSegments = trainingPoints.size() / numberOfClusters;
        int remaining = trainingPoints.size() - (numberOfSegments * numberOfClusters);
        //To know from which index in original array we need to start
        int currentStep = 0;

        System.out.println("Number of segments: " + numberOfSegments + " | " + remaining);

        //Divide into clusters
        for (int i = 0; i < numberOfClusters; i++) {
            ArrayList<Point> points = new ArrayList<>();

            if (i == numberOfClusters - 1) {
                //Last iteration
                System.err.println("LAST");
                numberOfSegments = numberOfSegments + remaining;
            }

            for (int m = 0; m < numberOfSegments; m++) {
                points.add(trainingPoints.get(currentStep));
                currentStep += 1;
            }
            clusterPoints.add(new Cluster("Cluster " + i, numberOfSegments, points));
        }
    }

    private static void learning(int numberOfClusters) {
        int isEquals = 0;
        double[] isDifferent = new double[numberOfClusters];
        double[] defaultValues = new double[numberOfClusters];

        do{
            int iterationDifference = 0;
            //Calculate centroid
            for (Cluster cluster : clusterPoints) {
                double[] centoid = getCentroid(cluster.getPoints(), cluster.getSize());
                centroids.add(new Centroid(cluster.getName(), centoid));
                System.err.println(Arrays.toString(centoid));
                cluster.setCentroid(centoid);
            }

            //Calculate distances from points to centroids
            for (Cluster cluster : clusterPoints) {
                String newCentroidName = null;
                double[]  newCentroidValues = new double[0];
                int size = 0;

                for (int z = 0; z < cluster.getPoints().size(); z++) {
                    //2. Calculate parameters and save info about smallest centroid
                    double smallestDistance = 10000;
                    double[] newCentroid = new double[0];
                    double[] currentValues = cluster.getPoints().get(z).getArray();
                    for (Centroid centroidEntry : centroids) {
                        double i = distanceFromCentroid(cluster.getPoints().get(z), centroidEntry.getValues());
                        //System.out.println("Disatnce from point " + Arrays.toString(cluster.getPoints().get(z).getArray()) + " to centroid " + centroidEntry.getName() + " is " + i);
                        if (i < smallestDistance) {
                            smallestDistance = i;
                            newCentroid = centroidEntry.getValues();

                            newCentroidName = centroidEntry.getName();
                            newCentroidValues = centroidEntry.getValues();
                            size = cluster.getSize();
                        }
                    }
                    //3. Remove point from old centroid
                    cluster.getPoints().remove(z);

                    //4. Add new point with to a new cluster
                    Cluster newCluster = new Cluster(newCentroidName, size, newCentroidValues);

                    Point point = new Point(currentValues);
                    point.setCentroid(newCentroid);
                    int indexOfNewCluster = clusterPoints.indexOf(newCluster);
                    if(indexOfNewCluster != -1) {
                        clusterPoints.get(indexOfNewCluster).getPoints().add(point);
                    } else {
                        cluster.getPoints().add(point);
                    }
                }
                System.out.println("Size: " + cluster.getPoints().size());
                isDifferent[iterationDifference] = cluster.getPoints().size();
                iterationDifference++;
            }

            if(Arrays.equals(isDifferent, defaultValues)) {
                isEquals++;
            } else {
                isEquals = 0;
            }

            if(isEquals > 10) {
                break;
            }

            defaultValues = isDifferent;
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        } while (true);
    }

}
