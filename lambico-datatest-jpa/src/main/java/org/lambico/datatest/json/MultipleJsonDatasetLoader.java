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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Load multiple dataset from a specific location.
 */
@Builder
public class MultipleJsonDatasetLoader implements DatasetLoader {

    /**
     * The name of the JSON resource from which loading the dataset.
     */
    private String datasetResourcePath;


    /**
     * Load the dataset from a resource (folder/package) containing one or more json files.          *
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load() {
        return load(null);
    }

    /**
     * Load the dataset from a resource (folder/package) containing one or more json files.
     * @param properties At present it's not used.
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load(Properties properties) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        JsonScanner jsonScanner = JsonScanner.getInstance();
        try {
            List<String> list = jsonScanner.listFiles(this.datasetResourcePath);
            List<InputStream> streamList = list.stream()
                    .map(this::getResourceAsStream)
                    .collect(Collectors.toList());
            HashMap<String, Object> merged = new HashMap<>();
            TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {
            };
            for (InputStream is : streamList) {
                String s = inputStreamToString(is);
                Map<String, Object> map = mapper.readValue(s, mapType);
                merged.putAll(map);
            }
            String valueAsString = mapper.writeValueAsString(merged);
            return mapper.readValue(valueAsString, DataAggregator.class);
        } catch (Exception e) {
            throw new RuntimeException("Issue while loading from:" + this.datasetResourcePath, e);
        }
    }

    /*
     ***** Internal methods *****
     */

    private String inputStreamToString(InputStream inputStream) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);
        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
