package org.lambico.datatest.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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


    private static final String JSON_EXTENSION = ".json";
    /**
     * The name of the JSON resource from which loading the dataset.
     */
    private String datasetResource;


    /**
     * Load the dataset from a resource (folder/package) containing one or more json files.     *
     *
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load() {
        return load(null);
    }

    /**
     * Load the dataset from a resource (folder/package) containing one or more json files.     *
     *
     * @param properties At present it's not used.
     * @return The loaded dataset.
     */
    @Override
    public DataAggregator load(Properties properties) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        try {
            List<InputStream> streamList = getResourceFiles(this.datasetResource).stream()
                    .map(s -> Paths.get(this.datasetResource, s))
                    .filter(path -> !Files.isDirectory(path))
                    .map(Path::toString)
                    .filter(s -> s.endsWith(JSON_EXTENSION))
                    .map(this::getResourceAsStream)
                    .collect(Collectors.toList());
            HashMap<String, Object> merged = new HashMap<>();
            TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {};
            for (InputStream is : streamList) {
                String s = inputStreamToString(is);
                Map<String, Object> map = mapper.readValue(s, mapType);
                merged.putAll(map);
            }
            String valueAsString = mapper.writeValueAsString(merged);
            return mapper.readValue(valueAsString, DataAggregator.class);
        } catch (Exception e) {
            throw new RuntimeException("Issue while loading from:" + this.datasetResource, e);
        }
    }


    private String inputStreamToString(InputStream inputStream) throws Exception {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /*
     ***************** https://stackoverflow.com/a/3923685/379173
     */

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();
        try (InputStream in = getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;
            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }
        return filenames;
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
