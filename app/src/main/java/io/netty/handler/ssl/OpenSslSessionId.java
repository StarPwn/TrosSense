package io.netty.handler.ssl;

import io.netty.util.internal.EmptyArrays;
import java.util.Arrays;

/* loaded from: classes4.dex */
final class OpenSslSessionId {
    static final OpenSslSessionId NULL_ID = new OpenSslSessionId(EmptyArrays.EMPTY_BYTES);
    private final int hashCode;
    private final byte[] id;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpenSslSessionId(byte[] id) {
        this.id = id;
        this.hashCode = Arrays.hashCode(id);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpenSslSessionId)) {
            return false;
        }
        return Arrays.equals(this.id, ((OpenSslSessionId) o).id);
    }

    public String toString() {
        return "OpenSslSessionId{id=" + Arrays.toString(this.id) + '}';
    }

    public int hashCode() {
        return this.hashCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] cloneBytes() {
        return (byte[]) this.id.clone();
    }
}
