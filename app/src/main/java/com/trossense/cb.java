package com.trossense;

import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;

@cg(a = "ChestStealer", b = b.World, c = "拿箱子", d = 30)
/* loaded from: classes3.dex */
public class cb extends bm {
    private static final long v = dj.a(-3484751544658645550L, 7270751127702297817L, MethodHandles.lookup().lookupClass()).a(93536242167242L);
    private static final String[] w;
    private final ct j;
    private final ct k;
    private final ct l;
    private final ct m;
    private final cp n;
    private final cp o;
    private final c7 p;
    private final c7 q;
    private final c7 r;
    private int s;
    private boolean t;
    private boolean u;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0046. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[12];
        String str = "\u0001\"z}T8z\"3f\f\u00075q|\u0006\u0019?\u001a ~`\u001c\b篿嬂弟吼抋厪早閺\t\u0003;q30\u0019s/+\u0004徳男垜圭\n昴肯拠叅\\厖拠李夯6\u000b\u001d7sv\u0017\b?\f7lg\u0006李屝拠叅斂閈\u0006李奵拠叅斂閈\t\u00033g30\u0019s/+";
        int length = "\u0001\"z}T8z\"3f\f\u00075q|\u0006\u0019?\u001a ~`\u001c\b篿嬂弟吼抋厪早閺\t\u0003;q30\u0019s/+\u0004徳男垜圭\n昴肯拠叅\\厖拠李夯6\u000b\u001d7sv\u0017\b?\f7lg\u0006李屝拠叅斂閈\u0006李奵拠叅斂閈\t\u00033g30\u0019s/+".length();
        char c = '\n';
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 72;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String b = b(i5, b(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = b;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "L\u007f1!P\u001d\u001aj\u007f?+\u0006箾孃儭閿旃闉";
                        length = "L\u007f1!P\u001d\u001aj\u007f?+\u0006箾孃儭閿旃闉".length();
                        c = 11;
                        i2 = -1;
                        i3 = i;
                        i5 = 9;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = b;
                i2 = i6 + c;
                if (i2 >= length) {
                    w = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 9;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public cb(int r22, short r23, short r24) {
        /*
            Method dump skipped, instructions count: 216
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cb.<init>(int, short, short):void");
    }

    private static String b(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 87;
            switch (i2 % 7) {
                case 0:
                    i3 = 6;
                    break;
                case 1:
                    i3 = 26;
                    break;
                case 3:
                    i3 = 91;
                    break;
                case 4:
                    i3 = 60;
                    break;
                case 5:
                    i3 = 52;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'W');
        }
        return charArray;
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof com.trossense.sdk.p) {
            com.trossense.sdk.p pVar = (com.trossense.sdk.p) baVar.e();
            if (pVar.b() != ContainerType.CONTAINER || pVar.a() == 0 || pVar.a() == 120 || pVar.a() == 121) {
                return;
            }
            baVar.c();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0243 A[LOOP:0: B:16:0x00cd->B:29:0x0243, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0240 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01f2 A[LOOP:1: B:62:0x0191->B:73:0x01f2, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01f0 A[SYNTHETIC] */
    @com.trossense.bk
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bg r48) {
        /*
            Method dump skipped, instructions count: 683
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cb.a(com.trossense.bg):void");
    }

    @Override // com.trossense.bm
    public void j(long j) {
        this.p.a();
        this.q.a();
        this.s = 0;
        this.t = false;
        this.u = false;
    }
}
