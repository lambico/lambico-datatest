package org.lambico.datatest.junit;

import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.rules.ExternalResource;
import org.lambico.datatest.json.DataAggregator;

public class Dataset extends ExternalResource {
    private String datasetResourceName;
    private DataAggregator dataAggregator;

    public Dataset(String datasetResourceName) {
        this.datasetResourceName = datasetResourceName;
    }

    @Override
    protected void before() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        InputStream jsonSource = this.getClass().getClassLoader()
                .getResourceAsStream(this.datasetResourceName);
        this.dataAggregator = mapper.readValue(jsonSource, DataAggregator.class);
        super.before();
    }

    /**
     * @return the dataAggregator
     */
    public DataAggregator getDataAggregator() {
        return dataAggregator;
    }

}