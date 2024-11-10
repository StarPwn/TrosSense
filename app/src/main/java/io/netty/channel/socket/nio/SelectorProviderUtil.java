package io.netty.channel.socket.nio;

import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ProtocolFamily;
import java.nio.channels.Channel;
import java.nio.channels.spi.SelectorProvider;

/* loaded from: classes4.dex */
final class SelectorProviderUtil {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) SelectorProviderUtil.class);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Method findOpenMethod(String methodName) {
        if (PlatformDependent.javaVersion() >= 15) {
            try {
                return SelectorProvider.class.getMethod(methodName, ProtocolFamily.class);
            } catch (Throwable e) {
                logger.debug("SelectorProvider.{}(ProtocolFamily) not available, will use default", methodName, e);
                return null;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <C extends Channel> C newChannel(Method method, SelectorProvider provider, InternetProtocolFamily family) throws IOException {
        if (family != null && method != null) {
            try {
                return (C) method.invoke(provider, ProtocolFamilyConverter.convert(family));
            } catch (IllegalAccessException e) {
                throw new IOException(e);
            } catch (InvocationTargetException e2) {
                throw new IOException(e2);
            }
        }
        return null;
    }

    private SelectorProviderUtil() {
    }
}
