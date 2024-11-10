package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class cu {
    private static final long c = dj.a(3888085826113706081L, -7271914994473993275L, MethodHandles.lookup().lookupClass()).a(2589371196293L);
    public final String a;
    public final String b;

    public cu(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public String a(int i, short s, int i2) {
        return TrosSense.INSTANCE.isEnglishLanguage ? this.a : this.b;
    }

    public String b() {
        return this.b;
    }

    public String c() {
        return this.a;
    }

    public boolean equals(Object obj) {
        if (obj instanceof cu) {
            cu cuVar = (cu) obj;
            if (cuVar.a.equals(this.a) && cuVar.b.equals(this.a)) {
                return true;
            }
        }
        return false;
    }
}
