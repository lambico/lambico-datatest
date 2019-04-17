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
package org.lambico.datatest;

import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.annotation.TestDataType;
import org.lambico.datatest.json.MultipleJsonDatasetLoader;
import org.lambico.datatest.json.SingleJsonDatasetLoader;
import org.slf4j.helpers.Util;

public class DatasetLoaderFactory {

	public static DatasetLoader create() {
        return MultipleJsonDatasetLoader.builder()
            .datasetResourcePath(defaultResourcePath())
            .build();
    }

	public static DatasetLoader create(TestData testData) {
        DatasetLoader result = null;
        if (testData == null) {
            result = create();
        } else {
            if (shouldResultToSingleJsonLoader(testData)) {
                result = SingleJsonDatasetLoader.builder()
                    .datasetResource(testData.resources()[0])
                    .build();
            } else if (shouldResultToMultipleJsonLoader(testData)) {
                result = MultipleJsonDatasetLoader.builder()
                    .datasetResourcePath(testData.resourcePaths()[0])
                    .build();
            } else {
                result = create();
            }
        }
		return result;
	}

    static boolean shouldResultToSingleJsonLoader(TestData testData) {
        return (testData.type() == TestDataType.JSON)
            && (testData.resources().length == 1);
    }

	static boolean shouldResultToMultipleJsonLoader(TestData testData) {
        return (testData.type() == TestDataType.JSON)
            && (testData.resourcePaths().length == 1);
	}

    static String defaultResourcePath() {
        return Util.getCallingClass().getPackage().getName().replace(".", "/");
    }

}
