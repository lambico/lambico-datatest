package org.lambico.datatest.junit;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.*;

public class DatasetTest {
    
    @Test
    public void testDefaultValues() {
        Dataset instance = Dataset.builder().build();
        assertThat(instance.flushWindowSize, is(nullValue()));
        assertThat(instance.useMerge, is(false));
    }

    @Test
    public void testFlushWindowSizeSetting() {
        Dataset instance = Dataset.builder()
            .flushWindowSize(1)
            .useMerge(true).build();
            assertThat(instance.flushWindowSize, is(1));
            assertThat(instance.useMerge, is(true));
        }
}