package com.example.simplematchmaker.match;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class Metric {
    Double max = 0.0;
    Double min = 0.0;
    Double avg = 0.0;

    public Metric(List<Double> values) {
        if (values != null) {
            this.max = getNonNullValuesStream(values).max().orElse(0.0);
            this.min = getNonNullValuesStream(values).min().orElse(0.0);
            this.avg = getNonNullValuesStream(values).average().orElse(0.0);
        }
    }

    private java.util.stream.DoubleStream getNonNullValuesStream(List<Double> values) {
        return values
                .stream()
                .filter(Objects::nonNull)
                .mapToDouble(aDouble -> aDouble);
    }

    @Override
    public String toString(){
        return String.format("max: %.2f min: %.2f avg: %.2f", this.max, this.min, this.avg) ;
    }
}
