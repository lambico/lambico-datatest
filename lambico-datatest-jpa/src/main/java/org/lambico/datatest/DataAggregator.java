/**
 * Copyright © 2018 The Lambico Datatest Team (lucio.benfante@gmail.com)
 *
 * This file is part of lambico-datatest-jpa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lambico.datatest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.lambico.datatest.json.DataAggregatorDeserializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A container aggregating the data of different entities. Usually it maps the
 * entity names to the collection of the entity instances.
 */
@Data
@JsonDeserialize(using = DataAggregatorDeserializer.class)
public class DataAggregator {
    Map<String, Collection<?>> objects = new HashMap<>();

    public Set<Class<?>> getTypes() {
        return objects.keySet().stream().map(v -> {
            try {
                return Class.forName(v);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Error extracting data type", e);
            }
        }).collect(Collectors.toSet());
    }
}