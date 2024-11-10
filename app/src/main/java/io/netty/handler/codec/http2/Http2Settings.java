package io.netty.handler.codec.http2;

import io.netty.util.collection.CharObjectHashMap;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class Http2Settings extends CharObjectHashMap<Long> {
    private static final int DEFAULT_CAPACITY = 13;
    private static final Long FALSE = 0L;
    private static final Long TRUE = 1L;

    public Http2Settings() {
        this(13);
    }

    public Http2Settings(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Http2Settings(int initialCapacity) {
        super(initialCapacity);
    }

    @Override // io.netty.util.collection.CharObjectHashMap, io.netty.util.collection.CharObjectMap
    public Long put(char key, Long value) {
        verifyStandardSetting(key, value);
        return (Long) super.put(key, (char) value);
    }

    public Long headerTableSize() {
        return get((char) 1);
    }

    public Http2Settings headerTableSize(long value) {
        put((char) 1, Long.valueOf(value));
        return this;
    }

    public Boolean pushEnabled() {
        Long value = get((char) 2);
        if (value == null) {
            return null;
        }
        return Boolean.valueOf(TRUE.equals(value));
    }

    public Http2Settings pushEnabled(boolean enabled) {
        put((char) 2, enabled ? TRUE : FALSE);
        return this;
    }

    public Long maxConcurrentStreams() {
        return get((char) 3);
    }

    public Http2Settings maxConcurrentStreams(long value) {
        put((char) 3, Long.valueOf(value));
        return this;
    }

    public Integer initialWindowSize() {
        return getIntValue((char) 4);
    }

    public Http2Settings initialWindowSize(int value) {
        put((char) 4, Long.valueOf(value));
        return this;
    }

    public Integer maxFrameSize() {
        return getIntValue((char) 5);
    }

    public Http2Settings maxFrameSize(int value) {
        put((char) 5, Long.valueOf(value));
        return this;
    }

    public Long maxHeaderListSize() {
        return get((char) 6);
    }

    public Http2Settings maxHeaderListSize(long value) {
        put((char) 6, Long.valueOf(value));
        return this;
    }

    public Http2Settings copyFrom(Http2Settings settings) {
        clear();
        putAll(settings);
        return this;
    }

    public Integer getIntValue(char key) {
        Long value = get(key);
        if (value == null) {
            return null;
        }
        return Integer.valueOf(value.intValue());
    }

    private static void verifyStandardSetting(int key, Long value) {
        ObjectUtil.checkNotNull(value, "value");
        switch (key) {
            case 1:
                if (value.longValue() < 0 || value.longValue() > 4294967295L) {
                    throw new IllegalArgumentException("Setting HEADER_TABLE_SIZE is invalid: " + value + ", expected [0, 4294967295]");
                }
                return;
            case 2:
                if (value.longValue() != 0 && value.longValue() != 1) {
                    throw new IllegalArgumentException("Setting ENABLE_PUSH is invalid: " + value + ", expected [0, 1]");
                }
                return;
            case 3:
                if (value.longValue() < 0 || value.longValue() > 4294967295L) {
                    throw new IllegalArgumentException("Setting MAX_CONCURRENT_STREAMS is invalid: " + value + ", expected [0, 4294967295]");
                }
                return;
            case 4:
                if (value.longValue() < 0 || value.longValue() > 2147483647L) {
                    throw new IllegalArgumentException("Setting INITIAL_WINDOW_SIZE is invalid: " + value + ", expected [0, 2147483647]");
                }
                return;
            case 5:
                if (!Http2CodecUtil.isMaxFrameSizeValid(value.intValue())) {
                    throw new IllegalArgumentException("Setting MAX_FRAME_SIZE is invalid: " + value + ", expected [16384, 16777215]");
                }
                return;
            case 6:
                if (value.longValue() < 0 || value.longValue() > 4294967295L) {
                    throw new IllegalArgumentException("Setting MAX_HEADER_LIST_SIZE is invalid: " + value + ", expected [0, 4294967295]");
                }
                return;
            default:
                if (value.longValue() < 0 || value.longValue() > 4294967295L) {
                    throw new IllegalArgumentException("Non-standard setting 0x" + Integer.toHexString(key) + " is invalid: " + value + ", expected unsigned 32-bit value");
                }
                return;
        }
    }

    @Override // io.netty.util.collection.CharObjectHashMap
    protected String keyToString(char key) {
        switch (key) {
            case 1:
                return "HEADER_TABLE_SIZE";
            case 2:
                return "ENABLE_PUSH";
            case 3:
                return "MAX_CONCURRENT_STREAMS";
            case 4:
                return "INITIAL_WINDOW_SIZE";
            case 5:
                return "MAX_FRAME_SIZE";
            case 6:
                return "MAX_HEADER_LIST_SIZE";
            default:
                return "0x" + Integer.toHexString(key);
        }
    }

    public static Http2Settings defaultSettings() {
        return new Http2Settings().maxHeaderListSize(Http2CodecUtil.DEFAULT_HEADER_LIST_SIZE);
    }
}
