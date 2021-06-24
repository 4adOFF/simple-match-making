package com.example.simplematchmaker.kmean;

import com.example.simplematchmaker.match.CriteriaName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Centroid {

    private final Map<CriteriaName, Double> coordinates;

}
