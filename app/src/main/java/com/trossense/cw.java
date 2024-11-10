package com.trossense;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.trossense.clients.TrosSense;
import com.trossense.sdk.math.Vec2f;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes3.dex */
public class cw {
    public static final File a;
    public static final File b;
    public static final File c;
    protected static final Gson d;
    private static String[] e;
    private static final long f = dj.a(-6115133652644601209L, -1849023457330730100L, MethodHandles.lookup().lookupClass()).a(163864090157469L);
    private static final String[] g;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004a. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[39];
        String str = "(L<C\b\u00042[2Y\u0002\u007fa\u000b8W0G\u0002\bG5Lyo\u0005-Y1B\b\u000b8W0G\u0002\bG5Lyn\u0002\u007fa\u00046Q.T\n0]$U\u0004\bF2V:\u00068W0U\f\u0012\f)Y4Y\u000f\tU\bH8R\t\u00058W1X\u001f\u0005,W/[\t\u0006+T<N\b\u0014\u0006)]3S\b\u0014\f)Y4Y\u000f\tU\bH8R\t\f?];V\u0018\nVuR.X\u0003\u000b8W0G\u0002\bG5Lyn\u00058W1X\u001f\u0011)Y4Y\u000f\tU\bY)B\u001f\u0007V2W3\u0002\u007f`\u00068W0U\f\u0012\u00042[2Y\b6W+R\u0000\u0003L/\u0005-Y1B\b\u0011)Y4Y\u000f\tU\bY)B\u001f\u0007V2W3\u0002\u007f`\n0]$U\u0004\bF2V:\t2K\u000fV\u0004\b@4O\u000b8M/E\b\bVu\\<C\u0005(L<C\b\u0006+T<N\b\u0014\u000b8W0G\u0002\bG5Lyo\u00046Q.T\t2K\u000fV\u0004\b@4O\u0005,W/[\t\u00078W3Q\u0004\u0001Q";
        int length = "(L<C\b\u00042[2Y\u0002\u007fa\u000b8W0G\u0002\bG5Lyo\u0005-Y1B\b\u000b8W0G\u0002\bG5Lyn\u0002\u007fa\u00046Q.T\n0]$U\u0004\bF2V:\u00068W0U\f\u0012\f)Y4Y\u000f\tU\bH8R\t\u00058W1X\u001f\u0005,W/[\t\u0006+T<N\b\u0014\u0006)]3S\b\u0014\f)Y4Y\u000f\tU\bH8R\t\f?];V\u0018\nVuR.X\u0003\u000b8W0G\u0002\bG5Lyn\u00058W1X\u001f\u0011)Y4Y\u000f\tU\bY)B\u001f\u0007V2W3\u0002\u007f`\u00068W0U\f\u0012\u00042[2Y\b6W+R\u0000\u0003L/\u0005-Y1B\b\u0011)Y4Y\u000f\tU\bY)B\u001f\u0007V2W3\u0002\u007f`\n0]$U\u0004\bF2V:\t2K\u000fV\u0004\b@4O\u000b8M/E\b\bVu\\<C\u0005(L<C\b\u0006+T<N\b\u0014\u000b8W0G\u0002\bG5Lyo\u00046Q.T\t2K\u000fV\u0004\b@4O\u0005,W/[\t\u00078W3Q\u0004\u0001Q".length();
        b(new String[1]);
        char c2 = 5;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 100;
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
                        str = " A=D\u0016\u0015Z9\u0006?K%E\u001e\u0002";
                        length = " A=D\u0016\u0015Z9\u0006?K%E\u001e\u0002".length();
                        c2 = '\b';
                        i2 = -1;
                        i3 = i;
                        i5 = 114;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a2;
                i2 = i6 + c2;
                if (i2 >= length) {
                    g = strArr;
                    File file = new File(TrosSense.INSTANCE.context.getDataDir(), strArr[36]);
                    a = file;
                    b = new File(file, strArr[16]);
                    c = new File(file, strArr[29]);
                    d = new GsonBuilder().setPrettyPrinting().create();
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 114;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    public cw(long j) {
        File file = a;
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = c;
        if (file2.exists()) {
            return;
        }
        try {
            file2.createNewFile();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 63;
                    break;
                case 1:
                    i2 = 92;
                    break;
                case 2:
                    i2 = 57;
                    break;
                case 3:
                    i2 = 83;
                    break;
                case 4:
                    i2 = 9;
                    break;
                case 5:
                    i2 = 2;
                    break;
                default:
                    i2 = 70;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static void a(String str, long j, JsonObject jsonObject, p pVar) {
        long j2 = (j ^ f) ^ 133463236208939L;
        StringBuilder append = new StringBuilder().append(str);
        String[] strArr = g;
        float asFloat = jsonObject.has(append.append(strArr[20]).toString()) ? jsonObject.get(str + strArr[20]).getAsFloat() : 0.0f;
        float asFloat2 = jsonObject.has(new StringBuilder().append(str).append(strArr[2]).toString()) ? jsonObject.get(str + strArr[2]).getAsFloat() : 0.0f;
        if (asFloat >= 1.0f) {
            asFloat = 0.0f;
        }
        if (asFloat2 >= 1.0f) {
            asFloat2 = 0.0f;
        }
        if (asFloat == 0.0f || asFloat2 == 0.0f) {
            return;
        }
        pVar.b(j2, f.a * asFloat, f.b * asFloat2);
    }

    private void a(String str, long j, boolean z) {
        long j2 = (j ^ f) ^ 139362319967360L;
        File file = new File(a, str);
        c(j2, file);
        if (z) {
            return;
        }
        a(file);
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'F');
        }
        return charArray;
    }

    private static void b(String str, JsonObject jsonObject, p pVar) {
        StringBuilder append = new StringBuilder().append(str);
        String[] strArr = g;
        jsonObject.addProperty(append.append(strArr[26]).toString(), Float.valueOf(pVar.e() / f.a));
        jsonObject.addProperty(str + strArr[6], Float.valueOf(pVar.h() / f.b));
    }

    public static void b(String[] strArr) {
        e = strArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x0398, code lost:            if (r0 <= 0) goto L114;     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x039a, code lost:            if (r9 == false) goto L112;     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x039c, code lost:            r9 = r13.get(r4[32]).getAsFloat();     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x03a8, code lost:            r14 = r9;        r9 = r13.has(r4[17]);     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x03b7, code lost:            if (r0 < 0) goto L120;     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x03b9, code lost:            if (r9 == false) goto L118;     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x03bb, code lost:            r9 = r13.get(r4[5]).getAsFloat();     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x03c8, code lost:            r22 = 0.0f;        r23 = r9;        r9 = (r14 > 0.0f ? 1 : (r14 == 0.0f ? 0 : -1));     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x03d7, code lost:            if (r0 <= 0) goto L124;     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x03d9, code lost:            if (r9 == 0) goto L126;     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x03db, code lost:            r9 = (r23 > r22 ? 1 : (r23 == r22 ? 0 : -1));     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x03f5, code lost:            r26 = r5;        r14 = r6;        r5 = r18;     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x03fa, code lost:            a(r4[27], r2, r13, r10.a());     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0405, code lost:            if (r15 != null) goto L137;     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x03dd, code lost:            if (r9 == 0) goto L126;     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x03df, code lost:            r26 = r5;        r9 = new com.trossense.sdk.math.Vec2f(r14 * com.trossense.f.a, com.trossense.f.b * r23);        r14 = r6;        r5 = r18;        r10.a(r5, r9);     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x03c7, code lost:            r9 = 0.0f;     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x03d3, code lost:            r22 = 0.0f;        r23 = 0.0f;        r9 = r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x03a7, code lost:            r9 = 0.0f;     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x03b6, code lost:            r14 = 0.0f;        r9 = r9;     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x02b8, code lost:            if (r7 != false) goto L80;     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x013f, code lost:            if (r15 == null) goto L25;     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x038b, code lost:            r4 = com.trossense.cw.g;        r9 = r13.has(r4[3]);     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0394, code lost:            if (r0 <= 0) goto L130;     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0408, code lost:            r26 = r5;        r16 = r6;        r22 = r7;        r10 = r9 ? 1 : 0;        r6 = r27;        r5 = r30;        r8 = r32;        r4 = r33;        r7 = r34;        r27 = r37;        r9 = r42;        r42 = r38;     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:137:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0219 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:148:0x0377 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0221  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0254  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0352  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x038b A[EDGE_INSN: B:94:0x038b->B:95:0x038b BREAK  A[LOOP:2: B:27:0x0155->B:40:0x0155], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean b(long r41, java.io.File r43) {
        /*
            Method dump skipped, instructions count: 1123
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cw.b(long, java.io.File):boolean");
    }

    private boolean c(long j, File file) {
        char c2;
        Iterator<bm> it2;
        long j2;
        long j3 = f ^ j;
        long j4 = 103291925573073L ^ j3;
        long j5 = 14759319081711L ^ j3;
        long j6 = j3 ^ 32822733112727L;
        int i = (int) (j6 >>> 32);
        int i2 = (int) ((j6 << 32) >>> 48);
        int i3 = (int) ((j6 << 48) >>> 48);
        try {
            file.createNewFile();
            JsonObject jsonObject = new JsonObject();
            f b2 = f.b();
            String[] strArr = g;
            b(strArr[22], jsonObject, b2.a());
            b(strArr[9], jsonObject, b2.j);
            b(strArr[37], jsonObject, b2.k);
            b(strArr[7], jsonObject, b2.l);
            b(strArr[31], jsonObject, b2.m);
            b(strArr[38], jsonObject, b2.n);
            b(strArr[35], jsonObject, b2.o);
            Iterator<bm> it3 = TrosSense.INSTANCE.f().c().iterator();
            while (it3.hasNext()) {
                bm next = it3.next();
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty(g[30], Boolean.valueOf(next.l()));
                HashMap hashMap = new HashMap();
                for (co coVar : next.b()) {
                    JsonObject jsonObject3 = new JsonObject();
                    if (coVar instanceof cp) {
                        it2 = it3;
                        jsonObject3.addProperty(g[24], ((cp) coVar).e());
                    } else {
                        it2 = it3;
                    }
                    if (coVar instanceof cr) {
                        j2 = j5;
                        jsonObject3.addProperty(g[24], ((cr) coVar).j(j5).a(i, (short) i2, i3));
                    } else {
                        j2 = j5;
                    }
                    if (coVar instanceof ct) {
                        jsonObject3.addProperty(g[24], ((ct) coVar).e());
                    }
                    if (coVar instanceof cs) {
                        for (Map.Entry<cu, Boolean> entry : ((cs) coVar).e().entrySet()) {
                            jsonObject3.addProperty(entry.getKey().c(), entry.getValue());
                        }
                    }
                    if (coVar instanceof cq) {
                        cq cqVar = (cq) coVar;
                        String[] strArr2 = g;
                        jsonObject3.addProperty(strArr2[11], cqVar.m(j4));
                        jsonObject3.addProperty(strArr2[28], Boolean.valueOf(cqVar.i()));
                        jsonObject3.addProperty(strArr2[15], cqVar.j().e());
                        jsonObject3.addProperty(strArr2[19], cqVar.k().e());
                    }
                    if (hashMap.containsKey(coVar.b())) {
                        jsonObject2.add(coVar.b() + "$" + hashMap.get(coVar.b()), jsonObject3);
                        hashMap.put(coVar.b(), Integer.valueOf(((Integer) hashMap.get(coVar.b())).intValue() + 1));
                    } else {
                        hashMap.put(coVar.b(), 0);
                        jsonObject2.add(coVar.b(), jsonObject3);
                    }
                    it3 = it2;
                    j5 = j2;
                }
                long j7 = j5;
                Iterator<bm> it4 = it3;
                Vec2f f2 = next.f();
                if (f2 != null) {
                    String[] strArr3 = g;
                    c2 = ' ';
                    jsonObject2.addProperty(strArr3[32], Float.valueOf(f2.x / f.a));
                    jsonObject2.addProperty(strArr3[5], Float.valueOf(f2.y / f.b));
                } else {
                    c2 = ' ';
                }
                b(g[8], jsonObject2, next.a());
                jsonObject.add(next.e(), jsonObject2);
                it3 = it4;
                j5 = j7;
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                d.toJson((JsonElement) jsonObject, (Appendable) bufferedWriter);
                bufferedWriter.flush();
                bufferedWriter.close();
                return true;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public static String[] d() {
        return e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void lambda$read$0(JsonObject jsonObject, cs csVar, cu cuVar) {
        if (jsonObject.has(cuVar.c())) {
            csVar.e().put(cuVar, Boolean.valueOf(jsonObject.get(cuVar.c()).getAsBoolean()));
        } else {
            csVar.e().put(cuVar, false);
        }
    }

    public File a(long j) {
        long j2 = (j ^ f) ^ 3249346114735L;
        try {
            FileReader fileReader = new FileReader(c);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                File file = b;
                a(file);
                readLine = file.getName();
            }
            File file2 = new File(a, readLine);
            if (!file2.exists()) {
                a(file2.getName(), j2, true);
            }
            fileReader.close();
            bufferedReader.close();
            return file2;
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void a(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(c));
            bufferedWriter.write(file.getName());
            bufferedWriter.flush();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void a(String str, long j) {
        a(str, (j ^ f) ^ 41064122294912L, false);
    }

    public void b(byte b2, int i, int i2) {
        long j = ((((i << 32) >>> 8) | (b2 << 56)) | ((i2 << 40) >>> 40)) ^ f;
        c(67806297375250L ^ j, a(j ^ 72431398654525L));
    }

    public void b(String str, long j) {
        long j2 = (j ^ f) ^ 140022133514479L;
        File file = new File(a, str);
        b(j2, file);
        a(file);
    }

    public void c(long j) {
        long j2 = j ^ f;
        b(111239895718470L ^ j2, a(j2 ^ 61091025830671L));
    }
}
