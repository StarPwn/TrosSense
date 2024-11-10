package io.netty.bootstrap;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ChannelInitializerExtensions {
    private static volatile ChannelInitializerExtensions implementation;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ChannelInitializerExtensions.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Collection<ChannelInitializerExtension> extensions(ClassLoader classLoader);

    private ChannelInitializerExtensions() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ChannelInitializerExtensions getExtensions() {
        ChannelInitializerExtensions impl = implementation;
        if (impl == null) {
            synchronized (ChannelInitializerExtensions.class) {
                ChannelInitializerExtensions impl2 = implementation;
                if (impl2 != null) {
                    return impl2;
                }
                String extensionProp = SystemPropertyUtil.get(ChannelInitializerExtension.EXTENSIONS_SYSTEM_PROPERTY);
                logger.debug("-Dio.netty.bootstrap.extensions: {}", extensionProp);
                if ("serviceload".equalsIgnoreCase(extensionProp)) {
                    impl = new ServiceLoadingExtensions(true);
                } else if ("log".equalsIgnoreCase(extensionProp)) {
                    impl = new ServiceLoadingExtensions(false);
                } else {
                    impl = new EmptyExtensions();
                }
                implementation = impl;
            }
        }
        return impl;
    }

    /* loaded from: classes.dex */
    private static final class EmptyExtensions extends ChannelInitializerExtensions {
        private EmptyExtensions() {
            super();
        }

        @Override // io.netty.bootstrap.ChannelInitializerExtensions
        Collection<ChannelInitializerExtension> extensions(ClassLoader cl) {
            return Collections.emptyList();
        }
    }

    /* loaded from: classes.dex */
    private static final class ServiceLoadingExtensions extends ChannelInitializerExtensions {
        private WeakReference<ClassLoader> classLoader;
        private Collection<ChannelInitializerExtension> extensions;
        private final boolean loadAndCache;

        ServiceLoadingExtensions(boolean loadAndCache) {
            super();
            this.loadAndCache = loadAndCache;
        }

        @Override // io.netty.bootstrap.ChannelInitializerExtensions
        synchronized Collection<ChannelInitializerExtension> extensions(ClassLoader cl) {
            Collection<ChannelInitializerExtension> loaded;
            ClassLoader configured = this.classLoader == null ? null : this.classLoader.get();
            if (configured == null || configured != cl) {
                Collection<ChannelInitializerExtension> loaded2 = serviceLoadExtensions(this.loadAndCache, cl);
                this.classLoader = new WeakReference<>(cl);
                this.extensions = this.loadAndCache ? loaded2 : Collections.emptyList();
            }
            loaded = this.extensions;
            return loaded;
        }

        private static Collection<ChannelInitializerExtension> serviceLoadExtensions(boolean load, ClassLoader cl) {
            List<ChannelInitializerExtension> extensions = new ArrayList<>();
            ServiceLoader<ChannelInitializerExtension> loader = ServiceLoader.load(ChannelInitializerExtension.class, cl);
            Iterator<ChannelInitializerExtension> it2 = loader.iterator();
            while (it2.hasNext()) {
                ChannelInitializerExtension extension = it2.next();
                extensions.add(extension);
            }
            if (extensions.isEmpty()) {
                ChannelInitializerExtensions.logger.debug("ServiceLoader {}(s) {}: []", ChannelInitializerExtension.class.getSimpleName(), load ? "registered" : "detected");
                return Collections.emptyList();
            }
            Collections.sort(extensions, new Comparator<ChannelInitializerExtension>() { // from class: io.netty.bootstrap.ChannelInitializerExtensions.ServiceLoadingExtensions.1
                @Override // java.util.Comparator
                public int compare(ChannelInitializerExtension a, ChannelInitializerExtension b) {
                    return Double.compare(a.priority(), b.priority());
                }
            });
            InternalLogger internalLogger = ChannelInitializerExtensions.logger;
            Object[] objArr = new Object[3];
            objArr[0] = ChannelInitializerExtension.class.getSimpleName();
            objArr[1] = load ? "registered" : "detected";
            objArr[2] = extensions;
            internalLogger.info("ServiceLoader {}(s) {}: {}", objArr);
            return Collections.unmodifiableList(extensions);
        }
    }
}
