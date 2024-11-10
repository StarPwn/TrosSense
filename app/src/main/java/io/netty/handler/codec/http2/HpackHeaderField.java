package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
class HpackHeaderField {
    static final int HEADER_ENTRY_OVERHEAD = 32;
    final CharSequence name;
    final CharSequence value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long sizeOf(CharSequence name, CharSequence value) {
        return name.length() + value.length() + 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HpackHeaderField(CharSequence name, CharSequence value) {
        this.name = (CharSequence) ObjectUtil.checkNotNull(name, "name");
        this.value = (CharSequence) ObjectUtil.checkNotNull(value, "value");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int size() {
        return this.name.length() + this.value.length() + 32;
    }

    public final boolean equalsForTest(HpackHeaderField other) {
        return HpackUtil.equalsVariableTime(this.name, other.name) && HpackUtil.equalsVariableTime(this.value, other.value);
    }

    public String toString() {
        return ((Object) this.name) + ": " + ((Object) this.value);
    }
}
