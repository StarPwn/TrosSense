package com.trossense.sdk;

import com.google.gson.GsonBuilder;
import com.trossense.hook.HopeHookEntrance;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class e {
    public static HashMap<String, String> a;
    public static HashMap<String, Number> b;
    private static final String[] c;

    static {
        InputStreamReader inputStreamReader;
        String[] strArr = new String[3];
        int length = "WxX5}\b,Xn_'f\th\u0019b_5d\bjLn\u0005:z\u0014m\u0004XdE5\u001dWxX5}\b,Xn_'f\th\u0019iG?j\u0010wYdG~c\blX".length();
        char c2 = 28;
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c2 + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(64, c("WxX5}\b,Xn_'f\th\u0019b_5d\bjLn\u0005:z\u0014m\u0004XdE5\u001dWxX5}\b,Xn_'f\th\u0019iG?j\u0010wYdG~c\blX".substring(i3, i4)));
            if (i4 >= length) {
                break;
            }
            i2 = i5;
            i = i4;
            c2 = "WxX5}\b,Xn_'f\th\u0019b_5d\bjLn\u0005:z\u0014m\u0004XdE5\u001dWxX5}\b,Xn_'f\th\u0019iG?j\u0010wYdG~c\blX".charAt(i4);
        }
        c = strArr;
        a = new HashMap<>();
        b = new HashMap<>();
        try {
            inputStreamReader = new InputStreamReader(HopeHookEntrance.class.getClassLoader().getResourceAsStream(strArr[2]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            a = (HashMap) new GsonBuilder().create().fromJson((Reader) inputStreamReader, HashMap.class);
            inputStreamReader.close();
            try {
                inputStreamReader = new InputStreamReader(HopeHookEntrance.class.getClassLoader().getResourceAsStream(c[0]));
                try {
                    b = (HashMap) new GsonBuilder().create().fromJson((Reader) inputStreamReader, HashMap.class);
                    inputStreamReader.close();
                } finally {
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                inputStreamReader.close();
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
    }

    public static int a(String str) {
        return b.getOrDefault(str, 0).intValue();
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 118;
                    break;
                case 1:
                    i2 = 75;
                    break;
                case 2:
                    i2 = 107;
                    break;
                case 3:
                    i2 = 16;
                    break;
                case 4:
                    i2 = 73;
                    break;
                case 5:
                    i2 = 59;
                    break;
                default:
                    i2 = 67;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    public static String b(String str) {
        return a.getOrDefault(str, c[1]);
    }

    private static char[] c(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'C');
        }
        return charArray;
    }
}
