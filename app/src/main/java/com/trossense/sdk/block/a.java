package com.trossense.sdk.block;

import com.trossense.sdk.math.Vec3i;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class a {
    public static final a DOWN;
    public static final a EAST;
    public static final a[] HORIZONTALS;
    public static final a NORTH;
    public static final a SOUTH;
    public static final a UP;
    public static final a[] VALUES;
    public static final a WEST;
    private static int d;
    private static final /* synthetic */ a[] e;
    private int a;
    private String b;
    private Vec3i c;
    public final int index;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0024. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[12];
        String str = "ht\u0004JA\u0018G\u0005NK\u001eG/\u0004ja8g\u0004XE\u0018G\u0005sk9g\u000f\u0004yk<}\u0004xe8g\u0002HT\u0005nk>g\u000f";
        int length = "ht\u0004JA\u0018G\u0005NK\u001eG/\u0004ja8g\u0004XE\u0018G\u0005sk9g\u000f\u0004yk<}\u0004xe8g\u0002HT\u0005nk>g\u000f".length();
        char c = 2;
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = i3 + 1;
            String substring = str.substring(i5, i5 + c);
            boolean z = -1;
            int i6 = 3;
            while (true) {
                String a = a(i6, a(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i2 = i4 + 1;
                        strArr[i4] = a;
                        i3 = i5 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = "D\\\u000eP8\u0004N\\\u000bJ";
                        length = "D\\\u000eP8\u0004N\\\u000bJ".length();
                        i = -1;
                        i4 = i2;
                        c = 5;
                        i6 = 20;
                        i5 = i + 1;
                        substring = str.substring(i5, i5 + c);
                        z = false;
                        break;
                }
                int i7 = i4 + 1;
                strArr[i4] = a;
                i = i5 + c;
                if (i >= length) {
                    DOWN = new a(strArr[6], 0, 1, -1, strArr[11], new Vec3i(0, -1, 0));
                    UP = new a(strArr[0], 1, 0, -1, strArr[8], new Vec3i(0, 1, 0));
                    NORTH = new a(strArr[5], 2, 3, 2, strArr[10], new Vec3i(0, 0, -1));
                    SOUTH = new a(strArr[9], 3, 2, 0, strArr[2], new Vec3i(0, 0, 1));
                    WEST = new a(strArr[3], 4, 5, 1, strArr[1], new Vec3i(-1, 0, 0));
                    EAST = new a(strArr[7], 5, 4, 3, strArr[4], new Vec3i(1, 0, 0));
                    e = c();
                    VALUES = new a[6];
                    HORIZONTALS = new a[4];
                    d = 0;
                    for (a aVar : values()) {
                        VALUES[aVar.index] = aVar;
                        if (aVar.c.y == 0) {
                            a[] aVarArr = HORIZONTALS;
                            int i8 = d;
                            aVarArr[i8] = aVar;
                            d = i8 + 1;
                        }
                    }
                    return;
                }
                c = str.charAt(i);
                i4 = i7;
                i6 = 20;
                i5 = i + 1;
                substring = str.substring(i5, i5 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i2;
        }
    }

    private a(String str, int i, int i2, int i3, String str2, Vec3i vec3i) {
        this.a = i3;
        this.index = i2;
        this.b = str2;
        this.c = vec3i;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 62;
                    break;
                case 1:
                    i2 = 39;
                    break;
                case 2:
                    i2 = 104;
                    break;
                case 3:
                    i2 = 48;
                    break;
                case 4:
                    i2 = 68;
                    break;
                case 5:
                    i2 = 114;
                    break;
                default:
                    i2 = 75;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'K');
        }
        return charArray;
    }

    private static /* synthetic */ a[] c() {
        return new a[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
    }

    public static a valueOf(String str) {
        return (a) Enum.valueOf(a.class, str);
    }

    public static a[] values() {
        return (a[]) e.clone();
    }

    public a a() {
        return VALUES[this.a];
    }

    public Vec3i b() {
        return this.c;
    }
}
