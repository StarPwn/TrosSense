package io.netty.util.internal.logging;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public abstract class InternalLoggerFactory {
    private static volatile InternalLoggerFactory defaultFactory;

    protected abstract InternalLogger newInstance(String str);

    private static InternalLoggerFactory newDefaultFactory(String name) {
        InternalLoggerFactory f = useSlf4JLoggerFactory(name);
        if (f != null) {
            return f;
        }
        InternalLoggerFactory f2 = useLog4J2LoggerFactory(name);
        if (f2 != null) {
            return f2;
        }
        InternalLoggerFactory f3 = useLog4JLoggerFactory(name);
        if (f3 != null) {
            return f3;
        }
        return useJdkLoggerFactory(name);
    }

    private static InternalLoggerFactory useSlf4JLoggerFactory(String name) {
        try {
            InternalLoggerFactory f = Slf4JLoggerFactory.getInstanceWithNopCheck();
            f.newInstance(name).debug("Using SLF4J as the default logging framework");
            return f;
        } catch (Exception e) {
            return null;
        } catch (LinkageError e2) {
            return null;
        }
    }

    private static InternalLoggerFactory useLog4J2LoggerFactory(String name) {
        try {
            InternalLoggerFactory f = Log4J2LoggerFactory.INSTANCE;
            f.newInstance(name).debug("Using Log4J2 as the default logging framework");
            return f;
        } catch (Exception e) {
            return null;
        } catch (LinkageError e2) {
            return null;
        }
    }

    private static InternalLoggerFactory useLog4JLoggerFactory(String name) {
        try {
            InternalLoggerFactory f = Log4JLoggerFactory.INSTANCE;
            f.newInstance(name).debug("Using Log4J as the default logging framework");
            return f;
        } catch (Exception e) {
            return null;
        } catch (LinkageError e2) {
            return null;
        }
    }

    private static InternalLoggerFactory useJdkLoggerFactory(String name) {
        InternalLoggerFactory f = JdkLoggerFactory.INSTANCE;
        f.newInstance(name).debug("Using java.util.logging as the default logging framework");
        return f;
    }

    public static InternalLoggerFactory getDefaultFactory() {
        if (defaultFactory == null) {
            defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
        }
        return defaultFactory;
    }

    public static void setDefaultFactory(InternalLoggerFactory defaultFactory2) {
        defaultFactory = (InternalLoggerFactory) ObjectUtil.checkNotNull(defaultFactory2, "defaultFactory");
    }

    public static InternalLogger getInstance(Class<?> clazz) {
        return getInstance(clazz.getName());
    }

    public static InternalLogger getInstance(String name) {
        return getDefaultFactory().newInstance(name);
    }
}
