package com.trossense.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trossense.dj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.common.DefinitionRegistry;

/* loaded from: classes3.dex */
public class h implements DefinitionRegistry<ItemDefinition> {
    private static final long b = dj.a(6854255176053273469L, 3332941531666896108L, MethodHandles.lookup().lookupClass()).a(70359696545140L);
    private static final String[] c;
    private HashMap<Integer, f> a;

    static {
        char c2 = 2;
        String[] strArr = new String[2];
        int length = "m6\u0004j3 \u0005".length();
        int i = 0;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c2 + i3;
            int i5 = i + 1;
            strArr[i] = a(111, a("m6\u0004j3 \u0005".substring(i3, i4)));
            if (i4 >= length) {
                c = strArr;
                return;
            } else {
                i2 = i4;
                c2 = "m6\u0004j3 \u0005".charAt(i4);
                i = i5;
            }
        }
    }

    public h(long j, InputStreamReader inputStreamReader) {
        long j2 = j ^ b;
        this.a = new HashMap<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                Iterator<JsonElement> it2 = new JsonParser().parse(bufferedReader).getAsJsonArray().iterator();
                while (it2.hasNext()) {
                    JsonObject asJsonObject = it2.next().getAsJsonObject();
                    String[] strArr = c;
                    int asInt = (asJsonObject.get(strArr[0]).getAsInt() / 3) - 7;
                    String asString = asJsonObject.get(strArr[1]).getAsString();
                    this.a.put(Integer.valueOf(asInt), new f(asInt, asString, i.a(asString), e.a(asString), e.b(asString)));
                    if (j2 < 0) {
                        return;
                    }
                    if (j2 <= 0) {
                        break;
                    }
                }
                bufferedReader.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 107;
                    break;
                case 1:
                    i2 = 61;
                    break;
                case 2:
                    i2 = 34;
                    break;
                case 3:
                    i2 = 15;
                    break;
                case 4:
                    i2 = 74;
                    break;
                case 5:
                    i2 = 12;
                    break;
                default:
                    i2 = 78;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'N');
        }
        return charArray;
    }

    public ItemDefinition a(long j, int i) {
        f.b();
        f orDefault = this.a.getOrDefault(Integer.valueOf(i), new g(i));
        if (PointerHolder.s() == null) {
            f.b("SJciGb");
        }
        return orDefault;
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean isRegistered(ItemDefinition itemDefinition) {
        return true;
    }

    @Override // org.cloudburstmc.protocol.common.DefinitionRegistry
    public /* bridge */ /* synthetic */ ItemDefinition getDefinition(int i) {
        return a((b ^ 110003014128922L) ^ 52666531104364L, i);
    }
}
