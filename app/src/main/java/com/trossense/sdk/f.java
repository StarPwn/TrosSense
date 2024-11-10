package com.trossense.sdk;

import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;

/* loaded from: classes3.dex */
public class f implements ItemDefinition {
    private static String f;
    public final int a;
    public final String b;
    public final List<String> c;
    public final int d;
    public final String e;

    static {
        if (b() == null) {
            b("Bz4oM");
        }
    }

    public f(int i, String str, List<String> list, int i2, String str2) {
        this.a = i;
        this.b = str;
        this.c = list;
        this.d = i2;
        this.e = str2;
    }

    public static String b() {
        return f;
    }

    public static void b(String str) {
        f = str;
    }

    @Override // org.cloudburstmc.protocol.common.NamedDefinition
    public String getIdentifier() {
        return this.b;
    }

    @Override // org.cloudburstmc.protocol.common.Definition
    public int getRuntimeId() {
        return this.a;
    }

    @Override // org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition
    public boolean isComponentBased() {
        return false;
    }

    public String toString() {
        return this.b;
    }
}
