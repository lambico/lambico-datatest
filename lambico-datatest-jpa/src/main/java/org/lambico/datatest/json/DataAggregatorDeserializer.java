package org.lambico.datatest.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class DataAggregatorDeserializer extends StdDeserializer<DataAggregator> {

    private static final long serialVersionUID = 1L;

    public DataAggregatorDeserializer() {
        super(DataAggregator.class);
    }

    @Override
    public DataAggregator deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);
        DataAggregator result = new DataAggregator();
        Iterator<Entry<String, JsonNode>> fields = root.fields();
        while (fields.hasNext()) {
            Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            CollectionType type;
            try {
                type = TypeFactory.defaultInstance().constructCollectionType(
                            ArrayList.class, Class.forName(key));
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(p, "Missing class for this sub-dataset", e);
            }
            JsonParser traverse = field.getValue().traverse(mapper);
            traverse.nextToken();
            Object value = ctxt.readValue(traverse, type);
            result.getObjects().put(key, (Collection<?>) value);
        }
        return result;
    }

}