package org.lambico.datatest.example1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.jpa.PersistenceUnitInfoImpl;
import org.lambico.datatest.junit.Dataset;

public class RudeJpaTest {
    private static EntityManagerFactory emf;

    @ClassRule
    public static Dataset dataset = new Dataset("org/lambico/datatest/example1/dataset/dataset.json");

    private EntityManager em;

    @BeforeClass
    public static void initTestClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        emf = buildEntityManagerFactory();
        populateTestDatabase();
    }

    private static void populateTestDatabase() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        for (Collection<?> entityList : dataset.getDataAggregator().getObjects().values()) {
            for (Object entity : entityList) {
                em.persist(entity);
            }
        }
        em.getTransaction().commit();
        em.close();
    }

    @AfterClass
    public static void destroyTestClass() {
        emf.close();
    }

    @Before
    public void initTest() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void destroyTest() {
        em.getTransaction();
        em.close();
    }

    @Test
    public void findObjects() throws IOException, ClassNotFoundException {
        TypedQuery<Entity1> query = em.createQuery("select e1 from Entity1 e1", Entity1.class);
        List<Entity1> results = query.getResultList();
        assertThat(results.size(), is(1));
        Entity1 entity1 = results.get(0);
        assertThat(entity1.getStringField(), is("test1"));
        Entity2 entity2 = entity1.getEntity2();
        assertThat(entity2.getStringField(), is("test2"));
        assertThat(entity2.getEntity1(), is(sameInstance(entity1)));
    }

    private static EntityManagerFactory buildEntityManagerFactory()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException, ClassNotFoundException {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo("testPU");
        PersistenceProvider provider = (PersistenceProvider) Class.forName(persistenceUnitInfo.getPersistenceProviderClassName()).getConstructor(new Class[] {}).newInstance(new Object[] {});
        return provider.createContainerEntityManagerFactory(persistenceUnitInfo, null);
    }

    private static PersistenceUnitInfo persistenceUnitInfo(String name) {
        Properties jpaProperties = jpaProperties();
        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(name, entityClassNames(),
                jpaProperties)
                .setNonJtaDataSource(dataSource(jpaProperties));
        return persistenceUnitInfo;
    }

    private static DataSource dataSource(Properties properties) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(properties.getProperty("javax.persistence.jdbc.driver"));
        result.setUrl(properties.getProperty("javax.persistence.jdbc.url"));
        result.setUsername(properties.getProperty("javax.persistence.jdbc.user"));
        result.setPassword(properties.getProperty("javax.persistence.jdbc.password"));
        return result;
    }

    private static Properties jpaProperties() {
        Properties result = new Properties();
        result.setProperty("javax.persistence.jdbc.driver", "org.h2.Driver");
        result.setProperty("javax.persistence.jdbc.url", "jdbc:h2:mem:test");
        result.setProperty("javax.persistence.jdbc.user", "username");
        result.setProperty("javax.persistence.jdbc.password", "password");
        result.setProperty("javax.persistence.schema-generation.database.action", "drop-and-create");
        result.setProperty("hibernate.show_sql", "true");
        return result;
    }

    private static Class<?>[] entities() {
        return new Class<?>[] { Entity1.class, Entity2.class };
    }

    private static List<String> entityClassNames() {
        return Arrays.asList(entities()).stream().map(Class::getName).collect(Collectors.toList());
    }

}
