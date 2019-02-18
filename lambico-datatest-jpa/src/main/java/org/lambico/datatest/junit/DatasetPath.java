package org.lambico.datatest.junit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.rules.ExternalResource;
import org.lambico.datatest.json.DataAggregator;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Scan all the json file in a specific package.
 */
public class DatasetPath extends ExternalResource {
    private String packageName;
    private DataAggregator dataAggregator;

    public DatasetPath(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void before() throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        Reflections reflections = new Reflections(packageName, new ResourcesScanner());
        Set<String> fileNames = reflections.getResources(Pattern.compile(".*\\.json"));
        if(fileNames.isEmpty()){
            throw new IllegalArgumentException("package "+packageName+" does not contain any json file!");
        }
        List<InputStream> streamList = fileNames.stream()
                .map(this::toInputStream)
                .collect(Collectors.toList());
        HashMap<String, Object> merged = new HashMap<>();
        for (InputStream is:streamList){
            String s = inputStreamToString(is);
            Map<String, Object> map = mapper.readValue(s, Map.class);
            merged.putAll(map);
        }
        String valueAsString = mapper.writeValueAsString(merged);
        this.dataAggregator = mapper.readValue(valueAsString, DataAggregator.class);
    }


    /**
     * @return the dataAggregator
     */
    public DataAggregator getDataAggregator() {
        return dataAggregator;
    }

    private InputStream toInputStream(String name){
        return this.getClass().getClassLoader()
                .getResourceAsStream(name);
    }

    private String inputStreamToString(InputStream inputStream) throws Exception{
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

}