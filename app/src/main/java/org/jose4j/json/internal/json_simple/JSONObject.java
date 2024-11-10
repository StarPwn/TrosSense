package org.jose4j.json.internal.json_simple;

import com.trossense.bl;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes5.dex */
public class JSONObject extends HashMap implements Map, JSONAware, JSONStreamAware {
    private static final long serialVersionUID = -503443796854799292L;

    public JSONObject() {
    }

    public JSONObject(Map map) {
        super(map);
    }

    public static void writeJSONString(Map map, Writer out) throws IOException {
        if (map == null) {
            out.write("null");
            return;
        }
        boolean first = true;
        out.write(123);
        for (Map.Entry entry : map.entrySet()) {
            if (first) {
                first = false;
            } else {
                out.write(44);
            }
            out.write(34);
            out.write(escape(String.valueOf(entry.getKey())));
            out.write(34);
            out.write(58);
            JSONValue.writeJSONString(entry.getValue(), out);
        }
        out.write(bl.bm);
    }

    @Override // org.jose4j.json.internal.json_simple.JSONStreamAware
    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }

    public static String toJSONString(Map map) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(map, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.jose4j.json.internal.json_simple.JSONAware
    public String toJSONString() {
        return toJSONString(this);
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return toJSONString();
    }

    public static String toString(String key, Object value) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        if (key == null) {
            sb.append("null");
        } else {
            JSONValue.escape(key, sb);
        }
        sb.append('\"').append(':');
        sb.append(JSONValue.toJSONString(value));
        return sb.toString();
    }

    public static String escape(String s) {
        return JSONValue.escape(s);
    }
}
