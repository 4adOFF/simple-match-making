package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.match.CriteriaName;

import java.util.Map;

public class EuclideanDistance implements Distance {

    @Override
    public double calculate(Map<CriteriaName, Double> f1, Map<CriteriaName, Double> f2) {
        double sum = 0;
        for (CriteriaName key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);

            if (v1 != null && v2 != null) {
                sum += Math.pow(v1 - v2, 2);
            }
        }

        return Math.sqrt(sum);
    }
}
