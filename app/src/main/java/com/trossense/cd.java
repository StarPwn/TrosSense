package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.PointerHolder;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import java.lang.invoke.MethodHandles;

@cg(a = "RemoteShop", b = b.World, c = "远程商店", d = 68)
/* loaded from: classes3.dex */
public class cd extends bm {
    private static final long k = dj.a(3378005923466168802L, -6100194092331134122L, MethodHandles.lookup().lookupClass()).a(119500530189588L);
    private static final String[] l;
    private final cr j;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0043. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[10];
        String str = "!ZV\u00165R\"\\_S\u00141$#/_#\u00173U!,\\&\u00117$%!]V\u00165Q\" \\,\u00171%%.Q#d2 \"\\_S\u00141S\")_!\u001a0)#,\\&\u0014=&P/Y!f3V ,\\&\u00140'$/_#\u00172\"#+^,\u00156'\".\\#fF$'-\\'\u00140& /[#\u00172#  \\%\u0014F&'/P#\u00172\"\"+^ \u00157'$.\\#g3#!!Q a1 /.Z\"\u00172\"!*_ \u0014@&%/P&\u0011=#U,Y!\u0014<'\".\\#fF$')_Q\u0014<&S.\\#\u00112\" )_#\u00151#W.]#\u001b3! \\_S\u0014@&\"[]%\u00143! \\_S\u00150&S/]Vf7'&^*!\u00121&R.\\\"\u00163!&(*!\u0012<&%/\\\"\u00102\" -_P\u00146'/+]-\u0011F$&,_,\u00151&#.-V\u00164% \\_,\u0014@&#.Z\"\u00103! .^!\u0011D&S.\\\"\u00163( -^'\u00147'$.P#\u00113RU,Y#\u00144&R./\"\u00173U!,*Q\u00102 P[]%\u00163T -^!\u00144 &[]%\u001b3#!-^'\u00157&#.,#\u00112)%)Q&a1 \".P\"\u00163% \\*!\u00131&R.P#g3% +^'\u00144& /]&c3)!*_S\u0014@%P.P#g3' ^^!a1  .X#f3V!-_P\u00151SR*^%dF$&,_Q\u00140'\".X%\u0012F$&!_&\u00150'$/[#\u00173U +^,\u00117(%[]%\u00163)!,_ \u0014AS\")]#f3) ]_ \u00146'$.X#\u00142$%Y_\"\u0014C&U.] d3) ]_\"\u0014C'\"[]%\u00143! \\_S\u00150&S/]Vf7'&^*!\u00121&R.\\\"\u00163!&(*!\u0012<&%/\\\"\u00102\" -_P\u00146'/+\\-\u0011F$&,_,\u00151&#.-V\u00164% \\_,\u0014@&#.Z\"\u00103! .^!\u0011D&S.\\\"\u00163( -^'\u0017C'%/]#\u00132\"U,Y#\u00144&R./\"\u00173U!,*Q\u00102 P[]%\u00163T -^!\u00144 &[Y\u0003帛呠岎\u0003P0A\u0003Z$B\u0004U\u0006qG\u0088!ZV\u00165R\"\\_S\u00141$#/_#\u00173U!,\\&\u00117$%!]V\u00165Q\" \\,\u00171%%.Q#d2 \"\\_S\u00141S\")_!\u001a0)#,\\&\u0014=&P/Y!f3V ,\\&\u00140'$/_#\u00172\"#+^,\u00156'\".\\#fF$&\\\\&\u0014=&P/Y!d2  -_P\u00160' .\\#g2$.(*%\u0002樹彦\u008e!ZT`1T ^_!\u00160' .\\#g2$#+Z'\u00166)\"Y, \u00123V +_W\u00140'\",-#d3$ -^'\u0016A&P.]W\u00150  ^_&\u0014G&#/]!f3V ,_ \u00157$%.*#\u001b3% ]^!\u00176'//Z\"\u00163% \\(S\u0016@$$.\\#\u00162' )^'\u0016C'&.\\#g0#  _S\u00155(&[Y";
        int length = "!ZV\u00165R\"\\_S\u00141$#/_#\u00173U!,\\&\u00117$%!]V\u00165Q\" \\,\u00171%%.Q#d2 \"\\_S\u00141S\")_!\u001a0)#,\\&\u0014=&P/Y!f3V ,\\&\u00140'$/_#\u00172\"#+^,\u00156'\".\\#fF$'-\\'\u00140& /[#\u00172#  \\%\u0014F&'/P#\u00172\"\"+^ \u00157'$.\\#g3#!!Q a1 /.Z\"\u00172\"!*_ \u0014@&%/P&\u0011=#U,Y!\u0014<'\".\\#fF$')_Q\u0014<&S.\\#\u00112\" )_#\u00151#W.]#\u001b3! \\_S\u0014@&\"[]%\u00143! \\_S\u00150&S/]Vf7'&^*!\u00121&R.\\\"\u00163!&(*!\u0012<&%/\\\"\u00102\" -_P\u00146'/+]-\u0011F$&,_,\u00151&#.-V\u00164% \\_,\u0014@&#.Z\"\u00103! .^!\u0011D&S.\\\"\u00163( -^'\u00147'$.P#\u00113RU,Y#\u00144&R./\"\u00173U!,*Q\u00102 P[]%\u00163T -^!\u00144 &[]%\u001b3#!-^'\u00157&#.,#\u00112)%)Q&a1 \".P\"\u00163% \\*!\u00131&R.P#g3% +^'\u00144& /]&c3)!*_S\u0014@%P.P#g3' ^^!a1  .X#f3V!-_P\u00151SR*^%dF$&,_Q\u00140'\".X%\u0012F$&!_&\u00150'$/[#\u00173U +^,\u00117(%[]%\u00163)!,_ \u0014AS\")]#f3) ]_ \u00146'$.X#\u00142$%Y_\"\u0014C&U.] d3) ]_\"\u0014C'\"[]%\u00143! \\_S\u00150&S/]Vf7'&^*!\u00121&R.\\\"\u00163!&(*!\u0012<&%/\\\"\u00102\" -_P\u00146'/+\\-\u0011F$&,_,\u00151&#.-V\u00164% \\_,\u0014@&#.Z\"\u00103! .^!\u0011D&S.\\\"\u00163( -^'\u0017C'%/]#\u00132\"U,Y#\u00144&R./\"\u00173U!,*Q\u00102 P[]%\u00163T -^!\u00144 &[Y\u0003帛呠岎\u0003P0A\u0003Z$B\u0004U\u0006qG\u0088!ZV\u00165R\"\\_S\u00141$#/_#\u00173U!,\\&\u00117$%!]V\u00165Q\" \\,\u00171%%.Q#d2 \"\\_S\u00141S\")_!\u001a0)#,\\&\u0014=&P/Y!f3V ,\\&\u00140'$/_#\u00172\"#+^,\u00156'\".\\#fF$&\\\\&\u0014=&P/Y!d2  -_P\u00160' .\\#g2$.(*%\u0002樹彦\u008e!ZT`1T ^_!\u00160' .\\#g2$#+Z'\u00166)\"Y, \u00123V +_W\u00140'\",-#d3$ -^'\u0016A&P.]W\u00150  ^_&\u0014G&#/]!f3V ,_ \u00157$%.*#\u001b3% ]^!\u00176'//Z\"\u00163% \\(S\u0016@$$.\\#\u00162' )^'\u0016C'&.\\#g0#  _S\u00155(&[Y".length();
        char c = 730;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = i4 + 1;
            String substring = str.substring(i5, i5 + c);
            int i6 = 10;
            boolean z = -1;
            while (true) {
                String b = b(i6, b(substring));
                switch (z) {
                    case false:
                        break;
                    default:
                        i = i3 + 1;
                        strArr[i3] = b;
                        i4 = i5 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "若雍廴\u0003\u0016h\u000e";
                        length = "若雍廴\u0003\u0016h\u000e".length();
                        c = 3;
                        i2 = -1;
                        i3 = i;
                        i6 = 70;
                        i5 = i2 + 1;
                        substring = str.substring(i5, i5 + c);
                        z = false;
                        break;
                }
                i = i3 + 1;
                strArr[i3] = b;
                i2 = i5 + c;
                if (i2 >= length) {
                    l = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i6 = 70;
                i5 = i2 + 1;
                substring = str.substring(i5, i5 + c);
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
    public cd(long r19) {
        /*
            r18 = this;
            r7 = r18
            long r0 = com.trossense.cd.k
            long r0 = r0 ^ r19
            r2 = 10781612190164(0x9ce4a2a29d4, double:5.326824189943E-311)
            long r2 = r2 ^ r0
            r4 = 48135368609664(0x2bc7638c0f80, double:2.378203197994E-310)
            long r4 = r4 ^ r0
            r6 = 48
            long r8 = r4 >>> r6
            int r8 = (int) r8
            r9 = 16
            long r9 = r4 << r9
            r11 = 32
            long r9 = r9 >>> r11
            int r9 = (int) r9
            long r4 = r4 << r6
            long r4 = r4 >>> r6
            int r10 = (int) r4
            r4 = 78830222041472(0x47b21761e180, double:3.89473045647287E-310)
            long r4 = r4 ^ r0
            r7.<init>(r2)
            com.trossense.cr r11 = new com.trossense.cr
            java.lang.String[] r17 = com.trossense.cd.l
            r0 = 4
            r1 = r17[r0]
            r0 = 6
            r2 = r17[r0]
            r0 = 9
            r6 = r17[r0]
            r0 = r11
            r3 = r18
            r0.<init>(r1, r2, r3, r4, r6)
            short r0 = (short) r8
            r1 = 3
            r14 = r17[r1]
            char r1 = (char) r10
            r2 = 1
            r16 = r17[r2]
            r12 = r0
            r13 = r9
            r15 = r1
            com.trossense.cr r11 = r11.a(r12, r13, r14, r15, r16)
            r2 = 2
            r14 = r17[r2]
            r2 = 8
            r16 = r17[r2]
            com.trossense.cr r0 = r11.a(r12, r13, r14, r15, r16)
            r7.j = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.cd.<init>(long):void");
    }

    public static byte[] a(String str, long j) {
        int[] n = ca.n();
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        int i = 0;
        while (i < length) {
            int i2 = i + 2;
            String substring = str.substring(i, i2);
            if (n == null) {
                break;
            }
            bArr[i / 2] = (byte) Integer.parseInt(substring, 16);
            if (n == null) {
                break;
            }
            i = i2;
        }
        return bArr;
    }

    private static String b(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 18;
                    break;
                case 1:
                    i2 = 99;
                    break;
                case 2:
                    i2 = 31;
                    break;
                case 3:
                    i2 = 40;
                    break;
                case 4:
                    i2 = 15;
                    break;
                case 5:
                    i2 = 26;
                    break;
                default:
                    i2 = 28;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 28);
        }
        return charArray;
    }

    @Override // com.trossense.bm
    public void j(long j) {
        long j2 = j ^ 120739904766283L;
        long j3 = j ^ 69810605493503L;
        long j4 = j ^ 9770049941261L;
        int i = (int) (j4 >>> 48);
        int i2 = (int) ((j4 << 16) >>> 32);
        int i3 = (int) ((j4 << 48) >>> 48);
        long j5 = j ^ 17801305162488L;
        int[] n = ca.n();
        String str = this.j.j(j ^ 29096378907956L).a;
        String[] strArr = l;
        if (str.equals(strArr[3])) {
            com.trossense.sdk.m mVar = new com.trossense.sdk.m();
            mVar.a(9753608L);
            mVar.a(a(strArr[7], j5));
            EntityLocalPlayer localPlayer = TrosSense.INSTANCE.getLocalPlayer();
            if (j <= 0 || localPlayer != null) {
                localPlayer.c(mVar, j3);
            }
            if (j >= 0) {
                if (n == null) {
                    n = new int[2];
                }
                a((char) i, i2, (char) i3, false);
            }
            PointerHolder.b(n);
        }
        com.trossense.sdk.m mVar2 = new com.trossense.sdk.m();
        mVar2.a(9753608L);
        EntityLocalPlayer localPlayer2 = TrosSense.INSTANCE.getLocalPlayer();
        mVar2.a(a(strArr[0], j5));
        if (j > 0) {
            if (localPlayer2 != null) {
                localPlayer2.b(mVar2, j2);
            }
            mVar2.a(a(strArr[5], j5));
            if (j < 0) {
                return;
            }
        }
        if (localPlayer2 != null) {
            localPlayer2.b(mVar2, j2);
        }
        a((char) i, i2, (char) i3, false);
    }
}
