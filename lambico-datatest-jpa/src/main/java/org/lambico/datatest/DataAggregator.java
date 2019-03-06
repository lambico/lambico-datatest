package org.lambico.datatest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.lambico.datatest.json.DataAggregatorDeserializer;

import lombok.Data;

/**
 * A container aggregating the data of different entities.
 * Usually it maps the entity names to the collection of the entity instances.
 */
@Data
@JsonDeserialize(using=DataAggregatorDeserializer.class)
public class DataAggregator {
    Map<String, Collection<?>> objects = new HashMap<>();
}