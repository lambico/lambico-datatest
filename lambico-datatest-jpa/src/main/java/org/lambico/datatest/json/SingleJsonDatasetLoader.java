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
