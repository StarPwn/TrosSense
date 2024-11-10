package com.trossense;

import android.util.Log;
import com.trossense.sdk.PointerHolder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class c1 {
    private static final long a = dj.a(9037969010502138304L, 5071617061586474257L, MethodHandles.lookup().lookupClass()).a(87066803873138L);
    private static String b;
    private static final String[] c;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004b. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        c("YkAGvc");
        String str = "T}^3\u0004+/DaVs\u0003<\u0004Yg_q";
        int length = "T}^3\u0004+/DaVs\u0003<\u0004Yg_q".length();
        char c2 = '\r';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 93;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, d(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a2;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "\u0016?\u001cqFim\u0006#\u00141A~\u0015Z4\u0010+S4n\u001a3\u00103\u001doo\u0005\u007f@qFcv";
                        length = "\u0016?\u001cqFim\u0006#\u00141A~\u0015Z4\u0010+S4n\u001a3\u00103\u001doo\u0005\u007f@qFcv".length();
                        c2 = '\r';
                        i2 = -1;
                        i3 = i;
                        i5 = 31;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a2;
                i2 = i6 + c2;
                if (i2 >= length) {
                    c = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 31;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 106;
                    break;
                case 1:
                    i2 = 79;
                    break;
                case 2:
                    i2 = 110;
                    break;
                case 3:
                    i2 = 64;
                    break;
                case 4:
                    i2 = 45;
                    break;
                case 5:
                    i2 = 4;
                    break;
                default:
                    i2 = 29;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    public static void a(long j, Object obj) {
        long j2 = j ^ a;
        String b2 = b();
        String[] strArr = c;
        int d = Log.d(strArr[2], obj == null ? strArr[1] : obj.toString());
        if (j2 > 0) {
            if (b2 != null) {
                return;
            } else {
                d = 3;
            }
        }
        PointerHolder.b(new int[d]);
    }

    public static void a(String str) {
        Log.d(c[0], str);
    }

    public static String b() {
        return b;
    }

    public static void b(String str) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(c[3]));
            try {
                bufferedWriter.write(str);
                bufferedWriter.close();
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void c(String str) {
        b = str;
    }

    private static char[] d(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 29);
        }
        return charArray;
    }
}
