package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.match.CriteriaName;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class KMeans {

    private static final Random random = new Random();

    public static Map<Centroid, List<UserRecord>> fit(List<UserRecord> records,
                                                      int k,
                                                      Distance distance,
                                                      int maxIterations,
                                                      int groupSize) {
        List<Centroid> centroids = randomCentroids(records, k);
        Map<Centroid, List<UserRecord>> clusters = new HashMap<>();
        Map<Centroid, List<UserRecord>> lastState = new HashMap<>();

        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            for (UserRecord record : records) {
                Centroid centroid = nearestCentroid(record, centroids, distance);
                assignToCluster(clusters, record, centroid);
            }

            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        lastState.forEach((centroid, records1) -> trimExtremum(centroid, records1, groupSize));

        return lastState;
    }

    private static List<Centroid> randomCentroids(List<UserRecord> records, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<CriteriaName, Double> maxs = new HashMap<>();
        Map<CriteriaName, Double> mins = new HashMap<>();

        for (UserRecord record : records) {
            record.getCriteria().forEach((key, value) -> {
                maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
            });
        }

        Set<CriteriaName> attributes = records.stream()
                .flatMap(e -> e.getCriteria().keySet().stream())
                .collect(toSet());
        for (int i = 0; i < k; i++) {
            Map<CriteriaName, Double> coordinates = new HashMap<>();
            for (CriteriaName attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(coordinates));
        }

        return centroids;
    }

    private static Centroid nearestCentroid(UserRecord record, List<Centroid> centroids, Distance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(record.getCriteria(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    private static void assignToCluster(Map<Centroid, List<UserRecord>> clusters,
                                        UserRecord record,
                                        Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(record);
            return list;
        });
    }

    private static Centroid average(Centroid centroid, List<UserRecord> records) {
        if (records == null || records.isEmpty()) {
            return centroid;
        }

        Map<CriteriaName, Double> average = centroid.getCoordinates();
        records.stream().flatMap(e -> e.getCriteria().keySet().stream())
                .forEach(k -> average.put(k, 0.0));

        for (UserRecord record : records) {
            record.getCriteria().forEach(
                    (k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue)
            );
        }

        average.forEach((k, v) -> average.put(k, v / records.size()));

        return new Centroid(average);
    }

    private static List<Centroid> relocateCentroids(Map<Centroid, List<UserRecord>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(toList());
    }

    private static void trimExtremum(Centroid centroid, List<UserRecord> userRecords, int groupSize) {
        if (userRecords.size() > groupSize) {
            Map<UserRecord, Double> distanceMap = new HashMap<>();
            for (UserRecord ur: userRecords) {
                distanceMap.put(ur, new EuclideanDistance().calculate(centroid.getCoordinates(),ur.getCriteria()));
            }
            while (userRecords.size() > groupSize) {
                UserRecord maxDistance = Collections.max(distanceMap.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();
                userRecords.remove(maxDistance);
                distanceMap.remove(maxDistance);
            }
        }
    }
}
