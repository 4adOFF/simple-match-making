package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

class KMeansTest {

    @Test
    void findGroupWithOneUnnecessaryUserTest() {
        int groupSize = 5;
        List<UserRecord> sourceRecords = new ArrayList<>();
        UserRecord ur1 = new UserRecord(new User("name4", 0.5,0.5, Instant.now()));
        UserRecord ur2 = new UserRecord(new User("name6", 2.2,2.2, Instant.now()));
        sourceRecords.add(new UserRecord(new User("name1", 0.1,0.1, Instant.now())));
        sourceRecords.add(new UserRecord(new User("name2", 0.6,0.6, Instant.now())));
        sourceRecords.add(new UserRecord(new User("name3", 0.9,0.9, Instant.now())));
        sourceRecords.add(ur1);
        sourceRecords.add(new UserRecord(new User("name5", 0.7,0.7, Instant.now())));
        sourceRecords.add(ur2);

        Map<Centroid, List<UserRecord>> result = KMeans.fit(sourceRecords, sourceRecords.size() / groupSize,
                new EuclideanDistance(),
                10,
                groupSize);
        List<UserRecord> group = result.values().stream().filter(records -> records.size() == 5).findAny().orElse(Collections.emptyList());

        Assertions.assertFalse(group.isEmpty());
        Assertions.assertFalse(group.contains(ur2));
        Assertions.assertTrue(group.contains(ur1));
    }

    @Test
    void findGroupWithManyUnnecessaryUsersTest() {
        int groupSize = 5;
        List<UserRecord> sourceRecords = new ArrayList<>();
        UserRecord ur1 = new UserRecord(new User("name4", 100.5,0.5, Instant.now()));
        UserRecord ur2 = new UserRecord(new User("name6", 200.2,2.2, Instant.now()));
        UserRecord ur3 = new UserRecord(new User("name7", 2.2,2.2, Instant.now()));
        UserRecord ur4 = new UserRecord(new User("name8", 300.2,2.2, Instant.now()));
        sourceRecords.add(new UserRecord(new User("name1", 100.1,0.1, Instant.now())));
        sourceRecords.add(new UserRecord(new User("name2", 100.6,0.6, Instant.now())));
        sourceRecords.add(new UserRecord(new User("name3", 100.9,0.9, Instant.now())));
        sourceRecords.add(new UserRecord(new User("name5", 100.7,0.7, Instant.now())));
        sourceRecords.add(ur1);
        sourceRecords.add(ur2);
        sourceRecords.add(ur3);
        sourceRecords.add(ur4);

        Map<Centroid, List<UserRecord>> result = KMeans.fit(sourceRecords, sourceRecords.size() / groupSize,
                new EuclideanDistance(),
                10,
                groupSize);
        List<UserRecord> group = result.values().stream().filter(records -> records.size() == 5).findAny().orElse(Collections.emptyList());

        Assertions.assertFalse(group.isEmpty());
        Assertions.assertFalse(group.contains(ur2));
        Assertions.assertFalse(group.contains(ur3));
        Assertions.assertFalse(group.contains(ur4));
        Assertions.assertTrue(group.contains(ur1));
    }
}