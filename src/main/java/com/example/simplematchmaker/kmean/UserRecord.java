package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.match.CriteriaName;
import com.example.simplematchmaker.user.User;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class UserRecord {
    String userName;
    Instant requestTime;
    long timeSpent;
    Map<CriteriaName, Double> criteria;

    public UserRecord(User user) {
        this.userName = user.getName();
        this.requestTime = user.getRequestTime();
        criteria = new HashMap<>();
        criteria.put(CriteriaName.LATENCY, user.getLatency());
        criteria.put(CriteriaName.SKILL, user.getSkill());
    }

    public void calculateTimeSpent() {
        criteria.put(CriteriaName.SPENT_TIME, (double) (Instant.now().getEpochSecond() - this.requestTime.getEpochSecond()));
    }
}
