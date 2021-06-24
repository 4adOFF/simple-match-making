package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.match.CriteriaName;

import java.util.Map;

public interface Distance {
    double calculate(Map<CriteriaName, Double> f1, Map<CriteriaName, Double> f2);
}
