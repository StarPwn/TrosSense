package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes3.dex */
public class n implements BedrockPacket {
    private static final long f = dj.a(3626110378599843056L, -6958673731064729705L, MethodHandles.lookup().lookupClass()).a(101110876203381L);
    private static final String[] g;
    private float a;
    private a b;
    private long c;
    private boolean d;
    private long e;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[5];
        String str = "\u000b=\u0018MC\u0013DJx/VY\u000eY^T\u000e\u0005\t\u000b=\u000b[Y\u000eBI \u0018\u000b=\u000f@Y\u0015Ldo\u0003LD\u0004LKX\u0004LD\u0013TnyW";
        int length = "\u000b=\u0018MC\u0013DJx/VY\u000eY^T\u000e\u0005\t\u000b=\u000b[Y\u000eBI \u0018\u000b=\u000f@Y\u0015Ldo\u0003LD\u0004LKX\u0004LD\u0013TnyW".length();
        char c = 18;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 13;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "7\u0001>eb\u001eioS79 UD\"ap(tZO?ip/tK@5ot/9iN!m\u007f<ErL39";
                        length = "7\u0001>eb\u001eioS79 UD\"ap(tZO?ip/tK@5ot/9iN!m\u007f<ErL39".length();
                        c = 11;
                        i2 = -1;
                        i3 = i;
                        i5 = 49;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    g = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 49;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 32;
            switch (i2 % 7) {
                case 0:
                    i3 = 42;
                    break;
                case 1:
                    i3 = 16;
                    break;
                case 2:
                    i3 = 103;
                    break;
                case 3:
                    i3 = 53;
                    break;
                case 5:
                    i3 = 106;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ ' ');
        }
        return charArray;
    }

    public float a() {
        return this.a;
    }

    public void a(float f2) {
        this.a = f2;
    }

    public void a(long j) {
        this.c = j;
    }

    public void a(a aVar) {
        this.b = aVar;
    }

    public void a(boolean z) {
        this.d = z;
    }

    protected boolean a(Object obj) {
        return obj instanceof n;
    }

    public a b() {
        return this.b;
    }

    public void b(long j) {
        this.e = j;
    }

    public long c() {
        return this.c;
    }

    public boolean d() {
        return this.d;
    }

    public long e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof n)) {
            return false;
        }
        n nVar = (n) obj;
        if (!nVar.a(this) || Float.compare(this.a, nVar.a) != 0 || this.c != nVar.c || this.d != nVar.d || this.e != nVar.e) {
            return false;
        }
        a aVar = this.b;
        a aVar2 = nVar.b;
        if (aVar == null) {
            if (aVar2 != null) {
                return false;
            }
        } else if (!aVar.equals(aVar2)) {
            return false;
        }
        return true;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ANIMATE;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler bedrockPacketHandler) {
        return PacketSignal.UNHANDLED;
    }

    public int hashCode() {
        int floatToIntBits = Float.floatToIntBits(this.a) + 59;
        long j = this.c;
        int i = (((floatToIntBits * 59) + ((int) (j ^ (j >>> 32)))) * 59) + (this.d ? 79 : 97);
        long j2 = this.e;
        int i2 = (i * 59) + ((int) (j2 ^ (j2 >>> 32)));
        a aVar = this.b;
        return (i2 * 59) + (aVar == null ? 43 : aVar.hashCode());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strArr = g;
        return sb.append(strArr[4]).append(this.a).append(strArr[1]).append(this.b).append(strArr[0]).append(this.c).append(strArr[3]).append(this.d).append(strArr[2]).append(this.e).append(")").toString();
    }
}
