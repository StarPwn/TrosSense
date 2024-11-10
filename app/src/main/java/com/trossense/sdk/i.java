package com.trossense.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.trossense.hook.HopeHookEntrance;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes3.dex */
public class i {
    public static final String a;
    public static final String b;
    public static final String c;
    public static final String d;
    public static final String e;
    public static final String f;
    public static final String g;
    public static final String h;
    public static final String i;
    public static final String j;
    public static final String k;
    public static final String l;
    public static final String m;
    public static final String n;
    public static final String o;
    public static final String p;
    public static final String q;
    public static final String r;
    public static final String s;
    public static final String t;
    public static final String u;
    public static final String v;
    private static final Map<String, List<String>> w;
    private static final String[] x;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0026. Please report as an issue. */
    static {
        int i2;
        int i3;
        String[] strArr = new String[23];
        String str = "&@\u0016()\u001a\b-]B)#\t\u0004$G\u001c\u0012>\u0001\f9\u0012&@\u0016()\u001a\b-]B$97\u001a<F\n)\u0015&@\u0016()\u001a\b-]B*%\u0004\r.G'9#\r\u001b\u0014&@\u0016()\u001a\b-]B$97\u001d9@\u001c($\u001c\u0015&@\u0016()\u001a\b-]B$97\u0005.N\u001f$$\u000f\u001a\u0010&@\u0016()\u001a\b-]B$97\u0001$L\u0015&@\u0016()\u001a\b-]B:%\u0007\r.G'9#\r\u001b\u0012&@\u0016()\u001a\b-]B$97\b9D\u0017?\u0012&@\u0016()\u001a\b-]B$97\u000b$F\f>\u0018&@\u0016()\u001a\b-]B.\"\t\u0000%D\u0019$&7\u001d\"L\n\u0011&@\u0016()\u001a\b-]B$97\u000f$F\u001c\u0013&@\u0016()\u001a\b-]B$97\u0001.E\u0015(>\u0017&@\u0016()\u001a\b-]B$97\n#L\u000b9:\u0004\b?L\u0013&@\u0016()\u001a\b-]B$8\u0007\u0007\u0014]\u0011(8\u0013&@\u0016()\u001a\b-]B$97\u001a#F\u000e(&\u0010&@\u0016()\u001a\b-]B$97\u000b$^\u0016&@\u0016()\u001a\b-]B!/\t\u001d#L\n\u0012>\u0001\f9\u0011&@\u0016()\u001a\b-]B$97\u001d$F\u0014\u0014&@\u0016()\u001a\b-]B>>\u0007\u0007.v\f$/\u001a\u0010&@\u0016()\u001a\b-]B$97\b3L\u001d*Z\u000b(>\u001bF%L\f:%\u001a\u0002d@\f('7\u001d*N\u000bc \u001b\u0006%";
        int length = "&@\u0016()\u001a\b-]B)#\t\u0004$G\u001c\u0012>\u0001\f9\u0012&@\u0016()\u001a\b-]B$97\u001a<F\n)\u0015&@\u0016()\u001a\b-]B*%\u0004\r.G'9#\r\u001b\u0014&@\u0016()\u001a\b-]B$97\u001d9@\u001c($\u001c\u0015&@\u0016()\u001a\b-]B$97\u0005.N\u001f$$\u000f\u001a\u0010&@\u0016()\u001a\b-]B$97\u0001$L\u0015&@\u0016()\u001a\b-]B:%\u0007\r.G'9#\r\u001b\u0012&@\u0016()\u001a\b-]B$97\b9D\u0017?\u0012&@\u0016()\u001a\b-]B$97\u000b$F\f>\u0018&@\u0016()\u001a\b-]B.\"\t\u0000%D\u0019$&7\u001d\"L\n\u0011&@\u0016()\u001a\b-]B$97\u000f$F\u001c\u0013&@\u0016()\u001a\b-]B$97\u0001.E\u0015(>\u0017&@\u0016()\u001a\b-]B$97\n#L\u000b9:\u0004\b?L\u0013&@\u0016()\u001a\b-]B$8\u0007\u0007\u0014]\u0011(8\u0013&@\u0016()\u001a\b-]B$97\u001a#F\u000e(&\u0010&@\u0016()\u001a\b-]B$97\u000b$^\u0016&@\u0016()\u001a\b-]B!/\t\u001d#L\n\u0012>\u0001\f9\u0011&@\u0016()\u001a\b-]B$97\u001d$F\u0014\u0014&@\u0016()\u001a\b-]B>>\u0007\u0007.v\f$/\u001a\u0010&@\u0016()\u001a\b-]B$97\b3L\u001d*Z\u000b(>\u001bF%L\f:%\u001a\u0002d@\f('7\u001d*N\u000bc \u001b\u0006%".length();
        char c2 = 22;
        int i4 = -1;
        int i5 = 0;
        while (true) {
            int i6 = 112;
            int i7 = i4 + 1;
            String substring = str.substring(i7, i7 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i6, b(substring));
                i2 = i5 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i5] = a2;
                        i4 = i7 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "n\b^`aR@e\u0015\nlq\u007fQj\u0002[dzE\u0018n\b^`aR@e\u0015\nkgTIf\u0013Yqg\u007fUj\u0004B";
                        length = "n\b^`aR@e\u0015\nlq\u007fQj\u0002[dzE\u0018n\b^`aR@e\u0015\nkgTIf\u0013Yqg\u007fUj\u0004B".length();
                        c2 = 20;
                        i5 = i2;
                        i3 = -1;
                        i6 = 56;
                        i7 = i3 + 1;
                        substring = str.substring(i7, i7 + c2);
                        z = false;
                        break;
                }
                strArr[i5] = a2;
                i3 = i7 + c2;
                if (i3 >= length) {
                    x = strArr;
                    m = strArr[16];
                    e = strArr[5];
                    s = strArr[9];
                    h = strArr[13];
                    o = strArr[11];
                    l = strArr[18];
                    p = strArr[12];
                    k = strArr[2];
                    i = strArr[19];
                    n = strArr[7];
                    q = strArr[4];
                    t = strArr[10];
                    v = strArr[15];
                    g = strArr[1];
                    f = strArr[0];
                    c = strArr[22];
                    j = strArr[6];
                    r = strArr[8];
                    u = strArr[3];
                    d = strArr[14];
                    a = strArr[17];
                    b = strArr[21];
                    w = new HashMap();
                    a();
                    return;
                }
                c2 = str.charAt(i3);
                i5 = i2;
                i6 = 56;
                i7 = i3 + 1;
                substring = str.substring(i7, i7 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i5 = i2;
        }
    }

    private static String a(int i2, char[] cArr) {
        int i3;
        int length = cArr.length;
        for (int i4 = 0; length > i4; i4++) {
            char c2 = cArr[i4];
            switch (i4 % 7) {
                case 0:
                    i3 = 59;
                    break;
                case 1:
                    i3 = 89;
                    break;
                case 2:
                    i3 = 8;
                    break;
                case 3:
                    i3 = 61;
                    break;
                case 4:
                    i3 = 58;
                    break;
                case 5:
                    i3 = 24;
                    break;
                default:
                    i3 = 25;
                    break;
            }
            cArr[i4] = (char) (c2 ^ (i3 ^ i2));
        }
        return new String(cArr).intern();
    }

    public static List<String> a(String str) {
        return w.getOrDefault(str, new ArrayList());
    }

    private static void a() {
        JsonParser.parseReader(new InputStreamReader(HopeHookEntrance.class.getClassLoader().getResourceAsStream(x[20]), StandardCharsets.UTF_8)).getAsJsonObject().entrySet().forEach(new Consumer() { // from class: com.trossense.sdk.i$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                i.w.put((String) r1.getKey(), (List) ((JsonElement) ((Map.Entry) obj).getValue()).getAsJsonArray().asList().stream().map(new Function() { // from class: com.trossense.sdk.i$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj2) {
                        String asString;
                        asString = ((JsonElement) obj2).getAsString();
                        return asString;
                    }
                }).collect(Collectors.toList()));
            }
        });
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 25);
        }
        return charArray;
    }
}
