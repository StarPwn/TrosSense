package io.netty.handler.ssl.ocsp;

/* loaded from: classes4.dex */
public final class OcspValidationEvent {
    private final OcspResponse response;

    public OcspValidationEvent(OcspResponse response) {
        this.response = response;
    }

    public OcspResponse response() {
        return this.response;
    }

    public String toString() {
        return "OcspValidationEvent{response=" + this.response + '}';
    }
}
