package org.lambico.datatest;

import java.util.Properties;

import org.lambico.datatest.DataAggregator;

/**
 * A loader for a set of data.
 */
public interface DatasetLoader {
    DataAggregator load();    
    DataAggregator load(Properties properties);    
}
