package org.jose4j.json.internal.json_simple;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import okhttp3.HttpUrl;

/* loaded from: classes5.dex */
public class JSONArray extends ArrayList implements JSONAware, JSONStreamAware {
    private static final long serialVersionUID = 3957988303675231981L;

    public JSONArray() {
    }

    public JSONArray(Collection c) {
        super(c);
    }

    public static void writeJSONString(Collection collection, Writer out) throws IOException {
        if (collection == null) {
            out.write("null");
            return;
        }
        boolean first = true;
        out.write(91);
        for (Object value : collection) {
            if (first) {
                first = false;
            } else {
                out.write(44);
            }
            if (value == null) {
                out.write("null");
            } else {
                JSONValue.writeJSONString(value, out);
            }
        }
        out.write(93);
    }

    @Override // org.jose4j.json.internal.json_simple.JSONStreamAware
    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }

    public static String toJSONString(Collection collection) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(collection, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(byte[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf((int) array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf((int) array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(byte[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(short[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf((int) array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf((int) array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(short[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(int[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf(array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf(array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(int[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(long[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf(array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf(array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(long[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(float[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf(array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf(array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(float[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(double[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf(array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf(array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(double[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(boolean[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        out.write(String.valueOf(array[0]));
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            out.write(String.valueOf(array[i]));
        }
        out.write("]");
    }

    public static String toJSONString(boolean[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(char[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[\"");
        out.write(JSONValue.escape(String.valueOf(array[0])));
        for (int i = 1; i < array.length; i++) {
            out.write("\",\"");
            out.write(JSONValue.escape(String.valueOf(array[i])));
        }
        out.write("\"]");
    }

    public static String toJSONString(char[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, (Writer) writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJSONString(Object[] array, Writer out) throws IOException {
        if (array == null) {
            out.write("null");
            return;
        }
        if (array.length == 0) {
            out.write(HttpUrl.PATH_SEGMENT_ENCODE_SET_URI);
            return;
        }
        out.write("[");
        JSONValue.writeJSONString(array[0], out);
        for (int i = 1; i < array.length; i++) {
            out.write(",");
            JSONValue.writeJSONString(array[i], out);
        }
        out.write("]");
    }

    public static String toJSONString(Object[] array) {
        StringWriter writer = new StringWriter();
        try {
            writeJSONString(array, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // org.jose4j.json.internal.json_simple.JSONAware
    public String toJSONString() {
        return toJSONString(this);
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        return toJSONString();
    }
}
