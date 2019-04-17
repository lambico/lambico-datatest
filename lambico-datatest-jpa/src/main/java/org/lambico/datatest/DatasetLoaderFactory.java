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
