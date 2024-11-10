package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@Deprecated
/* loaded from: classes4.dex */
public class DefaultCookie extends io.netty.handler.codec.http.cookie.DefaultCookie implements Cookie {
    private String comment;
    private String commentUrl;
    private boolean discard;
    private Set<Integer> ports;
    private Set<Integer> unmodifiablePorts;
    private int version;

    public DefaultCookie(String name, String value) {
        super(name, value);
        this.ports = Collections.emptySet();
        this.unmodifiablePorts = this.ports;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getName() {
        return name();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getValue() {
        return value();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getDomain() {
        return domain();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getPath() {
        return path();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getComment() {
        return comment();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String comment() {
        return this.comment;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setComment(String comment) {
        this.comment = validateValue("comment", comment);
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String getCommentUrl() {
        return commentUrl();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public String commentUrl() {
        return this.commentUrl;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setCommentUrl(String commentUrl) {
        this.commentUrl = validateValue("commentUrl", commentUrl);
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public boolean isDiscard() {
        return this.discard;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public Set<Integer> getPorts() {
        return ports();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public Set<Integer> ports() {
        if (this.unmodifiablePorts == null) {
            this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
        }
        return this.unmodifiablePorts;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setPorts(int... ports) {
        ObjectUtil.checkNotNull(ports, "ports");
        int[] portsCopy = (int[]) ports.clone();
        if (portsCopy.length == 0) {
            Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
            return;
        }
        Set<Integer> newPorts = new TreeSet<>();
        for (int p : portsCopy) {
            if (p <= 0 || p > 65535) {
                throw new IllegalArgumentException("port out of range: " + p);
            }
            newPorts.add(Integer.valueOf(p));
        }
        this.ports = newPorts;
        this.unmodifiablePorts = null;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setPorts(Iterable<Integer> ports) {
        Set<Integer> newPorts = new TreeSet<>();
        Iterator<Integer> it2 = ports.iterator();
        while (it2.hasNext()) {
            int p = it2.next().intValue();
            if (p <= 0 || p > 65535) {
                throw new IllegalArgumentException("port out of range: " + p);
            }
            newPorts.add(Integer.valueOf(p));
        }
        if (newPorts.isEmpty()) {
            Set<Integer> emptySet = Collections.emptySet();
            this.ports = emptySet;
            this.unmodifiablePorts = emptySet;
        } else {
            this.ports = newPorts;
            this.unmodifiablePorts = null;
        }
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public long getMaxAge() {
        return maxAge();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public int getVersion() {
        return version();
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public int version() {
        return this.version;
    }

    @Override // io.netty.handler.codec.http.Cookie
    @Deprecated
    public void setVersion(int version) {
        this.version = version;
    }
}
