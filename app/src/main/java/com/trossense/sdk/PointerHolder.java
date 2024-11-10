package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

/* loaded from: classes3.dex */
public class PointerHolder {
    private static int[] b;
    private static final long e = dj.a(-2203857235447899680L, -5974643209446972528L, MethodHandles.lookup().lookupClass()).a(59029915405574L);
    protected long pointer;

    static {
        if (s() == null) {
            b(new int[1]);
        }
    }

    public PointerHolder(long j) {
        this.pointer = j;
    }

    public static void b(int[] iArr) {
        b = iArr;
    }

    public static int[] s() {
        return b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && getClass() == obj.getClass() && this.pointer == ((PointerHolder) obj).pointer;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.pointer));
    }

    public long k() {
        return this.pointer;
    }
}
