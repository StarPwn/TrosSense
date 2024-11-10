package io.netty.handler.codec.http;

import io.netty.util.AsciiString;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class HttpMethod implements Comparable<HttpMethod> {
    private final AsciiString name;
    public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
    public static final HttpMethod GET = new HttpMethod("GET");
    public static final HttpMethod HEAD = new HttpMethod("HEAD");
    public static final HttpMethod POST = new HttpMethod("POST");
    public static final HttpMethod PUT = new HttpMethod("PUT");
    public static final HttpMethod PATCH = new HttpMethod("PATCH");
    public static final HttpMethod DELETE = new HttpMethod("DELETE");
    public static final HttpMethod TRACE = new HttpMethod("TRACE");
    public static final HttpMethod CONNECT = new HttpMethod("CONNECT");
    private static final EnumNameMap<HttpMethod> methodMap = new EnumNameMap<>(new EnumNameMap.Node(OPTIONS.toString(), OPTIONS), new EnumNameMap.Node(GET.toString(), GET), new EnumNameMap.Node(HEAD.toString(), HEAD), new EnumNameMap.Node(POST.toString(), POST), new EnumNameMap.Node(PUT.toString(), PUT), new EnumNameMap.Node(PATCH.toString(), PATCH), new EnumNameMap.Node(DELETE.toString(), DELETE), new EnumNameMap.Node(TRACE.toString(), TRACE), new EnumNameMap.Node(CONNECT.toString(), CONNECT));

    public static HttpMethod valueOf(String name) {
        if (name == GET.name()) {
            return GET;
        }
        if (name == POST.name()) {
            return POST;
        }
        HttpMethod result = methodMap.get(name);
        return result != null ? result : new HttpMethod(name);
    }

    public HttpMethod(String name) {
        String name2 = ObjectUtil.checkNonEmptyAfterTrim(name, "name");
        for (int i = 0; i < name2.length(); i++) {
            char c = name2.charAt(i);
            if (Character.isISOControl(c) || Character.isWhitespace(c)) {
                throw new IllegalArgumentException("invalid character in name");
            }
        }
        this.name = AsciiString.cached(name2);
    }

    public String name() {
        return this.name.toString();
    }

    public AsciiString asciiName() {
        return this.name;
    }

    public int hashCode() {
        return name().hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpMethod)) {
            return false;
        }
        HttpMethod that = (HttpMethod) o;
        return name().equals(that.name());
    }

    public String toString() {
        return this.name.toString();
    }

    @Override // java.lang.Comparable
    public int compareTo(HttpMethod o) {
        if (o == this) {
            return 0;
        }
        return name().compareTo(o.name());
    }

    /* loaded from: classes4.dex */
    private static final class EnumNameMap<T> {
        private final Node<T>[] values;
        private final int valuesMask;

        EnumNameMap(Node<T>... nodes) {
            this.values = new Node[MathUtil.findNextPositivePowerOfTwo(nodes.length)];
            this.valuesMask = this.values.length - 1;
            for (Node<T> node : nodes) {
                int i = hashCode(node.key) & this.valuesMask;
                if (this.values[i] != null) {
                    throw new IllegalArgumentException("index " + i + " collision between values: [" + this.values[i].key + ", " + node.key + ']');
                }
                this.values[i] = node;
            }
        }

        T get(String name) {
            Node<T> node = this.values[hashCode(name) & this.valuesMask];
            if (node == null || !node.key.equals(name)) {
                return null;
            }
            return node.value;
        }

        private static int hashCode(String name) {
            return name.hashCode() >>> 6;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static final class Node<T> {
            final String key;
            final T value;

            Node(String key, T value) {
                this.key = key;
                this.value = value;
            }
        }
    }
}
