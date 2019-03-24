package org.lambico.datatest.json;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class JsonScannerTest {

    private static final String MULTIPLE_DATASET_RESOURCE = "org/lambico/datatest/multiplejson";
    private static final String MULTIPLE_DATASET_RESOURCE_FROM_JAR = "lambico/multiple";
    private static final String EMPTY_DATASET_RESOURCE = "org/lambico/datatest";
    private static final String NOT_EXISTING_FOLDER = "org/lambico/notafolder";

    private JsonScanner jsonScanner;

    @Before
    public void setUp() {
        this.jsonScanner = JsonScanner.getInstance();
    }

    @Test
    public void listFiles_empty() throws IOException {
        List<String> list = jsonScanner.listFiles(EMPTY_DATASET_RESOURCE);
        assertThat(list, hasSize(0));
    }

    @Test
    public void listFiles_folder() throws IOException {
        List<String> list = jsonScanner.listFiles(MULTIPLE_DATASET_RESOURCE);
        assertThat(list, hasSize(2));
        list.stream()
                .filter(s -> s.startsWith(MULTIPLE_DATASET_RESOURCE))
                .filter(s -> s.endsWith("Entity1.json"))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void listFiles_jar() throws IOException {
        List<String> list = jsonScanner.listFiles(MULTIPLE_DATASET_RESOURCE_FROM_JAR);
        assertThat(list, hasSize(2));
        list.stream()
                .filter(s -> s.startsWith(MULTIPLE_DATASET_RESOURCE_FROM_JAR))
                .filter(s -> s.endsWith("Entity2.json"))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @Test
    public void listFiles_notExisting() throws IOException {
        List<String> list = jsonScanner.listFiles(NOT_EXISTING_FOLDER);
        assertThat(list, hasSize(0));
    }
}