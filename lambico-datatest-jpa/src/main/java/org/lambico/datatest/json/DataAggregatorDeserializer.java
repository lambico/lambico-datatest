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

import org.lambico.datatest.DataAggregator;
/**
 * A Jackson deserializer for the {@link DataAggregator} class.
 */
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