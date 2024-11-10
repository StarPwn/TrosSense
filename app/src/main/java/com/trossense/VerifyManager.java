package com.trossense;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.invoke.MethodHandles;
import java.util.Timer;
import okhttp3.OkHttpClient;

/* loaded from: classes3.dex */
public class VerifyManager {
    public static boolean c;
    private static String[] e;
    private String a;
    public OkHttpClient d = new OkHttpClient();
    private static final long f = dj.a(8743098364781027767L, 1924979258334915277L, MethodHandles.lookup().lookupClass()).a(2192921701456L);
    private static final Gson b = new GsonBuilder().setPrettyPrinting().create();

    static {
        b(new String[5]);
    }

    public VerifyManager() {
        c = false;
    }

    public static native String a(String str, String str2);

    public static native String b(String str, String str2, String str3);

    public static void b(String[] strArr) {
        e = strArr;
    }

    public static String[] b() {
        return e;
    }

    public static native String c(String str);

    public static native String d(String str, String str2);

    public static native boolean e(int i, long j, String str);

    public void a(String str, long j, byte b2) {
        if (c) {
            return;
        }
        c = true;
        new Timer().schedule(new dg(this, str), 60000L, 60000L);
    }
}
