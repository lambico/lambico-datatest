package org.lambico.datatest.jpa;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import lombok.Builder;
import lombok.Singular;

/**
 * Implementation of a JPA PersistenceUnitInfo for programmatically build a
 * persistence unit.
 * 
 * Based on
 * https://vladmihalcea.com/how-to-bootstrap-jpa-programmatically-without-the-persistence-xml-configuration-file/
 */
@Builder
public class CustomizablePersistenceUnitInfo implements PersistenceUnitInfo {

    public static final String DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME = "org.hibernate.jpa.HibernatePersistenceProvider";
    public static final String DEFAULT_PERSISTENCE_UNIT_NAME = "defaultPU";
    public static final String DEFAULT_JPA_VERSION = "2.1";

    @Builder.Default
    private final String jpaVersion = DEFAULT_JPA_VERSION;

    @Builder.Default
    private final String persistenceUnitName = DEFAULT_PERSISTENCE_UNIT_NAME;

    private PersistenceUnitTransactionType transactionType;

    @Singular
    private final List<String> managedClassNames;

    @Singular
    private final List<String> mappingFileNames;

    @Builder.Default
    private Properties properties = new Properties();

    private final DataSource jtaDataSource;

    private final DataSource nonJtaDataSource;

    @Override
    public String getPersistenceUnitName() {
        return this.persistenceUnitName;
    }

    @Override
    public String getPersistenceProviderClassName() {
        return this.properties.getProperty("javax.persistence.provider", DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME);
    }

    @Override
    public PersistenceUnitTransactionType getTransactionType() {
        if (this.transactionType == null) {
            final String transactionTypeName = this.properties.getProperty("javax.persistence.transactionType",
                    "RESOURCE_LOCAL");
            this.transactionType = PersistenceUnitTransactionType.valueOf(transactionTypeName);
        }
        return this.transactionType;
    }

    @Override
    public DataSource getJtaDataSource() {
        return jtaDataSource;
    }

    @Override
    public DataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    @Override
    public List<String> getMappingFileNames() {
        return mappingFileNames;
    }

    @Override
    public List<URL> getJarFileUrls() {
        return Collections.emptyList();
    }

    @Override
    public URL getPersistenceUnitRootUrl() {
        return null;
    }

    @Override
    public List<String> getManagedClassNames() {
        return managedClassNames;
    }

    @Override
    public boolean excludeUnlistedClasses() {
        return false;
    }

    @Override
    public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.UNSPECIFIED;
    }

    @Override
    public ValidationMode getValidationMode() {
        return ValidationMode.AUTO;
    }

    @Override
    public String getPersistenceXMLSchemaVersion() {
        return this.jpaVersion;
    }

    @Override
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addTransformer(ClassTransformer transformer) {

    }

    @Override
    public ClassLoader getNewTempClassLoader() {
        return null;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    public static class CustomizablePersistenceUnitInfoBuilder {

        public CustomizablePersistenceUnitInfoBuilder nonJtaDataSource(DataSource nonJtaDataSource) {
            this.nonJtaDataSource = nonJtaDataSource;
            this.jtaDataSource = null;
            this.transactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
            return this;
        }

        public CustomizablePersistenceUnitInfoBuilder jtaDataSource(DataSource jtaDataSource) {
            this.jtaDataSource = jtaDataSource;
            this.nonJtaDataSource = null;
            this.transactionType = PersistenceUnitTransactionType.JTA;
            return this;
        }

    }
}