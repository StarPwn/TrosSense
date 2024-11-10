package org.jose4j.lang;

import androidx.core.os.EnvironmentCompat;
import java.util.List;
import java.util.Map;
import org.jose4j.jwt.IntDate;

/* loaded from: classes5.dex */
public class JsonHelp {
    public static String getString(Map<String, Object> map, String name) {
        Object object = map.get(name);
        return (String) object;
    }

    public static String getStringChecked(Map<String, Object> map, String name) throws JoseException {
        Object o = map.get(name);
        try {
            return (String) o;
        } catch (ClassCastException e) {
            throw new JoseException("'" + name + "' parameter was " + jsonTypeName(o) + " type but is required to be a String.");
        }
    }

    public static List<String> getStringArray(Map<String, Object> map, String name) {
        Object object = map.get(name);
        return (List) object;
    }

    public static IntDate getIntDate(Map<String, Object> map, String name) {
        long l = getLong(map, name).longValue();
        return IntDate.fromSeconds(l);
    }

    public static Long getLong(Map<String, ?> map, String name) {
        Object o = map.get(name);
        if (o != null) {
            return Long.valueOf(((Number) o).longValue());
        }
        return null;
    }

    public static String jsonTypeName(Object value) {
        if (value instanceof Number) {
            return "Number";
        }
        if (value instanceof Boolean) {
            return "Boolean";
        }
        if (value instanceof List) {
            return "Array";
        }
        if (value instanceof Map) {
            return "Object";
        }
        if (value instanceof String) {
            return "String";
        }
        return EnvironmentCompat.MEDIA_UNKNOWN;
    }
}
