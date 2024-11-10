package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class RtspVersions {
    public static final HttpVersion RTSP_1_0 = new HttpVersion("RTSP", 1, 0, true);

    public static HttpVersion valueOf(String text) {
        ObjectUtil.checkNotNull(text, "text");
        String text2 = text.trim().toUpperCase();
        if ("RTSP/1.0".equals(text2)) {
            return RTSP_1_0;
        }
        return new HttpVersion(text2, true);
    }

    private RtspVersions() {
    }
}
