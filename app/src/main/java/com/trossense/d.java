package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.function.Function;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes3.dex */
public final class d {
    public static final d Decelerate;
    public static final d EASE_IN_BACK;
    public static final d EASE_IN_CIRC;
    public static final d EASE_IN_CUBIC;
    public static final d EASE_IN_EXPO;
    public static final d EASE_IN_OUT_CIRC;
    public static final d EASE_IN_OUT_CUBIC;
    public static final d EASE_IN_OUT_EXPO;
    public static final d EASE_IN_OUT_QUAD;
    public static final d EASE_IN_OUT_QUART;
    public static final d EASE_IN_OUT_QUINT;
    public static final d EASE_IN_OUT_SINE;
    public static final d EASE_IN_QUAD;
    public static final d EASE_IN_QUART;
    public static final d EASE_IN_QUINT;
    public static final d EASE_IN_SINE;
    public static final d EASE_OUT_CIRC;
    public static final d EASE_OUT_CUBIC;
    public static final d EASE_OUT_ELASTIC;
    public static final d EASE_OUT_EXPO;
    public static final d EASE_OUT_QUAD;
    public static final d EASE_OUT_QUART;
    public static final d EASE_OUT_QUINT;
    public static final d EASE_OUT_SINE;
    public static final d LINEAR;
    public static final d SIGMOID;
    public static final d SMOOTH;
    private static final d[] b;
    private static final long c = dj.a(935321998315782859L, -4472194899910037313L, MethodHandles.lookup().lookupClass()).a(1340416556378L);
    private final Function<Double, Double> a;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0047. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[27];
        String str = "\u000bf|\u00110yQ\u0011hz\u00000sV\u001cd\u0011\u000bf|\u00110yQ\u0011hz\u00000aJ\u0007i{\u0010\u000bf|\u00110yQ\u0011hz\u00000cV\u0000b\u000e\u000bf|\u00110\u007fJ\u001ax~\u0001.bK\r\u000bf|\u00110yQ\u0011vz\u0015=d\f\u000bf|\u00110yQ\u0011vz\u0015+\f\u000bf|\u00110yQ\u0011bw\u0004 \u0011\u000bf|\u00110yQ\u0011hz\u00000sJ\fnl\u0010\u000bf|\u00110yQ\u0011hz\u00000uG\u001eh\u000e\u000bf|\u00110\u007fJ\u001axl\u0001-y\\\u0007\u001dnh\u0019 y[\r\u000bf|\u00110\u007fJ\u001ax~\u0001.t\u0006\u0002na\u0011.b\r\u000bf|\u00110yQ\u0011dz\u0016&s\f\u000bf|\u00110yQ\u0011en\u0017$\u0010\u000bf|\u00110\u007fJ\u001axj\u0018.cK\u0007d\f\u000bf|\u00110yQ\u0011tf\u001a*\r\u000bf|\u00110\u007fJ\u001axj\f?\u007f\u0006\u001dj`\u001b;x\n\nBL1\u0003Um/SJ\f\u000bf|\u00110yQ\u0011df\u0006,\u000e\u000bf|\u00110\u007fJ\u001ax~\u0001&~K\u0010\u000bf|\u00110yQ\u0011hz\u00000aJ\u000fc\u0011\u000bf|\u00110yQ\u0011hz\u00000aJ\u000fu{\r\u000bf|\u00110\u007fJ\u001axl\u001d=s";
        int length = "\u000bf|\u00110yQ\u0011hz\u00000sV\u001cd\u0011\u000bf|\u00110yQ\u0011hz\u00000aJ\u0007i{\u0010\u000bf|\u00110yQ\u0011hz\u00000cV\u0000b\u000e\u000bf|\u00110\u007fJ\u001ax~\u0001.bK\r\u000bf|\u00110yQ\u0011vz\u0015=d\f\u000bf|\u00110yQ\u0011vz\u0015+\f\u000bf|\u00110yQ\u0011bw\u0004 \u0011\u000bf|\u00110yQ\u0011hz\u00000sJ\fnl\u0010\u000bf|\u00110yQ\u0011hz\u00000uG\u001eh\u000e\u000bf|\u00110\u007fJ\u001axl\u0001-y\\\u0007\u001dnh\u0019 y[\r\u000bf|\u00110\u007fJ\u001ax~\u0001.t\u0006\u0002na\u0011.b\r\u000bf|\u00110yQ\u0011dz\u0016&s\f\u000bf|\u00110yQ\u0011en\u0017$\u0010\u000bf|\u00110\u007fJ\u001axj\u0018.cK\u0007d\f\u000bf|\u00110yQ\u0011tf\u001a*\r\u000bf|\u00110\u007fJ\u001axj\f?\u007f\u0006\u001dj`\u001b;x\n\nBL1\u0003Um/SJ\f\u000bf|\u00110yQ\u0011df\u0006,\u000e\u000bf|\u00110\u007fJ\u001ax~\u0001&~K\u0010\u000bf|\u00110yQ\u0011hz\u00000aJ\u000fc\u0011\u000bf|\u00110yQ\u0011hz\u00000aJ\u000fu{\r\u000bf|\u00110\u007fJ\u001axl\u001d=s".length();
        int i3 = -1;
        char c2 = 16;
        int i4 = 0;
        while (true) {
            int i5 = 72;
            int i6 = i3 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i = i4 + 1;
                        strArr[i4] = a;
                        i3 = i6 + c2;
                        if (i3 < length) {
                            break;
                        }
                        str = "\u0016{a\f-dL\fkg\u0000<y\r\u0016{a\f-bW\u0007ea\u0000<h";
                        length = "\u0016{a\f-dL\fkg\u0000<y\r\u0016{a\f-bW\u0007ea\u0000<h".length();
                        i2 = -1;
                        c2 = '\r';
                        i4 = i;
                        i5 = 85;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                i = i4 + 1;
                strArr[i4] = a;
                i2 = i6 + c2;
                if (i2 >= length) {
                    LINEAR = new d(strArr[12], 0, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda0
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$0((Double) obj);
                        }
                    });
                    EASE_IN_QUAD = new d(strArr[5], 1, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda11
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r1.doubleValue() * ((Double) obj).doubleValue());
                            return valueOf;
                        }
                    });
                    EASE_OUT_QUAD = new d(strArr[11], 2, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda19
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r1.doubleValue() * (2.0d - ((Double) obj).doubleValue()));
                            return valueOf;
                        }
                    });
                    EASE_IN_OUT_QUAD = new d(strArr[22], 3, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda20
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r9.doubleValue() < 0.5d ? r1.doubleValue() * 2.0d * r1.doubleValue() : (-1.0d) + ((4.0d - (r1.doubleValue() * 2.0d)) * ((Double) obj).doubleValue()));
                            return valueOf;
                        }
                    });
                    EASE_IN_CUBIC = new d(strArr[13], 4, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda21
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r1.doubleValue() * r1.doubleValue() * ((Double) obj).doubleValue());
                            return valueOf;
                        }
                    });
                    EASE_OUT_CUBIC = new d(strArr[9], 5, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda22
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$5((Double) obj);
                        }
                    });
                    EASE_IN_OUT_CUBIC = new d(strArr[7], 6, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda23
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r8.doubleValue() < 0.5d ? r1.doubleValue() * 4.0d * r1.doubleValue() * r1.doubleValue() : 1.0d + ((r1.doubleValue() - 1.0d) * ((r1.doubleValue() * 2.0d) - 2.0d) * ((((Double) obj).doubleValue() * 2.0d) - 2.0d)));
                            return valueOf;
                        }
                    });
                    EASE_IN_QUART = new d(strArr[4], 7, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda24
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r1.doubleValue() * r1.doubleValue() * r1.doubleValue() * ((Double) obj).doubleValue());
                            return valueOf;
                        }
                    });
                    EASE_OUT_QUART = new d(strArr[3], 8, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda25
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$8((Double) obj);
                        }
                    });
                    EASE_IN_OUT_QUART = new d(strArr[23], 9, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda26
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$9((Double) obj);
                        }
                    });
                    EASE_IN_QUINT = new d(strArr[25], 10, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda1
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r1.doubleValue() * r1.doubleValue() * r1.doubleValue() * r1.doubleValue() * ((Double) obj).doubleValue());
                            return valueOf;
                        }
                    });
                    EASE_OUT_QUINT = new d(strArr[21], 11, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$11((Double) obj);
                        }
                    });
                    EASE_IN_OUT_QUINT = new d(strArr[1], 12, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda3
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$12((Double) obj);
                        }
                    });
                    EASE_IN_SINE = new d(strArr[16], 13, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda4
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(1.0d - Math.cos((((Double) obj).doubleValue() * 3.141592653589793d) / 2.0d));
                            return valueOf;
                        }
                    });
                    EASE_OUT_SINE = new d(strArr[26], 14, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda5
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(Math.sin((((Double) obj).doubleValue() * 3.141592653589793d) / 2.0d));
                            return valueOf;
                        }
                    });
                    EASE_IN_OUT_SINE = new d(strArr[2], 15, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda6
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(1.0d - Math.cos((((Double) obj).doubleValue() * 3.141592653589793d) / 2.0d));
                            return valueOf;
                        }
                    });
                    EASE_IN_EXPO = new d(strArr[6], 16, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda7
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r6.doubleValue() != 0.0d ? Math.pow(2.0d, (((Double) obj).doubleValue() * 10.0d) - 10.0d) : 0.0d);
                            return valueOf;
                        }
                    });
                    EASE_OUT_EXPO = new d(strArr[17], 17, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda8
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r8.doubleValue() != 1.0d ? 1.0d - Math.pow(2.0d, ((Double) obj).doubleValue() * (-10.0d)) : 1.0d);
                            return valueOf;
                        }
                    });
                    EASE_IN_OUT_EXPO = new d(strArr[8], 18, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda9
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$18((Double) obj);
                        }
                    });
                    EASE_IN_CIRC = new d(strArr[20], 19, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda10
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(1.0d - Math.sqrt(1.0d - (r1.doubleValue() * ((Double) obj).doubleValue())));
                            return valueOf;
                        }
                    });
                    EASE_OUT_CIRC = new d(strArr[24], 20, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda12
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$20((Double) obj);
                        }
                    });
                    EASE_IN_OUT_CIRC = new d(strArr[0], 21, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda13
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            return d.lambda$static$21((Double) obj);
                        }
                    });
                    SIGMOID = new d(strArr[10], 22, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda14
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(1.0d / (Math.exp(-((Double) obj).doubleValue()) + 1.0d));
                            return valueOf;
                        }
                    });
                    EASE_OUT_ELASTIC = new d(strArr[15], 23, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda15
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(r8.doubleValue() != 0.0d ? r8.doubleValue() == 1.0d ? 1.0d : (Math.pow(2.0d, r1.doubleValue() * (-10.0d)) * Math.sin(((((Double) obj).doubleValue() * 10.0d) - 0.75d) * 2.0943951023931953d) * 0.5d) + 1.0d : 0.0d);
                            return valueOf;
                        }
                    });
                    EASE_IN_BACK = new d(strArr[14], 24, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda16
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf((((r1.doubleValue() * 2.70158d) * r1.doubleValue()) * r1.doubleValue()) - ((r1.doubleValue() * 1.70158d) * ((Double) obj).doubleValue()));
                            return valueOf;
                        }
                    });
                    Decelerate = new d(strArr[19], 25, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda17
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf(1.0d - ((r1.doubleValue() - 1.0d) * (((Double) obj).doubleValue() - 1.0d)));
                            return valueOf;
                        }
                    });
                    SMOOTH = new d(strArr[18], 26, new Function() { // from class: com.trossense.d$$ExternalSyntheticLambda18
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            Double valueOf;
                            valueOf = Double.valueOf((Math.pow(r1.doubleValue(), 3.0d) * (-2.0d)) + (Math.pow(((Double) obj).doubleValue(), 2.0d) * 3.0d));
                            return valueOf;
                        }
                    });
                    b = b();
                    return;
                }
                c2 = str.charAt(i2);
                i4 = i;
                i5 = 85;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i3);
            i4 = i;
        }
    }

    private d(String str, int i, Function function) {
        this.a = function;
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 6;
                    break;
                case 1:
                    i2 = 111;
                    break;
                case 2:
                    i2 = 103;
                    break;
                case 3:
                    i2 = 28;
                    break;
                case 4:
                    i2 = 39;
                    break;
                case 5:
                    i2 = 120;
                    break;
                default:
                    i2 = 87;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'W');
        }
        return charArray;
    }

    private static d[] b() {
        return new d[]{LINEAR, EASE_IN_QUAD, EASE_OUT_QUAD, EASE_IN_OUT_QUAD, EASE_IN_CUBIC, EASE_OUT_CUBIC, EASE_IN_OUT_CUBIC, EASE_IN_QUART, EASE_OUT_QUART, EASE_IN_OUT_QUART, EASE_IN_QUINT, EASE_OUT_QUINT, EASE_IN_OUT_QUINT, EASE_IN_SINE, EASE_OUT_SINE, EASE_IN_OUT_SINE, EASE_IN_EXPO, EASE_OUT_EXPO, EASE_IN_OUT_EXPO, EASE_IN_CIRC, EASE_OUT_CIRC, EASE_IN_OUT_CIRC, SIGMOID, EASE_OUT_ELASTIC, EASE_IN_BACK, Decelerate, SMOOTH};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$0(Double d) {
        return d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$11(Double d) {
        Double valueOf = Double.valueOf(d.doubleValue() - 1.0d);
        return Double.valueOf((valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue()) + 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$12(Double d) {
        double doubleValue;
        double doubleValue2 = d.doubleValue();
        double doubleValue3 = d.doubleValue();
        if (doubleValue2 < 0.5d) {
            doubleValue = doubleValue3 * 16.0d * d.doubleValue() * d.doubleValue() * d.doubleValue() * d.doubleValue();
        } else {
            Double valueOf = Double.valueOf(doubleValue3 - 1.0d);
            doubleValue = (valueOf.doubleValue() * 16.0d * valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue()) + 1.0d;
        }
        return Double.valueOf(doubleValue);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$18(Double d) {
        double d2 = 0.0d;
        if (d.doubleValue() != 0.0d) {
            if (d.doubleValue() == 1.0d) {
                d2 = 1.0d;
            } else {
                d2 = (d.doubleValue() < 0.5d ? Math.pow(2.0d, (d.doubleValue() * 20.0d) - 10.0d) : 2.0d - Math.pow(2.0d, (d.doubleValue() * (-20.0d)) + 10.0d)) / 2.0d;
            }
        }
        return Double.valueOf(d2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$20(Double d) {
        Double valueOf = Double.valueOf(d.doubleValue() - 1.0d);
        return Double.valueOf(Math.sqrt(1.0d - (valueOf.doubleValue() * valueOf.doubleValue())));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$21(Double d) {
        double doubleValue = d.doubleValue();
        double doubleValue2 = d.doubleValue();
        return Double.valueOf(doubleValue < 0.5d ? (1.0d - Math.sqrt(1.0d - ((doubleValue2 * 4.0d) * d.doubleValue()))) / 2.0d : (Math.sqrt(1.0d - (((doubleValue2 - 1.0d) * 4.0d) * d.doubleValue())) + 1.0d) / 2.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$5(Double d) {
        Double valueOf = Double.valueOf(d.doubleValue() - 1.0d);
        return Double.valueOf((valueOf.doubleValue() * valueOf.doubleValue() * valueOf.doubleValue()) + 1.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$8(Double d) {
        Double valueOf = Double.valueOf(d.doubleValue() - 1.0d);
        return Double.valueOf(1.0d - (((valueOf.doubleValue() * valueOf.doubleValue()) * valueOf.doubleValue()) * valueOf.doubleValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Double lambda$static$9(Double d) {
        double doubleValue;
        double doubleValue2 = d.doubleValue();
        double doubleValue3 = d.doubleValue();
        if (doubleValue2 < 0.5d) {
            doubleValue = doubleValue3 * 8.0d * d.doubleValue() * d.doubleValue() * d.doubleValue();
        } else {
            Double valueOf = Double.valueOf(doubleValue3 - 1.0d);
            doubleValue = 1.0d - ((((valueOf.doubleValue() * 8.0d) * valueOf.doubleValue()) * valueOf.doubleValue()) * valueOf.doubleValue());
        }
        return Double.valueOf(doubleValue);
    }

    public static d valueOf(String str) {
        return (d) Enum.valueOf(d.class, str);
    }

    public static d[] values() {
        return (d[]) b.clone();
    }

    public Function<Double, Double> a() {
        return this.a;
    }
}
