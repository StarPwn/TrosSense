package com.trossense.sdk;

import org.cloudburstmc.nbt.NbtMap;

/* loaded from: classes3.dex */
public class c extends b {
    private static final String f = b(111, b("p}\u0013/D&e{`G?I?jrc\u0013"));

    public c(int i) {
        super(f, i, NbtMap.EMPTY);
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 114;
                    break;
                case 1:
                    i2 = 123;
                    break;
                case 2:
                    i2 = 18;
                    break;
                case 3:
                    i2 = 37;
                    break;
                case 4:
                    i2 = 72;
                    break;
                case 5:
                    i2 = 59;
                    break;
                default:
                    i2 = 107;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'k');
        }
        return charArray;
    }
}
