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
package org.lambico.datatest.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Load a dataset from a single JSON source.
 */
@Builder
public class SingleJsonDatasetLoader implements DatasetLoader {
    /**
     * The name of the JSON resource from which loading the dataset.
     */
    private String datasetResource;

    /**
     * Load the dataset from the single JSON resource.
     * 
     * @param properties At present it's not used.
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load(Properties properties) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        InputStream jsonSource = this.getClass().getClassLoader().getResourceAsStream(this.datasetResource);
        try {
            return mapper.readValue(jsonSource, DataAggregator.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't load dataset from " + this.datasetResource, e);
        }
    }

    /**
     * Load the dataset from the single JSON resource.
     * 
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load() {
        return load(null);
    }
    
}
