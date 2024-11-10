package io.netty.handler.ssl.ocsp;

import io.netty.util.internal.ObjectUtil;
import java.util.Date;

/* loaded from: classes4.dex */
public class OcspResponse {
    private final Date nextUpdate;
    private final Status status;
    private final Date thisUpdate;

    /* loaded from: classes4.dex */
    public enum Status {
        VALID,
        REVOKED,
        UNKNOWN
    }

    public OcspResponse(Status status, Date thisUpdate, Date nextUpdate) {
        this.status = (Status) ObjectUtil.checkNotNull(status, "Status");
        this.thisUpdate = (Date) ObjectUtil.checkNotNull(thisUpdate, "ThisUpdate");
        this.nextUpdate = (Date) ObjectUtil.checkNotNull(nextUpdate, "NextUpdate");
    }

    public Status status() {
        return this.status;
    }

    public Date thisUpdate() {
        return this.thisUpdate;
    }

    public Date nextUpdate() {
        return this.nextUpdate;
    }

    public String toString() {
        return "OcspResponse{status=" + this.status + ", thisUpdate=" + this.thisUpdate + ", nextUpdate=" + this.nextUpdate + '}';
    }
}
