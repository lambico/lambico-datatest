package org.lambico.datatest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Field;

import org.junit.Test;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.json.MultipleJsonDatasetLoader;
import org.lambico.datatest.json.SingleJsonDatasetLoader;

public class DatasetLoaderFactoryTest {

    private static final String A_TEST_RESOURCE_JSON = "a/test/resource.json";
    private static final String A_TEST_RESOURCE_PATH = "a/test/resource/path";

    @Test
    public void testCreateDefaultLoader()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        DatasetLoader loader = DatasetLoaderFactory.create();
        assertThat(loader, is(instanceOf(MultipleJsonDatasetLoader.class)));
        String datasetResourcePath = (String) getFieldValue(loader, "datasetResourcePath");
        assertThat(datasetResourcePath, is(invokerPackageWithSlashes()));
    }

    @Test
    public void testCreateLoaderPassingNull() {
        DatasetLoader loader = DatasetLoaderFactory.create((TestData) null);
        assertThat(loader, is(notNullValue()));
        assertThat(loader, is(instanceOf(MultipleJsonDatasetLoader.class)));
    }

    @Test
    @TestData
    public void testCreateLoaderWithAnnotationDefaults()
            throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
        TestData testData = getTestDataAnnotationOfMethod("testCreateLoaderWithAnnotationDefaults");
        DatasetLoader loader = DatasetLoaderFactory.create(testData);
        assertThat(loader, is(notNullValue()));
        assertThat(loader, is(instanceOf(MultipleJsonDatasetLoader.class)));
        String datasetResourcePath = (String) getFieldValue(loader, "datasetResourcePath");
        assertThat(datasetResourcePath, is(invokerPackageWithSlashes()));
    }

    @Test
    @TestData(resources = A_TEST_RESOURCE_JSON)
    public void testCreateSingleJsonLoader()
            throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
        TestData testData = getTestDataAnnotationOfMethod("testCreateSingleJsonLoader");
        assertThat(testData, is(notNullValue()));
        DatasetLoader loader = DatasetLoaderFactory.create(testData);
        assertThat(loader, is(notNullValue()));
        assertThat(loader, is(instanceOf(SingleJsonDatasetLoader.class)));
        String datasetResource = (String) getFieldValue(loader, "datasetResource");
        assertThat(datasetResource, is(A_TEST_RESOURCE_JSON));
    }

    @Test
    @TestData(resourcePaths = A_TEST_RESOURCE_PATH)
    public void testCreateMultipleJsonLoader()
            throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException {
        TestData testData = getTestDataAnnotationOfMethod("testCreateMultipleJsonLoader");
        assertThat(testData, is(notNullValue()));
        DatasetLoader loader = DatasetLoaderFactory.create(testData);
        assertThat(loader, is(notNullValue()));
        assertThat(loader, is(instanceOf(MultipleJsonDatasetLoader.class)));
        String datasetResourcePath = (String) getFieldValue(loader, "datasetResourcePath");
        assertThat(datasetResourcePath, is(A_TEST_RESOURCE_PATH));
    }

    @Test
    public void testDefaultResourcePath() {
        String defaultResourcePath = DatasetLoaderFactory.defaultResourcePath();
        assertThat(defaultResourcePath, is(invokerPackageWithSlashes()));
    }

    @Test
    @TestData(resources = A_TEST_RESOURCE_JSON)
    public void testShouldResultToSingleJsonLoader() throws NoSuchMethodException {
        TestData testData = getTestDataAnnotationOfMethod("testShouldResultToSingleJsonLoader");
        assertThat(DatasetLoaderFactory.shouldResultToSingleJsonLoader(testData), is(true));
    }

    @Test
    @TestData(resourcePaths = A_TEST_RESOURCE_PATH)
    public void testShouldResultToMultipleJsonLoader() throws NoSuchMethodException {
        TestData testData = getTestDataAnnotationOfMethod("testShouldResultToMultipleJsonLoader");
        assertThat(DatasetLoaderFactory.shouldResultToMultipleJsonLoader(testData), is(true));
    }

    @Test
    @TestData
    public void testShouldNotResultToSingleJsonLoaderWithAnnotationDefaults() throws NoSuchMethodException {
        TestData testData = getTestDataAnnotationOfMethod("testShouldNotResultToSingleJsonLoaderWithAnnotationDefaults");
        assertThat(DatasetLoaderFactory.shouldResultToSingleJsonLoader(testData), is(false));
    }

    private Object getFieldValue(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        String datasetResourcePath = (String) field.get(instance);
        return datasetResourcePath;
    }

    private String invokerPackageWithSlashes() {
        return "org/lambico/datatest";
    }

    private TestData getTestDataAnnotationOfMethod(String methodName) throws NoSuchMethodException {
        TestData testData = this.getClass().getDeclaredMethod(methodName, (Class<?>[]) null)
                .getAnnotation(TestData.class);
        return testData;
    }

}
