package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class QueryStringDecoder {
    private static final int DEFAULT_MAX_PARAMS = 1024;
    private final Charset charset;
    private final int maxParams;
    private Map<String, List<String>> params;
    private String path;
    private int pathEndIdx;
    private final boolean semicolonIsNormalChar;
    private final String uri;

    public QueryStringDecoder(String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }

    public QueryStringDecoder(String uri, boolean hasPath) {
        this(uri, HttpConstants.DEFAULT_CHARSET, hasPath);
    }

    public QueryStringDecoder(String uri, Charset charset) {
        this(uri, charset, true);
    }

    public QueryStringDecoder(String uri, Charset charset, boolean hasPath) {
        this(uri, charset, hasPath, 1024);
    }

    public QueryStringDecoder(String uri, Charset charset, boolean hasPath, int maxParams) {
        this(uri, charset, hasPath, maxParams, false);
    }

    public QueryStringDecoder(String uri, Charset charset, boolean hasPath, int maxParams, boolean semicolonIsNormalChar) {
        this.uri = (String) ObjectUtil.checkNotNull(uri, "uri");
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.maxParams = ObjectUtil.checkPositive(maxParams, "maxParams");
        this.semicolonIsNormalChar = semicolonIsNormalChar;
        this.pathEndIdx = hasPath ? -1 : 0;
    }

    public QueryStringDecoder(URI uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }

    public QueryStringDecoder(URI uri, Charset charset) {
        this(uri, charset, 1024);
    }

    public QueryStringDecoder(URI uri, Charset charset, int maxParams) {
        this(uri, charset, maxParams, false);
    }

    public QueryStringDecoder(URI uri, Charset charset, int maxParams, boolean semicolonIsNormalChar) {
        String rawPath = uri.getRawPath();
        rawPath = rawPath == null ? "" : rawPath;
        String rawQuery = uri.getRawQuery();
        this.uri = rawQuery == null ? rawPath : rawPath + '?' + rawQuery;
        this.charset = (Charset) ObjectUtil.checkNotNull(charset, "charset");
        this.maxParams = ObjectUtil.checkPositive(maxParams, "maxParams");
        this.semicolonIsNormalChar = semicolonIsNormalChar;
        this.pathEndIdx = rawPath.length();
    }

    public String toString() {
        return uri();
    }

    public String uri() {
        return this.uri;
    }

    public String path() {
        if (this.path == null) {
            this.path = decodeComponent(this.uri, 0, pathEndIdx(), this.charset, true);
        }
        return this.path;
    }

    public Map<String, List<String>> parameters() {
        if (this.params == null) {
            this.params = decodeParams(this.uri, pathEndIdx(), this.charset, this.maxParams, this.semicolonIsNormalChar);
        }
        return this.params;
    }

    public String rawPath() {
        return this.uri.substring(0, pathEndIdx());
    }

    public String rawQuery() {
        int start = pathEndIdx() + 1;
        return start < this.uri.length() ? this.uri.substring(start) : "";
    }

    private int pathEndIdx() {
        if (this.pathEndIdx == -1) {
            this.pathEndIdx = findPathEndIndex(this.uri);
        }
        return this.pathEndIdx;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:12:0x0026. Please report as an issue. */
    private static Map<String, List<String>> decodeParams(String s, int from, Charset charset, int paramsLimit, boolean semicolonIsNormalChar) {
        int len = s.length();
        if (from >= len) {
            return Collections.emptyMap();
        }
        if (s.charAt(from) == '?') {
            from++;
        }
        Map<String, List<String>> params = new LinkedHashMap<>();
        int nameStart = from;
        int nameStart2 = nameStart;
        int valueStart = -1;
        for (int i = from; i < len; i++) {
            switch (s.charAt(i)) {
                case '#':
                    addParam(s, nameStart2, valueStart, i, params, charset);
                    return params;
                case ';':
                    if (semicolonIsNormalChar) {
                        continue;
                    }
                case '&':
                    if (addParam(s, nameStart2, valueStart, i, params, charset) && paramsLimit - 1 == 0) {
                        return params;
                    }
                    int nameStart3 = i + 1;
                    nameStart2 = nameStart3;
                    break;
                case '=':
                    if (nameStart2 == i) {
                        nameStart2 = i + 1;
                    } else if (valueStart < nameStart2) {
                        valueStart = i + 1;
                    }
                default:
            }
        }
        addParam(s, nameStart2, valueStart, i, params, charset);
        return params;
    }

    private static boolean addParam(String s, int nameStart, int valueStart, int valueEnd, Map<String, List<String>> params, Charset charset) {
        if (nameStart >= valueEnd) {
            return false;
        }
        if (valueStart <= nameStart) {
            valueStart = valueEnd + 1;
        }
        String name = decodeComponent(s, nameStart, valueStart - 1, charset, false);
        String value = decodeComponent(s, valueStart, valueEnd, charset, false);
        List<String> values = params.get(name);
        if (values == null) {
            values = new ArrayList(1);
            params.put(name, values);
        }
        values.add(value);
        return true;
    }

    public static String decodeComponent(String s) {
        return decodeComponent(s, HttpConstants.DEFAULT_CHARSET);
    }

    public static String decodeComponent(String s, Charset charset) {
        if (s == null) {
            return "";
        }
        return decodeComponent(s, 0, s.length(), charset, false);
    }

    private static String decodeComponent(String s, int from, int toExcluded, Charset charset, boolean isPath) {
        int len = toExcluded - from;
        if (len <= 0) {
            return "";
        }
        int firstEscaped = -1;
        for (int i = from; i < toExcluded; i++) {
            char c = s.charAt(i);
            if (c == '%' || (c == '+' && !isPath)) {
                firstEscaped = i;
                break;
            }
        }
        if (firstEscaped == -1) {
            return s.substring(from, toExcluded);
        }
        int decodedCapacity = (toExcluded - firstEscaped) / 3;
        byte[] buf = PlatformDependent.allocateUninitializedArray(decodedCapacity);
        StringBuilder strBuf = new StringBuilder(len);
        strBuf.append((CharSequence) s, from, firstEscaped);
        int i2 = firstEscaped;
        while (i2 < toExcluded) {
            char c2 = s.charAt(i2);
            if (c2 != '%') {
                strBuf.append((c2 != '+' || isPath) ? c2 : ' ');
            } else {
                int bufIdx = 0;
                while (i2 + 3 <= toExcluded) {
                    int bufIdx2 = bufIdx + 1;
                    buf[bufIdx] = StringUtil.decodeHexByte(s, i2 + 1);
                    i2 += 3;
                    if (i2 >= toExcluded || s.charAt(i2) != '%') {
                        i2--;
                        strBuf.append(new String(buf, 0, bufIdx2, charset));
                    } else {
                        bufIdx = bufIdx2;
                    }
                }
                throw new IllegalArgumentException("unterminated escape sequence at index " + i2 + " of: " + s);
            }
            i2++;
        }
        return strBuf.toString();
    }

    private static int findPathEndIndex(String uri) {
        int len = uri.length();
        for (int i = 0; i < len; i++) {
            char c = uri.charAt(i);
            if (c == '?' || c == '#') {
                return i;
            }
        }
        return len;
    }
}
