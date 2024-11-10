package io.netty.handler.codec.spdy;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public class SpdySessionStatus implements Comparable<SpdySessionStatus> {
    private final int code;
    private final String statusPhrase;
    public static final SpdySessionStatus OK = new SpdySessionStatus(0, "OK");
    public static final SpdySessionStatus PROTOCOL_ERROR = new SpdySessionStatus(1, "PROTOCOL_ERROR");
    public static final SpdySessionStatus INTERNAL_ERROR = new SpdySessionStatus(2, "INTERNAL_ERROR");

    public static SpdySessionStatus valueOf(int code) {
        switch (code) {
            case 0:
                return OK;
            case 1:
                return PROTOCOL_ERROR;
            case 2:
                return INTERNAL_ERROR;
            default:
                return new SpdySessionStatus(code, "UNKNOWN (" + code + ')');
        }
    }

    public SpdySessionStatus(int code, String statusPhrase) {
        this.statusPhrase = (String) ObjectUtil.checkNotNull(statusPhrase, "statusPhrase");
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    public String statusPhrase() {
        return this.statusPhrase;
    }

    public int hashCode() {
        return code();
    }

    public boolean equals(Object o) {
        return (o instanceof SpdySessionStatus) && code() == ((SpdySessionStatus) o).code();
    }

    public String toString() {
        return statusPhrase();
    }

    @Override // java.lang.Comparable
    public int compareTo(SpdySessionStatus o) {
        return code() - o.code();
    }
}
