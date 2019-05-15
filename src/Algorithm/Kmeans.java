package Algorithm;

import Vector.*;
import Vector.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Kmeans {

    static public List<Cluster> clusterList;
    static public Set<Vector> vectorSet;
    static int k;
    static private List<Cluster> clustersToCompare;
    static Scanner scanner = new Scanner(System.in);
    static double totalDistances = 0;

    public static void main(String[] args) {
        vectorSet = readDataSet("/Users/tobeurdeath/Desktop/untitled/prog/NAI/NAI4/src/data/train.txt");
        System.out.println("Enter your k: ");
        k = scanner.nextInt();
        scanner.close();
        clusterList = initClusters(k);
        clusterise(clusterList, vectorSet);
        update(vectorSet, clusterList);
    }

    private static List<Cluster> initClusters(int k) {

        List<Cluster> clusters = new ArrayList<Cluster>();
        for (int i = 0; i < k; i++) {
            clusters.add(new Cluster(i));
        }
        return clusters;
    }

    private static Set<Vector> readDataSet(String path) {
        BufferedReader reader;
        Set<Vector> resultSet = new HashSet<>();
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                int i;
                String vectorClass;
                String[] lineSplit = line.split(",");
                List<Double> vectorValue = new ArrayList<>();
                for (i = 0; i < lineSplit.length - 1; i++) {
                    vectorValue.add(Double.parseDouble(lineSplit[i]));
                }
                vectorClass = lineSplit[i];
                Vector vector = new Vector(vectorValue, vectorClass);
                resultSet.add(vector);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void clusterise(List<Cluster> clusters, Set<Vector> vectors) {
        for (Vector v : vectors) {
            v.setCluster(clusters.get(getRandom(clusters.size())));
            System.out.println(v.getCluster());
        }
    }

    private static int getRandom(int max) {
        return (int) (Math.random() * max);
    }

    public static void update(Set<Vector> vectors, List<Cluster> clusters) {

        do {
            System.out.println("================================");

            getPurity();

            clustersToCompare = vectors.stream()
                    .map(vector -> vector.getCluster())
                    .collect(Collectors.toList());

            for (Cluster c : clusters) {
                if ((vectors.stream().filter(v -> v.getCluster().get_clusterNumber() == c.get_clusterNumber()).count() != 0)) {
                    c.set_centroid(calculateCentroid(vectors.stream()
                            .filter(v -> v.getCluster().get_clusterNumber() == c.get_clusterNumber())
                            .collect(Collectors.toList())));
                }
            }
            findAndSetClosestCluster(clusters, vectors);
            getTotalDistance(vectors, clusters);
        } while (!vectors.stream().map(vector -> vector.getCluster()).collect(Collectors.toList())
                .equals(clustersToCompare));
    }

    private static Vector calculateCentroid(List<Vector> vectors) {
            Vector centroid = new Vector(vectors.get(0).get_dimension());
            for (Vector v : vectors) {
                centroid = centroid.sum(v);
            }
            for (int i = 0; i < centroid.get_values().size(); i++) {
                centroid.get_values().set(i, (centroid.get_values().get(i) / vectors.size()));
            }
            return centroid;
    }

    private static void findAndSetClosestCluster(List<Cluster> clusters, Set<Vector> vectors) {
        double currClosest;
        int currClosestClusterNum = 0;

        for (Vector vector : vectors) {
            currClosest = Double.POSITIVE_INFINITY;
            for (Cluster cluster : clusters) {
                    double dist = vector.calculateDistance(cluster.get_centroid());
                    if (dist < currClosest) {
                        currClosest = dist;
                        currClosestClusterNum = cluster.get_clusterNumber();
                    }
            }
            int finalCurrClosestClusterNum = currClosestClusterNum;
            vector.setCluster(clusters.stream().filter(cluster -> cluster.get_clusterNumber() == finalCurrClosestClusterNum).findFirst().get());
        }
    }

    public static void getPurity() {
        List<String> classesNames = vectorSet.stream().map(vector -> vector.get_vectorClass()).distinct().collect(Collectors.toList());
        for (Cluster cluster : clusterList) {
            long totalCount = vectorSet.stream().filter(vector -> vector.getCluster().equals(cluster)).count();
            for (String className : classesNames) {
                long classCount = vectorSet.stream().filter(vector -> vector.getCluster().equals(cluster))
                        .filter(e -> e.get_vectorClass().equals(className)).count();
                System.out.println("In " + cluster + " there are " + classCount + " of " + className + " out of "
                        + totalCount + " items in this cluster, in percent it is " + ((double) classCount / totalCount) * 100 + " %");
            }
            System.out.println();
        }
    }
    public static void getTotalDistance(Set<Vector> vectors, List<Cluster> clusters)
    {
        totalDistances = 0;
        for (Cluster cluster: clusters) {
            for( Vector vector : vectors.stream().filter(v -> v.getCluster().get_clusterNumber()==cluster.get_clusterNumber()).collect(Collectors.toList())) {
                totalDistances += vector.calculateDistance(cluster.get_centroid());
            }
        }
        System.out.println("Total distances:  " + totalDistances);
    }
}