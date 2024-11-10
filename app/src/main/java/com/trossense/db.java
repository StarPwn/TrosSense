package com.trossense;

import android.graphics.Color;
import android.opengl.GLES20;
import java.lang.invoke.MethodHandles;
import java.nio.Buffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class db {
    private static final long b = dj.a(6701622718617574331L, 7625651734375536079L, MethodHandles.lookup().lookupClass()).a(269808530277457L);
    private static final String[] c;
    private final int a;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "\u000fc%\u0010\u00184I\u0016]\u000b\fl\u00075!\rA\rA#\u001b\t\u000fc%\u0010\u00184I\u0016]\u0003\u0018f<";
        int length = "\u000fc%\u0010\u00184I\u0016]\u000b\fl\u00075!\rA\rA#\u001b\t\u000fc%\u0010\u00184I\u0016]\u0003\u0018f<".length();
        char c2 = '\t';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 17;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "\u000fq+\u0006\u0018g2\u0018\t%";
                        length = "\u000fq+\u0006\u0018g2\u0018\t%".length();
                        c2 = 3;
                        i2 = -1;
                        i3 = i;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        i5 = 6;
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c2;
                if (i2 >= length) {
                    c = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                i5 = 6;
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public db(int i) {
        this.a = i;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 104;
                    break;
                case 1:
                    i2 = 34;
                    break;
                case 2:
                    i2 = 91;
                    break;
                case 3:
                    i2 = 114;
                    break;
                case 4:
                    i2 = 96;
                    break;
                case 5:
                    i2 = 81;
                    break;
                default:
                    i2 = 49;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '1');
        }
        return charArray;
    }

    public void a() {
        GLES20.glUseProgram(this.a);
    }

    public void a(float f, float f2, float f3, float f4) {
        GLES20.glEnable(3553);
        float f5 = f4 + f2;
        float f6 = f3 + f;
        e();
        a(new float[]{f, f5, f6, f5, f6, f2, f, f2}, 2);
        d();
        GLES20.glDrawArrays(6, 0, 4);
        GLES20.glDisable(3553);
    }

    public void a(int i) {
        GLES20.glUniform4f(GLES20.glGetUniformLocation(this.a, c[5]), Color.red(i) / 255.0f, Color.green(i) / 255.0f, Color.blue(i) / 255.0f, Color.alpha(i) / 255.0f);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000a. Please report as an issue. */
    public void a(String str, int i, int i2, float... fArr) {
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.a, str);
        switch (fArr.length) {
            case 1:
                GLES20.glUniform1f(glGetUniformLocation, fArr[0]);
                if (i >= 0) {
                    return;
                }
            case 2:
                GLES20.glUniform2f(glGetUniformLocation, fArr[0], fArr[1]);
                if (i >= 0) {
                    return;
                }
            case 3:
                GLES20.glUniform3f(glGetUniformLocation, fArr[0], fArr[1], fArr[2]);
                if (i >= 0) {
                    return;
                }
            case 4:
                GLES20.glUniform4f(glGetUniformLocation, fArr[0], fArr[1], fArr[2], fArr[3]);
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000e. Please report as an issue. */
    public void a(String str, long j, int... iArr) {
        int e = da.e();
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.a, str);
        switch (iArr.length) {
            case 1:
                GLES20.glUniform1i(glGetUniformLocation, iArr[0]);
                if (e == 0) {
                    return;
                }
            case 2:
                GLES20.glUniform2i(glGetUniformLocation, iArr[0], iArr[1]);
                if (e == 0) {
                    return;
                }
            case 3:
                GLES20.glUniform3i(glGetUniformLocation, iArr[0], iArr[1], iArr[2]);
                if (e == 0) {
                    return;
                }
            case 4:
                GLES20.glUniform4i(glGetUniformLocation, iArr[0], iArr[1], iArr[2], iArr[3]);
                return;
            default:
                return;
        }
    }

    public void a(float[] fArr, int i) {
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.a, c[2]);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation);
        GLES20.glVertexAttribPointer(glGetAttribLocation, i, 5126, false, 0, (Buffer) da.a(fArr));
    }

    public int b() {
        return this.a;
    }

    public void c(long j) {
        int i = this.a;
        String[] strArr = c;
        int glGetAttribLocation = GLES20.glGetAttribLocation(i, strArr[0]);
        if (glGetAttribLocation != -1) {
            GLES20.glDisableVertexAttribArray(glGetAttribLocation);
        }
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.a, strArr[4]);
        if (glGetAttribLocation2 != -1) {
            GLES20.glDisableVertexAttribArray(glGetAttribLocation2);
        }
        GLES20.glUseProgram(0);
    }

    public void d() {
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.a, c[3]);
        GLES20.glEnableVertexAttribArray(glGetAttribLocation);
        GLES20.glVertexAttribPointer(glGetAttribLocation, 2, 5126, false, 0, (Buffer) da.a(new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f}));
    }

    public void e() {
        GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(this.a, c[1]), 1, false, c_.e(), 0);
    }
}
