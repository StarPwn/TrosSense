package com.trossense.hook;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.List;

/* loaded from: classes3.dex */
public class HopeHookEntrance {
    private static final long a = dj.a(5326892347959931516L, -4724931861202439129L, MethodHandles.lookup().lookupClass()).a(227292570860369L);
    private static int[] b;
    private static final String[] c;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0049. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "-G>\u001a\u000b\rR$\t;T\"\fV\tL<C\t;T\"\fV\tL<C\r,I QQ\u001eM<U(\u0011V\t";
        int length = "-G>\u001a\u000b\rR$\t;T\"\fV\tL<C\t;T\"\fV\tL<C\r,I QQ\u001eM<U(\u0011V\t".length();
        b(null);
        char c2 = '\b';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 60;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
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
                        str = "Eq\u0006\r\u0007k\u000bt`:k]6F7h*";
                        length = "Eq\u0006\r\u0007k\u000bt`:k]6F7h*".length();
                        c2 = 3;
                        i2 = -1;
                        i3 = i;
                        i5 = 24;
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
                i5 = 24;
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
                    i2 = 115;
                    break;
                case 1:
                    i2 = 26;
                    break;
                case 2:
                    i2 = 113;
                    break;
                case 3:
                    i2 = 67;
                    break;
                case 4:
                    i2 = 25;
                    break;
                case 5:
                    i2 = 80;
                    break;
                default:
                    i2 = 30;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static String a(Activity activity, long j, String str, String str2) {
        long j2 = j ^ a;
        PackageManager packageManager = activity.getApplicationContext().getPackageManager();
        int[] b2 = b();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        if (installedPackages.size() <= 0) {
            return null;
        }
        for (PackageInfo packageInfo : installedPackages) {
            String str3 = packageInfo.applicationInfo.publicSourceDir;
            if (j2 >= 0) {
                if (str3.indexOf(str) != -1) {
                    str3 = packageInfo.applicationInfo.publicSourceDir;
                } else if (b2 != null) {
                    return null;
                }
            }
            String[] strArr = c;
            return str3.replace(strArr[0], strArr[5] + str2 + strArr[4]);
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0044, code lost:            if (r1 != null) goto L7;     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void a(long r8, android.app.Activity r10, int r11) {
        /*
            r0 = 32
            long r1 = r8 << r0
            long r3 = (long) r11
            long r3 = r3 << r0
            long r3 = r3 >>> r0
            long r1 = r1 | r3
            long r3 = com.trossense.hook.HopeHookEntrance.a
            long r1 = r1 ^ r3
            r3 = 134036253343119(0x79e7bf447d8f, double:6.6222708074109E-310)
            long r3 = r3 ^ r1
            r5 = 71460243677514(0x40fe2270a14a, double:3.5306051444504E-310)
            long r1 = r1 ^ r5
            r11 = 48
            long r5 = r1 >>> r11
            int r5 = (int) r5
            r6 = 16
            long r6 = r1 << r6
            long r6 = r6 >>> r0
            int r0 = (int) r6
            long r1 = r1 << r11
            long r1 = r1 >>> r11
            int r11 = (int) r1
            int[] r1 = b()
            com.trossense.clients.TrosSense r2 = com.trossense.clients.TrosSense.INSTANCE
            r2.activity = r10
            java.lang.String[] r2 = com.trossense.hook.HopeHookEntrance.c
            r6 = 3
            r6 = r2[r6]
            r7 = 2
            r7 = r2[r7]
            java.lang.String r3 = a(r10, r3, r6, r7)
            if (r3 == 0) goto L46
            java.lang.System.load(r3)
            r3 = 0
            int r8 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r8 < 0) goto L69
            if (r1 == 0) goto L4c
        L46:
            r8 = 1
            r8 = r2[r8]
            java.lang.System.loadLibrary(r8)
        L4c:
            com.trossense.sdk.InstanceGenerator.a()
            com.trossense.clients.TrosSense r8 = com.trossense.clients.TrosSense.INSTANCE
            short r9 = (short) r5
            short r11 = (short) r11
            r8.b(r9, r0, r11)
            android.os.Handler r8 = new android.os.Handler
            android.os.Looper r9 = android.os.Looper.getMainLooper()
            r8.<init>(r9)
            com.trossense.hook.HopeHookEntrance$1 r9 = new com.trossense.hook.HopeHookEntrance$1
            r9.<init>(r10)
            r10 = 5000(0x1388, double:2.4703E-320)
            r8.postDelayed(r9, r10)
        L69:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.hook.HopeHookEntrance.a(long, android.app.Activity, int):void");
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 30);
        }
        return charArray;
    }

    public static void b(int[] iArr) {
        b = iArr;
    }

    public static int[] b() {
        return b;
    }
}
