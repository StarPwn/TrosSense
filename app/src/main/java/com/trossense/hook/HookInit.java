package com.trossense.hook;

import android.app.Activity;
import android.content.Context;
import com.trossense.clients.TrosSense;
import com.trossense.dj;
import com.trossense.sdk.PointerHolder;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.lang.invoke.MethodHandles;

/* loaded from: classes3.dex */
public class HookInit implements IXposedHookLoadPackage {
    public static ClassLoader a;
    private static boolean b;
    private static final long c = dj.a(5282651997398247387L, -4402855549273098760L, MethodHandles.lookup().lookupClass()).a(135665653236465L);
    private static final String[] d;

    static {
        String[] strArr = new String[3];
        int length = ",)1\u000b\u0005)?#22\u0000\u00187?n3<\u0011!4'\u0007\u000f/\u0018!36%\u0003).%8'##/>H\u0002\".%! \u0003B&4$2<\u000f\bi*2/'\u0003\u000f3t\u00134&\u0004-7*".length();
        char c2 = 17;
        int i = -1;
        int i2 = 0;
        while (true) {
            int i3 = i + 1;
            int i4 = c2 + i3;
            int i5 = i2 + 1;
            strArr[i2] = a(104, a(",)1\u000b\u0005)?#22\u0000\u00187?n3<\u0011!4'\u0007\u000f/\u0018!36%\u0003).%8'##/>H\u0002\".%! \u0003B&4$2<\u000f\bi*2/'\u0003\u000f3t\u00134&\u0004-7*".substring(i3, i4)));
            if (i4 >= length) {
                d = strArr;
                b = false;
                return;
            } else {
                i2 = i5;
                i = i4;
                c2 = ",)1\u000b\u0005)?#22\u0000\u00187?n3<\u0011!4'\u0007\u000f/\u0018!36%\u0003).%8'##/>H\u0002\".%! \u0003B&4$2<\u000f\bi*2/'\u0003\u000f3t\u00134&\u0004-7*".charAt(i4);
            }
        }
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c2 = cArr[i2];
            int i3 = 40;
            switch (i2 % 7) {
                case 0:
                case 1:
                    break;
                case 2:
                    i3 = 59;
                    break;
                case 3:
                    i3 = 14;
                    break;
                case 4:
                    i3 = 4;
                    break;
                case 5:
                    i3 = 47;
                    break;
                default:
                    i3 = 50;
                    break;
            }
            cArr[i2] = (char) (c2 ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    static boolean a() {
        return b;
    }

    private boolean a(long j, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        long j2 = j ^ c;
        if (loadPackageParam.appInfo == null) {
            return false;
        }
        boolean exists = new File(loadPackageParam.appInfo.nativeLibraryDir, d[0]).exists();
        return j2 > 0 ? exists : exists;
    }

    static boolean a(boolean z) {
        b = z;
        return z;
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '2');
        }
        return charArray;
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (a((c ^ 140197088900859L) ^ 35933785500126L, loadPackageParam)) {
            String[] strArr = d;
            XposedHelpers.findAndHookMethod(strArr[2], loadPackageParam.classLoader, strArr[1], new Object[]{Context.class, new XC_MethodHook(this) { // from class: com.trossense.hook.HookInit.1
                private static final long b = dj.a(3515111081593542923L, -4606461175048963736L, MethodHandles.lookup().lookupClass()).a(111093742676628L);
                private static final String[] c;
                final HookInit a;

                static {
                    String[] strArr2 = new String[2];
                    int length = "\u0001@KB\u0017/\u001b\u0003AAB\u0017)\u001f\u0007LT\r\u001c4\u0001\u0007\u0001k\r\u0013.0\u0001[O\u001a\u00134\b\b\rAt\t\t5\u001c\u0007".length();
                    int i = 0;
                    char c2 = '#';
                    int i2 = -1;
                    while (true) {
                        int i3 = i2 + 1;
                        int i4 = c2 + i3;
                        int i5 = i + 1;
                        strArr2[i] = a(71, a("\u0001@KB\u0017/\u001b\u0003AAB\u0017)\u001f\u0007LT\r\u001c4\u0001\u0007\u0001k\r\u0013.0\u0001[O\u001a\u00134\b\b\rAt\t\t5\u001c\u0007".substring(i3, i4)));
                        if (i4 >= length) {
                            c = strArr2;
                            return;
                        } else {
                            i2 = i4;
                            c2 = "\u0001@KB\u0017/\u001b\u0003AAB\u0017)\u001f\u0007LT\r\u001c4\u0001\u0007\u0001k\r\u0013.0\u0001[O\u001a\u00134\b\b\rAt\t\t5\u001c\u0007".charAt(i4);
                            i = i5;
                        }
                    }
                }

                {
                    this.a = this;
                }

                private static String a(int i, char[] cArr) {
                    int i2;
                    int length = cArr.length;
                    for (int i3 = 0; length > i3; i3++) {
                        char c2 = cArr[i3];
                        switch (i3 % 7) {
                            case 0:
                                i2 = 37;
                                break;
                            case 1:
                                i2 = 104;
                                break;
                            case 2:
                                i2 = 97;
                                break;
                            case 3:
                                i2 = 43;
                                break;
                            case 4:
                                i2 = 61;
                                break;
                            case 5:
                                i2 = 7;
                                break;
                            default:
                                i2 = 54;
                                break;
                        }
                        cArr[i3] = (char) (c2 ^ (i2 ^ i));
                    }
                    return new String(cArr).intern();
                }

                private static char[] a(String str) {
                    char[] charArray = str.toCharArray();
                    if (charArray.length < 2) {
                        charArray[0] = (char) (charArray[0] ^ '6');
                    }
                    return charArray;
                }

                protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
                    int[] b2 = HopeHookEntrance.b();
                    super.afterHookedMethod(methodHookParam);
                    Context context = (Context) methodHookParam.args[0];
                    TrosSense.INSTANCE.context = context;
                    HookInit.a = context.getClassLoader();
                    String[] strArr2 = c;
                    XposedHelpers.findAndHookMethod(strArr2[0], HookInit.a, strArr2[1], new Object[]{new XC_MethodHook(this) { // from class: com.trossense.hook.HookInit.1.1
                        private static final long b = dj.a(245465036346390156L, -1454809708753495252L, MethodHandles.lookup().lookupClass()).a(31306502084442L);
                        final AnonymousClass1 a;

                        {
                            this.a = this;
                        }

                        public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam2) throws Throwable {
                            long j = (b ^ 85742381532638L) ^ 23220660962409L;
                            long j2 = j >>> 32;
                            int i = (int) ((j << 32) >>> 32);
                            super.afterHookedMethod(methodHookParam2);
                            if (HookInit.a()) {
                                return;
                            }
                            HopeHookEntrance.a(j2, (Activity) methodHookParam2.thisObject, i);
                            HookInit.a(true);
                        }
                    }});
                    if (b2 != null) {
                        PointerHolder.b(new int[1]);
                    }
                }
            }});
        }
    }
}
