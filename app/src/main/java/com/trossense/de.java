package com.trossense;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.lang.invoke.MethodHandles;
import java.nio.IntBuffer;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class de {
    private static final long b = dj.a(-8802464484850062474L, 2275403261469385360L, MethodHandles.lookup().lookupClass()).a(45764683011206L);
    private static final String c = a(9, a("\u0006X5$\u0011\u0016U"));
    private static HashMap<String, Integer> a = new HashMap<>();

    public static int a(long j, String str) {
        long j2 = j ^ b;
        if (a.containsKey(str)) {
            return a.get(str).intValue();
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(de.class.getClassLoader().getResourceAsStream(c + str));
            if (j2 >= 0 && decodeStream == null) {
                return 0;
            }
            int a2 = a(decodeStream);
            a.put(str, Integer.valueOf(a2));
            return a2;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int a(Bitmap bitmap) {
        IntBuffer allocate = IntBuffer.allocate(1);
        GLES20.glGenTextures(1, allocate);
        int i = allocate.get();
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10242, 10497);
        GLES20.glTexParameteri(3553, 10243, 10497);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLES20.glBindTexture(3553, 0);
        return i;
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c2 = cArr[i2];
            int i3 = 108;
            switch (i2 % 7) {
                case 0:
                    i3 = 110;
                    break;
                case 1:
                    i3 = 34;
                    break;
                case 2:
                    i3 = 79;
                    break;
                case 3:
                    i3 = 72;
                    break;
                case 4:
                case 5:
                    break;
                default:
                    i3 = 115;
                    break;
            }
            cArr[i2] = (char) (c2 ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 's');
        }
        return charArray;
    }
}
