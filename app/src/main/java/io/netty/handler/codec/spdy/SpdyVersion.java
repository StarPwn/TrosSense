package io.netty.handler.codec.spdy;

/* loaded from: classes4.dex */
public enum SpdyVersion {
    SPDY_3_1(3, 1);

    private final int minorVersion;
    private final int version;

    SpdyVersion(int version, int minorVersion) {
        this.version = version;
        this.minorVersion = minorVersion;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersion() {
        return this.version;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMinorVersion() {
        return this.minorVersion;
    }
}
