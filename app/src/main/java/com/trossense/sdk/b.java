package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;

/* loaded from: classes3.dex */
public class b implements BlockDefinition {
    private static final long d = dj.a(8984661111492242558L, 7857065054003241031L, MethodHandles.lookup().lookupClass()).a(93835891278416L);
    private static final String[] e;
    public final String a;
    public final int b;
    public final NbtMap c;

    static {
        String[] strArr = new String[3];
        int length = "O78 l\u0015`\u000er\u0003\u0011?\u000f!{%6i%l\u0005l$4o\u00044D\tO79!c\u0015l\u0010*".length();
        int i = 0;
        char c = '\f';
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c + i3;
            int i5 = i + 1;
            strArr[i] = a(41, a("O78 l\u0015`\u000er\u0003\u0011?\u000f!{%6i%l\u0005l$4o\u00044D\tO79!c\u0015l\u0010*".substring(i3, i4)));
            if (i4 >= length) {
                e = strArr;
                return;
            } else {
                i2 = i4;
                c = "O78 l\u0015`\u000er\u0003\u0011?\u000f!{%6i%l\u0005l$4o\u00044D\tO79!c\u0015l\u0010*".charAt(i4);
                i = i5;
            }
        }
    }

    public b(String str, int i, NbtMap nbtMap) {
        this.a = str;
        this.b = i;
        this.c = nbtMap;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 74;
                    break;
                case 1:
                    i2 = 62;
                    break;
                case 2:
                    i2 = 99;
                    break;
                case 3:
                    i2 = 124;
                    break;
                case 4:
                    i2 = 43;
                    break;
                case 5:
                    i2 = 72;
                    break;
                default:
                    i2 = 32;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ ' ');
        }
        return charArray;
    }

    @Override // org.cloudburstmc.protocol.common.Definition
    public int getRuntimeId() {
        return this.b;
    }

    public String toString() {
        String b = f.b();
        StringBuilder sb = new StringBuilder();
        String[] strArr = e;
        String sb2 = sb.append(strArr[1]).append(this.a).append('\'').append(strArr[0]).append(this.b).append(strArr[2]).append(this.c).append('}').toString();
        if (b == null) {
            PointerHolder.b(new int[4]);
        }
        return sb2;
    }
}
