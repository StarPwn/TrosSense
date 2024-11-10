package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: classes4.dex */
public final class SystemPropertyUtil {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SystemPropertyUtil.class);

    public static boolean contains(String key) {
        return get(key) != null;
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(final String key, String def) {
        ObjectUtil.checkNonEmpty(key, "key");
        String value = null;
        try {
            if (System.getSecurityManager() == null) {
                value = System.getProperty(key);
            } else {
                value = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: io.netty.util.internal.SystemPropertyUtil.1
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        } catch (SecurityException e) {
            logger.warn("Unable to retrieve a system property '{}'; default values will be used.", key, e);
        }
        if (value == null) {
            return def;
        }
        return value;
    }

    public static boolean getBoolean(String key, boolean def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        String value2 = value.trim().toLowerCase();
        if (value2.isEmpty()) {
            return def;
        }
        if ("true".equals(value2) || "yes".equals(value2) || "1".equals(value2)) {
            return true;
        }
        if ("false".equals(value2) || "no".equals(value2) || "0".equals(value2)) {
            return false;
        }
        logger.warn("Unable to parse the boolean system property '{}':{} - using the default value: {}", key, value2, Boolean.valueOf(def));
        return def;
    }

    public static int getInt(String key, int def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        String value2 = value.trim();
        try {
            return Integer.parseInt(value2);
        } catch (Exception e) {
            logger.warn("Unable to parse the integer system property '{}':{} - using the default value: {}", key, value2, Integer.valueOf(def));
            return def;
        }
    }

    public static long getLong(String key, long def) {
        String value = get(key);
        if (value == null) {
            return def;
        }
        String value2 = value.trim();
        try {
            return Long.parseLong(value2);
        } catch (Exception e) {
            logger.warn("Unable to parse the long integer system property '{}':{} - using the default value: {}", key, value2, Long.valueOf(def));
            return def;
        }
    }

    private SystemPropertyUtil() {
    }
}
