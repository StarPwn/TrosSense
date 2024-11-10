package com.trossense;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class c {
    public static final c DISABLE;
    public static final c ENABLE;
    private static final /* synthetic */ c[] a;
    private static boolean b;

    static {
        String[] strArr = new String[2];
        int length = "4]paW\u0006!\u00065ZbbY\u000f".length();
        b(false);
        char c = 7;
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(117, a("4]paW\u0006!\u00065ZbbY\u000f".substring(i3, i4)));
            if (i4 >= length) {
                ENABLE = new c(strArr[1], 0);
                DISABLE = new c(strArr[0], 1);
                a = a();
                return;
            } else {
                i2 = i5;
                i = i4;
                c = "4]paW\u0006!\u00065ZbbY\u000f".charAt(i4);
            }
        }
    }

    private c(String str, int i) {
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 5;
                    break;
                case 1:
                    i2 = 97;
                    break;
                case 2:
                    i2 = 86;
                    break;
                case 3:
                    i2 = 85;
                    break;
                case 4:
                    i2 = 96;
                    break;
                case 5:
                    i2 = 63;
                    break;
                default:
                    i2 = 17;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 17);
        }
        return charArray;
    }

    private static /* synthetic */ c[] a() {
        return new c[]{ENABLE, DISABLE};
    }

    public static void b(boolean z) {
        b = z;
    }

    public static boolean b() {
        return b;
    }

    public static boolean c() {
        return !b();
    }

    public static c valueOf(String str) {
        return (c) Enum.valueOf(c.class, str);
    }

    public static c[] values() {
        return (c[]) a.clone();
    }
}
