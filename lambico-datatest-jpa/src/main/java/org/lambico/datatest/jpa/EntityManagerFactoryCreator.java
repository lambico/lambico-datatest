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
            jpaProperty("hibernate.archive.autodetection", "true");
        }

        public EntityManagerFactoryBuilder jpaProperty(Object key, Object value) {
            this.jpaProperties.put(key, value);
            return this;
        }
    }

}