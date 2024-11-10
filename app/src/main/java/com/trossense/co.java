package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

/* loaded from: classes3.dex */
public class co<T> {
    private static boolean h;
    private static final long o = dj.a(1583859941542796634L, -4678817619905108034L, MethodHandles.lookup().lookupClass()).a(201019657609939L);
    protected String a;
    public BooleanSupplier b;
    protected String c;
    protected T d;
    protected T e;
    protected cv<T> f;
    protected bm g;

    static {
        if (h()) {
            return;
        }
        b(true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public co(String str, String str2, long j, bm bmVar, Object obj) {
        long j2 = (j ^ o) ^ 84283891818334L;
        this.a = str;
        this.c = str2;
        this.e = obj;
        a(obj, (char) (j2 >>> 48), (j2 << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public co(String str, String str2, bm bmVar, int i, int i2, Object obj, Object obj2) {
        long j = ((((i2 << 32) >>> 32) | (i << 32)) ^ o) ^ 121965332097759L;
        this.a = str;
        this.c = str2;
        this.e = obj;
        a(obj2, (char) (j >>> 48), (j << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public co(String str, String str2, bm bmVar, Object obj, Object obj2, long j, BooleanSupplier booleanSupplier) {
        long j2 = (j ^ o) ^ 45699174539567L;
        this.a = str;
        this.c = str2;
        this.e = obj;
        this.b = booleanSupplier;
        a(obj2, (char) (j2 >>> 48), (j2 << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public co(String str, String str2, bm bmVar, Object obj, BooleanSupplier booleanSupplier, long j) {
        long j2 = (j ^ o) ^ 12826968280156L;
        this.a = str;
        this.c = str2;
        this.e = obj;
        this.b = booleanSupplier;
        a(obj, (char) (j2 >>> 48), (j2 << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public co(String str, String str2, bm bmVar, BooleanSupplier booleanSupplier, long j, Object obj, Object obj2, cv cvVar) {
        long j2 = (j ^ o) ^ 118839294038909L;
        this.b = booleanSupplier;
        this.a = str;
        this.c = str2;
        this.e = obj2;
        this.f = cvVar;
        a(obj, (char) (j2 >>> 48), (j2 << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    public co(String str, String str2, short s, bm bmVar, Object obj, char c, cv cvVar, int i) {
        long j = ((((s << 48) | ((c << 48) >>> 16)) | ((i << 32) >>> 32)) ^ o) ^ 62738251283755L;
        this.a = str;
        this.c = str2;
        this.f = cvVar;
        a(obj, (char) (j >>> 48), (j << 16) >>> 16);
        if (bmVar != null) {
            bmVar.b().add(this);
        }
        this.g = bmVar;
    }

    public static void b(boolean z) {
        h = z;
    }

    public static boolean g() {
        return h;
    }

    public static boolean h() {
        return !g();
    }

    public void a(cv<T> cvVar) {
        this.f = cvVar;
    }

    public void a(Object obj, char c, long j) {
        cv<T> cvVar = this.f;
        if (cvVar != null) {
            obj = cvVar.a(obj);
        }
        this.d = (T) obj;
    }

    public void a(BooleanSupplier booleanSupplier) {
        this.b = booleanSupplier;
    }

    public boolean a(long j) {
        long j2 = j ^ o;
        BooleanSupplier booleanSupplier = this.b;
        if (booleanSupplier != null) {
            boolean asBoolean = booleanSupplier.getAsBoolean();
            if (j2 <= 0) {
                return asBoolean;
            }
            if (asBoolean) {
                return true;
            }
        }
        return false;
    }

    public String b() {
        return this.a;
    }

    public String c(long j) {
        return TrosSense.INSTANCE.isEnglishLanguage ? this.a : this.c;
    }

    public String d() {
        return this.c;
    }

    public T e() {
        return this.d;
    }

    public T f() {
        return this.e;
    }
}
