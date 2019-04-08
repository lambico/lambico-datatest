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
package org.lambico.datatest.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import javax.sql.DataSource;

public class DefaultDataSourceFactory implements DataSourceFactory {
    private static final String DATASOURCE_IMPLEMENTATION_CLASS_NAME_PROPERTY = "org.lambico.datatest.datasource.implementationClassName";
    private static final String DBCP2_BASIC_DATA_SOURCE_CLASS_NAME = "org.apache.commons.dbcp2.BasicDataSource";

    private DefaultDataSourceFactory() {
    }

    public static DataSourceFactory getFactory() {
        return new DefaultDataSourceFactory();
    }

    @Override
    public DataSource createDataSource(Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        DataSource result = null;
        String implementationClassName = properties.getProperty(DATASOURCE_IMPLEMENTATION_CLASS_NAME_PROPERTY,
                DBCP2_BASIC_DATA_SOURCE_CLASS_NAME);
        if (DBCP2_BASIC_DATA_SOURCE_CLASS_NAME.equals(implementationClassName)) {
            try {
                Class<?> clazz = Class.forName(implementationClassName);
                result = (DataSource) clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
                invokeSetter(clazz, result, "setDriverClassName", properties.getProperty("javax.persistence.jdbc.driver", "org.h2.Driver"));
                invokeSetter(clazz, result, "setUrl", properties.getProperty("javax.persistence.jdbc.url", "jdbc:h2:mem:defaultDb"));
                invokeSetter(clazz, result, "setUsername", properties.getProperty("javax.persistence.jdbc.user", "username"));
                invokeSetter(clazz, result, "setPassword", properties.getProperty("javax.persistence.jdbc.password", "password"));
            } catch (Exception e) {
                throw new IllegalArgumentException("Can't create DBCP2 BasicDataSource", e);
            }
        } else {
            throw new IllegalArgumentException("Unknow DataSource implementation: "+implementationClassName);
        }
        return result;
    }

    private void invokeSetter(Class<?> clazz, Object instance, String methodName, String value)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        Method method = clazz.getMethod(methodName, String.class);
        method.invoke(instance, value);
    }
}