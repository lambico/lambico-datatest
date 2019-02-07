package org.lambico.datatest.example1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Asset;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.example1.model.Job;
import org.lambico.datatest.json.DataAggregator;
import org.lambico.datatest.junit.Dataset;

public class InMemoryRuleTest {
    @Rule
    public Dataset dataset = new Dataset("org/lambico/datatest/example1/dataset/dataset.json");

    @Rule
    public Dataset dataset2 = new Dataset("asset.json");

    @Test
    public void loadAggregatedObject() throws IOException, ClassNotFoundException {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        assertTrue(dataAggregator instanceof DataAggregator);
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertEquals("test1", entity1.getStringField());
        assertEquals("test2", entity2.getStringField());
        assertSame(entity2, entity1.getEntity2());
        assertSame(entity1, entity2.getEntity1());
    }


    @Test
    public void loadAsset()  {
        DataAggregator dataAggregator = dataset2.getDataAggregator();
        assertTrue(dataAggregator instanceof DataAggregator);
        Collection<?> assets = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Asset");
        assertThat(assets.size(), is(1));
        Asset asset = ((Asset)assets.iterator().next());
        assertThat(asset.getJobs().size(), is(2));
        Collection<?> jobs = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Job");
        assertThat(jobs.size(), is(2));
        Job job = ((Job)jobs.iterator().next());
        assertThat(job.getAsset(), is(asset));
    }
    
}
