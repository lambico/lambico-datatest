package org.lambico.datatest.jpa;

import java.util.Properties;

import javax.sql.DataSource;

public interface DataSourceFactory {
    DataSource createDataSource(Properties properties);
}