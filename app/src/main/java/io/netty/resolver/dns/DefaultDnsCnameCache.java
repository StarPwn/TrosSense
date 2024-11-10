package io.netty.resolver.dns;

import io.netty.channel.EventLoop;
import io.netty.util.AsciiString;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

/* loaded from: classes4.dex */
public final class DefaultDnsCnameCache implements DnsCnameCache {
    private final Cache<String> cache;
    private final int maxTtl;
    private final int minTtl;

    public DefaultDnsCnameCache() {
        this(0, Cache.MAX_SUPPORTED_TTL_SECS);
    }

    public DefaultDnsCnameCache(int minTtl, int maxTtl) {
        this.cache = new Cache<String>() { // from class: io.netty.resolver.dns.DefaultDnsCnameCache.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean shouldReplaceAll(String entry) {
                return true;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // io.netty.resolver.dns.Cache
            public boolean equals(String entry, String otherEntry) {
                return AsciiString.contentEqualsIgnoreCase(entry, otherEntry);
            }
        };
        this.minTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositiveOrZero(minTtl, "minTtl"));
        this.maxTtl = Math.min(Cache.MAX_SUPPORTED_TTL_SECS, ObjectUtil.checkPositive(maxTtl, "maxTtl"));
        if (minTtl > maxTtl) {
            throw new IllegalArgumentException("minTtl: " + minTtl + ", maxTtl: " + maxTtl + " (expected: 0 <= minTtl <= maxTtl)");
        }
    }

    @Override // io.netty.resolver.dns.DnsCnameCache
    public String get(String hostname) {
        List<? extends String> cached = this.cache.get((String) ObjectUtil.checkNotNull(hostname, "hostname"));
        if (cached == null || cached.isEmpty()) {
            return null;
        }
        return cached.get(0);
    }

    @Override // io.netty.resolver.dns.DnsCnameCache
    public void cache(String hostname, String cname, long originalTtl, EventLoop loop) {
        ObjectUtil.checkNotNull(hostname, "hostname");
        ObjectUtil.checkNotNull(cname, "cname");
        ObjectUtil.checkNotNull(loop, "loop");
        this.cache.cache(hostname, cname, Math.max(this.minTtl, (int) Math.min(this.maxTtl, originalTtl)), loop);
    }

    @Override // io.netty.resolver.dns.DnsCnameCache
    public void clear() {
        this.cache.clear();
    }

    @Override // io.netty.resolver.dns.DnsCnameCache
    public boolean clear(String hostname) {
        return this.cache.clear((String) ObjectUtil.checkNotNull(hostname, "hostname"));
    }

    int minTtl() {
        return this.minTtl;
    }

    int maxTtl() {
        return this.maxTtl;
    }
}
