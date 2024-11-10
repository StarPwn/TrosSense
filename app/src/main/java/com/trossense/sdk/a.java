package com.trossense.sdk;

import com.trossense.bl;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class a {
    public static final a CRITICAL_HIT;
    public static final a MAGIC_CRITICAL_HIT;
    public static final a NO_ACTION;
    public static final a ROW_LEFT;
    public static final a ROW_RIGHT;
    public static final a SWING_ARM;
    public static final a WAKE_UP;
    private static final a[] a;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0024. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[7];
        String str = "yKN\\\u0000A\u0015vFOA\u001d\u0007mXLM\u0016W\u0004\thVPW\u001bK\u0013rM\ttVXI\nV\u001duW\u0012wX@A\n]\u0017hPSA\nC\u0018eQN\\";
        int length = "yKN\\\u0000A\u0015vFOA\u001d\u0007mXLM\u0016W\u0004\thVPW\u001bK\u0013rM\ttVXI\nV\u001duW\u0012wX@A\n]\u0017hPSA\nC\u0018eQN\\".length();
        char c = '\f';
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = 122;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a2;
                        i3 = i6 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = ")\u000e\u000e\u0006N\u001dU(\u0014\b(\u0016\u0010\u0017E\u0007R.";
                        length = ")\u000e\u000e\u0006N\u001dU(\u0014\b(\u0016\u0010\u0017E\u0007R.".length();
                        c = '\t';
                        i2 = -1;
                        i4 = i;
                        i5 = 58;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i4] = a2;
                i2 = i6 + c;
                if (i2 >= length) {
                    NO_ACTION = new a(strArr[3], 0);
                    SWING_ARM = new a(strArr[5], 1);
                    WAKE_UP = new a(strArr[1], 2);
                    CRITICAL_HIT = new a(strArr[0], 3);
                    MAGIC_CRITICAL_HIT = new a(strArr[4], 4);
                    ROW_RIGHT = new a(strArr[2], 5);
                    ROW_LEFT = new a(strArr[6], 6);
                    a = a();
                    return;
                }
                c = str.charAt(i2);
                i4 = i;
                i5 = 58;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i;
        }
    }

    private a(String str, int i) {
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 64;
                    break;
                case 1:
                    i2 = 99;
                    break;
                case 2:
                    i2 = bl.bm;
                    break;
                case 3:
                    i2 = 114;
                    break;
                case 4:
                    i2 = 51;
                    break;
                case 5:
                    i2 = 120;
                    break;
                default:
                    i2 = 46;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '.');
        }
        return charArray;
    }

    private static a[] a() {
        return new a[]{NO_ACTION, SWING_ARM, WAKE_UP, CRITICAL_HIT, MAGIC_CRITICAL_HIT, ROW_RIGHT, ROW_LEFT};
    }

    public static a valueOf(String str) {
        return (a) Enum.valueOf(a.class, str);
    }

    public static a[] values() {
        return (a[]) a.clone();
    }
}
