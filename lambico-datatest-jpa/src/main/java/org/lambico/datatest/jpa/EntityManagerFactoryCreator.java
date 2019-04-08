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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import lombok.Builder;
import lombok.Singular;

public class EntityManagerFactoryCreator {

    @Builder
    public static EntityManagerFactory newEntityManagerFactory(String persistenceUnitName, Properties jpaProperties,
            List<String> entityClassNames, @Singular List<Class<?>> entities, DataSource nonJtaDataSource, PersistenceUnitInfo persistenceUnitInfo) {
        if (jpaProperties == null) {
            jpaProperties = new Properties();
        }
        if (persistenceUnitInfo == null) {
            if (persistenceUnitName == null) {
                persistenceUnitName = CustomizablePersistenceUnitInfo.DEFAULT_PERSISTENCE_UNIT_NAME;
            }
            if (entityClassNames == null) {
                entityClassNames = new ArrayList<>();
            }
            if (entities != null) {
                entityClassNames
                        .addAll(entities.stream().map(Class::getName).collect(Collectors.toList()));
            }
            if (nonJtaDataSource == null) {
                nonJtaDataSource = DefaultDataSourceFactory.getFactory().createDataSource(jpaProperties);
            }
            persistenceUnitInfo = CustomizablePersistenceUnitInfo.builder()
                    .persistenceUnitName(persistenceUnitName).managedClassNames(entityClassNames)
                    .nonJtaDataSource(nonJtaDataSource).build();
        }
        PersistenceProvider provider = null;
        try {
            provider = (PersistenceProvider) Class.forName(persistenceUnitInfo.getPersistenceProviderClassName())
                    .getConstructor(new Class[] {}).newInstance(new Object[] {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't create persistence provider.", e);
        }
        return provider.createContainerEntityManagerFactory(persistenceUnitInfo, jpaProperties);
    }

    public static class EntityManagerFactoryBuilder {

        EntityManagerFactoryBuilder() {
            this.jpaProperties = new Properties();
            jpaProperty("javax.persistence.schema-generation.database.action", "drop-and-create");
            jpaProperty("hibernate.enable_lazy_load_no_trans", "true");
            jpaProperty("hibernate.event.merge.entity_copy_observer", "allow");
        }

        public EntityManagerFactoryBuilder jpaProperty(Object key, Object value) {
            this.jpaProperties.put(key, value);
            return this;
        }
    }

}