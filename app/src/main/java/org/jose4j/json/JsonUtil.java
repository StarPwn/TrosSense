package org.jose4j.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jose4j.json.internal.json_simple.JSONValue;
import org.jose4j.json.internal.json_simple.parser.ContainerFactory;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.jose4j.lang.JoseException;

/* loaded from: classes5.dex */
public class JsonUtil {
    private static final ContainerFactory CONTAINER_FACTORY = new ContainerFactory() { // from class: org.jose4j.json.JsonUtil.1
        @Override // org.jose4j.json.internal.json_simple.parser.ContainerFactory
        public List creatArrayContainer() {
            return new ArrayList();
        }

        @Override // org.jose4j.json.internal.json_simple.parser.ContainerFactory
        public Map createObjectContainer() {
            return new DupeKeyDisallowingLinkedHashMap();
        }
    };

    public static Map<String, Object> parseJson(String jsonString) throws JoseException {
        try {
            JSONParser parser = new JSONParser();
            Object parsed = parser.parse(jsonString, CONTAINER_FACTORY);
            if (parsed == null) {
                throw new JoseException("Parsing returned null");
            }
            return (Map) parsed;
        } catch (ClassCastException e) {
            throw new JoseException("Expecting a JSON object at the root but " + e, e);
        } catch (IllegalArgumentException e2) {
            e = e2;
            throw new JoseException("Parsing error: " + e, e);
        } catch (ParseException e3) {
            e = e3;
            throw new JoseException("Parsing error: " + e, e);
        }
    }

    public static String toJson(Map<String, ?> map) {
        return JSONValue.toJSONString(map);
    }

    public static void writeJson(Map<String, ?> map, Writer w) throws IOException {
        JSONValue.writeJSONString(map, w);
    }

    /* loaded from: classes5.dex */
    static class DupeKeyDisallowingLinkedHashMap extends LinkedHashMap<String, Object> {
        DupeKeyDisallowingLinkedHashMap() {
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object put(String key, Object value) {
            if (containsKey(key)) {
                throw new IllegalArgumentException("An entry for '" + key + "' already exists. Names must be unique.");
            }
            return super.put((DupeKeyDisallowingLinkedHashMap) key, (String) value);
        }
    }
}
