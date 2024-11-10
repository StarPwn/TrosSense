package com.trossense;

import android.content.Context;
import android.graphics.Typeface;
import com.trossense.clients.TrosSense;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class ay {
    private static final long d = dj.a(3544091847232932195L, -8980544919066460950L, MethodHandles.lookup().lookupClass()).a(73949001566608L);
    private Typeface a;
    private final int b;
    private final String c;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        IOException e;
        String[] strArr = new String[4];
        String str = "\u00154Q\u001fyhQ\u0018\u007fK\n|\r\u0000\"L\u001bnr\n\u0007>Q\ni.";
        int length = "\u00154Q\u001fyhQ\u0018\u007fK\n|\r\u0000\"L\u001bnr\n\u0007>Q\ni.".length();
        char c = '\f';
        int i3 = -1;
        int i4 = 0;
        while (true) {
            int i5 = 10;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a;
                        i3 = i6 + c;
                        if (i3 < length) {
                            break;
                        }
                        str = "Z`\u0002Bf'\u0003U\u0011Gf\u0003M+:\u0003J.\u000fC$7YGw\u000b";
                        length = "Z`\u0002Bf'\u0003U\u0011Gf\u0003M+:\u0003J.\u000fC$7YGw\u000b".length();
                        c = '\b';
                        i2 = -1;
                        i4 = i;
                        i5 = 88;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i4] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    String[] strArr2 = {strArr[2], strArr[0], strArr[3]};
                    Context context = TrosSense.INSTANCE.context;
                    for (int i7 = 0; i7 < 3; i7++) {
                        String str2 = strArr2[i7];
                        InputStream inputStream2 = null;
                        try {
                            inputStream = ay.class.getClassLoader().getResourceAsStream(strArr[1] + str2);
                            if (inputStream != null) {
                                try {
                                    fileOutputStream = new FileOutputStream(new File(context.getCacheDir(), str2));
                                } catch (IOException e2) {
                                    e = e2;
                                    fileOutputStream = null;
                                } catch (Throwable th) {
                                    th = th;
                                    fileOutputStream = null;
                                }
                                try {
                                    try {
                                        byte[] bArr = new byte[1024];
                                        while (true) {
                                            int read = inputStream.read(bArr);
                                            if (read > 0) {
                                                fileOutputStream.write(bArr, 0, read);
                                            } else {
                                                fileOutputStream.flush();
                                                if (inputStream != null) {
                                                    inputStream.close();
                                                }
                                                fileOutputStream.close();
                                            }
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        inputStream2 = inputStream;
                                        if (inputStream2 != null) {
                                            try {
                                                inputStream2.close();
                                            } catch (IOException e3) {
                                                e3.printStackTrace();
                                                throw th;
                                            }
                                        }
                                        if (fileOutputStream != null) {
                                            fileOutputStream.close();
                                        }
                                        throw th;
                                    }
                                } catch (IOException e4) {
                                    e = e4;
                                    e.printStackTrace();
                                    if (inputStream != null) {
                                        inputStream.close();
                                    }
                                    if (fileOutputStream != null) {
                                        fileOutputStream.close();
                                    }
                                }
                            } else if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e5) {
                                    e5.printStackTrace();
                                }
                            }
                        } catch (IOException e6) {
                            inputStream = null;
                            e = e6;
                            fileOutputStream = null;
                        } catch (Throwable th3) {
                            th = th3;
                            fileOutputStream = null;
                        }
                    }
                    return;
                }
                c = str.charAt(i2);
                i4 = i;
                i5 = 88;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i3);
            i4 = i;
        }
    }

    public ay(String str, int i) {
        this.c = str;
        this.b = i;
        try {
            this.a = Typeface.createFromFile(TrosSense.INSTANCE.context.getCacheDir() + "/" + str);
        } catch (Exception e) {
            e.printStackTrace();
            this.a = null;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 107;
                    break;
                case 1:
                    i2 = 91;
                    break;
                case 2:
                    i2 = 53;
                    break;
                case 3:
                    i2 = 116;
                    break;
                case 4:
                    i2 = 16;
                    break;
                case 5:
                    i2 = 11;
                    break;
                default:
                    i2 = 47;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '/');
        }
        return charArray;
    }

    public Typeface a() {
        return this.a;
    }

    public int b() {
        return this.b;
    }

    public boolean equals(Object obj) {
        ay ayVar;
        String str;
        String str2;
        return (obj instanceof ay) && (str = (ayVar = (ay) obj).c) != null && (str2 = this.c) != null && str.equals(str2) && this.b == ayVar.b;
    }
}
