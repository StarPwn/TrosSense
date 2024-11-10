package io.netty.util.internal;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/* loaded from: classes4.dex */
public final class ResourcesUtil {
    public static File getFile(Class resourceClass, String fileName) {
        try {
            return new File(URLDecoder.decode(resourceClass.getResource(fileName).getFile(), org.jose4j.lang.StringUtil.UTF_8));
        } catch (UnsupportedEncodingException e) {
            return new File(resourceClass.getResource(fileName).getFile());
        }
    }

    private ResourcesUtil() {
    }
}
