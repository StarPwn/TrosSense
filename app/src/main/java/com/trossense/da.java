package com.trossense;

import android.graphics.Color;
import android.opengl.GLES20;
import com.trossense.sdk.PointerHolder;
import io.netty.util.internal.StringUtil;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/* loaded from: classes3.dex */
public class da {
    private static db a;
    private static db b;
    private static db c;
    private static db d;
    private static db e;
    private static db f;
    private static int g;
    private static final long h = dj.a(3254987723383901127L, 733975251791871244L, MethodHandles.lookup().lookupClass()).a(106885290147272L);
    private static final String[] i;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004b. Please report as an issue. */
    static {
        int i2;
        int i3;
        String[] strArr = new String[17];
        b(112);
        String str = "C#`.|,`i\u001fi+a-ĭF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&2Q9}D\u000fr\u0018m0xY\u000e=M{1}P\u0013t*.)qUH&2Q,qU\u0013h#Q<{Z\u0013t|\u0004*z_\u001ai5c\u007f}X\b&2Q;}D\u0019e3g0z\rvp&|&}X\u001b&1k<&\u0016\nS15UaX\u0015`(|24E\u001dk7b:f\u00048&2]>yF\u0010c55UbY\u0015bgc>}XT/<\u0004\u007f4@\u0019es.<{Z\u0013tg3\u007fy_\u0004.2Q9}D\u000fr\u0018m0xY\u000e*g{\u0000gS\u001fi)j\u0000wY\u0010i5\"\u007fai\u0018o5k<`_\u0013hg0\u007f$\u0016C&1[):O\\<gx\nb\u0018\u0004/|\u0004\u007f4Q\u0010Y\u0001|>su\u0013j(|\u007f)\u0016\bc?z*fSNBo{\fu[\fj\"|sbc\n/g$\u007fwY\u0010i55Ui F\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&1M0xY\u000e=Mx>fO\u0015h .)qUN&1[)/<\ni.j\u007fyW\u0015ho'$\u001e\u0016\\a+Q\u0019fW\u001bE(b0f\u0016A&1M0xY\u000e&m.w%\u0018Qu*a0`^\u000fr\"~w$\u0018H>k>q!\u001a\u0010c)i+|\u001e\nS1.r4@\u0019eu&o:\u0003P&w j=\u001fU/|\u0004\"\rC#`.|,`i\u001fi+a-\u000bC#b.|:wB\u0015i)\u009dF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&1M0xY\u000e=Mx>fO\u0015h .)qUN&1[)/<\th.h0f[\\u&c/xS\u000e4\u0003.*GW\u0011v+k-/<\ni.j\u007fyW\u0015ho'$\u001e\u0016\\a+Q\u0019fW\u001bE(b0f\u0016A&3k'`C\u000ecuJwae\u001dk7b:f\u001a\nS1'\u007f>\u0016\nE(b0f\rv{\u0006C#u.t:\u000bC#b.|:wB\u0015i)\u0006C#u.t:ȁF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.9xY\u001drg{\u0000fW\u0018o2}d\u001eC\u0012o!a-y\u0016\nc$:\u007fai\u001ao5}+KU\u0013j(|d\u001eC\u0012o!a-y\u0016\nc$:\u007fai\u000fc$a1pi\u001fi+a-/<\th.h0f[\\p\"mm4C#u.t:/<\th.h0f[\\o)z\u007fai\u0018o5k<`_\u0013h|\u0004)uD\u0005o)i\u007fbS\u001f4gx\nb\rvp(g;4[\u001do)&vo<\\&1k< \u0016\u001fi+a-4\u000b\\k.vwai\u001ao5}+KU\u0013j(|s4C#u\"m0zR#e(b0f\u001a\\s\u0018j6fS\u001fr.a14\b\\6g1\u007fbc\n(>.e4@)pivv/<\\& b\u0000RD\u001da\u0004a3{D\\;gx:w\u0002Te(b0f\u0018\u000ea%\"\u007fwY\u0010i5 >4\u001c\\u*a0`^\u000fr\"~w%\u0018L*g>q$\u001a\\j\"`8`^Tk&vw<W\u001euox\nb\u0016Q&w j=\u0016W&w j=\u0016V&2Q,}L\u0019&j.*KE\u0015|\".t4C#t&j6aEP&w o=\u001f\\+g{\u0000fW\u0018o2}\u007f?\u0016L(r'v/<\\&.hwsZ#@5o8WY\u0010i5 >4\nA&w o=Mv&g.\u007fp_\u000fe&|;/<\\&:k3gS\u0007\fg.\u007f4Q\u0010Y\u0001|>su\u0013j(|qu\u0016A&v o4\u001c\\e(b0f\u0018\u001d=M.\u007fi<\u0001\bC/g*~3qDŠF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.9xY\u001drg{\u0000fW\u0018o2}d\u001eC\u0012o!a-y\u0016\nc$<\u007fai\u000fo=kd\u001eC\u0012o!a-y\u0016\nc$:\u007fbu\u0013j(|d\u001e@\u001dt>g1s\u0016\nc$<\u007fbc\n=M{1}P\u0013t*.,u[\fj\"|mP\u0016\tU&c/xS\u000e=Mx0}R\\k&g1<\u001f\u0007\fg.\u007f4@\u0019es.<{Z\\;gz:lB\tt\"<\u001b<C/g*~3qDPp\u0012xv/<\\&g.8xi:t&i\u001c{Z\u0013tg3\u007fbS\u001f2om0x\u0018\u000ea%\"\u007fwY\u0010(&.u4E\u0011i(z7gB\u0019vo?q$\u001a\\6i>s4Z\u0019h z7<[\u001d~o&>vETp\u0012x\u007f9\u0016L(r'\u007f?\u0016L(r'\u007f>\u0016\tY4g%q\u0016Q&2Q,}L\u0019&l.*KD\u001db.{,8\u0016L(w'v4\u001b\\s\u0018|>p_\tug%\u007f$\u0018I/n.u4@?i+a-/<\u0001\bC#t&j6aE\u000eC#u\"m0zR#e(b0f\bC/g*~3qD";
        int length = "C#`.|,`i\u001fi+a-ĭF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&2Q9}D\u000fr\u0018m0xY\u000e=M{1}P\u0013t*.)qUH&2Q,qU\u0013h#Q<{Z\u0013t|\u0004*z_\u001ai5c\u007f}X\b&2Q;}D\u0019e3g0z\rvp&|&}X\u001b&1k<&\u0016\nS15UaX\u0015`(|24E\u001dk7b:f\u00048&2]>yF\u0010c55UbY\u0015bgc>}XT/<\u0004\u007f4@\u0019es.<{Z\u0013tg3\u007fy_\u0004.2Q9}D\u000fr\u0018m0xY\u000e*g{\u0000gS\u001fi)j\u0000wY\u0010i5\"\u007fai\u0018o5k<`_\u0013hg0\u007f$\u0016C&1[):O\\<gx\nb\u0018\u0004/|\u0004\u007f4Q\u0010Y\u0001|>su\u0013j(|\u007f)\u0016\bc?z*fSNBo{\fu[\fj\"|sbc\n/g$\u007fwY\u0010i55Ui F\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&1M0xY\u000e=Mx>fO\u0015h .)qUN&1[)/<\ni.j\u007fyW\u0015ho'$\u001e\u0016\\a+Q\u0019fW\u001bE(b0f\u0016A&1M0xY\u000e&m.w%\u0018Qu*a0`^\u000fr\"~w$\u0018H>k>q!\u001a\u0010c)i+|\u001e\nS1.r4@\u0019eu&o:\u0003P&w j=\u001fU/|\u0004\"\rC#`.|,`i\u001fi+a-\u000bC#b.|:wB\u0015i)\u009dF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.)qUH&1M0xY\u000e=Mx>fO\u0015h .)qUN&1[)/<\th.h0f[\\u&c/xS\u000e4\u0003.*GW\u0011v+k-/<\ni.j\u007fyW\u0015ho'$\u001e\u0016\\a+Q\u0019fW\u001bE(b0f\u0016A&3k'`C\u000ecuJwae\u001dk7b:f\u001a\nS1'\u007f>\u0016\nE(b0f\rv{\u0006C#u.t:\u000bC#b.|:wB\u0015i)\u0006C#u.t:ȁF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.9xY\u001drg{\u0000fW\u0018o2}d\u001eC\u0012o!a-y\u0016\nc$:\u007fai\u001ao5}+KU\u0013j(|d\u001eC\u0012o!a-y\u0016\nc$:\u007fai\u000fc$a1pi\u001fi+a-/<\th.h0f[\\p\"mm4C#u.t:/<\th.h0f[\\o)z\u007fai\u0018o5k<`_\u0013h|\u0004)uD\u0005o)i\u007fbS\u001f4gx\nb\rvp(g;4[\u001do)&vo<\\&1k< \u0016\u001fi+a-4\u000b\\k.vwai\u001ao5}+KU\u0013j(|s4C#u\"m0zR#e(b0f\u001a\\s\u0018j6fS\u001fr.a14\b\\6g1\u007fbc\n(>.e4@)pivv/<\\& b\u0000RD\u001da\u0004a3{D\\;gx:w\u0002Te(b0f\u0018\u000ea%\"\u007fwY\u0010i5 >4\u001c\\u*a0`^\u000fr\"~w%\u0018L*g>q$\u001a\\j\"`8`^Tk&vw<W\u001euox\nb\u0016Q&w j=\u0016W&w j=\u0016V&2Q,}L\u0019&j.*KE\u0015|\".t4C#t&j6aEP&w o=\u001f\\+g{\u0000fW\u0018o2}\u007f?\u0016L(r'v/<\\&.hwsZ#@5o8WY\u0010i5 >4\nA&w o=Mv&g.\u007fp_\u000fe&|;/<\\&:k3gS\u0007\fg.\u007f4Q\u0010Y\u0001|>su\u0013j(|qu\u0016A&v o4\u001c\\e(b0f\u0018\u001d=M.\u007fi<\u0001\bC/g*~3qDŠF\u000ec$g,}Y\u0012&*k;}C\u0011vgh3{W\b=M{1}P\u0013t*.9xY\u001drg{\u0000fW\u0018o2}d\u001eC\u0012o!a-y\u0016\nc$<\u007fai\u000fo=kd\u001eC\u0012o!a-y\u0016\nc$:\u007fbu\u0013j(|d\u001e@\u001dt>g1s\u0016\nc$<\u007fbc\n=M{1}P\u0013t*.,u[\fj\"|mP\u0016\tU&c/xS\u000e=Mx0}R\\k&g1<\u001f\u0007\fg.\u007f4@\u0019es.<{Z\\;gz:lB\tt\"<\u001b<C/g*~3qDPp\u0012xv/<\\&g.8xi:t&i\u001c{Z\u0013tg3\u007fbS\u001f2om0x\u0018\u000ea%\"\u007fwY\u0010(&.u4E\u0011i(z7gB\u0019vo?q$\u001a\\6i>s4Z\u0019h z7<[\u001d~o&>vETp\u0012x\u007f9\u0016L(r'\u007f?\u0016L(r'\u007f>\u0016\tY4g%q\u0016Q&2Q,}L\u0019&l.*KD\u001db.{,8\u0016L(w'v4\u001b\\s\u0018|>p_\tug%\u007f$\u0018I/n.u4@?i+a-/<\u0001\bC#t&j6aE\u000eC#u\"m0zR#e(b0f\bC/g*~3qD".length();
        char c2 = StringUtil.CARRIAGE_RETURN;
        int i4 = 0;
        int i5 = -1;
        while (true) {
            int i6 = 85;
            int i7 = i5 + 1;
            String substring = str.substring(i7, i7 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i6, a(substring));
                i2 = i4 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i4] = a2;
                        i5 = i7 + c2;
                        if (i5 < length) {
                            break;
                        }
                        str = "\u0003c5b-p:\u0012c%h\"p&\b\u0003c4f*v!\u0005";
                        length = "\u0003c5b-p:\u0012c%h\"p&\b\u0003c4f*v!\u0005".length();
                        c2 = 14;
                        i3 = -1;
                        i4 = i2;
                        i6 = 21;
                        i7 = i3 + 1;
                        substring = str.substring(i7, i7 + c2);
                        z = false;
                        break;
                }
                strArr[i4] = a2;
                i3 = i7 + c2;
                if (i3 >= length) {
                    i = strArr;
                    a = dc.a();
                    b = dc.a(strArr[2]);
                    c = dc.a(strArr[9]);
                    d = dc.a(strArr[5]);
                    e = dc.a(strArr[11]);
                    f = dc.a(strArr[1]);
                    return;
                }
                c2 = str.charAt(i3);
                i4 = i2;
                i6 = 21;
                i7 = i3 + 1;
                substring = str.substring(i7, i7 + c2);
                z = false;
            }
            c2 = str.charAt(i5);
            i4 = i2;
        }
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c2 = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 99;
                    break;
                case 1:
                    i3 = 41;
                    break;
                case 2:
                    i3 = 83;
                    break;
                case 3:
                    i3 = 18;
                    break;
                case 4:
                    i3 = 91;
                    break;
                case 5:
                    i3 = 10;
                    break;
                default:
                    i3 = 65;
                    break;
            }
            cArr[i4] = (char) (c2 ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    public static FloatBuffer a(float[] fArr) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fArr.length * 32);
        allocateDirect.order(ByteOrder.nativeOrder());
        FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
        asFloatBuffer.put(fArr);
        asFloatBuffer.position(0);
        return asFloatBuffer;
    }

    public static void a() {
        GLES20.glDisable(3089);
    }

    public static void a(float f2, float f3, float f4, float f5) {
        GLES20.glEnable(3089);
        GLES20.glScissor((int) f2, (int) ((f.b - f3) - f5), (int) f4, (int) f5);
    }

    public static void a(float f2, float f3, float f4, float f5, float f6, long j, int i2, int i3) {
        a(f2, f3, f4, f5, f6, (h ^ j) ^ 53073164975870L, i2, i3, true);
    }

    public static void a(float f2, float f3, float f4, float f5, float f6, long j, int i2, int i3, boolean z) {
        long j2 = h ^ j;
        long j3 = 688081792450L ^ j2;
        int i4 = (int) (j3 >>> 32);
        int i5 = (int) ((j3 << 32) >>> 32);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        c.a();
        int d2 = d();
        db dbVar = c;
        String[] strArr = i;
        dbVar.a(strArr[6], i4, i5, f4, f5);
        c.a(strArr[16], i4, i5, f6);
        c.a(strArr[3], i4, i5, Color.red(i2) / 255.0f, Color.green(i2) / 255.0f, Color.blue(i2) / 255.0f, Color.alpha(i2) / 255.0f);
        c.a(strArr[13], i4, i5, Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f, Color.alpha(i3) / 255.0f);
        c.a(strArr[4], 20265400591114L ^ j2, z ? 1 : 0);
        c.a(f2, f3, f4, f5);
        c.c(79927664989741L ^ j2);
        if (j2 >= 0) {
            if (PointerHolder.s() != null) {
                return;
            } else {
                d2++;
            }
        }
        b(d2);
    }

    public static void a(float f2, float f3, float f4, float f5, int i2, long j) {
        b(f2, f3, f4, f5, 0.0f, (h ^ j) ^ 2643911149882L, i2);
    }

    public static void a(float f2, float f3, float f4, long j, float f5, float f6) {
        long j2 = h ^ j;
        long j3 = 31210441747622L ^ j2;
        c_.a(130602819775059L ^ j2);
        float f7 = (f4 / 2.0f) + f2;
        float f8 = (f5 / 2.0f) + f3;
        c_.a(j3, f7, f8, 0.0f);
        c_.a(f6, j2 ^ 108089580011812L, 0.0f, 0.0f, 1.0f);
        c_.a(j3, -f7, -f8, 0.0f);
    }

    public static void a(float f2, float f3, long j, float f4) {
        long j2 = j ^ h;
        long j3 = 97174296953930L ^ j2;
        c_.a(55850925638335L ^ j2);
        c_.a(j3, f2, f3, 0.0f);
        c_.b(f4, j2 ^ 5789314923060L, f4, 0.0f);
        c_.a(j3, -f2, -f3, 0.0f);
    }

    public static void a(float f2, float f3, long j, float f4, float f5, float f6, int i2) {
        a(f2, f3, f4, f5, f6, (h ^ j) ^ 40061277002533L, i2, i2);
    }

    public static void a(int i2, float f2, float f3, float f4, float f5, int i3, int i4, boolean z, long j) {
        long j2 = h ^ j;
        long j3 = 3915708026381L ^ j2;
        long j4 = 18974875893957L ^ j2;
        int i5 = (int) (j4 >>> 32);
        int i6 = (int) ((j4 << 32) >>> 32);
        int e2 = e();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        f.a();
        GLES20.glEnable(3553);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i2);
        db dbVar = f;
        String[] strArr = i;
        dbVar.a(strArr[0], i5, i6, Color.red(i3) / 255.0f, Color.green(i3) / 255.0f, Color.blue(i3) / 255.0f, Color.alpha(i3) / 255.0f);
        f.a(strArr[15], i5, i6, Color.red(i4) / 255.0f, Color.green(i4) / 255.0f, Color.blue(i4) / 255.0f, Color.alpha(i4) / 255.0f);
        f.a(strArr[7], j3, z ? 1 : 0);
        f.a(strArr[10], j3, 0);
        f.a(f2, f3, f4, f5);
        GLES20.glBindTexture(3553, 0);
        f.c(98201314425642L ^ j2);
        if (j2 >= 0) {
            if (e2 == 0) {
                return;
            } else {
                e2 = 2;
            }
        }
        PointerHolder.b(new int[e2]);
    }

    public static void a(int i2, float f2, float f3, float f4, float f5, int i3, long j) {
        long j2 = j ^ h;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        d.a();
        GLES20.glEnable(3553);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i2);
        d.a(i[10], 55814892302874L ^ j2, 0);
        d.a(i3);
        d.a(f2, f3, f4, f5);
        GLES20.glBindTexture(3553, 0);
        d.c(j2 ^ 114472131790653L);
    }

    public static void a(int i2, float f2, float f3, float f4, long j, float f5, float f6, int i3) {
        long j2 = h ^ j;
        long j3 = 64094173613652L ^ j2;
        long j4 = 106192569891699L ^ j2;
        long j5 = j2 ^ 44552948629660L;
        int i4 = (int) (j5 >>> 32);
        int i5 = (int) ((j5 << 32) >>> 32);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        e.a();
        GLES20.glEnable(3553);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, i2);
        db dbVar = e;
        String[] strArr = i;
        dbVar.a(strArr[8], i4, i5, f4, f5);
        e.a(strArr[14], j3, 0);
        e.a(strArr[12], i4, i5, f6);
        e.a(i3);
        e.a(f2, f3, f4, f5);
        GLES20.glBindTexture(3553, 0);
        e.c(j4);
    }

    public static void a(int i2, float f2, long j, float f3, float f4, float f5) {
        a(i2, f2, f3, f4, f5, -1, (h ^ j) ^ 89431645745943L);
    }

    public static void a(int i2, int i3, float f2, float f3, float f4, float f5, float f6, int i4, byte b2) {
        a(i2, f2, f3, f4, ((((i3 << 32) | ((i4 << 40) >>> 32)) | ((b2 << 56) >>> 56)) ^ h) ^ 16914407037969L, f5, f6, -1);
    }

    public static void a(long j, float f2, float f3, float f4, int i2) {
        long j2 = (j ^ h) ^ 108766929238424L;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        float f5 = f2 - f4;
        float f6 = f3 - f4;
        float f7 = f4 * 2.0f;
        b.a();
        b.a(i2);
        b.a(f5, f6, f7, f7);
        b.c(j2);
    }

    public static void a(String str, float f2, float f3, float f4, float f5, float f6, int i2, long j) {
        long j2 = h ^ j;
        long j3 = j2 ^ 30309033070720L;
        int a2 = de.a(j2 ^ 128044687272385L, str);
        if (a2 == 0) {
            return;
        }
        a(a2, f2, f3, f4, j3, f5, f6, i2);
    }

    public static void a(String str, float f2, float f3, float f4, long j, float f5) {
        a(str, f2, f3, f4, (h ^ j) ^ 35600134402750L, f5, -1);
    }

    public static void a(String str, float f2, float f3, float f4, long j, float f5, float f6) {
        a(str, f2, f3, f4, f5, f6, -1, (h ^ j) ^ 139843882489536L);
    }

    public static void a(String str, float f2, float f3, float f4, long j, float f5, int i2) {
        long j2 = h ^ j;
        long j3 = j2 ^ 121724863418206L;
        int a2 = de.a(10829200493137L ^ j2, str);
        if (a2 == 0) {
            return;
        }
        a(a2, f2, f3, f4, f5, i2, j3);
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'A');
        }
        return charArray;
    }

    public static void b(float f2, float f3, float f4, float f5, float f6, long j, int i2) {
        long j2 = (j ^ h) ^ 8370574878296L;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        if (f6 > 0.0f) {
            GLES20.glLineWidth(f6);
        }
        a.a();
        a.e();
        a.a(i2);
        a.a(new float[]{f2, f3, f4, f5}, 2);
        GLES20.glDrawArrays(1, 0, 2);
        a.c(j2);
    }

    public static void b(int i2) {
        g = i2;
    }

    public static void b(long j, byte b2) {
        c_.c((((j << 8) | ((b2 << 56) >>> 56)) ^ h) ^ 96104642510907L);
    }

    public static void b(long j, float f2, float f3, float f4, float f5, int i2) {
        long j2 = (j ^ h) ^ 78546056029798L;
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        float f6 = f4 + f2;
        float f7 = f3 + f5;
        a.a();
        a.e();
        a.a(i2);
        a.a(new float[]{f2, f3, f6, f3, f6, f7, f2, f7}, 2);
        GLES20.glDrawArrays(6, 0, 4);
        a.c(j2);
    }

    public static void c(long j, short s) {
        c_.c((((j << 16) | ((s << 48) >>> 48)) ^ h) ^ 30917227110120L);
    }

    public static int d() {
        return g;
    }

    public static int e() {
        return d() == 0 ? 123 : 0;
    }
}
