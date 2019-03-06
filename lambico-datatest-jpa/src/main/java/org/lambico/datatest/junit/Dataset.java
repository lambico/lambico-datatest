package org.lambico.datatest.junit;

import org.junit.rules.ExternalResource;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;
import org.lambico.datatest.json.SingleJsonDatasetLoader;

public class Dataset extends ExternalResource {
    private String datasetResourceName;
    private DataAggregator dataAggregator;

    public Dataset(String datasetResourceName) {
        this.datasetResourceName = datasetResourceName;
    }

    @Override
    protected void before() throws Throwable {
        DatasetLoader loader =
            SingleJsonDatasetLoader.builder()
                .datasetResource(this.datasetResourceName)
                .build();
        this.dataAggregator = loader.load();
        super.before();
    }

    /**
     * @return the dataAggregator
     */
    public DataAggregator getDataAggregator() {
        return dataAggregator;
    }

}
