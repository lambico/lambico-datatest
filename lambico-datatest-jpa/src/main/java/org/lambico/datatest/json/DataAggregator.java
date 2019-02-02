package org.lambico.datatest.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

@Data
@JsonDeserialize(using=DataAggregatorDeserializer.class)
public class DataAggregator {
    Map<String, Collection<?>> objects = new HashMap<>();
}