package org.jose4j.jwk;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.jose4j.http.Get;
import org.jose4j.http.SimpleGet;
import org.jose4j.http.SimpleResponse;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class HttpsJwks {
    private static final Logger log = LoggerFactory.getLogger((Class<?>) HttpsJwks.class);
    private final String location;
    private volatile long defaultCacheDuration = 3600;
    private volatile SimpleGet simpleHttpGet = new Get();
    private volatile long retainCacheOnErrorDurationMills = 0;
    private volatile Cache cache = new Cache(Collections.emptyList(), 0);
    private final ReentrantLock refreshLock = new ReentrantLock();
    private long refreshReprieveThreshold = 300;

    public HttpsJwks(String location) {
        this.location = location;
    }

    public void setDefaultCacheDuration(long defaultCacheDuration) {
        this.defaultCacheDuration = defaultCacheDuration;
    }

    public void setRetainCacheOnErrorDuration(long retainCacheOnErrorDuration) {
        this.retainCacheOnErrorDurationMills = 1000 * retainCacheOnErrorDuration;
    }

    public void setSimpleHttpGet(SimpleGet simpleHttpGet) {
        this.simpleHttpGet = simpleHttpGet;
    }

    public String getLocation() {
        return this.location;
    }

    public void setRefreshReprieveThreshold(long refreshReprieveThreshold) {
        this.refreshReprieveThreshold = refreshReprieveThreshold;
    }

    public List<JsonWebKey> getJsonWebKeys() throws JoseException, IOException {
        Cache c;
        long now = System.currentTimeMillis();
        Cache c2 = this.cache;
        if (c2.exp <= now) {
            if (!this.refreshLock.tryLock()) {
                if (c2.keys.isEmpty()) {
                    this.refreshLock.lock();
                } else {
                    return c2.keys;
                }
            }
            try {
                try {
                    refresh();
                    c = this.cache;
                } catch (Exception e) {
                    if (this.retainCacheOnErrorDurationMills <= 0 || c2.keys.isEmpty()) {
                        throw e;
                    }
                    Cache cache = new Cache(c2.keys, this.retainCacheOnErrorDurationMills + now);
                    c = cache;
                    this.cache = cache;
                    log.info("Because of {} unable to refresh JWKS content from {} so will continue to use cached keys for more {} seconds until about {} -> {}", ExceptionHelp.toStringWithCauses(e), this.location, Long.valueOf(this.retainCacheOnErrorDurationMills / 1000), new Date(c.exp), c.keys);
                }
                return c.keys;
            } finally {
                this.refreshLock.unlock();
            }
        }
        return c2.keys;
    }

    public void refresh() throws JoseException, IOException {
        this.refreshLock.lock();
        try {
            long last = System.currentTimeMillis() - this.cache.created;
            if (last < this.refreshReprieveThreshold && !this.cache.keys.isEmpty()) {
                log.debug("NOT refreshing/loading JWKS from {} because it just happened {} mills ago", this.location, Long.valueOf(last));
            } else {
                log.debug("Refreshing/loading JWKS from {}", this.location);
                SimpleResponse simpleResponse = this.simpleHttpGet.get(this.location);
                JsonWebKeySet jwks = new JsonWebKeySet(simpleResponse.getBody());
                List<JsonWebKey> keys = jwks.getJsonWebKeys();
                long cacheLife = getCacheLife(simpleResponse);
                if (cacheLife <= 0) {
                    log.debug("Will use default cache duration of {} seconds for content from {}", Long.valueOf(this.defaultCacheDuration), this.location);
                    cacheLife = this.defaultCacheDuration;
                }
                long exp = System.currentTimeMillis() + (1000 * cacheLife);
                log.debug("Updated JWKS content from {} will be cached for {} seconds until about {} -> {}", this.location, Long.valueOf(cacheLife), new Date(exp), keys);
                this.cache = new Cache(keys, exp);
            }
        } finally {
            this.refreshLock.unlock();
        }
    }

    static long getDateHeaderValue(SimpleResponse response, String headerName, long defaultValue) {
        List<String> values = getHeaderValues(response, headerName);
        Iterator<String> it2 = values.iterator();
        while (it2.hasNext()) {
            String value = it2.next();
            try {
                if (!value.endsWith("GMT")) {
                    value = value + " GMT";
                }
                return Date.parse(value);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static List<String> getHeaderValues(SimpleResponse response, String headerName) {
        List<String> values = response.getHeaderValues(headerName);
        return values == null ? Collections.emptyList() : values;
    }

    static long getExpires(SimpleResponse response) {
        return getDateHeaderValue(response, "expires", 0L);
    }

    static long getCacheLife(SimpleResponse response) {
        return getCacheLife(response, System.currentTimeMillis());
    }

    static long getCacheLife(SimpleResponse response, long currentTime) {
        String lowerCase;
        long expires = getExpires(response);
        long life = (expires - currentTime) / 1000;
        List<String> values = getHeaderValues(response, "cache-control");
        for (String value : values) {
            if (value == null) {
                lowerCase = "";
            } else {
                try {
                    lowerCase = value.toLowerCase();
                } catch (Exception e) {
                }
            }
            String value2 = lowerCase;
            int indexOfMaxAge = value2.indexOf("max-age");
            int indexOfComma = value2.indexOf(44, indexOfMaxAge);
            int end = indexOfComma == -1 ? value2.length() : indexOfComma;
            String part = value2.substring(indexOfMaxAge, end);
            long life2 = Long.parseLong(part.substring(part.indexOf(61) + 1).trim());
            return life2;
        }
        return life;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes5.dex */
    public static class Cache {
        private final long created;
        private final long exp;
        private final List<JsonWebKey> keys;

        private Cache(List<JsonWebKey> keys, long exp) {
            this.created = System.currentTimeMillis();
            this.keys = keys;
            this.exp = exp;
        }
    }
}
