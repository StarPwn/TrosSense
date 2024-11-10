package com.trossense;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class b {
    public static final b Combat;
    public static final b Misc;
    public static final b Movement;
    public static final b Player;
    public static final b Render;
    public static final b World;
    private static final /* synthetic */ b[] a;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0021. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "gF\u00143\u0007K\u0005sF\u000b=\u0002\u0004i@\n2\biF\u000f4\u000bZ4P";
        int length = "gF\u00143\u0007K\u0005sF\u000b=\u0002\u0004i@\n2\biF\u000f4\u000bZ4P".length();
        char c = 6;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = i3 + 1;
            String substring = str.substring(i5, i5 + c);
            boolean z = -1;
            int i6 = 3;
            while (true) {
                String a2 = a(i6, a(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i = i4 + 1;
                        strArr[i4] = a2;
                        i3 = i5 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = "\u000f5nLz4\u0006\r<aQz4";
                        length = "\u000f5nLz4\u0006\r<aQz4".length();
                        c = 6;
                        i2 = -1;
                        i4 = i;
                        i6 = 122;
                        i5 = i2 + 1;
                        substring = str.substring(i5, i5 + c);
                        z = false;
                        break;
                }
                i = i4 + 1;
                strArr[i4] = a2;
                i2 = i5 + c;
                if (i2 >= length) {
                    Combat = new b(strArr[0], 0);
                    Misc = new b(strArr[2], 1);
                    Movement = new b(strArr[3], 2);
                    Player = new b(strArr[5], 3);
                    Render = new b(strArr[4], 4);
                    World = new b(strArr[1], 5);
                    a = a();
                    return;
                }
                c = str.charAt(i2);
                i4 = i;
                i6 = 122;
                i5 = i2 + 1;
                substring = str.substring(i5, i5 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i;
        }
    }

    private b(String str, int i) {
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 39;
                    break;
                case 1:
                    i2 = 42;
                    break;
                case 2:
                    i2 = 122;
                    break;
                case 3:
                    i2 = 82;
                    break;
                case 4:
                    i2 = 101;
                    break;
                case 5:
                    i2 = 60;
                    break;
                default:
                    i2 = 89;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'Y');
        }
        return charArray;
    }

    private static /* synthetic */ b[] a() {
        return new b[]{Combat, Misc, Movement, Player, Render, World};
    }

    public static b valueOf(String str) {
        return (b) Enum.valueOf(b.class, str);
    }

    public static b[] values() {
        return (b[]) a.clone();
    }
}
