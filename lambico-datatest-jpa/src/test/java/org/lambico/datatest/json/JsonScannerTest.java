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
package org.lambico.datatest.json;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
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
                .map(Paths::get)
                .filter(path -> path.getParent().equals(Paths.get(MULTIPLE_DATASET_RESOURCE)))
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