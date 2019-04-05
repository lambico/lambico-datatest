package org.lambico.datatest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.lambico.datatest.json.DataAggregatorDeserializer;

import lombok.Data;

/**
 * A container aggregating the data of different entities. Usually it maps the
 * entity names to the collection of the entity instances.
 */
@Data
@JsonDeserialize(using = DataAggregatorDeserializer.class)
public class DataAggregator {
    Map<String, Collection<?>> objects = new HashMap<>();

    public Set<Class<?>> getTypes() {
        Set<Class<?>> result = objects.keySet().stream().map(v -> {
            try {
                return Class.forName(v);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Error extracting data type", e);
            }
        }).collect(Collectors.toSet());
        return result;
    }
}