package io.netty.handler.codec.http;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes4.dex */
public class EmptyHttpHeaders extends HttpHeaders {
    static final Iterator<Map.Entry<CharSequence, CharSequence>> EMPTY_CHARS_ITERATOR = Collections.emptyList().iterator();
    public static final EmptyHttpHeaders INSTANCE = instance();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public static EmptyHttpHeaders instance() {
        return InstanceInitializer.EMPTY_HEADERS;
    }

    protected EmptyHttpHeaders() {
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public String get(String name) {
        return null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Integer getInt(CharSequence name) {
        return null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int getInt(CharSequence name, int defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Short getShort(CharSequence name) {
        return null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public short getShort(CharSequence name, short defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Long getTimeMillis(CharSequence name) {
        return null;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public long getTimeMillis(CharSequence name, long defaultValue) {
        return defaultValue;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<String> getAll(String name) {
        return Collections.emptyList();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public List<Map.Entry<String, String>> entries() {
        return Collections.emptyList();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean contains(String name) {
        return false;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public boolean isEmpty() {
        return true;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public int size() {
        return 0;
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Set<String> names() {
        return Collections.emptySet();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders add(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders addShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Object value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders set(String name, Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setInt(CharSequence name, int value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders setShort(CharSequence name, short value) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders remove(String name) {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public HttpHeaders clear() {
        throw new UnsupportedOperationException("read only");
    }

    @Override // io.netty.handler.codec.http.HttpHeaders, java.lang.Iterable
    public Iterator<Map.Entry<String, String>> iterator() {
        return entries().iterator();
    }

    @Override // io.netty.handler.codec.http.HttpHeaders
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return EMPTY_CHARS_ITERATOR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    /* loaded from: classes4.dex */
    public static final class InstanceInitializer {

        @Deprecated
        private static final EmptyHttpHeaders EMPTY_HEADERS = new EmptyHttpHeaders();

        private InstanceInitializer() {
        }
    }
}
