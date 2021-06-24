package com.example.simplematchmaker.match;

import com.example.simplematchmaker.user.User;
import com.example.simplematchmaker.user.UserRepo;
import com.example.simplematchmaker.kmean.Centroid;
import com.example.simplematchmaker.kmean.EuclideanDistance;
import com.example.simplematchmaker.kmean.KMeans;
import com.example.simplematchmaker.kmean.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private UserRepo repo;

    private long orderGroup = 0L;

    @Value("${groupSize:1}")
    private int groupSize;

    @Value("${maxIterations:1}")
    private int maxIterations;

    public void tryMatch() {
        List<User> users = repo.findAll();
        if (users.size() >= groupSize) {
            findGroups(users);
        }
    }

    private void findGroups(List<User> users) {
        List<UserRecord> records = users.stream()
                .map(UserRecord::new)
                .collect(Collectors.toList());
        KMeans.fit(records,
                records.size() / groupSize,
                new EuclideanDistance(),
                maxIterations,
                groupSize)
                .values()
                .forEach(this::finalizeGroupCreation);
    }

    private void finalizeGroupCreation(List<UserRecord> userRecords) {
        if (userRecords.size() == groupSize) {
            userRecords.forEach(UserRecord::calculateTimeSpent);
            printGroupResult(userRecords);

            repo.deleteAllById(
                    userRecords.stream()
                            .map(UserRecord::getUserName)
                            .collect(Collectors.toList())
            );
        }
    }

    private void printGroupResult(List<UserRecord> userRecords) {
        orderGroup++;
        Map<CriteriaName, Metric> groupMetric = calculateGroupMetric(userRecords
                .stream()
                .map(UserRecord::getCriteria)
                .collect(Collectors.toList())
        );
        System.out.println("%n--------------------------");
        System.out.println("new group has been create:");
        System.out.printf("GrId: %d%n", orderGroup);
        groupMetric.forEach((s, metric) -> {
            System.out.printf("%s - %s%n", s, metric.toString());
        });
        System.out.printf("Names: %s%n", userRecords
                .stream()
                .map(UserRecord::getUserName)
                .collect(Collectors.joining(", ")));
        }

    private Map<CriteriaName, Metric> calculateGroupMetric(List<Map<CriteriaName, Double>> criterias) {
        Map<CriteriaName, Metric> metricMap = new HashMap<>();

        for (CriteriaName k : CriteriaName.values()) {
            List<Double> values = criterias
                    .stream()
                    .map(stringDoubleMap -> stringDoubleMap.get(k))
                    .collect(Collectors.toList());
            metricMap.put(k, new Metric(values));
        }
        return metricMap;
    }


}
