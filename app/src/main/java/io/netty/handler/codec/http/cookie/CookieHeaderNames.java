package io.netty.handler.codec.http.cookie;

/* loaded from: classes4.dex */
public final class CookieHeaderNames {
    public static final String DOMAIN = "Domain";
    public static final String EXPIRES = "Expires";
    public static final String HTTPONLY = "HTTPOnly";
    public static final String MAX_AGE = "Max-Age";
    public static final String PARTITIONED = "Partitioned";
    public static final String PATH = "Path";
    public static final String SAMESITE = "SameSite";
    public static final String SECURE = "Secure";

    /* loaded from: classes4.dex */
    public enum SameSite {
        Lax,
        Strict,
        None;

        /* JADX INFO: Access modifiers changed from: package-private */
        public static SameSite of(String name) {
            if (name != null) {
                for (SameSite each : (SameSite[]) SameSite.class.getEnumConstants()) {
                    if (each.name().equalsIgnoreCase(name)) {
                        return each;
                    }
                }
                return null;
            }
            return null;
        }
    }

    private CookieHeaderNames() {
    }
}
